package ch.maturaarbeit.util;

import ch.maturaarbeit.CipherManager;
import ch.maturaarbeit.ciphers.Cipher;
import ch.maturaarbeit.ciphers.hill.HillParams;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Measure {
    private Cipher cipherToMeasure;
    private CipherManager cipherManager;
//    private long cipherDuration;
//    private MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
//    private ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

    private final String[] FILE_PATHS = {
            "src/main/resources/input/10_chars.txt",
            "src/main/resources/input/20_chars.txt",
            "src/main/resources/input/30_chars.txt",
            "src/main/resources/input/100_chars.txt",
            "src/main/resources/input/1000_chars.txt",
            "src/main/resources/input/5000_chars.txt",
            "src/main/resources/input/10000_chars.txt",
            "src/main/resources/input/100000_chars.txt",
            "src/main/resources/input/500000_chars.txt",
            "src/main/resources/input/1000000_chars.txt"
    };
//    private final String[] FILE_PATHS = {"src/main/resources/input/10_chars.txt", "src/main/resources/input/20_chars.txt", "src/main/resources/input/30_chars.txt"};
    private ArrayList<Measure> cipherMesaures = new ArrayList<>();
    private long cipherDuration;
    private MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    private ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();



    public Measure(Cipher cipher){
        this.cipherToMeasure = cipher;
    }
    public Measure(CipherManager cipherManager){
        this.cipherManager = cipherManager;
    }
    public Measure(){

    }
    public CipherManager getCipherManager() {
        return cipherManager;
    }
    public void setCipherManager(CipherManager cipherManager) {
        this.cipherManager = cipherManager;
    }
    public Cipher getCipherToMeasure() {
        return cipherToMeasure;
    }

    public void setCipherToMeasure(Cipher cipherToMeasure) {
        this.cipherToMeasure = cipherToMeasure;
    }


    public String measureCipher(Cipher cipher){
        int N = 50; // Anzahl der Messläufe
        int WARMUP_RUNS = 10; // Warm-up Durchläufe (nicht gemessen)
        FileImporter fileImporter = new FileImporter();


        Map<Integer, List<Long>> wallTimesByLen = new LinkedHashMap<>();
        Map<Integer, List<Long>> cpuTimesByLen  = new LinkedHashMap<>();

        boolean cpuSupported = threadBean.isCurrentThreadCpuTimeSupported();
        if (cpuSupported && !threadBean.isThreadCpuTimeEnabled()) {
            try { threadBean.setThreadCpuTimeEnabled(true); } catch (UnsupportedOperationException ignore) {}
        }

        for (String path : FILE_PATHS) {
            String plaintext = fileImporter.importFile(path);
            int len = plaintext.length();
            System.out.println("\n=== Datei: " + path + " | Plaintext length: " + len + " ===");

            // --- Warm-up ---
            for (int i = 0; i < WARMUP_RUNS; i++) {
                cipher.encrypt(plaintext);
            }
            System.out.println("Warm-up (" + WARMUP_RUNS + " Durchläufe) abgeschlossen.");

            List<Long> wallTimes = new ArrayList<>(N);
            List<Long> cpuTimes  = new ArrayList<>(N);

            MemoryUsage heapBeforeOnce = memoryBean.getHeapMemoryUsage();

            for (int run = 1; run <= N; run++) {
                long startWall = System.currentTimeMillis();
                long startCpu  = cpuSupported ? threadBean.getCurrentThreadCpuTime() : 0L;

                String cipherText = cipher.encrypt(plaintext);

                long endWall = System.currentTimeMillis();
                long endCpu  = cpuSupported ? threadBean.getCurrentThreadCpuTime() : 0L;

                long wallMs = endWall - startWall;
                wallTimes.add(wallMs);

                if (cpuSupported) {
                    long cpuMs = (endCpu - startCpu) / 1_000_000L;
                    cpuTimes.add(cpuMs);
                }

                if (cipherText == null) throw new AssertionError("encrypt returned null");
            }

            MemoryUsage heapAfterOnce = memoryBean.getHeapMemoryUsage();
            long usedBeforeMB = heapBeforeOnce.getUsed() / (1024 * 1024);
            long usedAfterMB  = heapAfterOnce.getUsed()  / (1024 * 1024);
            long diffMB       = (heapAfterOnce.getUsed() - heapBeforeOnce.getUsed()) / (1024 * 1024);

            wallTimesByLen.put(len, wallTimes);
            if (cpuSupported) cpuTimesByLen.put(len, cpuTimes);

            // --- Ausgabe ---
            System.out.println("Wall times (ms): " + wallTimes);
            if (cpuSupported) {
                System.out.println("CPU times (ms):  " + cpuTimes);
            } else {
                System.out.println("CPU time measurement not supported");
            }
            System.out.println("Used heap before: " + usedBeforeMB + " MB");
            System.out.println("Used heap after:  " + usedAfterMB  + " MB");
            System.out.println("Difference:       " + diffMB       + " MB");
        }
        String outputPath = "src/main/resources/output/" + cipher.name() + "_benchmark.json";
        FileExporter.saveResultsAsJson(outputPath, wallTimesByLen, cpuTimesByLen);

        return "";
}

}
