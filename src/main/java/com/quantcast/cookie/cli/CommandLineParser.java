package com.quantcast.cookie.cli;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

public class CommandLineParser {
    private final String filePath;
    private final LocalDate targetDate;

    public CommandLineParser(String[] args) {
        Map<String, String> argsMap = parseArgs(args);
        
        if (!argsMap.containsKey("-f") || !argsMap.containsKey("-d")) {
            throw new IllegalArgumentException("Usage: java -jar app.jar -f <filename> -d <date>");
        }

        this.filePath = argsMap.get("-f");
        try {
            this.targetDate = LocalDate.parse(argsMap.get("-d"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd (e.g., 2018-12-09).");
        }
    }

    private Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length - 1; i += 2) {
            map.put(args[i], args[i + 1]);
        }
        return map;
    }

    public String getFilePath() {
        return filePath;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }
}
