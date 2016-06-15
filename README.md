# h2geo

[![Build Status](https://travis-ci.org/jawg/h2geo.svg?branch=master)](https://travis-ci.org/jawg/h2geo)

h2geo (HitchHiker's Guide to OpenStreetMap) allows the creation of a **data type descriptor** for OpenStreetMap.  
Its purpose is to provide an **easy** & **localized** search for data types.

To better understand h2geo, check out this generated [page](http://jawg.github.io/h2geo) 
where you can see all the data types validated by h2geo and the others.

=====

### Why are we doing this?
OpenStreetMap is the most awesome OpenSource database of the world, with over 2 million contributors.  

	OpenStreetMap uses tags to add meaning to geographic objects. 
	There is no fixed list of those tags. New tags can be invented and used as needed. Everybody can come up with a new tag and add it to new or existing objects. 
	
	This makes OpenStreetMap enormously flexible, but sometimes also a bit hard to work with.
	Whether you are contributing to OSM or using the OSM data, there are always questions like:  
	What tags do people use for feature X?  
	What tags can I use for feature Y so that it appears properly on the map?  
	Is the tag Z described on the wiki actually in use and where?  
*(source: taginfo.openstreetmap.org)*

Ex: 
*When I add a <b>Pharmacy</b>, I can either add a <b>shop=pharmacy</b> or <b>amenity=pharmacy</b>. Which one should be used?*


=========

When such questions are raised, two elements come in handy:  
 * The [Wiki](http://wiki.openstreetmap.org/wiki/Tag:shop%3Dpharmacy) which lays out the main OSM data types.
 * [Taginfo](http://taginfo.openstreetmap.org) which proposes statistics about all tags (which one is most used etc...) for making the right choice.

By definition, OpenStreetMap **lacks standards**, because it has been thought as a **completely open database**, to evolve with its time (ex: arrival of shop=e-cigarette).  
Most of the time, it is a **good** thing. Sometimes though, standards are good, especially for beginners.

=====

Our proposal is to place the wiki back as the **primary reference** for presets, and provide a mapping file enabling **easy** & **localized** search for data types.

=====

### What h2geo is
h2geo is basically a **json file**.  
This file contains a "machine-readable" description of all the presets of the **wiki**, crossed with **taginfo** stats and **wikidata** translations.  
For each preset, you have a **name** (amenity=pharmacy), a **label** ("Pharmacy"), a **description** ("A shop where a pharmacist sells medications"), a **usageCount** (number of occurences in OSM), **keywords** ("pharmacy", "drugstore", "drug store"), and of course the tags.
All label, description and keywords are **i18n compliant**.  

Therefore, h2geo enables you to implement an **easy** data-type search engine in your own locale.

#### The dream?  
A french guy typing *"Pharmacie"* will only get **one** result, which is the most common amenity=pharmacy.  
An italian guy *"Farmazia"* will get the **same** result.  
No duplicates, no "false-positive" results.

**h2geo** will also provide a nice **overview** of the Wiki, and will hopefully help to guarantee a **better QA** for it.

### What h2geo is not
h2geo is not meant to have **all** data-types of OpenStreetMap.  
Its purpose is to provide an easy-to-find tool for the **most-common** data types.  
Only `amenity`, `shop`, `highway`, `tourism`, `historic`, `emergency` tag keys are taken into account.

=====

### Get it
For now, the h2geo.json is available through GitHub pages:  
[Get it here](http://jawg.github.io/h2geo/h2geo.json)

### Run the application !
With gradle, result is output in `build/site/h2geo.json` and `build/site/h2geo_errors.json`:

    ./gradlew run

With maven , result is output in `h2geo.json` and `h2geo_errors.json`:

    mvn clean compile assembly:single
    java -jar target/h2geo-{version}-jar-with-dependencies.jar


### File format
    {
        "h2GeoVersion": "0.5.0-SNAPSHOT",
        "generationDate": "2015-12-09T11:31:29.858",
        "data": [
            {
                "name":"a technical name for the type",
                "label":{
                    "en":"the english display name of the type",
                    "fr":"le nom du type",
                    ...
                    }
                "description":{
                    "en":"the english description of the type",
                    "fr":"la description du type",
                    ...
                    }
                "keywords":{
                    "en":["keywords", "associated", "to", "the", "type"],
                    "fr":["mot-clés", "associés", "au", "type"],
                    ...
                    },
                "usageCount": 12345,
                "tags": [
                        {
                            "key":"tag key that should always be set for the type",
                            "value":"value to be set"
                        },
                        {
                            "key":"tag key than can be set for the type"
                        },
                        {
                            "key":"tag key than can be set for the type",
                            "possibleValues": ["possible", "values"]
                        },
                        { ... }
                    ]
            },
            { ... }
        ]
    }

### How it works
The program fetches data from [Tagsinfo](http://taginfo.openstreetmap.org/) and [Wikidata](https://www.wikidata.org/)
and aggregates it for create the json file. The data fetched from TagsInfo comes from the
[OpenStreetMap Wiki](http://wiki.openstreetmap.org). If the generated json file is incomplete or inaccurate, a simple
edit on the wiki should be just what's needed to correct it. We use Wikidata to add translations for the types names and
description. At the moment, we retrieve all possible values of tag keys `amenity`, `shop`, `highway`, `tourism`,
`historic`, `emergency` that have an associated wikipage,  can be applied on nodes and have an associated Wikidata entry.

=====

### How to improve the file
Just edit the osm wiki ! if you want to add a major type let us know with an issue (or make a pull request)

### They are using it:
[osm-contributor](https://github.com/jawg/osm-contributor)

### Contributors
This app is actively developed by:

 + [fredszaq](https://github.com/fredszaq)
 + [sdesprez](https://github.com/sdesprez)
 + [nicolasfavier](https://github.com/nicolasfavier)
 + [loicortola](https://github.com/loicortola)

We welcome any contributors with issues / pull requests.

### Tweet about it
Contact the team: @jawgio

### License
Copyright 2016 Jawg

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
