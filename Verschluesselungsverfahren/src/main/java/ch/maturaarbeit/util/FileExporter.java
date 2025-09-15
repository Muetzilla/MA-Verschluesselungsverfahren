package ch.maturaarbeit.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FileExporter {
    public static void saveResultsAsJson(String filePath,
                                         Map<Integer, List<Long>> wallTimesByLen,
                                         Map<Integer, List<Long>> cpuTimesByLen) {
        Map<Integer, Map<String, Object>> exportData = new LinkedHashMap<>();

        for (Map.Entry<Integer, List<Long>> entry : wallTimesByLen.entrySet()) {
            int textLen = entry.getKey();
            List<Long> wallTimes = entry.getValue();
            List<Long> cpuTimes  = cpuTimesByLen.getOrDefault(textLen, List.of());

            Map<String, Object> values = new LinkedHashMap<>();
            values.put("wall_times", wallTimes);
            values.put("wall_avg", avg(wallTimes));

            if (!cpuTimes.isEmpty()) {
                values.put("cpu_times", cpuTimes);
                values.put("cpu_avg", avg(cpuTimes));
            }

            exportData.put(textLen, values);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(exportData, writer);
            System.out.println("Ergebnisse erfolgreich gespeichert als JSON: " + filePath);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der JSON-Datei: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static double avg(List<Long> values) {
        if (values.isEmpty()) return 0.0;
        long sum = 0L;
        for (long v : values) sum += v;
        return sum / (double) values.size();
    }
}
