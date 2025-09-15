package ch.maturaarbeit.ciphers.hill;
import ch.maturaarbeit.ciphers.Cipher;
import ch.maturaarbeit.ciphers.DecryptParams;
import ch.maturaarbeit.ciphers.EncryptParams;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;


public class Hill implements Cipher<HillParams, HillParams> {
    private String key;
    private int blockSize;

    public Hill() {

    }
    public Hill(String key, int blockSize) {
        this.key = key;
        this.blockSize = blockSize;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }

    public String hillEncrypt(String plaintext, String key, int blockSize) {
//        plaintext = plaintext.toUpperCase();
        ArrayList<SimpleMatrix> plaintextBlocks = new ArrayList<>();
        SimpleMatrix keyMatrix = new SimpleMatrix(blockSize, blockSize);
        StringBuilder cipherText = new StringBuilder();
        String[] results = plaintext.split("(?s)(?<=\\G.{" + blockSize + "})");
        if (plaintext.length() % blockSize != 0) {
            int charsToAdd = blockSize - (plaintext.length() % blockSize);
            for (int i = 0; i < charsToAdd; i++) {
                results[results.length - 1] += "X"; // add X to the last block
            }
        }
        for (String result : results) {
            SimpleMatrix tempVector = new SimpleMatrix(blockSize, 1);

            for (int i = 0; i < result.length(); i++) {
                tempVector.set(i, (int) result.charAt(i) - 65);
            }
            plaintextBlocks.add(tempVector);

        }

        int keyCharCounter = 0;
        //TODO: if there are not enough chars ad "X" at the end
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                keyMatrix.set(i, j, (int) key.charAt(keyCharCounter) - 65);
                keyCharCounter++;
            }
        }
        ArrayList<SimpleMatrix> multipliedVectors = new ArrayList<>();
        for (SimpleMatrix vector : plaintextBlocks) {
            multipliedVectors.add(keyMatrix.mult(vector));
        }
        for (SimpleMatrix v : multipliedVectors) {
            for (int i = 0; i < v.numRows(); i++) {
                double value = v.get(i, 0);
                cipherText.append((char) ((int) (value % 26) + 65));
            }
        }
        return cipherText.toString();
    }


    @Override
    public String name() {
        return "Hill";
    }

    @Override
    public String encrypt(String plaintext) {
        return hillEncrypt(plaintext, key, blockSize);
    }

    @Override
    public String decrypt(String ciphertext, HillParams params) {
        return "";
    }
}
