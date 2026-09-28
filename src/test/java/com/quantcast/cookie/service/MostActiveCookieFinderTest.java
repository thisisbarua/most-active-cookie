package com.quantcast.cookie.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MostActiveCookieFinderTest {

    private MostActiveCookieFinder finder;
    private Path tempLogFile;

    @BeforeEach
    void setUp() throws IOException {
        finder = new MostActiveCookieFinder();
        tempLogFile = Files.createTempFile("cookie_log", ".csv");
        
        List<String> lines = Arrays.asList(
                "cookie,timestamp",
                "AtY0laUfhglK3107,2018-12-09T14:19:00+00:00",
                "SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00",
                "5UAVanZf6UtGyKVS,2018-12-09T07:25:00+00:00",
                "AtY0laUfhglK31C7,2018-12-09T06:19:00+00:00",
                "SAZuXPGUrfbcn5UA,2018-12-08T22:03:00+00:00",
                "4sMM2LxV07bPJzwf,2018-12-08T21:30:00+00:00",
                "fbcn5UAVanZf6UtG,2018-12-08T09:30:00+00:00",
                "4sMM2LxV07bPJzwf,2018-12-07T23:30:00+00:00"
        );
        Files.write(tempLogFile, lines);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempLogFile);
    }

    // SCENARIO 1: Multiple Winners on a specific date
    @Test
    void testSingleDateWithMultipleActiveCookies() {
        LocalDate date = LocalDate.parse("2018-12-08");
        List<String> result = finder.findMostActiveCookies(tempLogFile.toString(), date);
        
        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList("SAZuXPGUrfbcn5UA", "4sMM2LxV07bPJzwf", "fbcn5UAVanZf6UtG")));
    }

    // SCENARIO 2: Single Winner on a specific date
    @Test
    void testSingleMostActiveCookie() {
        LocalDate date = LocalDate.parse("2018-12-07");
        List<String> result = finder.findMostActiveCookies(tempLogFile.toString(), date);
        
        assertEquals(1, result.size());
        assertTrue(result.contains("4sMM2LxV07bPJzwf"));
    }
    
    // SCENARIO 3: No cookies match the date (Date not in file)
    @Test
    void testNoCookiesFoundForDate() {
        LocalDate date = LocalDate.parse("2018-12-10");
        List<String> result = finder.findMostActiveCookies(tempLogFile.toString(), date);
        
        assertTrue(result.isEmpty());
    }

    // SCENARIO 4: Empty File handling
    @Test
    void testEmptyFile() throws IOException {
        List<String> emptyLines = Arrays.asList("cookie,timestamp");
        Files.write(tempLogFile, emptyLines); // Overwrite with empty
        LocalDate date = LocalDate.parse("2018-12-09");
        List<String> result = finder.findMostActiveCookies(tempLogFile.toString(), date);
        assertTrue(result.isEmpty());
    }

    // SCENARIO 5: Malformed Data handling (missing commas, bad lines)
    @Test
    void testMalformedLinesAreIgnored() throws IOException {
        List<String> malformedLines = Arrays.asList(
            "cookie,timestamp",
            "AtY0laUfhglK3107", // missing timestamp
            "SAZuXPGUrfbcn5UA,2018-12-09T10:13:00+00:00",
            "invalid_line_format_no_comma",
            "SAZuXPGUrfbcn5UA,2018-12-09T09:13:00+00:00"
        );
        Files.write(tempLogFile, malformedLines); // Overwrite
        LocalDate date = LocalDate.parse("2018-12-09");
        List<String> result = finder.findMostActiveCookies(tempLogFile.toString(), date);
        
        // The malformed lines should be skipped, and SAZu... should win with count 2
        assertEquals(1, result.size());
        assertTrue(result.contains("SAZuXPGUrfbcn5UA"));
    }

    // SCENARIO 6: Early exit triggers properly
    @Test
    void testTargetDateOlderThanAnyLogInFile() {
        // We know the file starts at Dec 9th. If we ask for Dec 6th, it should break immediately.
        LocalDate date = LocalDate.parse("2018-12-06");
        List<String> result = finder.findMostActiveCookies(tempLogFile.toString(), date);
        assertTrue(result.isEmpty());
    }
}
