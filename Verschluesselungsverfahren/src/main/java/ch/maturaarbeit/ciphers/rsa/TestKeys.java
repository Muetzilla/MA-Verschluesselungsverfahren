package ch.maturaarbeit.ciphers.rsa;

import java.security.*;

public final class TestKeys {
    private TestKeys() {}

    public static KeyPair quickRsa() {
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024); // nur für schnellen Test! In echt: 2048/3072
            return kpg.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }
}