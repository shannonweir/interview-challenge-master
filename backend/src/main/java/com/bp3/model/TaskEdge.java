package com.bp3.model;

public class TaskEdge implements Edge {

  private String from;
  private String to;

  @Override
  public String getFrom() {
    return this.from;
  }

  @Override
  public String getTo() {
    return this.to;
  }

  @Override
  public void setFrom(String nodeId) {
    this.from = nodeId;
  }

  @Override
  public void setTo(String nodeId) {
    this.to = nodeId;
  }
}
