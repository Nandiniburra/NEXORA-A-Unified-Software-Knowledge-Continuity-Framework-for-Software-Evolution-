import React, { useState } from 'react';

interface TableColumn {
  key: string;
  label: string;
  sortable?: boolean;
  render?: (value: any) => React.ReactNode;
}

interface AdvancedTableProps {
  columns: TableColumn[];
  data: any[];
  title: string;
  onRowClick?: (row: any) => void;
  searchable?: boolean;
  filterable?: boolean;
}

export const AdvancedDataTable: React.FC<AdvancedTableProps> = ({
  columns,
  data,
  title,
  onRowClick,
  searchable = true,
  filterable = true
}) => {
  const [sortBy, setSortBy] = useState<string | null>(null);
  const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc');
  const [searchQuery, setSearchQuery] = useState('');
  const [filters, setFilters] = useState<Record<string, string>>({});

  let filteredData = data.filter(row => {
    // Search filter
    if (searchQuery) {
      return columns.some(col =>
        String(row[col.key]).toLowerCase().includes(searchQuery.toLowerCase())
      );
    }
    return true;
  });

  // Apply column filters
  filteredData = filteredData.filter(row => {
    return Object.entries(filters).every(([key, value]) => {
      if (!value) return true;
      return String(row[key]).toLowerCase().includes(value.toLowerCase());
    });
  });

  // Sort
  if (sortBy) {
    filteredData = [...filteredData].sort((a, b) => {
      const aVal = a[sortBy];
      const bVal = b[sortBy];
      const comparison = aVal < bVal ? -1 : aVal > bVal ? 1 : 0;
      return sortOrder === 'asc' ? comparison : -comparison;
    });
  }

  const handleSort = (key: string) => {
    if (sortBy === key) {
      setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
    } else {
      setSortBy(key);
      setSortOrder('asc');
    }
  };

  return (
    <div className="bg-white rounded-lg shadow overflow-hidden">
      {/* Header */}
      <div className="p-6 border-b border-gray-200">
        <h3 className="text-lg font-bold text-gray-800 mb-4">{title}</h3>
        
        {/* Search */}
        {searchable && (
          <input
            type="text"
            placeholder="Search..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
          />
        )}
      </div>

      {/* Table */}
      <div className="overflow-x-auto">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              {columns.map((col) => (
                <th
                  key={col.key}
                  className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase cursor-pointer hover:bg-gray-100"
                  onClick={() => col.sortable && handleSort(col.key)}
                >
                  <div className="flex items-center">
                    {col.label}
                    {col.sortable && sortBy === col.key && (
                      <span className="ml-2">{sortOrder === 'asc' ? '↑' : '↓'}</span>
                    )}
                  </div>
                </th>
              ))}
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {filteredData.length > 0 ? (
              filteredData.map((row, idx) => (
                <tr
                  key={idx}
                  className="hover:bg-gray-50 cursor-pointer"
                  onClick={() => onRowClick?.(row)}
                >
                  {columns.map((col) => (
                    <td key={col.key} className="px-6 py-4 text-sm text-gray-900">
                      {col.render ? col.render(row[col.key]) : row[col.key]}
                    </td>
                  ))}
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={columns.length} className="px-6 py-4 text-center text-gray-500">
                  No data found
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      {/* Footer */}
      <div className="px-6 py-4 bg-gray-50 border-t border-gray-200 text-sm text-gray-600">
        Showing {filteredData.length} of {data.length} records
      </div>
    </div>
  );
};

export default AdvancedDataTable;
