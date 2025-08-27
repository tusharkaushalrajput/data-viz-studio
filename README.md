<<<<<<< HEAD
# data-viz-studio
Data Visualization System built in Java (Swing/JFreeChart) with a dependency-light Simple mode and a client-side web demo (Chart.js + PapaParse). Import CSV, preview, auto-suggest charts, and export PNG. Deployed via GitHub Pages from /docs.
=======
# Data Visualization System

A powerful desktop application for creating interactive data visualizations from CSV files and manual data entry.

## Features

✅ **Multiple Chart Types**: Bar Charts, Pie Charts, Line Graphs, Scatter Plots  
✅ **CSV Import**: Load data from CSV files with automatic data type detection  
✅ **Manual Data Entry**: Interactive dialog for entering data manually  
✅ **Real-time Preview**: See your data before generating charts  
✅ **Export Options**: Save charts for reports and presentations  
✅ **Professional GUI**: Intuitive interface with toolbar and menus  

## System Requirements

- **Java**: Version 8 or higher
- **Memory**: 2GB RAM recommended
- **Storage**: 50MB free space
- **OS**: Windows 7/8/10/11, macOS, Linux

## Quick Start

### Option 1: Using Pre-built Package
1. Download and extract the distribution package
2. Double-click `run.bat` (Windows) or run `java -jar datavizsys.jar`
3. Import the sample CSV file from the `data` folder
4. Generate your first chart!

### Option 2: Building from Source
```bash
# Clone the repository
git clone [repository-url]
cd datavizsys

# Build with Maven
mvn clean package

# Run the application
java -jar target/datavizsys-1.0-SNAPSHOT.jar
```

## Usage Guide

### 1. Import Data
- **CSV Import**: Click "Import CSV" and select your file
- **Manual Entry**: Click "Manual Entry" to input data directly
- **Sample Data**: Use the provided sample data to explore features

- **Line Chart**: Display trends over time
- **Scatter Plot**: Analyze correlations
- Charts automatically update when data changes

```csv
Product,Sales,Quarter
Laptops,1500,Q1
Phones,2300,Q1
Tablets,800,Q1
```

### Time Series Data (CSV)
```csv
Month,Revenue,Expenses
Jan,50000,35000
Feb,55000,38000
Mar,48000,32000
```

## Troubleshooting

### Memory Issues
If you get "insufficient memory" errors:
1. Close other applications
2. Increase virtual memory (Windows: Control Panel → System → Advanced)
3. Use reduced memory settings: `java -Xmx256m -jar datavizsys.jar`

### Chart Generation Issues
- Ensure data has both text and numeric columns for bar/pie charts
- Use numeric data in both X and Y columns for line/scatter plots
- Check that CSV files use comma separators

- **Charts**: JFreeChart library
- **CSV Processing**: OpenCSV library
- **Build Tool**: Maven
- Dashboard creation
- Database connectivity
- Advanced statistical functions
- Check the troubleshooting section above
- Review sample data formats
- Ensure system requirements are met

