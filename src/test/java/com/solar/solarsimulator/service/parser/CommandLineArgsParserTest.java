package com.solar.solarsimulator.service.parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandLineArgsParserTest {

    private CommandLineArgsParser parser;

    @BeforeEach
    void setUp() {
        parser = new CommandLineArgsParser();
    }

    @Test
    void testParsePathHappyScenario() {
        String[] args = {"path/to/file.json", "10"};
        assertEquals("path/to/file.json", parser.parsePath(args));
    }

    @Test
    void testParsePathUnhappyScenario() {
        String[] args = {};
        assertThrows(IllegalArgumentException.class, () -> parser.parsePath(args));
    }

    @Test
    void testParseElapsedDaysHappyScenario() {
        String[] args = {"path/to/file.json", "10"};
        assertEquals(10, parser.parseElapsedDays(args));
    }

    @Test
    void testParseElapsedDaysUnhappyScenarioNoElapsedDays() {
        String[] args = {"path/to/file.json"};
        assertThrows(IllegalArgumentException.class, () -> parser.parseElapsedDays(args));
    }

    @Test
    void testParseElapsedDaysUnhappyScenarioInvalidElapsedDays() {
        String[] args = {"path/to/file.json", "not a number"};
        assertThrows(IllegalArgumentException.class, () -> parser.parseElapsedDays(args));
    }
}