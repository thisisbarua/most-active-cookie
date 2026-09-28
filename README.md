# Most Active Cookie

A command-line application that processes a CSV cookie log file and returns the most active cookie(s) for a specific date. 

## Table of Contents
- [Prerequisites](#prerequisites)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Design Decisions](#design-decisions)

## Prerequisites
- Java 11 or higher
- Maven (for building and testing)

## Building the Project
To compile the source code and build the executable JAR, run:
```bash
mvn clean package
```
This will create a `most-active-cookie.jar` inside the `target/` directory.

## Running the Application
The application requires two parameters:
- `-f`: The path to the CSV log file
- `-d`: The target date in `YYYY-MM-DD` format (UTC)

**Example usage:**
```bash
java -jar target/most-active-cookie.jar -f cookie_log.csv -d 2018-12-09
```

**Expected output:**
```text
AtY0laUfhglK3lC7
```
*(If multiple cookies tie for the most active on the given date, all tied cookies will be printed on separate lines).*

## Testing
This project includes a comprehensive JUnit 5 test suite that covers edge cases, malformed data, and chronological short-circuiting logic.

To run the test suite:
```bash
mvn test
```

## Design Decisions
- **Streaming Parser**: The application reads the file line-by-line using a `BufferedReader`. This ensures low memory usage for file parsing, allowing it to easily process massive log files without loading them entirely into RAM.
- **On-the-Fly Aggregation**: Max cookie frequencies are calculated dynamically during the stream, avoiding the need for a secondary iteration pass over the entire dataset.
- **Early Exit Optimization**: Taking advantage of the requirement that the input file is strictly sorted chronologically (newest to oldest), the parser immediately terminates once it encounters a date older than the target date, saving significant CPU and I/O cycles.
