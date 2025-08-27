package com.dvs.service;

import com.dvs.model.DataSet;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling data operations including CSV import/export
 * and data validation
 */
public class DataService {
    
    /**
     * Load data from a CSV file
     */
    public DataSet loadCSVFile(File csvFile) throws IOException, CsvException {
        DataSet dataSet = new DataSet(csvFile.getName());
        
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> allRows = reader.readAll();
            
            if (allRows.isEmpty()) {
                throw new IOException("CSV file is empty");
            }
            
            // First row as column headers
            String[] headers = allRows.get(0);
            for (String header : headers) {
                dataSet.addColumn(header.trim());
            }
            
            // Process data rows
            for (int i = 1; i < allRows.size(); i++) {
                String[] row = allRows.get(i);
                List<Object> rowData = new ArrayList<>();
                
                for (int j = 0; j < headers.length; j++) {
                    String cellValue = j < row.length ? row[j].trim() : "";
                    
                    // Try to convert to number if possible
                    Object value = parseValue(cellValue);
                    rowData.add(value);
                }
                
                dataSet.addRow(rowData);
            }
        }
        
        return dataSet;
    }
    
    /**
     * Parse a string value and convert to appropriate type (Number or String)
     */
    private Object parseValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "";
        }
        
        value = value.trim();
        
        // Try to parse as integer
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            // Not an integer, try double
        }
        
        // Try to parse as double
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            // Not a number, return as string
        }
        
        return value;
    }
    
    /**
     * Validate dataset for chart generation
     */
    public boolean validateDataSet(DataSet dataSet, String chartType) {
        if (dataSet == null || dataSet.isEmpty()) {
            return false;
        }
        
        switch (chartType.toLowerCase()) {
            case "bar chart":
            case "pie chart":
                return validateCategoricalData(dataSet);
            case "line chart":
            case "scatter plot":
                return validateNumericalData(dataSet);
            default:
                return false;
        }
    }
    
    /**
     * Validate data for categorical charts (bar, pie)
     */
    private boolean validateCategoricalData(DataSet dataSet) {
        if (dataSet.getColumnCount() < 2) {
            return false;
        }
        
        // Check if we have at least one text column and one numeric column
        boolean hasTextColumn = false;
        boolean hasNumericColumn = false;
        
        for (int col = 0; col < dataSet.getColumnCount(); col++) {
            List<Object> columnData = dataSet.getColumnData(col);
            if (!columnData.isEmpty()) {
                Object firstValue = columnData.get(0);
                if (firstValue instanceof Number) {
                    hasNumericColumn = true;
                } else {
                    hasTextColumn = true;
                }
            }
        }
        
        return hasTextColumn && hasNumericColumn;
    }
    
    /**
     * Validate data for numerical charts (line, scatter)
     */
    private boolean validateNumericalData(DataSet dataSet) {
        if (dataSet.getColumnCount() < 2) {
            return false;
        }
        
        // Check if we have at least two numeric columns
        int numericColumns = 0;
        
        for (int col = 0; col < dataSet.getColumnCount(); col++) {
            List<Object> columnData = dataSet.getColumnData(col);
            if (!columnData.isEmpty()) {
                Object firstValue = columnData.get(0);
                if (firstValue instanceof Number) {
                    numericColumns++;
                }
            }
        }
        
        return numericColumns >= 2;
    }
    
    /**
     * Get suggested chart types for a dataset
     */
    public List<String> getSuggestedChartTypes(DataSet dataSet) {
        List<String> suggestions = new ArrayList<>();
        
        if (validateCategoricalData(dataSet)) {
            suggestions.add("Bar Chart");
            suggestions.add("Pie Chart");
        }
        
        if (validateNumericalData(dataSet)) {
            suggestions.add("Line Chart");
            suggestions.add("Scatter Plot");
        }
        
        return suggestions;
    }
    
    /**
     * Create a sample dataset for testing
     */
    public DataSet createSampleDataSet() {
        DataSet sampleData = new DataSet("Sample Sales Data");
        
        sampleData.addColumn("Product");
        sampleData.addColumn("Sales");
        sampleData.addColumn("Quarter");
        
        sampleData.addRow("Laptops", 1500, "Q1");
        sampleData.addRow("Phones", 2300, "Q1");
        sampleData.addRow("Tablets", 800, "Q1");
        sampleData.addRow("Laptops", 1800, "Q2");
        sampleData.addRow("Phones", 2100, "Q2");
        sampleData.addRow("Tablets", 950, "Q2");
        
        return sampleData;
    }
}
