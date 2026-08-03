import React from 'react';
import { BarChart, Bar, LineChart, Line, PieChart, Pie, Cell, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

interface ChartData {
  name: string;
  value: number;
  [key: string]: any;
}

interface ComplexityChartProps {
  data: ChartData[];
  title: string;
}

export const ComplexityBarChart: React.FC<ComplexityChartProps> = ({ data, title }) => (
  <div className="bg-white rounded-lg shadow p-6">
    <h3 className="text-lg font-bold mb-4 text-gray-800">{title}</h3>
    <ResponsiveContainer width="100%" height={300}>
      <BarChart data={data}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="name" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Bar dataKey="value" fill="#0284c7" name="Complexity" />
      </BarChart>
    </ResponsiveContainer>
  </div>
);

export const MetricsLineChart: React.FC<ComplexityChartProps> = ({ data, title }) => (
  <div className="bg-white rounded-lg shadow p-6">
    <h3 className="text-lg font-bold mb-4 text-gray-800">{title}</h3>
    <ResponsiveContainer width="100%" height={300}>
      <LineChart data={data}>
        <CartesianGrid strokeDasharray="3 3" />
        <XAxis dataKey="name" />
        <YAxis />
        <Tooltip />
        <Legend />
        <Line type="monotone" dataKey="value" stroke="#0ea5e9" name="Metrics" />
      </LineChart>
    </ResponsiveContainer>
  </div>
);

interface PieChartData {
  name: string;
  value: number;
}

interface MetricsPieChartProps {
  data: PieChartData[];
  title: string;
}

const COLORS = ['#0284c7', '#059669', '#dc2626', '#f59e0b', '#8b5cf6', '#06b6d4'];

export const MetricsPieChart: React.FC<MetricsPieChartProps> = ({ data, title }) => (
  <div className="bg-white rounded-lg shadow p-6">
    <h3 className="text-lg font-bold mb-4 text-gray-800">{title}</h3>
    <ResponsiveContainer width="100%" height={300}>
      <PieChart>
        <Pie
          data={data}
          cx="50%"
          cy="50%"
          labelLine={false}
          label={({ name, percent }) => `${name} ${(percent * 100).toFixed(0)}%`}
          outerRadius={80}
          fill="#8884d8"
          dataKey="value"
        >
          {data.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
          ))}
        </Pie>
        <Tooltip />
      </PieChart>
    </ResponsiveContainer>
  </div>
);
