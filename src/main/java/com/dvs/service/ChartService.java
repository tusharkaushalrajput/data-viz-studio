package com.dvs.service;

import com.dvs.model.DataSet;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.util.List;

/**
 * Service class for creating various types of charts using JFreeChart
 */
public class ChartService {
    
    /**
     * Create a chart based on the dataset and chart type
     */
    public JPanel createChart(DataSet dataSet, String chartType) {
        JFreeChart chart = null;
        
        switch (chartType.toLowerCase()) {
            case "bar chart":
                chart = createBarChart(dataSet);
                break;
            case "pie chart":
                chart = createPieChart(dataSet);
                break;
            case "line chart":
                chart = createLineChart(dataSet);
                break;
            case "scatter plot":
                chart = createScatterPlot(dataSet);
                break;
            default:
                throw new IllegalArgumentException("Unsupported chart type: " + chartType);
        }
        
        if (chart != null) {
            customizeChart(chart);
            ChartPanel chartPanel = new ChartPanel(chart);
            chartPanel.setPreferredSize(new java.awt.Dimension(600, 400));
            return chartPanel;
        }
        
        return null;
    }
    
    /**
     * Create a bar chart from the dataset
     */
    private JFreeChart createBarChart(DataSet dataSet) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // Assume first column is category, second column is value
        if (dataSet.getColumnCount() >= 2) {
            List<Object> categories = dataSet.getColumnData(0);
            List<Object> values = dataSet.getColumnData(1);
            
            String seriesName = dataSet.getColumnNames().get(1);
            
            for (int i = 0; i < Math.min(categories.size(), values.size()); i++) {
                Object category = categories.get(i);
                Object value = values.get(i);
                
                if (value instanceof Number) {
                    dataset.addValue(((Number) value).doubleValue(), seriesName, category.toString());
                }
            }
        }
        
        return ChartFactory.createBarChart(
            dataSet.getName() + " - Bar Chart",
            dataSet.getColumnNames().get(0),
            dataSet.getColumnNames().get(1),
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
    }
    
    /**
     * Create a pie chart from the dataset
     */
    private JFreeChart createPieChart(DataSet dataSet) {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        
        // Assume first column is category, second column is value
        if (dataSet.getColumnCount() >= 2) {
            List<Object> categories = dataSet.getColumnData(0);
            List<Object> values = dataSet.getColumnData(1);
            
            for (int i = 0; i < Math.min(categories.size(), values.size()); i++) {
                Object category = categories.get(i);
                Object value = values.get(i);
                
                if (value instanceof Number) {
                    dataset.setValue(category.toString(), ((Number) value).doubleValue());
                }
            }
        }
        
        return ChartFactory.createPieChart(
            dataSet.getName() + " - Pie Chart",
            dataset,
            true,
            true,
            false
        );
    }
    
    /**
     * Create a line chart from the dataset
     */
    private JFreeChart createLineChart(DataSet dataSet) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        if (dataSet.getColumnCount() >= 2) {
            // Find numeric columns
            int xColumn = -1, yColumn = -1;
            
            for (int i = 0; i < dataSet.getColumnCount(); i++) {
                List<Object> columnData = dataSet.getColumnData(i);
                if (!columnData.isEmpty() && columnData.get(0) instanceof Number) {
                    if (xColumn == -1) {
                        xColumn = i;
                    } else if (yColumn == -1) {
                        yColumn = i;
                        break;
                    }
                }
            }
            
            if (xColumn >= 0 && yColumn >= 0) {
                XYSeries series = new XYSeries(dataSet.getColumnNames().get(yColumn));
                
                List<Object> xValues = dataSet.getColumnData(xColumn);
                List<Object> yValues = dataSet.getColumnData(yColumn);
                
                for (int i = 0; i < Math.min(xValues.size(), yValues.size()); i++) {
                    Object x = xValues.get(i);
                    Object y = yValues.get(i);
                    
                    if (x instanceof Number && y instanceof Number) {
                        series.add(((Number) x).doubleValue(), ((Number) y).doubleValue());
                    }
                }
                
                dataset.addSeries(series);
            }
        }
        
        String xAxisLabel = dataSet.getColumnCount() > 0 ? dataSet.getColumnNames().get(0) : "X";
        String yAxisLabel = dataSet.getColumnCount() > 1 ? dataSet.getColumnNames().get(1) : "Y";
        
        return ChartFactory.createXYLineChart(
            dataSet.getName() + " - Line Chart",
            xAxisLabel,
            yAxisLabel,
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
    }
    
    /**
     * Create a scatter plot from the dataset
     */
    private JFreeChart createScatterPlot(DataSet dataSet) {
        XYSeriesCollection dataset = new XYSeriesCollection();
        
        if (dataSet.getColumnCount() >= 2) {
            // Find numeric columns
            int xColumn = -1, yColumn = -1;
            
            for (int i = 0; i < dataSet.getColumnCount(); i++) {
                List<Object> columnData = dataSet.getColumnData(i);
                if (!columnData.isEmpty() && columnData.get(0) instanceof Number) {
                    if (xColumn == -1) {
                        xColumn = i;
                    } else if (yColumn == -1) {
                        yColumn = i;
                        break;
                    }
                }
            }
            
            if (xColumn >= 0 && yColumn >= 0) {
                XYSeries series = new XYSeries("Data Points");
                
                List<Object> xValues = dataSet.getColumnData(xColumn);
                List<Object> yValues = dataSet.getColumnData(yColumn);
                
                for (int i = 0; i < Math.min(xValues.size(), yValues.size()); i++) {
                    Object x = xValues.get(i);
                    Object y = yValues.get(i);
                    
                    if (x instanceof Number && y instanceof Number) {
                        series.add(((Number) x).doubleValue(), ((Number) y).doubleValue());
                    }
                }
                
                dataset.addSeries(series);
            }
        }
        
        String xAxisLabel = dataSet.getColumnCount() > 0 ? dataSet.getColumnNames().get(0) : "X";
        String yAxisLabel = dataSet.getColumnCount() > 1 ? dataSet.getColumnNames().get(1) : "Y";
        
        return ChartFactory.createScatterPlot(
            dataSet.getName() + " - Scatter Plot",
            xAxisLabel,
            yAxisLabel,
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
    }
    
    /**
     * Apply custom styling to the chart
     */
    private void customizeChart(JFreeChart chart) {
        // Set chart background
        chart.setBackgroundPaint(Color.WHITE);
        
        // Customize title
        chart.getTitle().setFont(new Font("SansSerif", Font.BOLD, 16));
        chart.getTitle().setPaint(Color.DARK_GRAY);
        
        // Customize plot background
        if (chart.getPlot() != null) {
            chart.getPlot().setBackgroundPaint(Color.WHITE);
            chart.getPlot().setOutlineStroke(null);
        }
    }
    
    /**
     * Export chart to file (implementation to be added)
     */
    public void exportChart(JFreeChart chart, String filePath, int width, int height, String format) {
        // Implementation for chart export will be added later
        throw new UnsupportedOperationException("Chart export not implemented yet");
    }
}
