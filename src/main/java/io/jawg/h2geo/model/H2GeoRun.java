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
import java.util.Set;

public class H2GeoRun<T> {
    private String h2GeoVersion;
    private String generationDate;
    private Set<T> data;

    public H2GeoRun(String h2GeoVersion, LocalDateTime generationDate, Set<T> data) {
        this.h2GeoVersion = h2GeoVersion;
        this.generationDate = generationDate.toString();
        this.data = data;
    }

    public String getH2GeoVersion() {
        return h2GeoVersion;
    }

    public void setH2GeoVersion(String h2GeoVersion) {
        this.h2GeoVersion = h2GeoVersion;
    }

    public String getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(String generationDate) {
        this.generationDate = generationDate;
    }

    public Set<T> getData() {
        return data;
    }

    public void setData(Set<T> data) {
        this.data = data;
    }
}
