import React, { useState } from 'react';

interface CodeFile {
  name: string;
  path: string;
  extension: string;
  lines: number;
  complexity: number;
  children?: CodeFile[];
}

interface CodeBrowserProps {
  fileTree: CodeFile[];
  onFileSelect?: (file: CodeFile) => void;
}

const FileTreeNode: React.FC<{
  file: CodeFile;
  level: number;
  onSelect?: (file: CodeFile) => void;
}> = ({ file, level, onSelect }) => {
  const [isExpanded, setIsExpanded] = useState(level === 0);
  const hasChildren = file.children && file.children.length > 0;
  const isDirectory = !file.extension;

  return (
    <div>
      <div
        className="flex items-center py-1 px-2 hover:bg-blue-50 rounded cursor-pointer text-sm"
        style={{ paddingLeft: `${level * 16 + 8}px` }}
        onClick={() => {
          if (isDirectory) setIsExpanded(!isExpanded);
          if (onSelect && !isDirectory) onSelect(file);
        }}
      >
        {isDirectory && (
          <span className="mr-2 w-4 text-center text-gray-600">
            {isExpanded ? '📂' : '📁'}
          </span>
        )}
        {!isDirectory && (
          <span className="mr-2 w-4 text-center">📄</span>
        )}
        <span className="text-gray-800 font-medium">{file.name}</span>
        {!isDirectory && file.lines && (
          <span className="ml-2 text-xs text-gray-500">({file.lines} lines)</span>
        )}
      </div>
      {isExpanded && hasChildren && file.children?.map((child, idx) => (
        <FileTreeNode key={idx} file={child} level={level + 1} onSelect={onSelect} />
      ))}
    </div>
  );
};

export const CodeBrowser: React.FC<CodeBrowserProps> = ({ fileTree, onFileSelect }) => {
  const [selectedFile, setSelectedFile] = useState<CodeFile | null>(null);

  const handleSelect = (file: CodeFile) => {
    setSelectedFile(file);
    onFileSelect?.(file);
  };

  return (
    <div className="grid grid-cols-1 lg:grid-cols-3 gap-4 h-full">
      {/* File Tree */}
      <div className="lg:col-span-1 bg-white rounded-lg shadow p-4">
        <h3 className="text-lg font-bold mb-4 text-gray-800">Project Structure</h3>
        <div className="overflow-y-auto border border-gray-200 rounded p-2 space-y-0.5">
          {fileTree.map((file, idx) => (
            <FileTreeNode key={idx} file={file} level={0} onSelect={handleSelect} />
          ))}
        </div>
      </div>

      {/* File Details */}
      <div className="lg:col-span-2 bg-white rounded-lg shadow p-4">
        {selectedFile ? (
          <div>
            <div className="mb-4 pb-4 border-b border-gray-200">
              <h3 className="text-lg font-bold text-gray-800">{selectedFile.name}</h3>
              <p className="text-sm text-gray-600 font-mono">{selectedFile.path}</p>
              <div className="mt-2 flex gap-4 text-sm">
                <span className="text-gray-600">Lines: <span className="font-bold">{selectedFile.lines}</span></span>
                <span className="text-gray-600">Complexity: <span className="font-bold text-orange-600">{selectedFile.complexity}</span></span>
              </div>
            </div>
            <div className="text-gray-600 text-center py-8">
              <p>Code preview would be displayed here</p>
              <p className="text-sm mt-2">Integrate with a code viewer component for full implementation</p>
            </div>
          </div>
        ) : (
          <div className="flex items-center justify-center h-full text-gray-500">
            <p>Select a file to view details</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default CodeBrowser;
