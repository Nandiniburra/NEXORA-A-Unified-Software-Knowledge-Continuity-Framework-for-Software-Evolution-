import React from 'react';

interface ImpactedClass {
  name: string;
  type: 'direct' | 'indirect';
  distance: number;
  affectedMethods: number;
}

interface ImpactAnalysisViewerProps {
  sourceClass: string;
  impactedClasses: ImpactedClass[];
  totalImpact: number;
}

export const ImpactAnalysisViewer: React.FC<ImpactAnalysisViewerProps> = ({
  sourceClass,
  impactedClasses,
  totalImpact
}) => {
  const directImpact = impactedClasses.filter(c => c.type === 'direct').length;
  const indirectImpact = impactedClasses.filter(c => c.type === 'indirect').length;

  return (
    <div className="space-y-6">
      {/* Impact Summary */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-red-50 rounded-lg shadow p-6 border border-red-200">
          <h3 className="text-sm font-medium text-red-700 mb-2">Source Class</h3>
          <p className="text-xl font-bold text-red-900 font-mono">{sourceClass}</p>
        </div>
        <div className="bg-orange-50 rounded-lg shadow p-6 border border-orange-200">
          <h3 className="text-sm font-medium text-orange-700 mb-2">Direct Impact</h3>
          <p className="text-3xl font-bold text-orange-900">{directImpact}</p>
          <p className="text-xs text-orange-600 mt-1">Directly impacted classes</p>
        </div>
        <div className="bg-yellow-50 rounded-lg shadow p-6 border border-yellow-200">
          <h3 className="text-sm font-medium text-yellow-700 mb-2">Indirect Impact</h3>
          <p className="text-3xl font-bold text-yellow-900">{indirectImpact}</p>
          <p className="text-xs text-yellow-600 mt-1">Transitively impacted classes</p>
        </div>
      </div>

      {/* Impact Details */}
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <div className="p-6 border-b border-gray-200">
          <h3 className="text-lg font-bold text-gray-800">Impact Analysis Details</h3>
          <p className="text-sm text-gray-600 mt-1">Total Affected Classes: {totalImpact}</p>
        </div>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Class Name</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Impact Type</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Distance</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Affected Methods</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Risk Level</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {impactedClasses.map((impact, idx) => {
                const riskLevel = 
                  impact.type === 'direct' ? 'High' : 
                  impact.distance <= 2 ? 'Medium' : 'Low';
                
                return (
                  <tr key={idx} className="hover:bg-gray-50">
                    <td className="px-6 py-4 text-sm font-mono text-gray-900">{impact.name}</td>
                    <td className="px-6 py-4 text-sm">
                      <span className={`px-3 py-1 rounded-full text-xs font-semibold ${
                        impact.type === 'direct' 
                          ? 'bg-red-100 text-red-800'
                          : 'bg-yellow-100 text-yellow-800'
                      }`}>
                        {impact.type === 'direct' ? 'Direct' : 'Indirect'}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sm text-gray-900">{impact.distance}</td>
                    <td className="px-6 py-4 text-sm text-gray-900">{impact.affectedMethods}</td>
                    <td className="px-6 py-4 text-sm">
                      <span className={`px-3 py-1 rounded-full text-xs font-semibold ${
                        riskLevel === 'High' ? 'bg-red-100 text-red-800' :
                        riskLevel === 'Medium' ? 'bg-yellow-100 text-yellow-800' :
                        'bg-green-100 text-green-800'
                      }`}>
                        {riskLevel}
                      </span>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
};

export default ImpactAnalysisViewer;
