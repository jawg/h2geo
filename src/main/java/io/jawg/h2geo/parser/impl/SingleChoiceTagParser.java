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
package io.jawg.h2geo.parser.impl;


import io.jawg.h2geo.parser.TagParser;
import io.jawg.h2geo.parser.Type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SingleChoiceTagParser implements TagParser {

    private static final String TAG = "SingleChoice";

    /**
     * Limit to consider the use of a single choice widget. If size of possible values
     * are higher that 7, we use another widget (AUTOCOMPLETE).
     */
    private static final int LIMIT = 7;

    /**
     * Possible values.
     */
    private List<String> possibleValues = new ArrayList<>(0);

    /**
     * Supported value.
     */
    private List<String> supportedValues = Arrays.asList("y", "n", "0", "1", "true", "false", "oui", "non", "si", "yes", "no", "undefined", "");

    @Override
    public Type getType() {
        return Type.SINGLE_CHOICE;
    }

    @Override
    public boolean isCandidate(String key, List<String> values) {
        this.possibleValues = values;
        // If size is > 7 and values must be choose in a list with yes/no.
        return values.size() < LIMIT && (values.contains("yes") || values.contains("no"));
    }

    @Override
    public boolean supports(String value) {
        // If value is not in possible values, user have to format the value.
        return value == null || possibleValues.contains(value) || supportedValues.contains(value);
    }

    @Override
    public int getPriority() {
        return Priority.HIGH;
    }
}