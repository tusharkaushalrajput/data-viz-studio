# Data Visualization Studio - Web Demo

This is a client-side web application for data visualization that runs on GitHub Pages.

## Features

- **CSV Import**: Upload and parse CSV files using PapaParse
- **Chart Generation**: Create Bar Charts, Pie Charts, Line Charts, and Scatter Plots using Chart.js
- **Data Preview**: View your data in a table format before visualization
- **Export**: Download charts as PNG images
- **Drag & Drop**: Drag CSV files directly onto the page
- **Sample Data**: Load sample data to test the application

## Live Demo

The application is deployed at: https://tusharkaushalrajput.github.io/data-viz-studio/

## How to Use

1. **Load Data**: 
   - Click "Import CSV" to upload a file
   - Click "Load Sample" to use the provided sample data
   - Or drag and drop a CSV file anywhere on the page

2. **Generate Charts**:
   - Select the desired chart type from the dropdown
   - Click "Generate Chart" to create the visualization
   - Use "Export PNG" to download the chart

3. **View Insights**:
   - The system automatically suggests appropriate chart types based on your data
   - Preview your data in the table before creating charts

## Technical Details

- **Dependencies**: Chart.js, PapaParse (loaded via CDN)
- **Processing**: All data processing happens client-side
- **Deployment**: Static files deployed via GitHub Actions to GitHub Pages
- **Browser Support**: Modern browsers with ES6+ support

## Sample Data Format

```csv
Product,Sales,Quarter
Laptops,1500,Q1
Phones,2300,Q1
Tablets,800,Q1
```

The application works best with data that has:
- A header row with column names
- At least one text column for categories
- At least one numeric column for values