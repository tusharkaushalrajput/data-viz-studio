package com.dvs;

import com.dvs.ui.MainWindow;
import javax.swing.SwingUtilities;

/**
 * Data Visualization System - Main Application Entry Point
 * 
 * A desktop-based application for creating interactive data visualizations
 * from CSV files and manual data entry using JFreeChart library.
 */
public class App 
{
    public static void main( String[] args )
    {
        // Start the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
            } catch (Exception e) {
                System.err.println("Error starting application: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
