import { Injectable } from '@angular/core';
import * as processData from '../../assets/3-branching-process.json';

@Injectable({ providedIn: 'root'})
export class BusinessProcessService {

  data: any = processData;
  index = 0;

  getProcessedTask(): any {
    const task = this.data.default;
    const nodes = task.nodes;
    const edges = task.edges;
    const applicableNodes = nodes.filter(node => ['Start', 'HumanTask', 'Gateway', 'End'].includes(node.type));

    if (edges.length > 0) {
      const applicableEdges = [];

      while (this.arrHasNext(edges, this.index)) {
        const next = edges[this.index];
        const taskEdge = {
          from: next.from,
          to: this.getTo(task, edges, this.index, next)
        };
        applicableEdges.push(taskEdge);
        this.index++;
      }

      task.nodes = applicableNodes;
      task.edges = applicableEdges;
    }
    return task;
  }

  getTo(task: any, edges: any[], index: number, edge: any): any {
    if (!this.arrHasNext(edges, index)) {
      return edge.to;
    }
    if (this.isApplicableNodeType(task.nodes, edge.to)) {
      return edge.to;
    }
    this.index = ++index;
    return this.getTo(task, edges, this.index, edges[this.index]);
  }

  arrHasNext(arr: any[], currentIndex: number): boolean {
    return arr.length > currentIndex;
  }

  isApplicableNodeType(nodes: any[], id: string): boolean {
    const applicableNodeTypes = ['Start', 'HumanTask', 'Gateway', 'End'];
    const node: any = nodes.filter(node1 => node1.id === id);
    return applicableNodeTypes.includes(node[0].type);
  }

}
