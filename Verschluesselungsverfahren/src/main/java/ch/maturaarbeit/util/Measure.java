package ch.maturaarbeit.util;

import ch.maturaarbeit.ciphers.Cipher;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;

public class Measure {
    private Cipher cipherToMeasure;
    private long cipherDuration;
    private MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    private ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();



    public Measure(Cipher cipher){
        this.cipherToMeasure = cipher;
    }
    public Measure(){

    }
    public Cipher getCipherToMeasure() {
        return cipherToMeasure;
    }

    public void setCipherToMeasure(Cipher cipherToMeasure) {
        this.cipherToMeasure = cipherToMeasure;
    }

    public long getCipherDuration() {
        return cipherDuration;
    }

    public void setCipherDuration(long cipherDuration) {
        this.cipherDuration = cipherDuration;
    }


    public String measureCipher(String plaintext, Object key){
        if (threadBean.isCurrentThreadCpuTimeSupported()) {
            long cpuTimeBefore = threadBean.getCurrentThreadCpuTime();
            long startingTime = System.currentTimeMillis();
            MemoryUsage heapBefore = memoryBean.getHeapMemoryUsage();
            String cipherText = cipherToMeasure.encrypt(plaintext, key);
            doWork();
            long endingTime = System.currentTimeMillis();
            MemoryUsage heapAfter = memoryBean.getHeapMemoryUsage();
            System.out.println("Used heap before: " + heapBefore.getUsed() / (1024 * 1024) + " MB");
            System.out.println("Used heap after: " + heapAfter.getUsed() / (1024 * 1024) + " MB");
            System.out.println("Difference: " + (heapAfter.getUsed() - heapBefore.getUsed()) / (1024 * 1024) + " MB");
            cipherDuration = endingTime - startingTime;
            long cpuTimeAfter = threadBean.getCurrentThreadCpuTime();
            System.out.println("CPU time used: " + (cpuTimeAfter - cpuTimeBefore) / 1_000_000 + " ms");
            return cipherText;
        } else {
            System.out.println("CPU time measurement not supported");
        }
        return "";
    }


    public void doWork(){
        int[] array = new int[10_000_00000]; // allocate some memory
        for (int i = 0; i < array.length; i++) array[i] = i;
    }
}
