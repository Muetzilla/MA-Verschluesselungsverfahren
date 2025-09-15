package ch.maturaarbeit;

import ch.maturaarbeit.ciphers.Cipher;
import ch.maturaarbeit.ciphers.caesar.Caesar;
import ch.maturaarbeit.ciphers.Hill;
import ch.maturaarbeit.util.FileImporter;
import ch.maturaarbeit.util.Measure;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;

public class CipherManager {
    private ArrayList<Cipher> ciphers = new ArrayList<>();
    private final String[] FILE_PATHS = {"src/main/resources/10_chars.txt", "src/main/resources/100_chars.txt", "src/main/resources/1000_chars.txt", "src/main/resources/5000_chars.txt","src/main/resources/10000_chars.txt", "src/main/resources/100000_chars.txt"};
    private final String HILL_KEY = "HILL";
    private ArrayList<Measure> cipherMesaures = new ArrayList<>();
    private long cipherDuration;
    private MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    private ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    public CipherManager(){
        ciphers.add(new Caesar());

    }
    public void manager(){
        Hill hill = new Hill();
        FileImporter fileImporter = new FileImporter();
        for (String path : FILE_PATHS) {
            String cipherText = "";
            String plaintext = fileImporter.importFile(path);
            System.out.println("Plaintext length: " + plaintext.length());
            if (threadBean.isCurrentThreadCpuTimeSupported()) {
                long cpuTimeBefore = threadBean.getCurrentThreadCpuTime();
                long startingTime = System.currentTimeMillis();
                MemoryUsage heapBefore = memoryBean.getHeapMemoryUsage();
                cipherText = hill.encrypt(plaintext, HILL_KEY, 2);
                long endingTime = System.currentTimeMillis();
                MemoryUsage heapAfter = memoryBean.getHeapMemoryUsage();
                System.out.println("Used heap before: " + heapBefore.getUsed() / (1024 * 1024) + " MB");
                System.out.println("Used heap after: " + heapAfter.getUsed() / (1024 * 1024) + " MB");
                System.out.println("Difference: " + (heapAfter.getUsed() - heapBefore.getUsed()) / (1024 * 1024) + " MB");
                cipherDuration = endingTime - startingTime;
                long cpuTimeAfter = threadBean.getCurrentThreadCpuTime();
                System.out.println("CPU time used: " + (cpuTimeAfter - cpuTimeBefore) / 1_000_000 + " ms");
            } else {
                System.out.println("CPU time measurement not supported");
            }
//            System.out.println("The encode text is: \"" + cipherText + "\" and the duration is: " + cipherDuration +  "ms.");
        }


//        for (Cipher c : ciphers) {
//          cipherMesaures.add(new Measure(c));
//        }
//
//        for(Measure cm : cipherMesaures){
//            String cipherText = cm.measureCipher("hackme", 5);
//            long duration = cm.getCipherDuration();
//            System.out.println("The encode text is: \"" + cipherText + "\" and the duration is: " + duration +  "ms.");
//        }
    }
}
