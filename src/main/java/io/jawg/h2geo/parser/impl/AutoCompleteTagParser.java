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

public class AutoCompleteTagParser implements TagParser {

    private static final int LIMIT = 6;

    @Override
    public Type getType() {
        return Type.TEXT;
    }

    @Override
    public boolean isCandidate(String key, List<String> values) {
        return values.size() > LIMIT;
    }

    @Override
    public boolean supports(String value) {
        return true;
    }

    @Override
    public int getPriority() {
        return Priority.NORMAL;
    }

    @Override
    public int hashCode() {
        return getPriority();
    }
}