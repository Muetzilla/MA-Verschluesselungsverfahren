package ch.maturaarbeit.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author:
 * Importiert die Inhalte einer Datei als String.
 */
public class FileImporter {

    /**
     * Importiert die Datei am angegebenen Pfad und gibt deren Inhalt als String zurück.
     * @param path Der Pfad zur Datei.
     * @return Der Inhalt der Datei als String.
     */
    public String importFile(String path){
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(path))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
