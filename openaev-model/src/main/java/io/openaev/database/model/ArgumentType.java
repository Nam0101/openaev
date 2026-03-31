package io.openaev.database.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.Arrays;

/**
 * Enumeration of supported argument types for {@link PayloadArgument}.
 *
 * <p>This enum mirrors {@link ContractOutputType} and additionally exposes the two field types used
 * exclusively as payload arguments: {@link #Document} and {@link #TargetedAsset}.
 *
 * <p>Types that expose sub-fields (e.g. {@link #PortsScan}, {@link #Credentials}) can pair with an
 * {@link ArgumentSubType} to address a specific field. Types whose processor has an empty field
 * list ({@link #Text}, {@link #Number}, {@link #Port}, {@link #IPv4}, {@link #IPv6}) do not require
 * a sub-type.
 */
public enum ArgumentType {

  // -- Mirror of ContractOutputType --

  @JsonProperty("text")
  Text("text"),

  @JsonProperty("number")
  Number("number"),

  @JsonProperty("port")
  Port("port"),

  @JsonProperty("portscan")
  PortsScan("portscan"),

  @JsonProperty("ipv4")
  IPv4("ipv4"),

  @JsonProperty("ipv6")
  IPv6("ipv6"),

  @JsonProperty("credentials")
  Credentials("credentials"),

  @JsonProperty("cve")
  CVE("cve"),

  @Hidden
  @JsonProperty("asset")
  Asset("asset"),

  // -- Argument-only types --

  @JsonProperty("document")
  Document("document"),

  @JsonProperty("targeted-asset")
  TargetedAsset("targeted-asset");

  private final String label;

  ArgumentType(String label) {
    this.label = label;
  }

  /**
   * Returns the JSON/wire value of this type (e.g. {@code "text"}, {@code "targeted-asset"}).
   *
   * @return serialised label
   */
  @JsonValue
  public String getLabel() {
    return label;
  }

  /**
   * Deserialises an {@link ArgumentType} from its JSON label.
   *
   * @param label the raw string value from JSON
   * @return the matching enum constant
   * @throws IllegalArgumentException when no constant matches
   */
  @JsonCreator
  public static ArgumentType fromLabel(String label) {
    return Arrays.stream(values())
        .filter(v -> v.label.equals(label))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Unknown ArgumentType label: '"
                        + label
                        + "'. Valid values: "
                        + Arrays.toString(values())));
  }
}
