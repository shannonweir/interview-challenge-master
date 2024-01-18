package com.bp3.model;

import java.util.List;

public class Task {

  private List<TaskNode> nodes;
  private List<TaskEdge> edges;

  public List<TaskNode> getNodes() {
    return nodes;
  }

  public void setNodes(List<TaskNode> nodes) {
    this.nodes = nodes;
  }

  public List<TaskEdge> getEdges() {
    return edges;
  }

  public void setEdges(List<TaskEdge> edges) {
    this.edges = edges;
  }
}
