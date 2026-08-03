import React from 'react';
import { MetricsPieChart } from './MetricsCharts';

interface QualityMetrics {
  documentationScore: number;
  testCoverage: number;
  maintainability: number;
  complexity: number;
  duplication: number;
}

interface CodeQualityDashboardProps {
  metrics: QualityMetrics;
  repository: string;
}

const getQualityScore = (metrics: QualityMetrics): number => {
  return Math.round(
    (metrics.documentationScore * 0.2 +
     metrics.testCoverage * 0.25 +
     metrics.maintainability * 0.25 +
     (100 - metrics.complexity) * 0.2 +
     (100 - metrics.duplication) * 0.1) / 5
  );
};

const getQualityColor = (score: number): string => {
  if (score >= 80) return 'text-green-600';
  if (score >= 60) return 'text-yellow-600';
  return 'text-red-600';
};

const getQualityBg = (score: number): string => {
  if (score >= 80) return 'bg-green-50 border-green-200';
  if (score >= 60) return 'bg-yellow-50 border-yellow-200';
  return 'bg-red-50 border-red-200';
};

export const CodeQualityDashboard: React.FC<CodeQualityDashboardProps> = ({ 
  metrics, 
  repository 
}) => {
  const score = getQualityScore(metrics);

  const scoreData = [
    { name: 'Documentation', value: metrics.documentationScore },
    { name: 'Test Coverage', value: metrics.testCoverage },
    { name: 'Maintainability', value: metrics.maintainability },
    { name: 'Complexity (inv)', value: 100 - metrics.complexity },
  ];

  return (
    <div className="space-y-6">
      {/* Overall Quality Score */}
      <div className={`rounded-lg shadow p-8 border-2 text-center ${getQualityBg(score)}`}>
        <p className="text-gray-600 text-sm font-medium mb-2">Code Quality Score</p>
        <p className={`text-5xl font-bold ${getQualityColor(score)}`}>{score}</p>
        <p className="text-gray-500 text-sm mt-2">Repository: {repository}</p>
        <div className="mt-4">
          <div className="w-full bg-gray-200 rounded-full h-2">
            <div
              className={`h-2 rounded-full ${
                score >= 80 ? 'bg-green-600' :
                score >= 60 ? 'bg-yellow-600' :
                'bg-red-600'
              }`}
              style={{ width: `${score}%` }}
            />
          </div>
        </div>
      </div>

      {/* Metrics Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
        <div className="bg-white rounded-lg shadow p-4">
          <p className="text-sm text-gray-600 font-medium mb-2">Documentation</p>
          <p className="text-3xl font-bold text-blue-600">{metrics.documentationScore}%</p>
          <div className="mt-2 bg-gray-200 rounded-full h-2">
            <div className="bg-blue-600 h-2 rounded-full" style={{ width: `${metrics.documentationScore}%` }} />
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-4">
          <p className="text-sm text-gray-600 font-medium mb-2">Test Coverage</p>
          <p className="text-3xl font-bold text-green-600">{metrics.testCoverage}%</p>
          <div className="mt-2 bg-gray-200 rounded-full h-2">
            <div className="bg-green-600 h-2 rounded-full" style={{ width: `${metrics.testCoverage}%` }} />
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-4">
          <p className="text-sm text-gray-600 font-medium mb-2">Maintainability</p>
          <p className="text-3xl font-bold text-purple-600">{metrics.maintainability}%</p>
          <div className="mt-2 bg-gray-200 rounded-full h-2">
            <div className="bg-purple-600 h-2 rounded-full" style={{ width: `${metrics.maintainability}%` }} />
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-4">
          <p className="text-sm text-gray-600 font-medium mb-2">Complexity</p>
          <p className="text-3xl font-bold text-orange-600">{100 - metrics.complexity}%</p>
          <div className="mt-2 bg-gray-200 rounded-full h-2">
            <div className="bg-orange-600 h-2 rounded-full" style={{ width: `${100 - metrics.complexity}%` }} />
          </div>
        </div>
      </div>

      {/* Pie Chart */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div>
          <MetricsPieChart data={scoreData} title="Quality Metrics Breakdown" />
        </div>
        <div className="bg-white rounded-lg shadow p-6">
          <h3 className="text-lg font-bold mb-4 text-gray-800">Recommendations</h3>
          <ul className="space-y-3 text-sm text-gray-700">
            {metrics.documentationScore < 60 && (
              <li className="flex items-start">
                <span className="text-yellow-500 mr-2">⚠️</span>
                <span>Improve documentation coverage - Currently at {metrics.documentationScore}%</span>
              </li>
            )}
            {metrics.testCoverage < 70 && (
              <li className="flex items-start">
                <span className="text-yellow-500 mr-2">⚠️</span>
                <span>Increase test coverage - Currently at {metrics.testCoverage}%</span>
              </li>
            )}
            {metrics.complexity > 50 && (
              <li className="flex items-start">
                <span className="text-red-500 mr-2">❌</span>
                <span>Refactor complex methods to reduce cyclomatic complexity</span>
              </li>
            )}
            {metrics.maintainability < 50 && (
              <li className="flex items-start">
                <span className="text-red-500 mr-2">❌</span>
                <span>Major refactoring needed to improve maintainability</span>
              </li>
            )}
            {score >= 80 && (
              <li className="flex items-start">
                <span className="text-green-500 mr-2">✓</span>
                <span>Excellent code quality! Continue maintaining these standards.</span>
              </li>
            )}
          </ul>
        </div>
      </div>
    </div>
  );
};

export default CodeQualityDashboard;
