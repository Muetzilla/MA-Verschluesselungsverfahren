package ch.maturaarbeit.ciphers.caesar;
import org.ejml.simple.SimpleMatrix;

import java.util.ArrayList;


public class Hill {

    public Hill(){

    }

    public String encrypt(String plaintext, String key, int blockSize){
        ArrayList<SimpleMatrix> plaintextBlocks = new ArrayList<>();
        SimpleMatrix keyMatrix = new SimpleMatrix(blockSize , blockSize);
        StringBuilder cipherText = new StringBuilder();
        String[] results = plaintext.split("(?<=\\G.{" + blockSize + "})");

        if(plaintext.length() % blockSize != 0) {
            int charsToAdd = blockSize - (plaintext.length() % blockSize);
            for(int i = 0; i < charsToAdd; i++){
                results[results.length -1] += "X"; // add X to the last block
            }
        }

        for (String result : results) {
            SimpleMatrix tempVector = new SimpleMatrix(blockSize, 1);

            for (int i = 0; i < result.length(); i++) {
                System.out.println((int)result.charAt(i));
                System.out.println("-65 : " + ((int)result.charAt(i) - 65));
                tempVector.set(i, (int)result.charAt(i) - 65);
                System.out.println("Temp Vector: " + tempVector);
            }
            plaintextBlocks.add(tempVector);

        }
        System.out.println("Plaintext Blocks: " + plaintextBlocks);

        int keyCharCounter = 0;
        //TODO: if there are not enough chars ad "X" at the end
        for (int i = 0; i < blockSize; i++) {
            for (int j = 0; j < blockSize; j++) {
                keyMatrix.set(i,j, (int)key.charAt(keyCharCounter) - 65);
                keyCharCounter++;
            }
        }

        System.out.println(plaintextBlocks);
        System.out.println("Key Matrix: " + keyMatrix);

        ArrayList<SimpleMatrix> multipliedVectors = new ArrayList<>();
        System.out.println("Plaintext Blocks: " + plaintextBlocks.size());
        for (SimpleMatrix vector : plaintextBlocks) {
            System.out.println("Vector: " + vector);
            System.out.println("Key Matrix: " + keyMatrix);
            multipliedVectors.add(keyMatrix.mult(vector));
        }
        System.out.println("Multiplied Vectors: " + multipliedVectors);
        System.out.println("Multiplied Vectors SIZE: " + multipliedVectors.size());

        for (SimpleMatrix v : multipliedVectors) {
            for (int i = 0; i < v.numRows(); i++) {
                    double value = v.get(i,0);
                    System.out.println("value before modulo: " + value);
                    System.out.println("Char: " +  (char)((int)(value % 26) + 65));
                    System.out.printf("vec %d shape: %dx%d%n", i, v.numRows(), v.numCols());

                cipherText.append((char)((int)(value % 26) + 65)); // apply modulo and convert to char
//                    v.set(i, j, value % 26); // apply modulo
            }
        }
        System.out.println("Cipher Text: " + cipherText);
        return cipherText.toString();
    }



}
