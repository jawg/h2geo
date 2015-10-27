/**
 * Copyright (C) 2015 eBusiness Information
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
package io.mapsquare.h2geo.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import io.mapsquare.h2geo.dto.KeyValue;
import io.mapsquare.h2geo.dto.WikiPage;

import java.util.*;

public class PoiType {


    @SerializedName("name")
    private String name;

    @SerializedName("label")
    private Map<String, String> label = new HashMap<>();

    @SerializedName("description")
    private Map<String, String> description = new HashMap<>();

    @SerializedName(("keyWords"))
    private Map<String, List<String>> keyWords = new HashMap<>();

    @SerializedName("tags")
    private List<PoiTypeTag> tags = new ArrayList<>();

    @SerializedName("usageCount")
    private int usageCount;

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<PoiTypeTag> getTags() {
        return tags;
    }

    public void setTags(List<PoiTypeTag> tags) {
        this.tags = tags;
    }

    public Map<String, List<String>> getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(Map<String, List<String>> keyWords) {
        this.keyWords = keyWords;
    }


    public static PoiType from(String category, KeyValue categoryValue, WikiPage wikiPage, List<WikiPage> wikiPages, JsonObject wikidata) {
        String name = category + "=" + categoryValue.getValue();
        System.out.println("creating poitype " + name);
        PoiType poiType = new PoiType();
        poiType.setName(name);

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
                poiType.getKeyWords().put(aliasesForLang.getKey(), keywords);
            }
        }

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
            if (split.length > 1 && !split[1].equals("*")) {
                poiTypeTag.setPossibleValues(Arrays.asList(split[1].split("/")));
            }
            poiType.getTags().add(poiTypeTag);
        }
        return poiType;
    }
}
