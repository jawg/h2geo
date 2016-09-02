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
    private Map<String, String> name;
    private Map<String, String> description;
    private String version;
    private String lastUpdate;
    private List<Group<T>> groups;
    private List<List<Double>> offlineArea;
    private String image;

    public H2GeoRun(String version, LocalDateTime lastUpdate, List<Group<T>> groups) {
        name = new HashMap<>();
        name.put("default", "Default preset");
        name.put("en", "Default preset");
        name.put("fr", "Preset par défaut");

        description = new HashMap<>();
        description.put("default", "This profile contains the complte list of POIs");
        description.put("en", "This profile contains the complte list of POIs");
        description.put("fr", "Ce profil contient la liste de tous les POIs Open Street Map");

        this.image = "http://www.survoldefrance.fr/photos/highdef/11/11569.jpg";
        this.version = version;
        this.lastUpdate = lastUpdate.toString();
        this.groups = groups;
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

    public Map<String, String> getDescription() {
        return description;
    }

    public Map<String, String> getName() {
        return name;
    }

    public List<List<Double>> getOfflineArea() {
        return offlineArea;
    }

    public void setOfflineArea(List<List<Double>> offlineArea) {
        this.offlineArea = offlineArea;
    }

    public void setDescription(Map<String, String> description) {
        this.description = description;
    }

    public void setName(Map<String, String> name) {
        this.name = name;
    }
}
