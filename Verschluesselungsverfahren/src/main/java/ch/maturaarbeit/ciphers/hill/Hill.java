package ch.maturaarbeit.ciphers.hill;
import ch.maturaarbeit.ciphers.Cipher;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;

/**
 * @Author
 * Implementiert die Hill Chiffre
 */
public class Hill implements Cipher<HillParams> {
    private String key;
    private int blockSize;
    private long operationsCount = 0;


    public Hill() {

    }
    public Hill(String key, int blockSize) {
        this.key = key;
        this.blockSize = blockSize;
    }
    @Override
    public long getOperationCount() {
        return operationsCount;
    }

    @Override
    public void setOperationCount(long operationsCount) {
        this.operationsCount = operationsCount;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Hill Chiffre Verschlüsselung
     * @param plaintext der Klartext
     * @param key das Schlüsselwort
     * @param blockSize die Länge der Blöcke und Grösse der Vektoren
     * @return der verschlüsselte Text
     */
    public String hillEncrypt(String plaintext, String key, int blockSize) {
        ArrayList<SimpleMatrix> plaintextBlocks = new ArrayList<>();
        SimpleMatrix keyMatrix = new SimpleMatrix(blockSize, blockSize);
        StringBuilder cipherText = new StringBuilder();
        String[] results = plaintext.split("(?s)(?<=\\G.{" + blockSize + "})");
        if (plaintext.length() % blockSize != 0) {
            int charsToAdd = blockSize - (plaintext.length() % blockSize);
            for (int i = 0; i < charsToAdd; i++) {
                results[results.length - 1] += "X";
            }
        }

        for (String result : results) {
            SimpleMatrix tempVector = new SimpleMatrix(blockSize, 1);

            for (int i = 0; i < result.length(); i++) {
                tempVector.set(i, (int) result.charAt(i) - 65);
                // Anzahl der Operationen wird erhöht, da ein Schritt nötig ist, um das Zeichen in eine Zahl umzuwandeln.
                operationsCount++;
            }
            plaintextBlocks.add(tempVector);

        }

        int keyCharCounter = 0;
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                keyMatrix.set(i, j, (int) key.charAt(keyCharCounter) - 65);
                keyCharCounter++;
            }
        }

        ArrayList<SimpleMatrix> multipliedVectors = new ArrayList<>();
        for (SimpleMatrix vector : plaintextBlocks) {
            multipliedVectors.add(keyMatrix.mult(vector));
            // Anzahl der Operationen wird erhöht, da die Matrixmultiplikation durchgeführt wird.
            operationsCount++;
        }


        for (SimpleMatrix v : multipliedVectors) {
            for (int i = 0; i < v.numRows(); i++) {
                double value = v.get(i, 0);
                cipherText.append((char) ((int) (value % 26) + 65));
                // Anzahl der Operationen wird erhöht, da das Ergebnis in ein Zeichen umgewandelt werden muss.
                operationsCount++;
            }
        }
        return cipherText.toString();
    }

    /**
     * Name der Chiffre
     * @return Der Name der Chiffre
     */
    @Override
    public String name() {
        return "Hill";
    }

    /**
     * Verschlüsselung des Klartexts
     * @param plaintext der Klartext
     * @return der verschlüsselte Text
     */
    @Override
    public String encrypt(String plaintext) {
        return hillEncrypt(plaintext, key, blockSize);
    }

}
