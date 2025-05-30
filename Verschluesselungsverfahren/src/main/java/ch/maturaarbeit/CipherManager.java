package ch.maturaarbeit;

import ch.maturaarbeit.ciphers.Cipher;
import ch.maturaarbeit.ciphers.caesar.Caesar;
import ch.maturaarbeit.util.Measure;

import java.util.ArrayList;

public class CipherManager {
    private ArrayList<Cipher> ciphers = new ArrayList<>();
    private ArrayList<Measure> cipherMesaures = new ArrayList<>();
    public CipherManager(){
        ciphers.add(new Caesar());

    }
    public void manager(){
        for (Cipher c : ciphers) {
          cipherMesaures.add(new Measure(c));
        }

        for(Measure cm : cipherMesaures){
            String cipherText = cm.measureCipherRuntime("hackme", 5);
            long duration = cm.getCipherDuration();
            System.out.println("The encode text is: \"" + cipherText + "\" and the duration is: " + duration +  "ms.");
        }
    }
}
