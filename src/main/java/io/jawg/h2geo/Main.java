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
package io.jawg.h2geo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.jawg.h2geo.model.H2GeoRun;
import io.jawg.h2geo.model.ScrappingError;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

public class Main {

    public static void main(String... args) throws Exception {
        File outDir = new File(args.length > 0 ? args[0] : "build/site");
        if (!outDir.exists() && !outDir.isDirectory() && !outDir.mkdirs()) {
            System.out.println("could not create directory " + outDir.getAbsolutePath());
            return;
        }
        File outFile = new File(outDir, args.length > 1 ? args[1] : "h2geo.json");
        File outErrorFile = new File(outDir, args.length > 2 ? args[2] : "h2geo_errors.json");
        Gson gson = new GsonBuilder().registerTypeAdapter(ScrappingError.class, new ScrappingError.ScrappingErrorSerializer()).create();

        H2GeoScrapper.Result result = new H2GeoScrapper().scrapeTypes();

        H2GeoRun h2GeoRun = new H2GeoRun(BuildProperties.VERSION, LocalDateTime.now(), result.getTypes());
        H2GeoRun h2GeoErrorsRun = new H2GeoRun(BuildProperties.VERSION, LocalDateTime.now(), ScrappingError.asSet());

        try (FileOutputStream out = new FileOutputStream(outFile);
             FileOutputStream outError = new FileOutputStream(outErrorFile)) {
            out.write(gson.toJson(h2GeoRun).getBytes());
            outError.write(gson.toJson(h2GeoErrorsRun).getBytes());
        }
    }

}

