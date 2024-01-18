package com.bp3.enums;

import com.bp3.model.Node;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

import java.util.Arrays;

/**
 * The different types of {@link Node}s within a BPM process diagram.
 */
public enum NodeType {

  GATEWAY("Gateway"),
  END("End"),
  HUMAN_TASK("HumanTask"),
  SERVICE_TASK("ServiceTask"),
  START("Start");

  private String value;

  NodeType(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return this.value;
  }

  @JsonCreator
  public static NodeType fromValue(String value) {
    return Arrays.stream(NodeType.values()).filter(type -> type.value.equals(value))
        .findFirst().orElseThrow(() -> new IllegalArgumentException());
  }

}
