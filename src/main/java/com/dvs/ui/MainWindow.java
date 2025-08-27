package com.dvs.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.dvs.model.DataSet;
import com.dvs.service.ChartService;
import com.dvs.service.DataService;

/**
 * Main window of the Data Visualization System
 * Provides the main user interface for data import, visualization, and export
 */
public class MainWindow extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel chartPanel;
    private JTextArea dataPreviewArea;
    private JButton importCSVButton;
    private JButton loadSampleButton;
    private JButton manualDataButton;
    private JButton generateChartButton;
    private JButton exportChartButton;
    private JComboBox<String> chartTypeCombo;
    
    private DataService dataService;
    private ChartService chartService;
    private DataSet currentDataSet;
    private JPanel lastChartComponent; // holds reference for export
    
    /**
     * Create the main window
     */
    public MainWindow() {
        initializeServices();
        initializeUI();
        setupEventHandlers();
    }
    
    private void initializeServices() {
        dataService = new DataService();
        chartService = new ChartService();
    }
    
    private void initializeUI() {
        setTitle("Data Visualization System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1000, 700);
        setLocationRelativeTo(null);
        
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);
        
        // Create menu bar
        createMenuBar();
        
        // Create toolbar
        createToolbar();
        
        // Create main panel
        createMainPanel();
        
        // Create status bar
        createStatusBar();
    }
    
    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem importItem = new JMenuItem("Import CSV");
        JMenuItem exportItem = new JMenuItem("Export Chart");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        fileMenu.add(importItem);
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Data menu
        JMenu dataMenu = new JMenu("Data");
        JMenuItem manualEntryItem = new JMenuItem("Manual Data Entry");
        JMenuItem clearDataItem = new JMenuItem("Clear Data");
        
        dataMenu.add(manualEntryItem);
        dataMenu.add(clearDataItem);
        
        // Chart menu
        JMenu chartMenu = new JMenu("Chart");
        JMenuItem barChartItem = new JMenuItem("Bar Chart");
        JMenuItem pieChartItem = new JMenuItem("Pie Chart");
        JMenuItem lineChartItem = new JMenuItem("Line Chart");
        JMenuItem scatterPlotItem = new JMenuItem("Scatter Plot");
        
        chartMenu.add(barChartItem);
        chartMenu.add(pieChartItem);
        chartMenu.add(lineChartItem);
        chartMenu.add(scatterPlotItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(dataMenu);
        menuBar.add(chartMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void createToolbar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
    importCSVButton = new JButton("Import CSV");
    importCSVButton.setIcon(createIcon("📂"));

    loadSampleButton = new JButton("Load Sample");
    loadSampleButton.setIcon(createIcon("🧪"));
        
        manualDataButton = new JButton("Manual Entry");
        manualDataButton.setIcon(createIcon("✏️"));
        
        generateChartButton = new JButton("Generate Chart");
        generateChartButton.setIcon(createIcon("📊"));
        generateChartButton.setEnabled(false);
        
        exportChartButton = new JButton("Export");
        exportChartButton.setIcon(createIcon("💾"));
        exportChartButton.setEnabled(false);
        
        chartTypeCombo = new JComboBox<>(new String[]{
            "Bar Chart", "Pie Chart", "Line Chart", "Scatter Plot"
        });
        
        toolBar.add(importCSVButton);
    toolBar.add(manualDataButton);
    toolBar.add(loadSampleButton);
        toolBar.addSeparator();
        toolBar.add(new JLabel("Chart Type: "));
        toolBar.add(chartTypeCombo);
        toolBar.add(generateChartButton);
        toolBar.addSeparator();
        toolBar.add(exportChartButton);
        
        contentPane.add(toolBar, BorderLayout.NORTH);
    }
    
    private void createMainPanel() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        // Left panel for data preview
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Data Preview"));
        leftPanel.setPreferredSize(new Dimension(300, 0));
        
        dataPreviewArea = new JTextArea();
        dataPreviewArea.setEditable(false);
        dataPreviewArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        JScrollPane dataScrollPane = new JScrollPane(dataPreviewArea);
        leftPanel.add(dataScrollPane, BorderLayout.CENTER);
        
        // Right panel for chart display
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Chart Visualization"));
        
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.add(new JLabel("No chart generated yet", JLabel.CENTER), BorderLayout.CENTER);
        
        rightPanel.add(chartPanel, BorderLayout.CENTER);
        
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(300);
        
        contentPane.add(splitPane, BorderLayout.CENTER);
    }
    
    private void createStatusBar() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        statusBar.add(new JLabel("Ready"));
        
        contentPane.add(statusBar, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
    importCSVButton.addActionListener(e -> importCSVFile());
    loadSampleButton.addActionListener(e -> loadSampleData());
        manualDataButton.addActionListener(e -> openManualDataEntry());
        generateChartButton.addActionListener(e -> generateChart());
        exportChartButton.addActionListener(e -> exportChart());
    }
    
    private void importCSVFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                currentDataSet = dataService.loadCSVFile(selectedFile);
                updateDataPreview();
                postDataLoadActions();
                JOptionPane.showMessageDialog(this, "CSV file loaded successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error loading CSV file: " + ex.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadSampleData() {
        try {
            currentDataSet = dataService.createSampleDataSet();
            updateDataPreview();
            postDataLoadActions();
            JOptionPane.showMessageDialog(this, "Sample dataset loaded.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Unable to load sample data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openManualDataEntry() {
        DataEntryDialog dialog = new DataEntryDialog(this);
        dialog.setVisible(true);
        
        if (dialog.getDataSet() != null) {
            currentDataSet = dialog.getDataSet();
            updateDataPreview();
            postDataLoadActions();
        }
    }
    
    private void generateChart() {
        if (currentDataSet == null) {
            JOptionPane.showMessageDialog(this, "Please load data first!");
            return;
        }
        
        String chartType = (String) chartTypeCombo.getSelectedItem();
        try {
            JPanel chart = chartService.createChart(currentDataSet, chartType);
            lastChartComponent = chart;

            chartPanel.removeAll();
            chartPanel.add(chart, BorderLayout.CENTER);
            chartPanel.revalidate();
            chartPanel.repaint();

            exportChartButton.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Chart generated successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating chart: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportChart() {
        if (lastChartComponent == null) {
            JOptionPane.showMessageDialog(this, "No chart to export");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("chart.png"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File out = chooser.getSelectedFile();
            try {
                // If this is a JFreeChart ChartPanel, use ChartUtils; else snapshot the component
                if (lastChartComponent.getClass().getName().contains("ChartPanel")) {
                    try {
                        // Reflection to avoid hard compile dependency in simplified builds
                        java.lang.reflect.Method getChart = lastChartComponent.getClass().getMethod("getChart");
                        Object chartObj = getChart.invoke(lastChartComponent);
                        Class<?> chartUtils = Class.forName("org.jfree.chart.ChartUtils");
                        java.lang.reflect.Method save = chartUtils.getMethod("saveChartAsPNG", File.class, Class.forName("org.jfree.chart.JFreeChart"), int.class, int.class);
                        save.invoke(null, out, chartObj, Math.max(600, lastChartComponent.getWidth()), Math.max(400, lastChartComponent.getHeight()));
                    } catch (Exception reflectionEx) {
                        // Fallback: snapshot
                        snapshotComponent(lastChartComponent, out);
                    }
                } else {
                    snapshotComponent(lastChartComponent, out);
                }
                JOptionPane.showMessageDialog(this, "Chart exported to " + out.getAbsolutePath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void updateDataPreview() {
        if (currentDataSet != null) {
            dataPreviewArea.setText(currentDataSet.getPreviewText());
        }
    }

    private void postDataLoadActions() {
        generateChartButton.setEnabled(true);
        updateChartTypeSuggestions();
    }

    private void updateChartTypeSuggestions() {
        if (currentDataSet == null) return;
        java.util.List<String> suggestions = dataService.getSuggestedChartTypes(currentDataSet);
        if (!suggestions.isEmpty()) {
            chartTypeCombo.removeAllItems();
            for (String s : suggestions) chartTypeCombo.addItem(s);
        }
    }
    
    private Icon createIcon(String emoji) {
        // Simple text-based icon
        return new ImageIcon();
    }

    private void snapshotComponent(JPanel comp, File file) throws Exception {
        int w = Math.max(600, comp.getWidth());
        int h = Math.max(400, comp.getHeight());
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(w, h, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics2D g2 = img.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0,0,w,h);
        // Layout and paint to ensure full render
        comp.setSize(w,h);
        comp.doLayout();
        comp.paint(g2);
        g2.dispose();
        javax.imageio.ImageIO.write(img, "png", file);
    }
}
