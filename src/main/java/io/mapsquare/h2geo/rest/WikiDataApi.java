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
package io.mapsquare.h2geo.rest;


import com.google.gson.JsonObject;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface WikiDataApi {
    @GET("Special%3AEntityData/{entityId}.json")
    public Observable<JsonObject> getDataForEntity(@Path("entityId") String entityId);
}
