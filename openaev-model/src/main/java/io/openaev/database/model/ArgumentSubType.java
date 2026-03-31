package io.openaev.database.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

/**
 * Enumeration of the sub-field keys exposed by each type of {@link PayloadArgument}.
 *
 * <p>The sub-type is <em>optional</em>: simple scalar types ({@link ArgumentType#Text}, {@link
 * ArgumentType#Number}, {@link ArgumentType#Port}, {@link ArgumentType#IPv4}, {@link
 * ArgumentType#IPv6}) have no subtypes.
 */
public enum ArgumentSubType {
  @JsonProperty("asset_id")
  AssetId("asset_id"),

  @JsonProperty("host")
  Host("host"),

  @JsonProperty("port")
  Port("port"),

  @JsonProperty("service")
  Service("service"),

  @JsonProperty("username")
  Username("username"),

  @JsonProperty("password")
  Password("password"),

  @JsonProperty("id")
  Id("id"),

  @JsonProperty("severity")
  Severity("severity"),

  @JsonProperty("name")
  Name("name"),

  @JsonProperty("type")
  AssetType("type"),

  @JsonProperty("description")
  Description("description"),

  @JsonProperty("external_reference")
  ExternalReference("external_reference"),

  @JsonProperty("tags")
  Tags("tags"),

  @JsonProperty("extended_attributes")
  ExtendedAttributes("extended_attributes");

  private final String label;

  ArgumentSubType(String label) {
    this.label = label;
  }

  /**
   * Returns the JSON/wire key for this sub-type (e.g. {@code "asset_id"}, {@code "username"}).
   *
   * @return serialised label
   */
  @JsonValue
  public String getLabel() {
    return label;
  }

  /**
   * Deserialises an {@link ArgumentSubType} from its JSON key.
   *
   * @param label the raw string value from JSON
   * @return the matching enum constant
   * @throws IllegalArgumentException when no constant matches
   */
  @JsonCreator
  public static ArgumentSubType fromLabel(String label) {
    return Arrays.stream(values())
        .filter(v -> v.label.equals(label))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Unknown ArgumentSubType label: '"
                        + label
                        + "'. Valid values: "
                        + Arrays.toString(values())));
  }
}
