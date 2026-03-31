package io.openaev.database.model;

import com.fasterxml.jackson.annotation.JsonProperty;
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

  // -- Argument-only types --

  @JsonProperty("document")
  Document("document"),

  @JsonProperty("targeted-asset")
  TargetedAsset("targeted-asset");

  public final String label;

  ArgumentType(String label) {
    this.label = label;
  }

  /**
   * Looks up an {@link ArgumentType} by its JSON label. Used for manual JSON parsing (e.g. {@code
   * PayloadUtils.buildPayload}). Jackson uses the {@code @JsonProperty} annotations on the
   * constants directly.
   *
   * @param label the raw string value
   * @return the matching enum constant
   * @throws IllegalArgumentException when no constant matches
   */
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
