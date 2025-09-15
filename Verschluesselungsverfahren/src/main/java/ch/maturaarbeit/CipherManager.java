package ch.maturaarbeit;

import ch.maturaarbeit.ciphers.Cipher;
import ch.maturaarbeit.ciphers.CipherRegistry;
import ch.maturaarbeit.ciphers.caesar.Caesar;
import ch.maturaarbeit.ciphers.hill.Hill;
import ch.maturaarbeit.ciphers.rsa.TestKeys;
import ch.maturaarbeit.util.FileImporter;
import ch.maturaarbeit.util.Measure;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;


public class CipherManager {
    private ArrayList<Cipher> ciphers = new ArrayList<>();


    public CipherManager() {
        ciphers.add(new Caesar());

    }

    public void manager() {
        Hill hill = new Hill();
        FileImporter fileImporter = new FileImporter();
        Measure measure = new Measure();

        KeyPair kp = TestKeys.quickRsa();
        PublicKey pub = kp.getPublic();
        PrivateKey prv = kp.getPrivate();

        CipherRegistry reg = new CipherRegistry();
        reg.register(new Caesar(5));
        reg.register(new Hill("HILL", 2));
//        reg.register(new Playfair());
//        reg.register(new RSA(pub));

        for (int i = 0; i < reg.getRegisteredCiphers().size(); i++) {
            String s = reg.getRegisteredCiphers().keySet().toArray()[i].toString();
            measure.measureCipher(reg.get(s));
        }

    }
}
