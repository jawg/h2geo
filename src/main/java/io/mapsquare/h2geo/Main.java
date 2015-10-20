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
package io.mapsquare.h2geo;

import java.io.File;
import java.io.FileOutputStream;

public class Main {

    public static void main(String... args) throws Exception {
        File outFile = new File(args.length > 0 ? args[0] : "h2geo_output.json");
        FileOutputStream out = new FileOutputStream(outFile);
        out.write(new H2GeoScrapper().scrapeTypes().getBytes());
        System.out.println("saved result in " + outFile.getAbsolutePath());
    }

}

