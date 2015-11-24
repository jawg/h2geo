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
package io.mapsquare.h2geo.model;

import com.google.gson.annotations.SerializedName;

public class ScrappingError implements Comparable<ScrappingError>{
    @SerializedName("key")
    private String key;

    @SerializedName("value")
    private String value;

    @SerializedName("error")
    private String error;

    public ScrappingError() {
    }

    public ScrappingError(String key, String value, String error) {
        this.key = key;
        this.value = value;
        this.error = error;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public int compareTo(ScrappingError scrappingError) {
        int keyComparison = key.compareTo(scrappingError.key);
        return keyComparison !=0 ? keyComparison : value.compareTo(scrappingError.value);
    }
}
