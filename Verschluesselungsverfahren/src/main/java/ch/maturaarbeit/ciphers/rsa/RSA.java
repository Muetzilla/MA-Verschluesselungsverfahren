package ch.maturaarbeit.ciphers.rsa;

import ch.maturaarbeit.ciphers.Cipher;
import ch.maturaarbeit.ciphers.DecryptParams;
import ch.maturaarbeit.ciphers.EncryptParams;
import ch.maturaarbeit.ciphers.hill.HillParams;

public class RSA implements Cipher<RSAParams, RSAParams>{
    private final java.security.PublicKey publicKey;

    public RSA(java.security.PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String name() {
        return "RSA";
    }

    @Override
    public String encrypt(String plaintext) {
        try {
            var cipher = javax.crypto.Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, publicKey);
            byte[] ct = cipher.doFinal(plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            return java.util.Base64.getEncoder().encodeToString(ct);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String decrypt(String ciphertext, RSAParams params) {
        return "";
    }
}
