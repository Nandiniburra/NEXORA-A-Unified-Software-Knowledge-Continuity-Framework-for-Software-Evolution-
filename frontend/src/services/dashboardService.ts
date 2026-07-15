import api from './api';

export interface DashboardMetrics {
  id?: number;
  totalProjects: number;
  totalRepositories: number;
  developerCount: number;
  knowledgeCoverage: number;
  documentationScore: number;
  architectureStability: number;
  knowledgeRisk: number;
  projectHealth: number;
  activeContributors: number;
  recentCommits: number;
}

const dashboardService = {
  getMetrics: async (projectId: number): Promise<DashboardMetrics> => {
    const response = await api.get(`/projects/${projectId}/dashboard/metrics`);
    return response.data;
  },

  updateMetrics: async (projectId: number, metrics: DashboardMetrics): Promise<DashboardMetrics> => {
    const response = await api.post(`/projects/${projectId}/dashboard/metrics`, metrics);
    return response.data;
  },
};

export default dashboardService;
