package com.quantcast.cookie.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MostActiveCookieFinder {

    public List<String> findMostActiveCookies(String filePathStr, LocalDate targetDate) {
        Path filePath = Paths.get(filePathStr);
        Map<String, Integer> cookieCounts = new HashMap<>();
        int maxCount = 0;
        List<String> mostActiveCookies = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            boolean isFirstLine = true;

            while ((line = reader.readLine()) != null) {
                if (isFirstLine && line.toLowerCase().startsWith("cookie")) {
                    isFirstLine = false;
                    continue;
                }
                isFirstLine = false;

                String[] parts = line.split(",");
                if (parts.length != 2) continue;

                String cookie = parts[0].trim();
                String timestampStr = parts[1].trim();
                
                LocalDate logDate = OffsetDateTime.parse(timestampStr)
                                                  .withOffsetSameInstant(ZoneOffset.UTC)
                                                  .toLocalDate();

                if (logDate.isAfter(targetDate)) {
                    continue; 
                } else if (logDate.isBefore(targetDate)) {
                    break; 
                }

                int count = cookieCounts.getOrDefault(cookie, 0) + 1;
                cookieCounts.put(cookie, count);

                if (count > maxCount) {
                    maxCount = count;
                    mostActiveCookies.clear();
                    mostActiveCookies.add(cookie);
                } else if (count == maxCount) {
                    mostActiveCookies.add(cookie);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return mostActiveCookies;
    }
}
