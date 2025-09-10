package ch.maturaarbeit.ciphers.caesar;

import ch.maturaarbeit.ciphers.Cipher;

public class Caesar implements Cipher{

    private double numberOfOperations = 0;

    @Override
    public String encrypt(String plaintext, Object key) {
        StringBuffer ciphertext = new StringBuffer();
        for (int i = 0; i < plaintext.length(); i++) {
            ciphertext.append(plaintext.charAt(i));
        }
        return ciphertext.toString();
    }
}
