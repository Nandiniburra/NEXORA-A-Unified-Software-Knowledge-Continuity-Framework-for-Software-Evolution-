import React, { useState } from 'react';

interface MethodNode {
  id: string;
  name: string;
  complexity: number;
  calledBy: MethodNode[];
  calls: MethodNode[];
}

interface MethodCallHierarchyProps {
  rootMethod: MethodNode;
}

const TreeNode: React.FC<{ node: MethodNode; level: number }> = ({ node, level }) => {
  const [isExpanded, setIsExpanded] = useState(level === 0);

  return (
    <div className="font-mono text-sm">
      <div 
        className="flex items-center py-1 px-2 hover:bg-blue-50 rounded cursor-pointer"
        onClick={() => setIsExpanded(!isExpanded)}
        style={{ paddingLeft: `${level * 20 + 8}px` }}
      >
        {(node.calls && node.calls.length > 0) && (
          <span className="mr-2 w-4 text-center">{isExpanded ? '▼' : '▶'}</span>
        )}
        {(!node.calls || node.calls.length === 0) && (
          <span className="mr-2 w-4">•</span>
        )}
        <span className="text-blue-600 font-semibold">{node.name}</span>
        <span className={`ml-2 px-2 py-0.5 rounded text-xs font-bold ${
          node.complexity > 10 ? 'bg-red-100 text-red-700' :
          node.complexity > 5 ? 'bg-yellow-100 text-yellow-700' :
          'bg-green-100 text-green-700'
        }`}>
          C: {node.complexity}
        </span>
      </div>
      {isExpanded && node.calls && node.calls.map((child) => (
        <TreeNode key={child.id} node={child} level={level + 1} />
      ))}
    </div>
  );
};

export const MethodCallHierarchy: React.FC<MethodCallHierarchyProps> = ({ rootMethod }) => {
  return (
    <div className="bg-white rounded-lg shadow p-6">
      <h3 className="text-lg font-bold mb-4 text-gray-800">Method Call Hierarchy</h3>
      <div className="bg-gray-50 p-4 rounded border border-gray-200 overflow-x-auto">
        <TreeNode node={rootMethod} level={0} />
      </div>
    </div>
  );
};

export default MethodCallHierarchy;
