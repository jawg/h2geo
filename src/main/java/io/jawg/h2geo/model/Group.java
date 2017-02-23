/**
 * Copyright (C) 2017 Jawg
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
package io.jawg.h2geo.model;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Hugo Bernardi
 *         Date: 01/09/16.
 */
public class Group<T> {

    @SerializedName("name")
    private Map<String, String> name;

    @SerializedName("icon")
    private String icon;

    @SerializedName("url")
    private String url;

    @SerializedName("items")
    private Set<T> items;

    public Group(Set<T> items) {
        this.items = items;
        this.name = new HashMap<>();
        name.put("default", "Default h2geo Group");
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<T> getItems() {
        return items;
    }

    public void setItems(Set<T> items) {
        this.items = items;
    }

}
