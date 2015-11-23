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

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;

public class Main {

    public static void main(String... args) throws Exception {
        File outFile = new File(args.length > 0 ? args[0] : "h2geo_output.json");
        File outErrorFile = new File(args.length > 1 ? args[1] : "h2geo_output_errors.json");
        FileOutputStream out = new FileOutputStream(outFile);
        FileOutputStream outError = new FileOutputStream(outErrorFile);
        H2GeoScrapper.Result result = new H2GeoScrapper().scrapeTypes();
        Gson gson = new Gson();
        out.write(gson.toJson(result.getTypes()).getBytes());
        outError.write(gson.toJson(result.getErrors()).getBytes());
        System.out.println("saved result in " + outFile.getAbsolutePath() +" and "+outErrorFile.getAbsolutePath());
    }

}

