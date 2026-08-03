import React, { useEffect, useState } from 'react';
import { ComplexityBarChart, MetricsLineChart } from './MetricsCharts';

interface ComplexityData {
  method: string;
  complexity: number;
  lineCount: number;
}

interface ComplexityVisualizerProps {
  methodsData: ComplexityData[];
  threshold?: number;
}

export const ComplexityVisualizer: React.FC<ComplexityVisualizerProps> = ({ 
  methodsData, 
  threshold = 10 
}) => {
  const [filteredData, setFilteredData] = useState<any[]>([]);
  const [highComplexityCount, setHighComplexityCount] = useState(0);

  useEffect(() => {
    const data = methodsData
      .filter(m => m.complexity > threshold)
      .map(m => ({
        name: m.method,
        value: m.complexity,
        lineCount: m.lineCount
      }))
      .sort((a, b) => b.value - a.value);

    setFilteredData(data);
    setHighComplexityCount(data.length);
  }, [methodsData, threshold]);

  return (
    <div className="space-y-6">
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-red-50 rounded-lg p-4 border border-red-200">
          <p className="text-sm text-red-600 font-medium">High Complexity Methods</p>
          <p className="text-3xl font-bold text-red-700 mt-2">{highComplexityCount}</p>
          <p className="text-xs text-red-500 mt-1">Above threshold: {threshold}</p>
        </div>
        <div className="bg-yellow-50 rounded-lg p-4 border border-yellow-200">
          <p className="text-sm text-yellow-600 font-medium">Average Complexity</p>
          <p className="text-3xl font-bold text-yellow-700 mt-2">
            {(methodsData.reduce((sum, m) => sum + m.complexity, 0) / methodsData.length).toFixed(2)}
          </p>
        </div>
        <div className="bg-blue-50 rounded-lg p-4 border border-blue-200">
          <p className="text-sm text-blue-600 font-medium">Total Methods</p>
          <p className="text-3xl font-bold text-blue-700 mt-2">{methodsData.length}</p>
        </div>
      </div>

      <ComplexityBarChart 
        data={filteredData} 
        title="Method Complexity Distribution" 
      />

      <div className="bg-white rounded-lg shadow p-6">
        <h3 className="text-lg font-bold mb-4 text-gray-800">Complex Methods Details</h3>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Method Name</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Complexity</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Lines</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {filteredData.map((item, idx) => (
                <tr key={idx} className="hover:bg-gray-50">
                  <td className="px-6 py-4 text-sm text-gray-900 font-mono">{item.name}</td>
                  <td className="px-6 py-4 text-sm text-gray-900">{item.value}</td>
                  <td className="px-6 py-4 text-sm text-gray-900">{item.lineCount}</td>
                  <td className="px-6 py-4 text-sm">
                    <span className={`px-3 py-1 rounded-full text-xs font-semibold ${
                      item.value > 20 ? 'bg-red-100 text-red-800' :
                      item.value > 15 ? 'bg-yellow-100 text-yellow-800' :
                      'bg-orange-100 text-orange-800'
                    }`}>
                      {item.value > 20 ? 'Critical' : item.value > 15 ? 'High' : 'Moderate'}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default ComplexityVisualizer;
