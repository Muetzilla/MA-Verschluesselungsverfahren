package ch.maturaarbeit.ciphers.rsa;

import ch.maturaarbeit.ciphers.Cipher;


public class RSA implements Cipher<RSAParams, RSAParams>{
//    private final java.security.PublicKey publicKey;
//
//    public RSA(java.security.PublicKey publicKey) {
//        this.publicKey = publicKey;
//    }
    private long[] publicKey ; // e, npublic RSA(){
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
            long c = 1;
            for (int i = 0; i < e; i++) {
                c = (c * m) % n;
                operationsCount++;
            }
            ciphertext.append(c).append(" ");
            operationsCount++;
        }
        return "";
//        try {
//            var cipher = javax.crypto.Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
//            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, publicKey);
//            byte[] ct = cipher.doFinal(plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8));
//            return java.util.Base64.getEncoder().encodeToString(ct);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public String decrypt(String ciphertext, RSAParams params) {
        return "";
    }
}
