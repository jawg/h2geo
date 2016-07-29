package io.jawg.h2geo.parser;

import io.jawg.h2geo.model.PoiTypeTag;
import io.jawg.h2geo.parser.impl.AutoCompleteTagParser;
import io.jawg.h2geo.parser.impl.NumberTagParser;
import io.jawg.h2geo.parser.impl.OpeningTimeTagParser;
import io.jawg.h2geo.parser.impl.SingleChoiceTagParser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Use the best UI widget based on the tag name and possible values.
 */
public enum Type {
  OPENING_HOURS,          // Use when tag value is opening_hours
  SINGLE_CHOICE,          // Use when tag value can be choose in a short list (< 7)
  CONSTANT,               // Use when tag value can't be modified (ex: type amenity)
  NUMBER,                 // Use when tag value is a number (ex: height, floors)
  TEXT;                   // Use by default

  private static final List<TagParser> PARSERS = Collections.unmodifiableList(Arrays.asList(
    new OpeningTimeTagParser(),
    new SingleChoiceTagParser(),
    new NumberTagParser(),
    new AutoCompleteTagParser()
  ));

  /**
   * This method parses the type of a tag.
   * @param tag the tag
   * @return the tag type
   */
  public static Type parse(PoiTypeTag tag) {
    String key = tag.getKey();
    String value = tag.getValue();
    List<String> values = tag.getValues();
    // If only one possible value, type is constant
    if (value == null && (values == null || values.isEmpty())) {
      return CONSTANT;
    }
    TagParser tagParser = PARSERS.stream().filter(p -> p.isCandidate(key, values)).findFirst().get();
    // If none found, type is text
    if (tagParser == null) {
      return Type.TEXT;
    }
    // If found, return type
    return tagParser.getType();
  }
}