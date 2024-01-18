import {Component } from '@angular/core';
import { FlowShapeModel, NodeModel, OrthogonalSegmentModel } from '@syncfusion/ej2-angular-diagrams';
import {BusinessProcessService} from './services/business-process.service';
import {ConnectorModel} from '@syncfusion/ej2-diagrams/src/diagram/objects/connector-model';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  terminatorShape: FlowShapeModel = {type: 'Flow', shape: 'Terminator'};
  processShape: FlowShapeModel = {type: 'Flow', shape: 'Process'};
  decisionShape: FlowShapeModel = {type: 'Flow', shape: 'Decision'};

  rightToBottom: OrthogonalSegmentModel = [
    {
      direction: 'Right', length: 40
    },
    {
      direction: 'Bottom', length: 100
    }
  ];

  leftToBottom: OrthogonalSegmentModel = [
    {
      direction: 'Left', length: 40
    },
    {
      direction: 'Bottom', length: 200
    }
  ];

  nodes: any[] = [];
  connectors: any[] = [];
  processNodes: any[];
  processConnectors: any[];

  constructor(private businessProcessService: BusinessProcessService) {}

  public defaultNodeValues(defaultNode: NodeModel): NodeModel {
    defaultNode.height = 50;
    defaultNode.width = 200;
    defaultNode.offsetX = 800;
    return defaultNode;
  }

  // tslint:disable-next-line:use-lifecycle-interface
  ngOnInit() {
    const processedTask = this.businessProcessService.getProcessedTask();
    this.processNodes = processedTask.nodes;
    this.processConnectors = processedTask.edges;
    console.log('ProcessedTask:', processedTask);
    console.log('Nodes:', this.processNodes);
    console.log('Connectors:', this.processConnectors);
    this.generateBusinessProcessDiagram();
  }

  generateBusinessProcessDiagram(): any {
    const offsetX = 800;
    let offsetY = 100;

    this.processNodes.forEach(node1 => {
      const generatedNode: NodeModel = {
        id: node1.id + '_node', width: 140, height: 50, offsetX: offsetX, offsetY: offsetY,
        annotations: [{
          content: node1.name
        }],
        shape: this.getFlowShapeModelByNodeType(node1.type)
      };
      this.nodes.push(generatedNode);
      offsetY = offsetY + 100;
    });

    let gatewayConnectorId = null;
    this.processConnectors.forEach(connector1 => {
      const generatedConnector: ConnectorModel = {
          sourceID: connector1.from + '_node',
          targetID: connector1.to + '_node',
          type: 'Orthogonal',
        // Space between source point and source object
        sourcePadding: 100,
        // Space between target point and target object
        targetPadding: 50
      };
      if (this.isGatewayConnector(connector1.from)) {
         if (gatewayConnectorId == null) {
            // @ts-ignore
            generatedConnector.segments = this.rightToBottom;
            gatewayConnectorId = connector1.from;
          } else {
            // @ts-ignore
            generatedConnector.segments = this.leftToBottom;
          }


       }
      this.connectors.push(generatedConnector);
    });
  }

  getFlowShapeModelByNodeType(type: string) {
    const typeToShapeMap : Map<string, FlowShapeModel> = new Map<string, FlowShapeModel>([
      ['Start', this.terminatorShape],
      ['HumanTask', this.processShape],
      ['Gateway', this.decisionShape],
      ['End', this.terminatorShape],
    ]);
    return typeToShapeMap.get(type);
  }

  isGatewayConnector(from: string): boolean {
    return this.processNodes.find(o => o.id == from).type == 'Gateway';
  }

  get outcomeNodes() {
    return JSON.stringify(this.processNodes, null, 2);
  }

  set outcomeNodes(v) {
    try {
      this.processNodes = JSON.parse(v);
    } catch (e) {
      console.log('error occored while you were typing the JSON');
    }
  }

  get outcomeEdges() {
    return JSON.stringify(this.processConnectors, null, 2);
  }

  set outcomeEdges(v) {
    try {
      this.processConnectors = JSON.parse(v);
    } catch (e) {
      console.log('error occored while you were typing the JSON');
    }
  }
}
