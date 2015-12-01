package io.mapsquare.h2geo.model;

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
