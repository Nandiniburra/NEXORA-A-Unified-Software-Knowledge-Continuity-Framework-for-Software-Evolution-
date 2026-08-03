import React from 'react';
import html2pdf from 'html2pdf.js';

interface ExportOptions {
  format: 'pdf' | 'csv' | 'json';
  filename: string;
  title?: string;
}

interface ExportableReportProps {
  data: any;
  options: ExportOptions;
  children: React.ReactNode;
}

export const ExportableReport: React.FC<ExportableReportProps> = ({
  data,
  options,
  children
}) => {
  const handleExport = () => {
    switch (options.format) {
      case 'pdf':
        exportToPDF();
        break;
      case 'csv':
        exportToCSV();
        break;
      case 'json':
        exportToJSON();
        break;
    }
  };

  const exportToPDF = () => {
    const element = document.getElementById('report-content');
    if (!element) return;

    const opt = {
      margin: 10,
      filename: `${options.filename}.pdf`,
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: { scale: 2 },
      jsPDF: { orientation: 'portrait', unit: 'mm', format: 'a4' }
    };

    html2pdf().set(opt).from(element).save();
  };

  const exportToCSV = () => {
    const csv = convertToCSV(data);
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${options.filename}.csv`;
    a.click();
  };

  const exportToJSON = () => {
    const json = JSON.stringify(data, null, 2);
    const blob = new Blob([json], { type: 'application/json' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `${options.filename}.json`;
    a.click();
  };

  const convertToCSV = (data: any): string => {
    if (!Array.isArray(data)) return '';
    const headers = Object.keys(data[0]);
    const rows = data.map(row => headers.map(h => `"${row[h]}"`).join(','));
    return [headers.join(','), ...rows].join('\n');
  };

  return (
    <div>
      <div className="flex justify-end mb-4 gap-2">
        <button
          onClick={handleExport}
          className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 flex items-center gap-2"
        >
          📥 Export as {options.format.toUpperCase()}
        </button>
      </div>
      <div id="report-content">
        {options.title && <h1 className="text-3xl font-bold mb-6">{options.title}</h1>}
        {children}
      </div>
    </div>
  );
};

export default ExportableReport;
