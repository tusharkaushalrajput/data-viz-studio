package com.dvs.simple;

import com.dvs.model.DataSet;

import javax.swing.*;
import java.awt.*;

/**
 * Simplified version of the Data Visualization System
 * Works without external dependencies for testing
 */
public class SimpleApp extends JFrame {
    private JTextArea dataArea;
    private JButton loadSampleButton;
    private JButton loadCsvButton;
    private JButton showDataButton;
    private JButton showChartButton;
    private DataSet currentData; // holds last loaded dataset
    
    public SimpleApp() {
        initializeUI();
    }
    private void initializeUI() {
        setTitle("Data Visualization System - Simple Version");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        
        // Create layout
        setLayout(new BorderLayout());
        
        // Top panel with buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
    loadSampleButton = new JButton("Load Sample Data");
    loadCsvButton = new JButton("Load CSV");
    showDataButton = new JButton("Show Data Preview");
    showChartButton = new JButton("Show Bar Chart");
    showChartButton.setEnabled(false);
        
    buttonPanel.add(loadSampleButton);
    buttonPanel.add(loadCsvButton);
    buttonPanel.add(showDataButton);
    buttonPanel.add(showChartButton);
        
        // Center panel with text area
        dataArea = new JTextArea();
        dataArea.setEditable(false);
        dataArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(dataArea);
        
        // Add components
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(new JLabel("Simple Data Visualization System - Core functionality test"), BorderLayout.SOUTH);
        
        // Add event listeners
    loadSampleButton.addActionListener(e -> loadSampleData());
    loadCsvButton.addActionListener(e -> loadCsvData());
    showDataButton.addActionListener(e -> showDataPreview());
    showChartButton.addActionListener(e -> showBarChart());
        
        // Show initial message
        dataArea.setText("Welcome to Data Visualization System!\n\nThis is a simplified version for testing core functionality.\n\nClick 'Load Sample Data' to test the data model.\nClick 'Show Data Preview' to see data formatting.\n\nNote: Chart generation requires full version with JFreeChart library.");
    }
    
    private void loadSampleData() {
        try {
            // Test DataSet functionality
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

            currentData = sampleData;
            showChartButton.setEnabled(true);

            dataArea.setText("Sample data loaded successfully!\n\n" + sampleData.getPreviewText());

            JOptionPane.showMessageDialog(this, "Sample data loaded successfully!\nDataSet contains " +
                    sampleData.getRowCount() + " rows and " + sampleData.getColumnCount() + " columns.");
                
        } catch (Exception e) {
            dataArea.setText("Error loading sample data: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCsvData() {
        JFileChooser chooser = new JFileChooser();
        int res = chooser.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;
        java.io.File file = chooser.getSelectedFile();
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String header = br.readLine();
            if (header == null) throw new IllegalArgumentException("Empty file");
            String[] cols = header.split(",");
            DataSet ds = new DataSet(file.getName());
            for (String c : cols) ds.addColumn(c.trim());
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                java.util.List<Object> row = new java.util.ArrayList<>();
                for (int i = 0; i < cols.length; i++) {
                    String v = i < parts.length ? parts[i].trim() : "";
                    // attempt numeric parse
                    Object val;
                    try { val = Integer.valueOf(v); } catch (Exception ex1) {
                        try { val = Double.valueOf(v); } catch (Exception ex2) { val = v; }
                    }
                    row.add(val);
                }
                ds.addRow(row);
            }
            currentData = ds;
            showChartButton.setEnabled(true);
            dataArea.setText("CSV loaded: " + file.getName() + "\n\n" + ds.getPreviewText());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to load CSV: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showBarChart() {
        if (currentData == null || currentData.getColumnCount() < 2) {
            JOptionPane.showMessageDialog(this, "Need at least 2 columns (Category, Value)");
            return;
        }
        // Build simple bar chart panel
        JDialog dialog = new JDialog(this, "Bar Chart", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.add(new BarChartPanel(currentData), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    /** Simple Java2D bar chart (no external library). Assumes first column = category (String), second column = numeric */
    private static class BarChartPanel extends JPanel {
        private final DataSet ds;
        BarChartPanel(DataSet ds) { this.ds = ds; setBackground(Color.WHITE); }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(); int h = getHeight();
            java.util.List<Object> cats = ds.getColumnData(0);
            java.util.List<Object> vals = ds.getColumnData(1);
            // Extract numeric values
            java.util.List<Double> numbers = new java.util.ArrayList<>();
            for (Object v : vals) if (v instanceof Number) numbers.add(((Number) v).doubleValue()); else {
                try { numbers.add(Double.parseDouble(String.valueOf(v))); } catch (Exception ignored) { numbers.add(0d);} }
            if (numbers.isEmpty()) { g2.drawString("No numeric data", 20, 20); return; }
            double max = numbers.stream().mapToDouble(d -> d).max().orElse(1);
            int leftPad = 60; int bottomPad = 60; int topPad = 30; int rightPad = 20;
            int chartW = w - leftPad - rightPad; int chartH = h - topPad - bottomPad;
            // Axes
            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(leftPad, topPad, leftPad, topPad + chartH);
            g2.drawLine(leftPad, topPad + chartH, leftPad + chartW, topPad + chartH);
            int n = numbers.size();
            if (n == 0) return;
            int barSpace = chartW / n;
            int barWidth = Math.max(10, (int)(barSpace * 0.6));
            int x = leftPad + (barSpace - barWidth)/2;
            FontMetrics fm = g2.getFontMetrics();
            for (int i = 0; i < n; i++) {
                double val = numbers.get(i);
                int barH = (int) ((val / max) * (chartH - 10));
                int y = topPad + chartH - barH;
                g2.setColor(new Color(70,120,200));
                g2.fillRect(x, y, barWidth, barH);
                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, barWidth, barH);
                String label = cats.size() > i ? String.valueOf(cats.get(i)) : ("Cat"+i);
                String valStr = (val % 1 == 0) ? String.valueOf((long)val) : String.format("%.1f", val);
                // value above bar
                g2.drawString(valStr, x + (barWidth - fm.stringWidth(valStr))/2, y - 4);
                // category label rotated if needed
                int lblWidth = fm.stringWidth(label);
                if (lblWidth < barSpace) {
                    g2.drawString(label, x + (barWidth - lblWidth)/2, topPad + chartH + fm.getAscent() + 5);
                } else {
                    // rotate 45 degrees
                    Graphics2D gSave = (Graphics2D) g2.create();
                    gSave.translate(x + barWidth/2, topPad + chartH + 5);
                    gSave.rotate(-Math.PI/4);
                    gSave.drawString(label, -fm.stringWidth(label)/2, fm.getAscent()/2);
                    gSave.dispose();
                }
                x += barSpace;
            }
            // Title
            String title = ds.getName() + " - Bar Chart";
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 16f));
            g2.drawString(title, leftPad + (chartW - g2.getFontMetrics().stringWidth(title))/2, 20);
            // Y-axis ticks
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 11f));
            for (int i=0;i<=5;i++){
                double v = max * i / 5.0;
                int yTick = topPad + chartH - (int)((v/max)* (chartH -10));
                g2.setColor(new Color(220,220,220));
                g2.drawLine(leftPad+1, yTick, leftPad + chartW, yTick);
                g2.setColor(Color.DARK_GRAY);
                String vs = (v %1==0)? String.valueOf((long)v): String.format("%.1f", v);
                g2.drawString(vs, leftPad - 10 - g2.getFontMetrics().stringWidth(vs), yTick + 4);
            }
        }
    }
    
    private void showDataPreview() {
        StringBuilder info = new StringBuilder();
        info.append("=== DATA VISUALIZATION SYSTEM ===\n");
        info.append("Version: 1.0 (Simplified)\n");
        info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        info.append("OS: ").append(System.getProperty("os.name")).append("\n");
        info.append("Available Memory: ").append(Runtime.getRuntime().maxMemory() / 1024 / 1024).append(" MB\n\n");
        
        info.append("=== CORE FUNCTIONALITY STATUS ===\n");
        info.append("[OK] DataSet Model: Working\n");
        info.append("[OK] Data Import/Export: Basic functionality\n");
        info.append("[OK] GUI Framework: Swing UI working\n");
        info.append("[PENDING] Chart Generation: Requires JFreeChart library\n");
        info.append("[PENDING] CSV Processing: Requires OpenCSV library\n\n");
        
        info.append("=== NEXT STEPS ===\n");
        info.append("1. Download required JAR files to target/lib/\n");
        info.append("2. Run manual-compile.bat to compile full version\n");
        info.append("3. Run run-manual.bat to start complete application\n\n");
        
        info.append("Required JAR files:\n");
        info.append("- jfreechart-1.5.3.jar\n");
        info.append("- jcommon-1.0.24.jar\n");
        info.append("- opencsv-5.7.1.jar\n");
        info.append("- commons-lang3-3.12.0.jar\n");
        info.append("- commons-io-2.11.0.jar\n");
        
        dataArea.setText(info.toString());
    }
    
    public static void main(String[] args) {
        // Set look and feel
        try {
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            // Use default look and feel
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimpleApp().setVisible(true);
            }
        });
    }
}
