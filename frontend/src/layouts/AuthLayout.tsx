import React from 'react';
import { Outlet } from 'react-router-dom';

const AuthLayout: React.FC = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-primary-600 to-primary-900 p-4">
      <div className="bg-white rounded-lg shadow-xl p-8 w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-4xl font-bold text-primary-600">NEXORA</h1>
          <p className="text-gray-600 mt-2">Software Knowledge Continuity Framework</p>
        </div>
        <Outlet />
      </div>
    </div>
  );
};

export default AuthLayout;
