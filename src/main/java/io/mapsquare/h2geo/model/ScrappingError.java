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
package io.mapsquare.h2geo.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public enum ScrappingError {
    NOT_IN_WIKI(1, "Not in wiki"),
    NOT_EN_WIKI(2, "No English wiki page"),
    NOT_AVAILABLE_ON_NODES(3, "Not available on nodes"),
    NOT_CORRESPONDING_WIKIDATA_PROJECT(4, "No corresponding wikidata project (must not use wildcards)");

    private int errorCode;

    private String errorMessage;

    private Set<WikiError> errors;

    private ScrappingError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errors = new TreeSet<>();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Set<WikiError> getErrors() {
        return errors;
    }

    public void addError(WikiError wikiError) {
        errors.add(wikiError);
    }

    @Override
    public String toString() {
        return errors.size() + " " + errorMessage;
    }

    public static Set<ScrappingError> asSet() {
        Set<ScrappingError> set = new TreeSet<>();
        Collections.addAll(set, ScrappingError.values());
        return set;
    }

    public static class ScrappingErrorSerializer implements JsonSerializer<ScrappingError> {
        @Override
        public JsonElement serialize(ScrappingError src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.add("errorCode", context.serialize(src.getErrorCode()));
            object.add("errorMessage", context.serialize(src.getErrorMessage()));
            object.add("errors", context.serialize(src.getErrors()));
            return object;
        }
    }
}
