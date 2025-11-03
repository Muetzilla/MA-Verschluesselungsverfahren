package ch.maturaarbeit.ciphers.caesar;

import ch.maturaarbeit.ciphers.Cipher;
import ch.maturaarbeit.ciphers.DecryptParams;
import ch.maturaarbeit.ciphers.EncryptParams;

public class Caesar implements Cipher<CaesarParams, CaesarParams> {

//    private double numberOfOperations = 0;

    private int key;
    private long operationsCount = 0;

    public Caesar(int key){
        this.key = key;
    }
    public Caesar(){

    }

    public int getKey() {

        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public long getOperationCount() {
        return operationsCount;
    }

    @Override
    public void setOperationCount(long operationsCount) {
        this.operationsCount = operationsCount;
    }

    @Override
    public String name() {
        return "Caesar";
    }

    @Override
    public String encrypt(String plaintext) {
        return caesarEncrypt(plaintext, key);
    }

    @Override
    public String decrypt(String ciphertext, CaesarParams params) {
        return "";
    }

    private String caesarEncrypt(String text, int key) {
        StringBuilder sb = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                sb.append((char) ('A' + (c - 'A' + key) % 26));
            } else if (c >= 'a' && c <= 'z') {
                sb.append((char) ('a' + (c - 'a' + key) % 26));
            } else {
                sb.append(c);
            }
            operationsCount++;
        }
        return sb.toString();
    }

//    @Override
//    public String encrypt(String plaintext, Object key) {
//        StringBuffer ciphertext = new StringBuffer();
//        for (int i = 0; i < plaintext.length(); i++) {
//            ciphertext.append(plaintext.charAt(i));
//        }
//        return ciphertext.toString();
//    }
}
