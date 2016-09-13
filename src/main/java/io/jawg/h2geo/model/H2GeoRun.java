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
package io.jawg.h2geo.model;

import java.time.LocalDateTime;

import java.util.*;

public class H2GeoRun<T> {
    private String author = "h2geo";
    private Map<String, String> name;
    private Map<String, String> description;
    private String version;
    private String lastUpdate;
    private List<Group<T>> groups;
    private List<List<Double>> offlineArea;
    private String image;

    public H2GeoRun(String version, LocalDateTime lastUpdate, List<Group<T>> groups) {
        name = new HashMap<>();
        name.put("default", "Default h2geo preset");
        description = new HashMap<>();
        description.put("default", "This profile contains the crawled data from the OpenStreetMap Wiki by h2geo");
        this.image = "http://www.survoldefrance.fr/photos/highdef/11/11569.jpg";
        this.version = version;
        this.lastUpdate = lastUpdate.toString();
        this.groups = groups;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Map<String, String> getName() {
        return name;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }

    public Map<String, String> getDescription() {
        return description;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<Group<T>> getGroups() {
        return groups;
    }

    public void setGroups(List<Group<T>> groups) {
        this.groups = groups;
    }

    public List<List<Double>> getOfflineArea() {
        return offlineArea;
    }

    public void setOfflineArea(List<List<Double>> offlineArea) {
        this.offlineArea = offlineArea;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
