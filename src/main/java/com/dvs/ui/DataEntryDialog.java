package com.dvs.ui;

import com.dvs.model.DataSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog for manual data entry
 */
public class DataEntryDialog extends JDialog {
    
    private static final long serialVersionUID = 1L;
    private JTable dataTable;
    private DefaultTableModel tableModel;
    private JTextField datasetNameField;
    private JSpinner rowSpinner;
    private JSpinner columnSpinner;
    private DataSet resultDataSet;
    private boolean approved = false;
    
    public DataEntryDialog(JFrame parent) {
        super(parent, "Manual Data Entry", true);
        initializeDialog();
        setupComponents();
        setupEventHandlers();
    }
    
    private void initializeDialog() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
    
    private void setupComponents() {
        setLayout(new BorderLayout(10, 10));
        
        // Top panel for dataset configuration
        JPanel configPanel = createConfigPanel();
        add(configPanel, BorderLayout.NORTH);
        
        // Center panel for data table
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // Bottom panel for buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Initialize with default size
        resizeTable();
    }
    
    private JPanel createConfigPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Dataset Configuration"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Dataset name
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Dataset Name:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        datasetNameField = new JTextField("Manual Dataset", 15);
        panel.add(datasetNameField, gbc);
        
        // Rows
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Rows:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        rowSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
        panel.add(rowSpinner, gbc);
        
        // Columns
        gbc.gridx = 2; gbc.gridy = 1;
        panel.add(new JLabel("Columns:"), gbc);
        
        gbc.gridx = 3; gbc.gridy = 1;
        columnSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 20, 1));
        panel.add(columnSpinner, gbc);
        
        // Resize button
        gbc.gridx = 4; gbc.gridy = 1;
        JButton resizeButton = new JButton("Resize Table");
        resizeButton.addActionListener(e -> resizeTable());
        panel.add(resizeButton, gbc);
        
        return panel;
    }
    
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Data Entry"));
        
        // Create table with default model
        tableModel = new DefaultTableModel();
        dataTable = new JTable(tableModel);
        dataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane scrollPane = new JScrollPane(dataTable);
        scrollPane.setPreferredSize(new Dimension(550, 300));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Instructions
        JLabel instructions = new JLabel(
            "<html>Enter your data in the table below. First row should contain column headers.<br>" +
            "Use numbers for numerical data and text for categorical data.</html>"
        );
        instructions.setBorder(new EmptyBorder(5, 5, 5, 5));
        panel.add(instructions, BorderLayout.NORTH);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton loadSampleButton = new JButton("Load Sample Data");
        loadSampleButton.addActionListener(e -> loadSampleData());
        
        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> clearTable());
        
        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> approveAndClose());
        
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());
        
        panel.add(loadSampleButton);
        panel.add(clearButton);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(okButton);
        panel.add(cancelButton);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Add listeners for spinners
        rowSpinner.addChangeListener(e -> updateTableSize());
        columnSpinner.addChangeListener(e -> updateTableSize());
    }
    
    private void resizeTable() {
        int rows = (Integer) rowSpinner.getValue();
        int columns = (Integer) columnSpinner.getValue();
        
        // Create column names
        String[] columnNames = new String[columns];
        for (int i = 0; i < columns; i++) {
            columnNames[i] = "Column " + (i + 1);
        }
        
        // Create empty data
        Object[][] data = new Object[rows][columns];
        
        tableModel.setDataVector(data, columnNames);
        dataTable.revalidate();
    }
    
    private void updateTableSize() {
        // This method can be used for dynamic updates if needed
        // For now, user needs to click "Resize Table" button
    }
    
    private void loadSampleData() {
        // Clear current data
        resizeTable();
        
        // Set sample data
        datasetNameField.setText("Sample Product Sales");
        
        // Set headers
        tableModel.setValueAt("Product", 0, 0);
        tableModel.setValueAt("Sales", 0, 1);
        tableModel.setValueAt("Quarter", 0, 2);
        
        // Set data
        Object[][] sampleData = {
            {"Product", "Sales", "Quarter"},
            {"Laptops", 1500, "Q1"},
            {"Phones", 2300, "Q1"},
            {"Tablets", 800, "Q1"},
            {"Laptops", 1800, "Q2"},
            {"Phones", 2100, "Q2"}
        };
        
        for (int i = 0; i < sampleData.length && i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < sampleData[i].length && j < tableModel.getColumnCount(); j++) {
                tableModel.setValueAt(sampleData[i][j], i, j);
            }
        }
        
        dataTable.revalidate();
        dataTable.repaint();
    }
    
    private void clearTable() {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                tableModel.setValueAt("", i, j);
            }
        }
        dataTable.revalidate();
        dataTable.repaint();
    }
    
    private void approveAndClose() {
        try {
            // Create dataset from table data
            resultDataSet = new DataSet(datasetNameField.getText());
            
            // Get column names from first row
            List<String> columnNames = new ArrayList<>();
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                Object value = tableModel.getValueAt(0, j);
                String columnName = value != null ? value.toString().trim() : "Column " + (j + 1);
                if (columnName.isEmpty()) {
                    columnName = "Column " + (j + 1);
                }
                columnNames.add(columnName);
            }
            
            // Set column names
            for (String colName : columnNames) {
                resultDataSet.addColumn(colName);
            }
            
            // Add data rows (skip first row which contains headers)
            for (int i = 1; i < tableModel.getRowCount(); i++) {
                List<Object> rowData = new ArrayList<>();
                boolean hasData = false;
                
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    Object value = tableModel.getValueAt(i, j);
                    if (value != null && !value.toString().trim().isEmpty()) {
                        hasData = true;
                        // Try to parse as number
                        String strValue = value.toString().trim();
                        try {
                            if (strValue.contains(".")) {
                                rowData.add(Double.parseDouble(strValue));
                            } else {
                                rowData.add(Integer.parseInt(strValue));
                            }
                        } catch (NumberFormatException e) {
                            rowData.add(strValue);
                        }
                    } else {
                        rowData.add("");
                    }
                }
                
                // Only add row if it contains some data
                if (hasData) {
                    resultDataSet.addRow(rowData);
                }
            }
            
            if (resultDataSet.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter some data before proceeding.", 
                    "No Data", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            approved = true;
            dispose();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error processing data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public DataSet getDataSet() {
        return approved ? resultDataSet : null;
    }
}
