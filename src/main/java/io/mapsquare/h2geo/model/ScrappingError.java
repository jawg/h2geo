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
