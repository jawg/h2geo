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

import java.util.List;


public class OpeningTimeTagParser implements TagParser {

    public static final String PATTERN_MONTH = "(\\bJan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec\\b)";

    public static final String PATTERN_DAY = "(\\bMo|Tu|We|Th|Fr|Sa|Su\\b)";

    public static final String PATTERN_HOUR = "\\d{2}:\\d{2}-\\d{2}:\\d{2}";

    public static final String PATTERN =
            "(((" + PATTERN_MONTH + "(-" + PATTERN_MONTH + ")?" + "(,\\s?" + PATTERN_MONTH +
                    "(-" + PATTERN_MONTH + ")?)*:\\s?)?" +
                    "(" + PATTERN_DAY + "(-" + PATTERN_DAY + ")?" +
                    "((,\\s?" + PATTERN_DAY + "(-" + PATTERN_DAY + ")?)?)*\\s" +
                    PATTERN_HOUR + ")(,\\s?(" + PATTERN_DAY +
                    "(-" + PATTERN_DAY + ")?" +
                    "((,\\s?" + PATTERN_DAY +
                    "(-" + PATTERN_DAY + ")?)?)*\\s" +
                    PATTERN_HOUR + "))*)" +
                    "|" + "(" + PATTERN_MONTH + "(-" + PATTERN_MONTH + ")?" + "(,\\s?" + PATTERN_MONTH +
                    "(-" + PATTERN_MONTH + ")?)*))";

    public static final String PATTERN_FINAL = PATTERN + "(;\\s?" + PATTERN + ")*";

    @Override
    public Type getType() {
        return Type.OPENING_HOURS;
    }

    @Override
    public boolean isCandidate(String key, List<String> values) {
        return key.contains("hours");
    }

    @Override
    public boolean supports(String value) {
        return value == null || value.equals("24/7") || value.matches(PATTERN_FINAL);
    }

    @Override
    public int getPriority() {
        return Priority.HIGHEST;
    }

    @Override
    public int hashCode() {
        return getPriority();
    }
}