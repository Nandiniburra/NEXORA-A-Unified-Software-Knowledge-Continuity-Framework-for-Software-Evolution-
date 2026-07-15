import api from './api';

export interface Project {
  id: number;
  name: string;
  description: string;
  repositoryUrl: string;
  framework?: string;
  language?: string;
  architecturePattern?: string;
  isActive: boolean;
}

export interface CreateProjectRequest {
  name: string;
  description: string;
  repositoryUrl: string;
}

const projectService = {
  getAllProjects: async (): Promise<Project[]> => {
    const response = await api.get('/projects');
    return response.data;
  },

  getProjectById: async (id: number): Promise<Project> => {
    const response = await api.get(`/projects/${id}`);
    return response.data;
  },

  createProject: async (request: CreateProjectRequest): Promise<Project> => {
    const response = await api.post('/projects', request);
    return response.data;
  },

  updateProject: async (id: number, request: CreateProjectRequest): Promise<Project> => {
    const response = await api.put(`/projects/${id}`, request);
    return response.data;
  },

  deleteProject: async (id: number): Promise<void> => {
    await api.delete(`/projects/${id}`);
  },
};

export default projectService;
