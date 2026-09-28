package com.quantcast.cookie;

import com.quantcast.cookie.cli.CommandLineParser;
import com.quantcast.cookie.service.MostActiveCookieFinder;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            CommandLineParser parser = new CommandLineParser(args);
            MostActiveCookieFinder finder = new MostActiveCookieFinder();

            List<String> mostActive = finder.findMostActiveCookies(
                    parser.getFilePath(), 
                    parser.getTargetDate()
            );

            for (String cookie : mostActive) {
                System.out.println(cookie);
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}
