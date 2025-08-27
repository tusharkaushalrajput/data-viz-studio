package com.dvs.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a dataset containing tabular data for visualization
 */
public class DataSet {
    
    private List<String> columnNames;
    private List<List<Object>> data;
    private String name;
    
    public DataSet() {
        this.columnNames = new ArrayList<>();
        this.data = new ArrayList<>();
        this.name = "Untitled Dataset";
    }
    
    public DataSet(String name) {
        this();
        this.name = name;
    }
    
    /**
     * Add a column to the dataset
     */
    public void addColumn(String columnName) {
        columnNames.add(columnName);
        // Add empty values for existing rows
        for (List<Object> row : data) {
            row.add(null);
        }
    }
    
    /**
     * Add a row of data
     */
    public void addRow(List<Object> rowData) {
        if (rowData.size() != columnNames.size()) {
            throw new IllegalArgumentException("Row data size must match number of columns");
        }
        data.add(new ArrayList<>(rowData));
    }
    
    /**
     * Add a row of data from array
     */
    public void addRow(Object... values) {
        List<Object> rowData = new ArrayList<>();
        for (Object value : values) {
            rowData.add(value);
        }
        addRow(rowData);
    }
    
    /**
     * Get value at specific row and column
     */
    public Object getValue(int row, int column) {
        if (row >= 0 && row < data.size() && column >= 0 && column < columnNames.size()) {
            return data.get(row).get(column);
        }
        return null;
    }
    
    /**
     * Set value at specific row and column
     */
    public void setValue(int row, int column, Object value) {
        if (row >= 0 && row < data.size() && column >= 0 && column < columnNames.size()) {
            data.get(row).set(column, value);
        }
    }
    
    /**
     * Get column data as a list
     */
    public List<Object> getColumnData(int columnIndex) {
        List<Object> columnData = new ArrayList<>();
        for (List<Object> row : data) {
            if (columnIndex < row.size()) {
                columnData.add(row.get(columnIndex));
            }
        }
        return columnData;
    }
    
    /**
     * Get column data by name
     */
    public List<Object> getColumnData(String columnName) {
        int index = columnNames.indexOf(columnName);
        if (index >= 0) {
            return getColumnData(index);
        }
        return new ArrayList<>();
    }
    
    /**
     * Get a preview text representation of the data
     */
    public String getPreviewText() {
        StringBuilder sb = new StringBuilder();
        
        // Add column headers
        sb.append("Dataset: ").append(name).append("\n");
        sb.append("Rows: ").append(data.size()).append(", Columns: ").append(columnNames.size()).append("\n\n");
        
        // Add column names
        for (int i = 0; i < columnNames.size(); i++) {
            sb.append(String.format("%-15s", columnNames.get(i)));
            if (i < columnNames.size() - 1) {
                sb.append(" | ");
            }
        }
        sb.append("\n");
        
        // Add separator
        for (int i = 0; i < columnNames.size(); i++) {
            sb.append("---------------");
            if (i < columnNames.size() - 1) {
                sb.append("-+-");
            }
        }
        sb.append("\n");
        
        // Add data rows (limit to first 10 rows for preview)
        int maxRows = Math.min(data.size(), 10);
        for (int i = 0; i < maxRows; i++) {
            List<Object> row = data.get(i);
            for (int j = 0; j < columnNames.size(); j++) {
                Object value = j < row.size() ? row.get(j) : "";
                String valueStr = value != null ? value.toString() : "null";
                sb.append(String.format("%-15s", valueStr.length() > 15 ? valueStr.substring(0, 12) + "..." : valueStr));
                if (j < columnNames.size() - 1) {
                    sb.append(" | ");
                }
            }
            sb.append("\n");
        }
        
        if (data.size() > 10) {
            sb.append("... and ").append(data.size() - 10).append(" more rows\n");
        }
        
        return sb.toString();
    }
    
    // Getters and setters
    public List<String> getColumnNames() {
        return new ArrayList<>(columnNames);
    }
    
    public void setColumnNames(List<String> columnNames) {
        this.columnNames = new ArrayList<>(columnNames);
    }
    
    public List<List<Object>> getData() {
        return new ArrayList<>(data);
    }
    
    public void setData(List<List<Object>> data) {
        this.data = new ArrayList<>();
        for (List<Object> row : data) {
            this.data.add(new ArrayList<>(row));
        }
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public int getRowCount() {
        return data.size();
    }
    
    public int getColumnCount() {
        return columnNames.size();
    }
    
    public boolean isEmpty() {
        return data.isEmpty() || columnNames.isEmpty();
    }
    
    public void clear() {
        data.clear();
        columnNames.clear();
    }
}
