package ch.maturaarbeit.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:
 * Exportiert die Messergebnisse in eine JSON-Datei.
 */
public class FileExporter {
    /**
     *  Speichert die Messergebnisse als JSON-Datei.
     * @param filePath Der Pfad zur Ausgabedatei.
     * @param wallTimesByLen Messwerte der Verschlüsselungsdauer.
     * @param cpuTimesByLen CPU-Zeiten der Verschlüsselung.
     * @param operationsNumber Anzahl der durchgeführten Operationen.
     * @param ratioPlainCipherText Verhältnis von Klartextlänge und verschlüsseltem Text.
     * @param ramUsageByLen RAM-Nutzung während der Verschlüsselung.
     */
    public static void saveResultsAsJson(String filePath,
                                         Map<Integer, List<Long>> wallTimesByLen,
                                         Map<Integer, List<Long>> cpuTimesByLen, Map<Integer, Long> operationsNumber, float ratioPlainCipherText, Map<Integer, List<Long>> ramUsageByLen) {
        Map<Integer, Map<String, Object>> exportData = new LinkedHashMap<>();

        for (Map.Entry<Integer, List<Long>> entry : wallTimesByLen.entrySet()) {
            int textLen = entry.getKey();
            List<Long> wallTimes = entry.getValue();
            List<Long> cpuTimes  = cpuTimesByLen.getOrDefault(textLen, List.of());
            List<Long> ramUsage  = ramUsageByLen.getOrDefault(textLen, List.of());


            Map<String, Object> values = new LinkedHashMap<>();
            values.put("wall_times", wallTimes);
            values.put("wall_avg", avg(wallTimes));

            values.put("cpu_times", cpuTimes);
            values.put("cpu_avg", avg(cpuTimes));

            Long ops = operationsNumber.get(textLen);
            if (ops != null) {
                values.put("operations_number", ops);
            }
            values.put("ratio_plain_cipher_text", ratioPlainCipherText);

            values.put("ram_usage", ramUsage);
            values.put("ram_avg", avg(ramUsage));

            exportData.put(textLen, values);
        }

        Map<String, Object> root = new LinkedHashMap<>();
        root.put("textLen", exportData);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(root, writer);
            System.out.println("Ergebnisse erfolgreich gespeichert als JSON: " + filePath);
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der JSON-Datei: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Berechnet den Durchschnittswert einer Liste von Long-Werten.
     * @param values Die Liste der Long-Werte.
     * @return Der Durchschnittswert als double.
     */
    private static double avg(List<Long> values) {
        if (values.isEmpty()) return 0.0;
        long sum = 0L;
        for (long v : values) sum += v;
        return sum / (double) values.size();
    }
}
