package com.bp3.service;

import com.bp3.constants.Constants;
import com.bp3.enums.NodeType;
import com.bp3.model.Task;
import com.bp3.model.TaskEdge;
import com.bp3.model.TaskNode;
import org.apache.commons.collections4.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class BusinessProcessService {

  private static Logger log = LoggerFactory.getLogger(BusinessProcessService.class);
  private static final String PROCESSING_FILE = Constants.SIMPLE_FILE_NAME;

  public void processEdgesToNodes(Task task) {
    try{
      List<TaskNode> applicableNodes = task.getNodes().stream()
          .filter(node -> Arrays.asList(NodeType.START, NodeType.HUMAN_TASK, NodeType.GATEWAY, NodeType.END).contains(node.getType()))
          .collect(Collectors.toList());

      List<TaskEdge> edges = task.getEdges();
      List<TaskEdge> applicableEdges = new ArrayList<>();

      if (CollectionUtils.isNotEmpty(edges)) {
        Iterator<TaskEdge> iterator = edges.stream().iterator();
        while (iterator.hasNext()) {
          TaskEdge next = iterator.next();
          TaskEdge taskEdge = new TaskEdge();
          taskEdge.setFrom(next.getFrom());
          taskEdge.setTo(getTo(task, iterator, next));
          applicableEdges.add(taskEdge);
        }

        task.setNodes(applicableNodes);
        task.setEdges(applicableEdges);
      }
    } catch(Exception e) {
      log.error("Error occurred processing edges to nodes:", e);
    }
  }

  public Task readTasks() {
    try {
      Path path = Paths.get(Constants.DATA_DIRECTORY_BASE_URL.concat(PROCESSING_FILE).concat(Constants.JSON));
      ObjectMapper mapper = new ObjectMapper();
      Task tasks = mapper.readValue(Files.newInputStream(path), Task.class);
      return tasks;
    } catch (IOException e) {
      log.error("Error occurred reading tasks from json file:", e);
    }
    return null;
  }

  public void writeTasks(Task task) {
    try {
      ObjectMapper mapper = new ObjectMapper();
      String processedDate = new SimpleDateFormat("yyyyMMddHHmm'-processed'").format(new Date());
      mapper.writerWithDefaultPrettyPrinter().writeValue(new File(Constants.DATA_DIRECTORY_BASE_URL
          .concat(PROCESSING_FILE).concat("-").concat(processedDate).concat(Constants.JSON)), task);
    } catch (IOException e) {
      log.error("Error occurred writing tasks to json file:", e);
    }
  }

  private String getTo(Task task, Iterator<TaskEdge> iterator, TaskEdge edge) {
    if (!iterator.hasNext()) {
      return edge.getTo();
    }
    if (isApplicableNodeType(task.getNodes(), edge.getTo())) {
      return edge.getTo();
    }
    return getTo(task, iterator, iterator.next());
  }

  private boolean isApplicableNodeType(List<TaskNode> nodes, String id) {
    List<NodeType> applicableNodeTypes = Arrays.asList(NodeType.START, NodeType.HUMAN_TASK, NodeType.GATEWAY, NodeType.END);
    NodeType nodeType = nodes.stream()
        .filter(node -> node.getId().equals(id))
        .map(TaskNode::getType)
        .findFirst().orElse(null);
    return applicableNodeTypes.contains(nodeType);
  }
}
