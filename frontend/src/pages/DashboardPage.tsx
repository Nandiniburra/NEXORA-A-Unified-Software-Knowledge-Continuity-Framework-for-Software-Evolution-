import React, { useEffect, useState } from 'react';
import dashboardService, { DashboardMetrics } from '../services/dashboardService';
import toast from 'react-hot-toast';

const DashboardPage: React.FC = () => {
  const [metrics, setMetrics] = useState<DashboardMetrics | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchMetrics = async () => {
      try {
        // Default project ID - should be passed from context or params
        const data = await dashboardService.getMetrics(1);
        setMetrics(data);
      } catch (error: any) {
        toast.error('Failed to load metrics');
      } finally {
        setLoading(false);
      }
    };

    fetchMetrics();
  }, []);

  if (loading) {
    return <div className="text-center py-12">Loading...</div>;
  }

  if (!metrics) {
    return <div className="text-center py-12">No metrics available</div>;
  }

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8 text-gray-800">Dashboard</h1>

      {/* Metrics Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        <MetricCard
          title="Total Projects"
          value={metrics.totalProjects}
          color="bg-blue-500"
        />
        <MetricCard
          title="Total Repositories"
          value={metrics.totalRepositories}
          color="bg-green-500"
        />
        <MetricCard
          title="Developers"
          value={metrics.developerCount}
          color="bg-purple-500"
        />
        <MetricCard
          title="Knowledge Coverage"
          value={`${metrics.knowledgeCoverage}%`}
          color="bg-orange-500"
        />
      </div>

      {/* Charts Row */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-xl font-bold mb-4">Project Health Metrics</h2>
          <div className="space-y-4">
            <MetricBar label="Documentation Score" value={metrics.documentationScore} />
            <MetricBar label="Architecture Stability" value={metrics.architectureStability} />
            <MetricBar label="Project Health" value={metrics.projectHealth} />
          </div>
        </div>

        <div className="bg-white rounded-lg shadow p-6">
          <h2 className="text-xl font-bold mb-4">Activity Metrics</h2>
          <div className="space-y-4">
            <MetricStat label="Active Contributors" value={metrics.activeContributors} />
            <MetricStat label="Recent Commits" value={metrics.recentCommits} />
            <MetricStat label="Knowledge Risk" value={`${metrics.knowledgeRisk}%`} />
          </div>
        </div>
      </div>
    </div>
  );
};

interface MetricCardProps {
  title: string;
  value: string | number;
  color: string;
}

const MetricCard: React.FC<MetricCardProps> = ({ title, value, color }) => (
  <div className={`${color} rounded-lg shadow p-6 text-white`}>
    <h3 className="text-sm font-medium opacity-90">{title}</h3>
    <p className="text-3xl font-bold mt-2">{value}</p>
  </div>
);

interface MetricBarProps {
  label: string;
  value: number;
}

const MetricBar: React.FC<MetricBarProps> = ({ label, value }) => (
  <div>
    <div className="flex justify-between mb-1">
      <span className="text-sm font-medium text-gray-700">{label}</span>
      <span className="text-sm font-bold text-gray-900">{value}%</span>
    </div>
    <div className="w-full bg-gray-200 rounded-full h-2">
      <div
        className="bg-blue-600 h-2 rounded-full"
        style={{ width: `${value}%` }}
      ></div>
    </div>
  </div>
);

interface MetricStatProps {
  label: string;
  value: string | number;
}

const MetricStat: React.FC<MetricStatProps> = ({ label, value }) => (
  <div className="flex justify-between items-center pb-4 border-b border-gray-200">
    <span className="text-gray-700 font-medium">{label}</span>
    <span className="text-2xl font-bold text-primary-600">{value}</span>
  </div>
);

export default DashboardPage;
