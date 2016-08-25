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

import io.jawg.h2geo.dto.DescriptionDto;
import io.jawg.h2geo.dto.NameDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class H2GeoRun<T> {
    private NameDto name;
    private DescriptionDto description;
    private String version;
    private String lastUpdate;
    private Set<T> data;
    private List<List<Double>> offlineArea;
    private String image;

    public H2GeoRun(String version, LocalDateTime lastUpdate, Set<T> data) {
        this.name = new NameDto("Default preset", "Preset par défaut");
        this.description = new DescriptionDto("This profile contains the complte list of POIs", "Ce profil contient la liste de tous les POIs Open Street Map");
        this.image = "";
        this.version = version;
        this.lastUpdate = lastUpdate.toString();
        this.data = data;
        this.offlineArea = new ArrayList<>();
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

    public Set<T> getData() {
        return data;
    }

    public void setData(Set<T> data) {
        this.data = data;
    }

    public DescriptionDto getDescription() {
        return description;
    }

    public NameDto getName() {
        return name;
    }

    public List<List<Double>> getOfflineArea() {
        return offlineArea;
    }

    public void setOfflineArea(List<List<Double>> offlineArea) {
        this.offlineArea = offlineArea;
    }

    public void setDescription(DescriptionDto description) {
        this.description = description;
    }

    public void setName(NameDto name) {
        this.name = name;
    }
}
