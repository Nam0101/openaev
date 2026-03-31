package io.openaev.database.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;

/**
 * Enumeration of the sub-field keys exposed by each structured {@link ContractOutputType}
 * processor.
 *
 * <p>When a {@link PayloadArgument} has a structured type (e.g. {@link ArgumentType#PortsScan},
 * {@link ArgumentType#Credentials}, {@link ArgumentType#CVE}), the {@code subtype} field on {@link
 * PayloadArgument} can narrow the argument down to a single field of that structured object.
 *
 * <p>The sub-type is <em>optional</em>: simple scalar types ({@link ArgumentType#Text}, {@link
 * ArgumentType#Number}, {@link ArgumentType#Port}, {@link ArgumentType#IPv4}, {@link
 * ArgumentType#IPv6}) have no fields in their processors and therefore never need a sub-type.
 *
 * <p>Field-to-type mapping (from output processors):
 *
 * <ul>
 *   <li>{@link ArgumentType#PortsScan} → {@link #Host}, {@link #Port}, {@link #Service}
 *   <li>{@link ArgumentType#Credentials} → {@link #Username}, {@link #Password}
 *   <li>{@link ArgumentType#CVE} → {@link #Id}, {@link #Host}, {@link #Severity}
 * </ul>
 */
public enum ArgumentSubType {

  // -- PortsScan & CVE shared --

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

  @JsonProperty("severity")
  Severity("severity");

  public final String label;

  ArgumentSubType(String label) {
    this.label = label;
  }

  /**
   * Looks up an {@link ArgumentSubType} by its JSON label. Used for manual JSON parsing (e.g.
   * {@code PayloadUtils.buildPayload}). Jackson uses the {@code @JsonProperty} annotations on the
   * constants directly.
   *
   * @param label the raw string value
   * @return the matching enum constant
   * @throws IllegalArgumentException when no constant matches
   */
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
