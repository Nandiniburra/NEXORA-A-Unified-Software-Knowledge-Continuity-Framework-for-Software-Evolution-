import React, { useEffect, useState } from 'react';
import projectService, { Project } from '../services/projectService';
import toast from 'react-hot-toast';

const RepositoriesPage: React.FC = () => {
  const [projects, setProjects] = useState<Project[]>([]);
  const [loading, setLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);

  useEffect(() => {
    fetchProjects();
  }, []);

  const fetchProjects = async () => {
    try {
      const data = await projectService.getAllProjects();
      setProjects(data);
    } catch (error: any) {
      toast.error('Failed to load projects');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="text-center py-12">Loading...</div>;
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-gray-800">Repositories</h1>
        <button
          onClick={() => setShowForm(!showForm)}
          className="bg-primary-600 text-white px-4 py-2 rounded-lg hover:bg-primary-700"
        >
          Add Repository
        </button>
      </div>

      {showForm && <CreateProjectForm onSuccess={() => { setShowForm(false); fetchProjects(); }} />}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {projects.map((project) => (
          <div key={project.id} className="bg-white rounded-lg shadow p-6 hover:shadow-lg transition">
            <h3 className="text-lg font-bold text-gray-800 mb-2">{project.name}</h3>
            <p className="text-gray-600 text-sm mb-4">{project.description}</p>
            <div className="flex flex-wrap gap-2">
              {project.language && (
                <span className="bg-blue-100 text-blue-800 text-xs px-3 py-1 rounded-full">
                  {project.language}
                </span>
              )}
              {project.framework && (
                <span className="bg-green-100 text-green-800 text-xs px-3 py-1 rounded-full">
                  {project.framework}
                </span>
              )}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

interface CreateProjectFormProps {
  onSuccess: () => void;
}

const CreateProjectForm: React.FC<CreateProjectFormProps> = ({ onSuccess }) => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [repositoryUrl, setRepositoryUrl] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);

    try {
      await projectService.createProject({ name, description, repositoryUrl });
      toast.success('Project created successfully!');
      onSuccess();
    } catch (error: any) {
      toast.error('Failed to create project');
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="bg-white rounded-lg shadow p-6 mb-8">
      <h2 className="text-xl font-bold mb-4">New Project</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <input
          type="text"
          placeholder="Project Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
          required
        />
        <input
          type="text"
          placeholder="Repository URL"
          value={repositoryUrl}
          onChange={(e) => setRepositoryUrl(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
          required
        />
      </div>
      <textarea
        placeholder="Description"
        value={description}
        onChange={(e) => setDescription(e.target.value)}
        className="w-full mt-4 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-primary-500"
        rows={3}
      />
      <button
        type="submit"
        disabled={loading}
        className="mt-4 bg-primary-600 text-white px-4 py-2 rounded-lg hover:bg-primary-700 disabled:bg-gray-400"
      >
        {loading ? 'Creating...' : 'Create Project'}
      </button>
    </form>
  );
};

export default RepositoriesPage;
