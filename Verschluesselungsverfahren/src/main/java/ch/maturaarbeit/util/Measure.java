package ch.maturaarbeit.util;

import ch.maturaarbeit.ciphers.Cipher;

public class Measure {
    private Cipher cipherToMeasure;
    private long cipherDuration;

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


    public String measureCipherRuntime(String plaintext, Object key){
        long startingTime = System.currentTimeMillis();
        String cipherText = cipherToMeasure.encrypt(plaintext, key);
        long endingTime = System.currentTimeMillis();
        cipherDuration = endingTime - startingTime;

        return cipherText;
    }
}
