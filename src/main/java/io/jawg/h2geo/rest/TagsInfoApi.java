/**
 * Copyright (C) 2016 Jawg
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
package io.jawg.h2geo.rest;

import io.jawg.h2geo.dto.WikiPage;
import io.jawg.h2geo.dto.KeyValue;
import io.jawg.h2geo.dto.LinkedProject;
import io.jawg.h2geo.dto.Page;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface TagsInfoApi {


    @GET("key/values")
    public Observable<Page<KeyValue>> getKeyValues(@Query("key") String key);

    @GET("tag/wiki_pages")
    public Observable<Page<WikiPage>> getWikiPages(@Query("key") String key, @Query("value") String value);

    @GET("tag/projects")
    public Observable<Page<LinkedProject>> getLinkedProjects(@Query("key") String key, @Query("value") String value);

}
