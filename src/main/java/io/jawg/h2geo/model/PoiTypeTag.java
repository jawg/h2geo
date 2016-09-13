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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or editable.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jawg.h2geo.model;

import com.google.gson.annotations.SerializedName;
import io.jawg.h2geo.parser.Type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PoiTypeTag {

  @SerializedName("key")
  private String key;

  @SerializedName("value")
  private String value;

  @SerializedName("type")
  private Type type;

  @SerializedName("required")
  private Boolean required;

  @SerializedName("editable")
  private Boolean editable;
  
  @SerializedName("show")
  private Boolean show;

  @SerializedName("values")
  private List<Map<String, Map<String,String>>> values;


  public PoiTypeTag() {
    values = new ArrayList<>();
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

  public Boolean getRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  public Boolean getShow() {
    return show;
  }

  public void setShow(Boolean show) {
    this.show = show;
  }

  public Boolean getEditable() {
    return editable;
  }
  
  public void setEditable(Boolean editable) {
    this.editable = editable;
  }

  public List<Map<String, Map<String,String>>> getValues() {
    return values;
  }

  public List<String> getReducedValues() {
    return values.stream().flatMap(o -> o.keySet().stream()).collect(Collectors.toList());
  }

  public void setValues(List<String> values) {
    if (values == null) {
      this.values = null;
    } else {
      this.values = Collections.synchronizedList(new ArrayList<>(values.size()));
      values.forEach(v -> {
        Map<String, Map<String, String>> valueMap = new HashMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("default", v);
        valueMap.put(v, map);
        this.values.add(valueMap);
      });
      
    }
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public void addValue(String value) {
    if (this.values == null) {
      values = Collections.synchronizedList(new ArrayList<>());
    }
    Map<String, Map<String, String>> valueMap = new HashMap<>();
    Map<String, String> map = new HashMap<>();
    map.put("default", value);
    valueMap.put(value, map);
    this.values.add(valueMap);
  }
}
