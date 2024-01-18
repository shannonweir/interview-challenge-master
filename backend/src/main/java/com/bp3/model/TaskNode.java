package com.bp3.model;

import com.bp3.enums.NodeType;

public class TaskNode implements Node {

  private String id;
  private String name;
  private NodeType type;

  @Override
  public String getId() {
    return this.id;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public NodeType getType() {
    return this.type;
  }

  @Override
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public void setType(NodeType type) {
    this.type = type;
  }

}
