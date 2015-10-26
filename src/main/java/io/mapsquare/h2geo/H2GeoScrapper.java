/**
 * Copyright (C) 2015 eBusiness Information
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

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import io.mapsquare.h2geo.dto.KeyValue;
import io.mapsquare.h2geo.dto.LinkedProject;
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
                .from(Arrays.asList("amenity", "shop", "highway", "tourism", "historic"))
                        // parallel processing
                .subscribeOn(Schedulers.io())
                .flatMap(category ->
                        // get possible  tags values for category
                        tagsInfoApi.getKeyValues(category)
                                .subscribeOn(Schedulers.io())
                                .retry(10)
                                .flatMap(it -> Observable.from(it.getData())
                                        // filter out values with no wiki pages
                                        .filter(KeyValue::isInWiki)
                                        .flatMap(keyValue ->
                                                //getting wiki page for category=keyValue
                                                tagsInfoApi.getWikiPages(category, keyValue.getValue())
                                                        .subscribeOn(Schedulers.io())
                                                        .retry(10)
                                                        .flatMap(pages -> Observable.from(pages)
                                                                // filter english wiki page (will use it to get tags)
                                                                .filter(page -> page.getLang().equals("en") && page.isOnNode())
                                                                .flatMap(page ->
                                                                        //getting project page for category=keyValue
                                                                        tagsInfoApi.getLinkedProjects(category, keyValue.getValue())
                                                                                .subscribeOn(Schedulers.io())
                                                                                .retry(10)
                                                                                .flatMap(linkedProjects -> Observable.<LinkedProject>from(linkedProjects.getData()))
                                                                                        // filter out types with no wikidata link
                                                                                .filter(linkedProject -> linkedProject.getProjectId().equals("wikidata_org")
                                                                                        && category.equals(linkedProject.getKey())
                                                                                        && keyValue.getValue().equals(linkedProject.getValue()))
                                                                                        // get wikidata infos
                                                                                .flatMap(linkedProject ->
                                                                                        wikiDataApi.getDataForEntity(linkedProject.getId())
                                                                                                .retry(10)
                                                                                                .subscribeOn(Schedulers.io()))
                                                                                        // create poi type
                                                                                .map(wikidata -> PoiType.from(category, keyValue, page, pages, wikidata)))))));


        Observable<List<PoiType>> typesList = types.toSortedList((t1, t2) -> t1.getName().compareTo(t2.getName()));
        List<PoiType> list = typesList.toBlocking().single();
        System.out.println("Scrapped " + list.size() + " types");
        return new Gson().toJson(list);
    }

}
