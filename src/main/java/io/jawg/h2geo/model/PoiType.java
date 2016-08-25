/**
 * Copyright (C) 2016 Jawg
 * <p>
 * This file is part of h2geo.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
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
import io.jawg.h2geo.parser.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PoiType implements Comparable<PoiType> {


  @SerializedName("name")
  private String name;

  @SerializedName("wikiUrl")
  private String wikiUrl;

  @SerializedName("label")
  private Map<String, String> label = new HashMap<>();

  @SerializedName("description")
  private Map<String, String> description = new HashMap<>();

  @SerializedName("usageCount")
  private int usageCount;

  @SerializedName(("keywords"))
  private Map<String, List<String>> keywords = new HashMap<>();

  @SerializedName("tags")
  private List<PoiTypeTag> tags = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getWikiUrl() {
    return wikiUrl;
  }

  public void setWikiUrl(String wikiUrl) {
    this.wikiUrl = wikiUrl;
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

  public int getUsageCount() {
    return usageCount;
  }

  public void setUsageCount(int usageCount) {
    this.usageCount = usageCount;
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

  public static PoiType from(String category, KeyValue categoryValue, WikiPage wikiPage, List<WikiPage> wikiPages, JsonObject wikidata) {
    String name = category + "=" + categoryValue.getValue();
    PoiType poiType = new PoiType();
    poiType.setName(name);
    poiType.setWikiUrl("https://wiki.openstreetmap.org/wiki/" + wikiPage.getTitle());

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
        poiType.getKeywords().put(aliasesForLang.getKey(), keywords);
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
    poiType.setUsageCount(categoryValue.getCount());

    for (String tag : wikiPage.getTagsImplies()) {
      String[] split = tag.split("=");
      PoiTypeTag poiTypeTag = new PoiTypeTag();
      poiTypeTag.setKey(split[0]);
      if (split.length > 1 && !split[1].equals("*")) {
        poiTypeTag.setValue(split[1]);
        poiTypeTag.setImplied(true);
      }
      poiType.getTags().add(poiTypeTag);
    }

    for (String tag : wikiPage.getTagsCombination()) {
      String[] split = tag.split("=");
      PoiTypeTag poiTypeTag = new PoiTypeTag();
      poiTypeTag.setKey(split[0]);
      poiType.getTags().add(poiTypeTag);
    }

    poiType.getTags().forEach(h2GeoScrapper::insertPossibleValues);

    for (PoiTypeTag poiTypeTag : poiType.getTags()) {
      Type type = poiType.getTags().get(poiType.getTags().indexOf(poiTypeTag)).getType();
      if (type == null || type == Type.TEXT) {
        poiTypeTag.setType(Type.parse(poiTypeTag));
      }
    }
    return poiType;
  }

  @Override
  public int compareTo(PoiType poiType) {
    return name.compareTo(poiType.name);
  }
}
