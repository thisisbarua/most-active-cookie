package com.quantcast.cookie.cli;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CommandLineParserTest {

    // SCENARIO 1: Valid arguments
    @Test
    void testValidArguments() {
        String[] args = {"-f", "cookie_log.csv", "-d", "2018-12-09"};
        CommandLineParser parser = new CommandLineParser(args);
        
        assertEquals("cookie_log.csv", parser.getFilePath());
        assertEquals(LocalDate.parse("2018-12-09"), parser.getTargetDate());
    }

    // SCENARIO 2: Valid arguments but order is swapped
    @Test
    void testValidArgumentsOutOfOrder() {
        String[] args = {"-d", "2018-12-09", "-f", "cookie_log.csv"};
        CommandLineParser parser = new CommandLineParser(args);
        
        assertEquals("cookie_log.csv", parser.getFilePath());
        assertEquals(LocalDate.parse("2018-12-09"), parser.getTargetDate());
    }

    // SCENARIO 3: Missing required flags
    @Test
    void testMissingArgumentsThrowsException() {
        String[] args = {"-f", "cookie_log.csv"}; // missing -d
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CommandLineParser(args);
        });
        assertTrue(exception.getMessage().contains("Usage:"));
    }

    // SCENARIO 4: Bad date formatting
    @Test
    void testInvalidDateFormatThrowsException() {
        String[] args = {"-f", "cookie_log.csv", "-d", "12-09-2018"}; // bad format
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CommandLineParser(args);
        });
        assertTrue(exception.getMessage().contains("Invalid date format"));
    }

    // SCENARIO 5: Odd number of arguments
    @Test
    void testOddNumberOfArguments() {
        String[] args = {"-f", "cookie_log.csv", "-d"}; // dangling flag
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new CommandLineParser(args);
        });
        // It throws Usage error because -d won't be mapped properly
        assertTrue(exception.getMessage().contains("Usage:"));
    }
}
