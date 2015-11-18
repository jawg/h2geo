/**
 * Copyright (C) 2015 eBusiness Information
 *
 * This file is part of h2geo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mapsquare.h2geo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import io.mapsquare.h2geo.dto.KeyValue;
import io.mapsquare.h2geo.dto.LinkedProject;
import io.mapsquare.h2geo.dto.WikiPage;
import io.mapsquare.h2geo.model.PoiType;
import io.mapsquare.h2geo.rest.TagsInfoApi;
import io.mapsquare.h2geo.rest.WikiDataApi;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;

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

    public String scrapeTypes() {
        Observable<PoiType> types = Observable
                .from(Arrays.asList("amenity", "shop", "highway", "tourism", "historic", "emergency"))
                .subscribeOn(Schedulers.io())  // parallel processing
                .map(ResultBuilder::new)
                .flatMap(ResultBuilder::findPossibleValuesForCategory)
                .flatMap(ResultBuilder::getWikiPages)
                .flatMap(ResultBuilder::filterEnWikiPage)
                .flatMap(ResultBuilder::getWikiData)
                .map(ResultBuilder::toPoiType);

        Observable<List<PoiType>> typesList = types.toSortedList((t1, t2) -> t1.getName().compareTo(t2.getName()));
        List<PoiType> list = typesList.toBlocking().single();
        System.out.println("Scrapped " + list.size() + " types");
        return new Gson().toJson(list);
    }

    /**
     * Class wrapping the operations we make to get the results
     *
     * In order to create a PoiType, this class should be constructed with a category name, and then call the following
     * methods in that order :
     *
     * <ul>
     *  <li>{@link #findPossibleValuesForCategory()}</li>
     *  <li>{@link #getWikiPages()}</li>
     *  <li>{@link #filterEnWikiPage()}</li>
     *  <li>{@link #getWikiData()}</li>
     *  <li>{@link #toPoiType()}</li>
     * </ul>
     *
     * Note that all these methods return {@link Observable}s
     */
    private class ResultBuilder {
        private final String category;
        private final KeyValue categoryValue;
        private final WikiPage wikiPage;
        private final List<WikiPage> wikiPages;
        private final JsonObject wikiData;

        private ResultBuilder(String category, KeyValue categoryValue, List<WikiPage> wikiPages, WikiPage wikiPage, JsonObject wikiData) {
            this.category = category;
            this.categoryValue = categoryValue;
            this.wikiPage = wikiPage;
            this.wikiPages = wikiPages;
            this.wikiData = wikiData;
        }

        public ResultBuilder(String category) {
            this.category = category;
            this.categoryValue = null;
            this.wikiPage = null;
            this.wikiPages = null;
            this.wikiData = null;
        }

        public ResultBuilder withCategoryValue(KeyValue categoryValue) {
            return new ResultBuilder(category, categoryValue, wikiPages, wikiPage, wikiData);
        }

        public Observable<ResultBuilder> findPossibleValuesForCategory() {
            return tagsInfoApi.getKeyValues(category)
                    .subscribeOn(Schedulers.io())
                    .retry(MAX_RETRY_COUNT)
                    .flatMap(result -> Observable.from(result.getData()))
                    .filter(KeyValue::isInWiki)  // filter out values with no wiki pages
                    .map(this::withCategoryValue);
        }

        public ResultBuilder withWikiPages(List<WikiPage> wikiPages) {
            return new ResultBuilder(category, categoryValue, wikiPages, wikiPage, wikiData);
        }

        public Observable<ResultBuilder> getWikiPages() {
            return tagsInfoApi.getWikiPages(category, categoryValue.getValue())
                    .subscribeOn(Schedulers.io())
                    .retry(MAX_RETRY_COUNT)
                    .map(result -> withWikiPages(result.getData()));
        }

        public ResultBuilder withWikiPage(WikiPage wikiPage) {
            return new ResultBuilder(category, categoryValue, wikiPages, wikiPage, wikiData);
        }

        Observable<ResultBuilder> filterEnWikiPage() {
            return Observable.from(wikiPages)
                    .filter(page -> page.getLang().equals("en") && page.isOnNode())
                    .map(this::withWikiPage);
        }

        public ResultBuilder withWikiData(JsonObject wikidata) {
            return new ResultBuilder(category, categoryValue, wikiPages, wikiPage, wikidata);
        }

        public Observable<ResultBuilder> getWikiData() {
            return tagsInfoApi.getLinkedProjects(category, categoryValue.getValue())
                    .subscribeOn(Schedulers.io())
                    .retry(MAX_RETRY_COUNT)
                    .flatMap(linkedProjects -> Observable.<LinkedProject>from(linkedProjects.getData()))
                            // filter out non wikiData projects and project not exactly about this key and value
                    .filter(linkedProject -> linkedProject.getProjectId().equals("wikidata_org")
                            && category.equals(linkedProject.getKey())
                            && categoryValue.getValue().equals(linkedProject.getValue()))
                    .take(1)  // sometimes there are multiple wikiData links, take only the first one
                    .flatMap(linkedProject ->
                            wikiDataApi.getDataForEntity(linkedProject.getId())
                                    .retry(MAX_RETRY_COUNT)
                                    .subscribeOn(Schedulers.io()))
                    .map(this::withWikiData);
        }

        public PoiType toPoiType() {
            return PoiType.from(category, categoryValue, wikiPage, wikiPages, wikiData);
        }
    }

}
