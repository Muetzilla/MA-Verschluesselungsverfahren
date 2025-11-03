package ch.maturaarbeit.util;

import ch.maturaarbeit.CipherManager;
import ch.maturaarbeit.ciphers.Cipher;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.*;

public class Measure {
    private Cipher cipherToMeasure;
    private CipherManager cipherManager;
//    private long cipherDuration;
//    private MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
//    private ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();

    private final String[] FILE_PATHS = {
            "src/main/resources/input/10_chars.txt",
//            "src/main/resources/input/20_chars.txt",
//            "src/main/resources/input/30_chars.txt",
//            "src/main/resources/input/50_chars.txt",
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
        int NUMBER_OF_RUNS = 5;
        int WARMUP_RUNS = 1;
        FileImporter fileImporter = new FileImporter();


        Map<Integer, List<Long>> wallTimesByLen = new LinkedHashMap<>();
        Map<Integer, List<Long>> cpuTimesByLen  = new LinkedHashMap<>();
        Map<Integer, Long> operationsNumber  = new LinkedHashMap<>();

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

            List<Long> wallTimes = new ArrayList<>(NUMBER_OF_RUNS);
            List<Long> cpuTimes  = new ArrayList<>(NUMBER_OF_RUNS);

            MemoryUsage heapBeforeOnce = memoryBean.getHeapMemoryUsage();
            cipher.setOperationCount(0);

            for (int run = 1; run <= NUMBER_OF_RUNS; run++) {
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
            operationsNumber.put(len, cipher.getOperationCount() / NUMBER_OF_RUNS);

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
        FileExporter.saveResultsAsJson(outputPath, wallTimesByLen, cpuTimesByLen, operationsNumber);

        return "";
}

}
