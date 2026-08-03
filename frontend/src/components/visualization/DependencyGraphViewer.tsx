import React, { useEffect, useRef } from 'react';
import cytoscape from 'cytoscape';

interface Node {
  id: string;
  label: string;
  type: 'class' | 'method' | 'dependency';
}

interface Edge {
  source: string;
  target: string;
  type: string;
}

interface DependencyGraphProps {
  nodes: Node[];
  edges: Edge[];
  title: string;
}

export const DependencyGraphViewer: React.FC<DependencyGraphProps> = ({ nodes, edges, title }) => {
  const cyRef = useRef<HTMLDivElement>(null);
  const cyInstance = useRef<any>(null);

  useEffect(() => {
    if (!cyRef.current) return;

    // Prepare elements for cytoscape
    const elements = [
      ...nodes.map(node => ({
        data: {
          id: node.id,
          label: node.label,
          type: node.type
        }
      })),
      ...edges.map(edge => ({
        data: {
          source: edge.source,
          target: edge.target,
          label: edge.type
        }
      }))
    ];

    // Initialize cytoscape
    cyInstance.current = cytoscape({
      container: cyRef.current,
      elements: elements,
      style: [
        {
          selector: 'node',
          style: {
            'content': 'data(label)',
            'text-valign': 'center',
            'text-halign': 'center',
            'background-color': '#0284c7',
            'color': '#fff',
            'border-width': 2,
            'border-color': '#0369a1',
            'width': '60px',
            'height': '60px',
            'font-size': '10px',
            'text-wrap': 'wrap'
          }
        },
        {
          selector: 'node[type="method"]',
          style: {
            'background-color': '#059669'
          }
        },
        {
          selector: 'node[type="dependency"]',
          style: {
            'background-color': '#dc2626'
          }
        },
        {
          selector: 'edge',
          style: {
            'line-color': '#cccccc',
            'target-arrow-color': '#cccccc',
            'target-arrow-shape': 'triangle',
            'curve-style': 'bezier',
            'width': 2
          }
        }
      ],
      layout: {
        name: 'cose',
        directed: true,
        animate: true,
        animationDuration: 500
      }
    });

    // Add zoom and pan controls
    cyInstance.current.on('tap', 'node', (evt: any) => {
      evt.target.select();
    });

    return () => {
      cyInstance.current?.destroy();
    };
  }, [nodes, edges]);

  return (
    <div className="bg-white rounded-lg shadow overflow-hidden">
      <div className="p-4 border-b border-gray-200">
        <h3 className="text-lg font-bold text-gray-800">{title}</h3>
      </div>
      <div ref={cyRef} style={{ height: '600px', width: '100%' }} />
      <div className="p-4 bg-gray-50 text-sm text-gray-600">
        <p>💡 Drag to pan • Scroll to zoom • Click nodes for details</p>
      </div>
    </div>
  );
};

export default DependencyGraphViewer;
