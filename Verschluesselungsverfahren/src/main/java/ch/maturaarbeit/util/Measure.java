package ch.maturaarbeit.util;

import ch.maturaarbeit.ciphers.Cipher;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.*;

/**
 * @Author
 * Misst die veschiedenen Werte Messwerte der Chiffren
 */
public class Measure {
    private final String[] FILE_PATHS = {
         // Pfad hier einfügen
    };


    public Measure(){

    }

    /**
     * Misst die verschiedenen Werte einer Chiffre
     * @param cipher die zu messende Chiffre
     */
    public void measureCipher(Cipher cipher) {

        final int NUMBER_OF_RUNS = 15;
        final int WARMUP_RUNS = 5;
        final long MB = 1024 * 1024;

        FileImporter fileImporter = new FileImporter();
        float ratioPlainCipherText = 1.0f;

        Map<Integer, List<Long>> wallTimesByLen = new LinkedHashMap<>();
        Map<Integer, List<Long>> cpuTimesByLen  = new LinkedHashMap<>();
        Map<Integer, List<Long>> heapUsagePerRunByLen = new LinkedHashMap<>();
        Map<Integer, Long> operationsNumber     = new LinkedHashMap<>();

        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();

        boolean cpuSupported = threadBean.isThreadCpuTimeSupported();
        if (cpuSupported && !threadBean.isThreadCpuTimeEnabled()) {
            try {
                threadBean.setThreadCpuTimeEnabled(true);
            } catch (UnsupportedOperationException ignore) {}
        }

        for (String path : FILE_PATHS) {

            String plaintext = fileImporter.importFile(path);
            int len = plaintext.length();

            System.out.println("\n=== Datei: " + path + " | Plaintext length: " + len + " ===");

            // -------------------- WARM-UP --------------------
            for (int i = 0; i < WARMUP_RUNS; i++) {
                cipher.encrypt(plaintext);
            }
            System.out.println("Warm-up (" + WARMUP_RUNS + " Durchläufe) abgeschlossen.");

            List<Long> wallTimes = new ArrayList<>(NUMBER_OF_RUNS);
            List<Long> cpuTimes  = new ArrayList<>(NUMBER_OF_RUNS);
            List<Long> heapDeltas = new ArrayList<>(NUMBER_OF_RUNS);

            cipher.setOperationCount(0);
            int totalNumberOfCipherChars = 0;

            long peakUsed = 0;

            for (int run = 1; run <= NUMBER_OF_RUNS; run++) {

                forceGc();
                long heapBefore = memoryBean.getHeapMemoryUsage().getUsed();

                long startWallNs = System.nanoTime();
                long startCpuNs  = cpuSupported
                        ? threadBean.getCurrentThreadCpuTime()
                        : 0L;

                String cipherText = cipher.encrypt(plaintext);

                long endWallNs = System.nanoTime();
                long endCpuNs  = cpuSupported
                        ? threadBean.getCurrentThreadCpuTime()
                        : 0L;

                long heapAfter = memoryBean.getHeapMemoryUsage().getUsed();
                long heapDelta = heapAfter - heapBefore;

                peakUsed = Math.max(peakUsed, heapAfter);

                // Zeiten speichern
                wallTimes.add((endWallNs - startWallNs) / 1_000_000L);
                if (cpuSupported) {
                    cpuTimes.add((endCpuNs - startCpuNs) / 1_000_000L);
                }

                heapDeltas.add(heapDelta / MB);

                totalNumberOfCipherChars += cipherText.length();
            }

            heapUsagePerRunByLen.put(len, heapDeltas);

            wallTimesByLen.put(len, wallTimes);
            if (cpuSupported) cpuTimesByLen.put(len, cpuTimes);

            operationsNumber.put(len,
                    cipher.getOperationCount() / NUMBER_OF_RUNS);

            ratioPlainCipherText =
                    (float) (totalNumberOfCipherChars / NUMBER_OF_RUNS)
                            / plaintext.length();

        }

        String outputPath =
                "src/main/resources/output/" + cipher.name() + "_benchmark.json";

        FileExporter.saveResultsAsJson(
                outputPath,
                wallTimesByLen,
                cpuTimesByLen,
                operationsNumber,
                ratioPlainCipherText,
                heapUsagePerRunByLen
        );
    }


    /**
    * Erzwingt Speicherbereinigung für stabilere Messungen
    */
    private static void forceGc() {
        System.gc();
        try {
            Thread.sleep(200);
        } catch (InterruptedException ignored) {}
    }

}
