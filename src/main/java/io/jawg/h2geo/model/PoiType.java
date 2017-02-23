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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import io.jawg.h2geo.H2GeoScrapper;
import io.jawg.h2geo.dto.KeyValue;
import io.jawg.h2geo.dto.WikiPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoiType implements Comparable<PoiType> {

  @SerializedName("name")
  private String name;

  @SerializedName("url")
  private String url;

  @SerializedName("icon")
  private String icon;

  @SerializedName("label")
  private Map<String, String> label = new HashMap<>();

  @SerializedName("description")
  private Map<String, String> description = new HashMap<>();

  @SerializedName(("keywords"))
  private Map<String, List<String>> keywords;

  @SerializedName("tags")
  private List<PoiTypeTag> tags = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Map<String, String> getLabel() {
    return label;
  }

  public void setLabel(Map<String, String> label) {
    this.label = label;
  }

  public Map<String, String> getDescription() {
    return description;
  }

  public void setDescription(Map<String, String> description) {
    this.description = description;
  }

  public Map<String, List<String>> getKeywords() {
    return keywords;
  }

  public void setKeywords(Map<String, List<String>> keywords) {
    this.keywords = keywords;
  }

  public List<PoiTypeTag> getTags() {
    return tags;
  }

  public void setTags(List<PoiTypeTag> tags) {
    this.tags = tags;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public static PoiType from(String category, KeyValue categoryValue, WikiPage wikiPage, List<WikiPage> wikiPages, JsonObject wikidata) {
    String name = category + "=" + categoryValue.getValue();
    PoiType poiType = new PoiType();
    poiType.setName(name);
    poiType.setUrl("https://wiki.openstreetmap.org/wiki/" + wikiPage.getTitle());

    JsonObject entity = wikidata.getAsJsonObject("entities").entrySet().iterator().next().getValue().getAsJsonObject();

    JsonObject labels = entity.getAsJsonObject("labels");

    for (Map.Entry<String, JsonElement> labelEntry : labels.entrySet()) {
      JsonObject label = labelEntry.getValue().getAsJsonObject();
      poiType.getLabel().put(label.get("language").getAsString(), label.get("value").getAsString());
    }

    JsonElement aliasesElement = entity.get("aliases");
    if (aliasesElement.isJsonObject()) {
      JsonObject aliases = aliasesElement.getAsJsonObject();
      for (Map.Entry<String, JsonElement> aliasesForLang : aliases.entrySet()) {
        List<String> keywords = new ArrayList<>();
        JsonArray asJsonArray = aliasesForLang.getValue().getAsJsonArray();
        for (JsonElement alias : asJsonArray) {
          keywords.add(alias.getAsJsonObject().get("value").getAsString());
        }
        poiType.putKeyword(aliasesForLang.getKey(), keywords);
      }
    }

    H2GeoScrapper h2GeoScrapper = new H2GeoScrapper();

    for (WikiPage page : wikiPages) {
      poiType.getDescription().put(page.getLang(), page.getDescription());
    }
    PoiTypeTag categoryTag = new PoiTypeTag();
    categoryTag.setKey(category);
    categoryTag.setValue(categoryValue.getValue());
    poiType.getTags().add(categoryTag);

    for (String tag : wikiPage.getTagsImplies()) {
      String[] split = tag.split("=");
      PoiTypeTag poiTypeTag = new PoiTypeTag();
      poiTypeTag.setKey(split[0]);
      if (split.length > 1 && !split[1].equals("*")) {
        poiTypeTag.setValue(split[1]);
        poiTypeTag.setEditable(false);
      }
      poiType.getTags().add(poiTypeTag);
    }

    for (String tag : wikiPage.getTagsCombination()) {
      String[] split = tag.split("=");
      PoiTypeTag poiTypeTag = new PoiTypeTag();
      poiTypeTag.setKey(split[0]);
      poiType.getTags().add(poiTypeTag);
    }

    return poiType;
  }

  private void putKeyword(String key, List<String> keywords) {
    if (this.keywords == null) {
      this.keywords = new HashMap<>();
    }
    this.keywords.put(key, keywords);
  }

  @Override
  public int compareTo(PoiType poiType) {
    return name.compareTo(poiType.name);
  }
}