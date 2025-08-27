package com.dvs.test;

import com.dvs.model.DataSet;
import com.dvs.service.DataService;

/**
 * Simple test class to verify the basic functionality
 */
public class SimpleTest {
    
    public static void main(String[] args) {
        try {
            System.out.println("Testing Data Visualization System...");
            
            // Test DataSet creation
            DataSet testData = new DataSet("Test Dataset");
            testData.addColumn("Product");
            testData.addColumn("Sales");
            
            testData.addRow("Laptop", 1500);
            testData.addRow("Phone", 2000);
            testData.addRow("Tablet", 800);
            
            System.out.println("DataSet created successfully!");
            System.out.println("Dataset preview:");
            System.out.println(testData.getPreviewText());
            
            // Test DataService
            DataService dataService = new DataService();
            DataSet sampleData = dataService.createSampleDataSet();
            
            System.out.println("\nSample dataset created:");
            System.out.println(sampleData.getPreviewText());
            
            System.out.println("\nBasic functionality test completed successfully!");
            
        } catch (Exception e) {
            System.err.println("Error during testing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
