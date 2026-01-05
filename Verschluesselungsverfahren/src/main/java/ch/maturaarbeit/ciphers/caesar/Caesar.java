package ch.maturaarbeit.ciphers.caesar;

import ch.maturaarbeit.ciphers.Cipher;


/**
 * @Author
 * Implementiert die Cäsar Chffre
 */
public class Caesar implements Cipher<CaesarParams> {


    private int key;
    private long operationsCount = 0;

    public Caesar(int key){
        this.key = key;
    }
    public Caesar(){    }

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

    /**
     * Cäsar Chiffre Verschlüsselung
     * @param plaintext der Klartext
     * @return der verschlüsselte Text
     */
    @Override
    public String encrypt(String plaintext) {
        return caesarEncrypt(plaintext, key);
    }

    /**
     * Cäsar Chiffre Verschlüsselung
     * @param text Der Klartext
     * @param key Der Schlüssel zur Verschlüsselung
     * @return Der verschlüsselte Text
     */
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

}
