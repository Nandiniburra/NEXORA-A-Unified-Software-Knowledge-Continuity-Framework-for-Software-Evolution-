import React from 'react';
import LoginForm from '../components/auth/LoginForm';

const LoginPage: React.FC = () => {
  return (
    <div>
      <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">Login to NEXORA</h2>
      <LoginForm />
    </div>
  );
};

export default LoginPage;
