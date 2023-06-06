package com.solar.solarsimulator.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.solar.solarsimulator.exception.JsonFileIOException;

public interface FileUtils {

    static String getStringFromFile(String path) {
        try {
            Path jsonPath = Paths.get(path);
            return Files.readString(jsonPath);
        } catch (IOException exception) {
            throw new JsonFileIOException(
                    "Error finding JSON file: " + exception.getMessage(), exception);
        }
    }
}
