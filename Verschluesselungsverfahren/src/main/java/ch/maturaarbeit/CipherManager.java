package ch.maturaarbeit;

import ch.maturaarbeit.ciphers.Cipher;
import ch.maturaarbeit.ciphers.CipherRegistry;
import ch.maturaarbeit.ciphers.caesar.Caesar;
import ch.maturaarbeit.ciphers.hill.Hill;
import ch.maturaarbeit.ciphers.playfair.Playfair;
import ch.maturaarbeit.ciphers.rsa.RSA;
import ch.maturaarbeit.util.FileImporter;
import ch.maturaarbeit.util.Measure;

import java.util.ArrayList;


public class CipherManager {
    private ArrayList<Cipher> ciphers = new ArrayList<>();
    private long[] RSA_TWO_DIGIT_PRIMES = {11, 17};
    private long[] RSA_THREE_DIGIT_PRIMES = {571, 883};
    private long[] RSA_FOUR_DIGIT_PRIMES = {3613, 4987};
    private long[] RSA_FIVE_DIGIT_PRIMES = {10007, 10009};
    private long[] RSA_SIX_DIGIT_PRIMES = {100003, 100019};
    private long[] RSA_EIGHT_DIGIT_PRIMES = {50000741, 99999989};


    public CipherManager() {
        ciphers.add(new Caesar());

    }

    public void manager() {
        Hill hill = new Hill();
        FileImporter fileImporter = new FileImporter();
        Measure measure = new Measure();

        CipherRegistry reg = new CipherRegistry();
        reg.register(new Caesar(5));
        reg.register(new Hill("HILL", 2));
        reg.register(new Playfair("PLAYFAIR"));
        reg.register(new RSA(RSA_SIX_DIGIT_PRIMES));

        for (int i = 0; i < reg.getRegisteredCiphers().size(); i++) {
            String s = reg.getRegisteredCiphers().keySet().toArray()[i].toString();
            measure.measureCipher(reg.get(s));
        }

    }
}
