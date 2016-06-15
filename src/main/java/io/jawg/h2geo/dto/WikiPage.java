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
package io.jawg.h2geo.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by fredszaq on 19/10/15.
 */
public class WikiPage {

    @SerializedName("lang")
    private String lang;

    @SerializedName("language_en")
    private String language;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("on_node")
    private boolean onNode;

    @SerializedName("tags_implies")
    private List<String> tagsImplies;

    @SerializedName("tags_combination")
    private List<String> tagsCombination;


    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOnNode() {
        return onNode;
    }

    public void setOnNode(boolean onNode) {
        this.onNode = onNode;
    }

    public List<String> getTagsImplies() {
        return tagsImplies;
    }

    public void setTagsImplies(List<String> tagsImplies) {
        this.tagsImplies = tagsImplies;
    }

    public List<String> getTagsCombination() {
        return tagsCombination;
    }

    public void setTagsCombination(List<String> tagsCombination) {
        this.tagsCombination = tagsCombination;
    }
}
