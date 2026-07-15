import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Sidebar: React.FC = () => {
  const location = useLocation();

  const menuItems = [
    { name: 'Dashboard', path: '/dashboard' },
    { name: 'Projects', path: '/projects' },
    { name: 'Repositories', path: '/repositories' },
    { name: 'Analyzer', path: '/analyzer' },
    { name: 'Reports', path: '/reports' },
  ];

  return (
    <aside className="w-64 bg-secondary-700 text-white">
      <div className="p-6">
        <h2 className="text-xl font-bold mb-8">Menu</h2>
        <nav className="space-y-2">
          {menuItems.map((item) => (
            <Link
              key={item.path}
              to={item.path}
              className={`block px-4 py-2 rounded-lg transition ${
                location.pathname === item.path
                  ? 'bg-primary-600'
                  : 'hover:bg-secondary-600'
              }`}
            >
              {item.name}
            </Link>
          ))}
        </nav>
      </div>
    </aside>
  );
};

export default Sidebar;
