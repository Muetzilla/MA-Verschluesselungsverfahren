package ch.maturaarbeit.ciphers.rsa;

import ch.maturaarbeit.ciphers.Cipher;


public class RSA implements Cipher<RSAParams, RSAParams>{

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

    @Override
    public String encrypt(String plaintext) {
        long e = publicKey[0];
        long n = publicKey[1];
        StringBuilder ciphertext = new StringBuilder();
        for (char character : plaintext.toCharArray()) {
            long m = (long) character;
            // Anzahl Operationen wird erhöht, da das Zeichen in eine Zahl umgewandelt werden muss.
            operationsCount++;
            long c = 1;
            for (int i = 0; i < e; i++) {
                c = (c * m) % n;
                //Anzahl Operationen muss erhöht werden, da hier die Verschlüsselung mittels Multiklikation und Modulo durchgeführt wird.
                operationsCount++;
            }
            char cipherChar = (char) c;
            // Anzahl Operationen wird erhöht, da die verschlüsselte Zahl wieder in ein Zeichen umgewandelt wird.
            operationsCount++;

            ciphertext.append(cipherChar).append(" ");
            // Anzahl Operationen wird erhöht, da die verschlüsselte Zahl zum Ciphertext hinzugefügt wird.
            operationsCount++;
        }
        System.out.println("Ciphertext: " + ciphertext);
        return ciphertext.toString();

    }

    @Override
    public String decrypt(String ciphertext, RSAParams params) {
        return "";
    }
}
