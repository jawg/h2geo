/**
 * Copyright (C) 2016 eBusiness Information
 *
 * This file is part of OSM Contributor.
 *
 * OSM Contributor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OSM Contributor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OSM Contributor.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.jawg.h2geo.parser;

import java.util.List;

/**
 * A Tag parser parses key / values tags and guesses out which type the Tag is.
 */
public interface TagParser extends Comparable<TagParser> {

    interface Priority {
        int LOWEST = 100;
        int LOW = 75;
        int NORMAL = 50;
        int HIGH = 25;
        int HIGHEST = 0;        
    }
    
    Type getType();

    boolean isCandidate(String key, List<String> values);

    boolean supports(String value);

    int getPriority();

    @Override
    default int compareTo(TagParser o) {
        return o.getPriority() - getPriority();
    }
    
}

