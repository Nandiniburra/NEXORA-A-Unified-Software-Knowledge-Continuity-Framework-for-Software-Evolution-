import api from './api';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  fullName: string;
}

export interface AuthResponse {
  token: string;
  username: string;
  email: string;
  role: string;
  message: string;
}

const authService = {
  login: async (request: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/login', request);
    return response.data;
  },

  register: async (request: RegisterRequest): Promise<AuthResponse> => {
    const response = await api.post('/auth/register', request);
    return response.data;
  },

  getCurrentUser: async () => {
    const response = await api.get('/auth/me');
    return response.data;
  },

  logout: async () => {
    await api.post('/auth/logout');
  },
};

export default authService;
