package io.jawg.h2geo.model;

import com.google.gson.annotations.SerializedName;

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
