package io.openaev.database.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

/**
 * Enumeration of the sub-field keys exposed by each structured {@link ContractOutputType}
 * processor.
 *
 * <p>When a {@link PayloadArgument} has a structured type (e.g. {@link ArgumentType#PortsScan},
 * {@link ArgumentType#Credentials}, {@link ArgumentType#CVE}, {@link ArgumentType#Asset}), the
 * {@code subtype} field on {@link PayloadArgument} can narrow the argument down to a single field
 * of that structured object.
 *
 * <p>The sub-type is <em>optional</em>: simple scalar types ({@link ArgumentType#Text}, {@link
 * ArgumentType#Number}, {@link ArgumentType#Port}, {@link ArgumentType#IPv4}, {@link
 * ArgumentType#IPv6}) have no fields in their processors and therefore never need a sub-type.
 *
 * <p>Field-to-type mapping (from output processors):
 *
 * <ul>
 *   <li>{@link ArgumentType#PortsScan} → {@link #AssetId}, {@link #Host}, {@link #Port}, {@link
 *       #Service}
 *   <li>{@link ArgumentType#Credentials} → {@link #Username}, {@link #Password}
 *   <li>{@link ArgumentType#CVE} → {@link #AssetId}, {@link #Id}, {@link #Host}, {@link #Severity}
 *   <li>{@link ArgumentType#Asset} → {@link #Name}, {@link #AssetType}, {@link #Description},
 *       {@link #ExternalReference}, {@link #Tags}, {@link #ExtendedAttributes}
 * </ul>
 */
public enum ArgumentSubType {

  // -- PortsScan & CVE shared --

  @JsonProperty("asset_id")
  AssetId("asset_id"),

  @JsonProperty("host")
  Host("host"),

  // -- PortsScan --

  @JsonProperty("port")
  Port("port"),

  @JsonProperty("service")
  Service("service"),

  // -- Credentials --

  @JsonProperty("username")
  Username("username"),

  @JsonProperty("password")
  Password("password"),

  // -- CVE --

  @JsonProperty("id")
  Id("id"),

  @JsonProperty("severity")
  Severity("severity"),

  // -- Asset --

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
