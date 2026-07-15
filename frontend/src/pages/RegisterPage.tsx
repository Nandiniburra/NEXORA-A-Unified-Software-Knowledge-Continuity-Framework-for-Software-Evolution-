import React from 'react';
import RegisterForm from '../components/auth/RegisterForm';

const RegisterPage: React.FC = () => {
  return (
    <div>
      <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">Create Account</h2>
      <RegisterForm />
    </div>
  );
};

export default RegisterPage;
