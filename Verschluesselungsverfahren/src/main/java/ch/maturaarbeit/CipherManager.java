package ch.maturaarbeit;

import ch.maturaarbeit.ciphers.Cipher;
import ch.maturaarbeit.ciphers.caesar.Caesar;

import java.util.ArrayList;

public class CipherManager {
    private ArrayList<Cipher> ciphers = new ArrayList<>();
    public CipherManager(){
        ciphers.add(new Caesar());

    }
    public void manager(){
        for (Cipher c : ciphers) {
            String encryption = c.encrypt("hackme", 5);
            System.out.println(encryption);
        }

    }
}
