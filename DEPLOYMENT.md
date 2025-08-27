# Data Visualization System - Deployment Guide

## Prerequisites Verification

### 1. Java Environment
- Java 8 or higher required
- Check: `java -version` and `javac -version`

### 2. Maven (Recommended)
- Download from: https://maven.apache.org/download.cgi
- Add to PATH environment variable
- Check: `mvn -version`

### 3. Memory Requirements
- Minimum 2GB RAM available for JVM
- Increase virtual memory/paging file if needed

## Quick Memory Fix (Windows)
1. Win + R → sysdm.cpl
2. Advanced → Performance Settings → Advanced → Change
3. Set paging file: Initial 4096 MB, Maximum 8192 MB
4. Restart system

## Deployment Options

### Option A: Maven-based Deployment (Recommended)

#### Step 1: Build the Application
```bash
cd datavizsys
mvn clean compile package
```

#### Step 2: Run the Application
```bash
# Method 1: Using Maven exec plugin
mvn exec:java -Dexec.mainClass="com.dvs.App"

# Method 2: Using built JAR
java -jar target/datavizsys-1.0-SNAPSHOT.jar

# Method 3: With custom memory settings
java -Xms64m -Xmx512m -jar target/datavizsys-1.0-SNAPSHOT.jar
```

### Option B: Manual Deployment (No Maven)

#### Step 1: Download Dependencies Manually
Create `lib` folder and download:
- jfreechart-1.5.3.jar
- jcommon-1.0.24.jar
- opencsv-5.7.1.jar
- commons-lang3-3.12.0.jar
- commons-io-2.11.0.jar

#### Step 2: Compile Java Files
```bash
javac -d target/classes -cp "lib/*" src/main/java/com/dvs/**/*.java
```

#### Step 3: Create JAR
```bash
jar cfm datavizsys.jar MANIFEST.MF -C target/classes .
```

#### Step 4: Run Application
```bash
java -cp "datavizsys.jar;lib/*" com.dvs.App
```

### Option C: Distribution Package

#### Step 1: Create Distribution
```bash
# Run the packaging script
package.bat
```

#### Step 2: Test Distribution
```bash
cd dist
run.bat
```

#### Step 3: Create ZIP for Distribution
Zip the entire `dist` folder for end users.

## Production Deployment

### 1. Server Deployment
```bash
# For headless servers (no GUI)
java -Djava.awt.headless=true -jar datavizsys.jar

# With service wrapper (Linux)
nohup java -jar datavizsys.jar > app.log 2>&1 &
```

### 2. Desktop Application Distribution
- Package with `package.bat`
- Include JRE for user convenience
- Create installer with tools like Install4J or Inno Setup

### 3. Web Application (Future)
- Add Spring Boot for web interface
- Deploy to cloud platforms (AWS, Azure, Heroku)

## Performance Optimization

### Memory Settings
```bash
# Low memory systems
java -Xms32m -Xmx256m -jar datavizsys.jar

# Standard systems
java -Xms128m -Xmx1024m -jar datavizsys.jar

# High memory systems
java -Xms256m -Xmx2048m -jar datavizsys.jar
```

### Garbage Collection
```bash
# G1 Garbage Collector (recommended)
java -XX:+UseG1GC -jar datavizsys.jar

# Parallel GC for better throughput
java -XX:+UseParallelGC -jar datavizsys.jar
```

## Testing Deployment

### 1. Smoke Test
```bash
# Test basic functionality
java -cp target/classes com.dvs.test.SimpleTest
```

### 2. GUI Test
```bash
# Start application and verify:
# - Window opens successfully
# - Menus and toolbar work
# - Sample data loads
# - Charts generate correctly
```

### 3. Data Test
```bash
# Test with various data formats:
# - CSV import
# - Manual data entry
# - Different chart types
# - Export functionality
```

## Troubleshooting Deployment

### Common Issues

#### 1. Memory Errors
- **Problem**: `OutOfMemoryError` or `Could not reserve enough space`
- **Solution**: Increase virtual memory, use `-Xmx` flags

#### 2. Missing Dependencies
- **Problem**: `ClassNotFoundException`
- **Solution**: Ensure all JAR files are in classpath

#### 3. GUI Not Showing
- **Problem**: Application starts but no window appears
- **Solution**: Check display settings, try different look-and-feel

#### 4. CSV Import Fails
- **Problem**: Cannot read CSV files
- **Solution**: Verify file encoding, check file permissions

### Debug Mode
```bash
# Enable debug logging
java -Djava.util.logging.level=FINE -jar datavizsys.jar

# Enable Swing debugging
java -Dswing.debug=true -jar datavizsys.jar
```

## Monitoring & Maintenance

### 1. Log Files
- Application logs: `app.log`
- Error logs: `error.log`
- Performance metrics via JMX

### 2. Health Checks
- Memory usage monitoring
- Response time tracking
- Error rate monitoring

### 3. Updates & Patches
- Version control with Git
- Automated builds with CI/CD
- Database migration scripts (future)

## Security Considerations

### 1. File Access
- Validate CSV file inputs
- Sanitize file paths
- Limit file size uploads

### 2. Data Privacy
- No data transmitted externally
- Local data processing only
- Secure temporary file handling

### 3. System Resources
- Memory limits enforcement
- CPU usage monitoring
- Disk space management

---
**Deployment Guide v1.0** - Complete deployment instructions for Data Visualization System
