package ch.maturaarbeit.ciphers.rsa;

import ch.maturaarbeit.ciphers.Cipher;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * @Author
 * Implementiert die RSA Chiffre
 */
public class RSA implements Cipher<RSAParams >{

    private long[] publicKey ;
    private long operationsCount = 0;


    public RSA(long[] publicKey){
        this.publicKey = publicKey;
    }
    public RSA(){

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
        return "RSA";
    }

    /**
     * RSA Chiffre Verschlüsselung
     * @param plaintext der Klartext
     * @return der verschlüsselte Text
     */
    @Override
    public String encrypt(String plaintext) {
        BigInteger e = BigInteger.valueOf(publicKey[0]);
        BigInteger n = BigInteger.valueOf(publicKey[1]);

        byte[] data = plaintext.getBytes(StandardCharsets.UTF_8);
        int blockSize = (n.bitLength() - 1) / 8;

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < data.length; i += blockSize) {
            int len = Math.min(blockSize, data.length - i);

            byte[] block = Arrays.copyOfRange(data, i, i + len);
            BigInteger m = new BigInteger(1, block);
            // Anzahl Operationen wird erhöht, da der Block in Zahlen umgewandelt werden muss.
            operationsCount++;

            BigInteger c = m.modPow(e, n);
            //Anzahl Operationen muss erhöht werden, da hier die Verschlüsselung mittels Multiplikation und Modulo durchgeführt wird.
            operationsCount++;
            String encoded = Base64.getEncoder().encodeToString(c.toByteArray());

            result.append(encoded);
            // Anzahl Operationen wird erhöht, da die verschlüsselte Zahl zum Ciphertext hinzugefügt wird.
            operationsCount++;
        }
        return result.toString();
    }
}
