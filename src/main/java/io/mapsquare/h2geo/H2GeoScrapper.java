/**
 * Copyright (C) 2016 eBusiness Information
 *
 * This file is part of h2geo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mapsquare.h2geo;

import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import io.mapsquare.h2geo.dto.KeyValue;
import io.mapsquare.h2geo.dto.LinkedProject;
import io.mapsquare.h2geo.dto.Page;
import io.mapsquare.h2geo.dto.WikiPage;
import io.mapsquare.h2geo.model.PoiType;
import io.mapsquare.h2geo.model.ScrappingError;
import io.mapsquare.h2geo.model.WikiError;
import io.mapsquare.h2geo.rest.TagsInfoApi;
import io.mapsquare.h2geo.rest.WikiDataApi;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class H2GeoScrapper {

    public static final int MAX_RETRY_COUNT = 10;
    private TagsInfoApi tagsInfoApi;
    private WikiDataApi wikiDataApi;

    public H2GeoScrapper() {

        OkHttpClient okClient = new OkHttpClient();

        okClient.interceptors().add(chain -> {
            Request request = chain.request();
            System.out.println(request.urlString());
            return chain.proceed(request);
        });

        tagsInfoApi = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://taginfo.openstreetmap.org/api/4/")
                .client(okClient)
                .build()
                .create(TagsInfoApi.class);

        wikiDataApi = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("https://www.wikidata.org/wiki/")
                .client(okClient)
                .build()
                .create(WikiDataApi.class);
    }


    public static class Result {
        private final Set<PoiType> types = new TreeSet<>();

        public Set<PoiType> getTypes() {
            return types;
        }

        public void add(ResultBuilder resultBuilder) {
            if (resultBuilder.hasError()) {
                resultBuilder.error.addError(resultBuilder.toWIkiError());
            } else {
                types.add(resultBuilder.toPoiType());
            }
        }
    }

    public Result scrapeTypes() {
        Result result = Observable.from(Arrays.asList("amenity", "shop", "highway", "tourism", "historic", "emergency"))
                .subscribeOn(Schedulers.io())  // parallel processing
                .map(ResultBuilder::new)
                .flatMap(ResultBuilder::findPossibleValuesForCategory)
                .flatMap(ResultBuilder::getWikiPages)
                .flatMap(ResultBuilder::filterEnWikiPage)
                .flatMap(ResultBuilder::getWikiDataId)
                .flatMap(ResultBuilder::getWikiData)
                .collect(Result::new, Result::add)
                .toBlocking()
                .single();

        System.out.println("Scrapped " + result.getTypes().size() + " types");
        ScrappingError.asSet().forEach(scrappingError -> System.out.println("Got " + scrappingError));
        return result;

    }

    /**
     * Class wrapping the operations we make to get the results
     *
     * In order to create a PoiType, this class should be constructed with a category name, and then call the following
     * methods in that order :
     *
     * <ul>
     * <li>{@link #findPossibleValuesForCategory()}</li>
     * <li>{@link #getWikiPages()}</li>
     * <li>{@link #filterEnWikiPage()}</li>
     * <li>{@link #getWikiDataId()}</li>
     * <li>{@link #getWikiData()}</li>
     * </ul>
     *
     * then either {@link #toPoiType()} or {@link #toScrappingError()} depending on the result of {@link #hasError()}
     *
     * Note that all these methods return {@link Observable}s
     */
    private class ResultBuilder {
        private final ScrappingError error;
        private final String category;
        private final KeyValue categoryValue;
        private final WikiPage enWikiPage;
        private final List<WikiPage> wikiPages;
        private final String wikiDataId;
        private final JsonObject wikiData;

        private ResultBuilder(String category, KeyValue categoryValue, List<WikiPage> wikiPages, WikiPage enWikiPage, String wikiDataId, JsonObject wikiData) {
            this.category = category;
            this.categoryValue = categoryValue;
            this.enWikiPage = enWikiPage;
            this.wikiPages = wikiPages;
            this.wikiDataId = wikiDataId;
            this.wikiData = wikiData;
            this.error = null;
        }

        public ResultBuilder(String category) {
            this.category = category;
            this.categoryValue = null;
            this.enWikiPage = null;
            this.wikiPages = null;
            this.wikiDataId = null;
            this.wikiData = null;
            this.error = null;
        }

        public ResultBuilder(ResultBuilder existing, ScrappingError error) {
            this.category = existing.category;
            this.categoryValue = existing.categoryValue;
            this.enWikiPage = existing.enWikiPage;
            this.wikiPages = existing.wikiPages;
            this.wikiDataId = existing.wikiDataId;
            this.wikiData = existing.wikiData;
            this.error = error;
        }

        public Boolean hasError() {
            return error != null;
        }

        public Observable<ResultBuilder> findPossibleValuesForCategory() {
            return tagsInfoApi.getKeyValues(category)
                    .subscribeOn(Schedulers.io())
                    .retry(MAX_RETRY_COUNT)
                    .flatMapIterable(Page::getData)
                    .map(this::withCategoryValue);
        }

        public Observable<ResultBuilder> getWikiPages() {
            return doIfNoError(() ->
                    tagsInfoApi.getWikiPages(category, categoryValue.getValue())
                            .subscribeOn(Schedulers.io())
                            .retry(MAX_RETRY_COUNT)
                            .map(Page::getData)
                            .map(this::withWikiPages));
        }

        public Observable<ResultBuilder> filterEnWikiPage() {
            return doIfNoError(() ->
                    Observable.from(wikiPages)
                            .firstOrDefault(null, page -> page.getLang().equals("en"))
                            .map(this::withEnWikiPage)
                            .map(ResultBuilder::checkOnNode));
        }

        public Observable<ResultBuilder> getWikiDataId() {
            return doIfNoError(() ->
                    tagsInfoApi.getLinkedProjects(category, categoryValue.getValue())
                            .subscribeOn(Schedulers.io())
                            .retry(MAX_RETRY_COUNT)
                            .flatMapIterable(Page::getData)
                            .firstOrDefault(null, linkedProject -> linkedProject.getProjectId().equals("wikidata_org")
                                    && category.equals(linkedProject.getKey())
                                    && categoryValue.getValue().equals(linkedProject.getValue()))
                            .map(this::withWikiDataId));
        }

        public Observable<ResultBuilder> getWikiData() {
            return doIfNoError(() ->
                    wikiDataApi.getDataForEntity(wikiDataId)
                            .retry(MAX_RETRY_COUNT)
                            .subscribeOn(Schedulers.io())
                            .map(this::withWikiData));

        }

        public PoiType toPoiType() {
            return PoiType.from(category, categoryValue, enWikiPage, wikiPages, wikiData);
        }

        public WikiError toWIkiError() {
            return new WikiError(category, categoryValue == null ? "null" : categoryValue.getValue());
        }

        private Observable<ResultBuilder> doIfNoError(Work work) {
            return error != null
                    ? Observable.just(this)
                    : work.perform();
        }

        private ResultBuilder checkOnNode() {
            return error != null || enWikiPage.isOnNode()
                    ? this
                    : new ResultBuilder(this, ScrappingError.NOT_AVAILABLE_ON_NODES);
        }

        private ResultBuilder withCategoryValue(KeyValue categoryValue) {
            ResultBuilder result = new ResultBuilder(category, categoryValue, wikiPages, enWikiPage, wikiDataId, wikiData);
            return categoryValue.isInWiki()
                    ? result
                    : new ResultBuilder(result, ScrappingError.NOT_IN_WIKI);
        }

        private ResultBuilder withWikiPages(List<WikiPage> wikiPages) {
            return new ResultBuilder(category, categoryValue, wikiPages, enWikiPage, wikiDataId, wikiData);
        }

        private ResultBuilder withEnWikiPage(WikiPage enWikiPage) {
            return enWikiPage != null
                    ? new ResultBuilder(category, categoryValue, wikiPages, enWikiPage, wikiDataId, wikiData)
                    : new ResultBuilder(this, ScrappingError.NOT_EN_WIKI);
        }

        private ResultBuilder withWikiData(JsonObject wikidata) {
            return new ResultBuilder(category, categoryValue, wikiPages, enWikiPage, wikiDataId, wikidata);
        }

        private ResultBuilder withWikiDataId(LinkedProject wikiDataId) {
            return wikiDataId != null
                    ? new ResultBuilder(category, categoryValue, wikiPages, enWikiPage, wikiDataId.getId(), wikiData)
                    : new ResultBuilder(this, ScrappingError.NOT_CORRESPONDING_WIKIDATA_PROJECT);
        }

    }

    private interface Work {
        Observable<ResultBuilder> perform();
    }

}
