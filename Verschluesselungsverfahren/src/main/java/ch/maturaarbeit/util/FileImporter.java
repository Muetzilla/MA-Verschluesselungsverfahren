package ch.maturaarbeit.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileImporter {

    public String importFile(String path){
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return "";
    }
}
