package ch.maturaarbeit.ciphers.playfair;

import ch.maturaarbeit.ciphers.Cipher;
import ch.maturaarbeit.ciphers.DecryptParams;
import ch.maturaarbeit.ciphers.EncryptParams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Playfair implements Cipher {
    private String key;
    private long operationsCount = 0;


    public Playfair() {
    }
    public Playfair(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
        return "Playfair";
    }

    @Override
    public String decrypt(String ciphertext, DecryptParams params) {
        return "";
    }

    @Override
    public String encrypt(String plaintext) {
        char[][] sq = new char[5][5];
        int[] row = new int[26];
        int[] col = new int[26];

        buildSquare(key, sq, row, col);

        String prep = normalize(plaintext);

        List<char[]> pairs = toPairs(prep);

        StringBuilder ciphertext = new StringBuilder();
        for (char[] p : pairs) {
            ciphertext.append(encryptPair(p[0], p[1], sq, row, col));
            // Anzahl Operationen wird erhöht, da jedes Paar verschlüsselt und der Cipher angehängt wird.
            operationsCount++;
        }

        return ciphertext.toString();
    }

    private void buildSquare(String key, char[][] sq, int[] row, int[] col) {
        boolean[] seen = new boolean[26];
        int pos = 0;

        for (char ch : normalize(key).toCharArray()) {
            int idx = ch - 'A';

            if (!seen[idx] && ch != 'J') {
                seen[idx] = true;
                sq[pos / 5][pos % 5] = ch;
                row[idx] = pos / 5;
                col[idx] = pos % 5;
                pos++;
                // Anzahl der Operationen wird erhöht, da ein neues Zeichen ins Quadrat eingefügt wird.
                operationsCount++;
            }
        }

        for (char c = 'A'; c <= 'Z'; c++) {
            if (c == 'J') continue;
            int idx = c - 'A';
            if (!seen[idx]) {
                seen[idx] = true;
                sq[pos / 5][pos % 5] = c;
                row[idx] = pos / 5;
                col[idx] = pos % 5;
                pos++;
                // Anzahl der Operationen wird erhöht, da ein neues Zeichen ins Quadrat eingefügt wird.
                operationsCount++;
            }
        }
    }

    private String normalize(String s) {
        StringBuilder b = new StringBuilder();
        for (char ch : s.toUpperCase(Locale.ROOT).toCharArray()) {
            if (ch < 'A' || ch > 'Z') continue;
            if (ch == 'J'){
                ch = 'I';
                b.append(ch);
            }else{
                b.append(ch);
            }
            // Die Anzahl der Operationen wird für jedes verarbeitete Zeichen erhöht.
            operationsCount++;
        }
        return b.toString();
    }

    private List<char[]> toPairs(String t) {
        StringBuilder buf = new StringBuilder(t);
        List<char[]> pairs = new ArrayList<>();

        for (int i = 0; i < buf.length(); i += 2) {
            char a = buf.charAt(i);
            char b;

            if (i + 1 >= buf.length()) {
                buf.append('X');
                b = 'X';
            } else {
                b = buf.charAt(i + 1);
            }

            if (a == b) {
                buf.insert(i + 1, 'X');
                b = 'X';
            }

            pairs.add(new char[]{a, b});
        }
        return pairs;
    }

    private String encryptPair(char a, char b, char[][] sq, int[] row, int[] col) {
        int ra = row[a - 'A'];
        int ca = col[a - 'A'];
        int rb = row[b - 'A'];
        int cb = col[b - 'A'];

        char ea, eb;
        if (ra == rb) {                 // REGEL 1 – gleiche Zeile
            ea = sq[ra][(ca + 1) % 5];
            eb = sq[rb][(cb + 1) % 5];
            // Die Anzahl der Operationen wird erhöht, da eine Verschlüsselung stattfindet
            operationsCount++;
        } else if (ca == cb) {          // REGEL 2 – gleiche Spalte
            ea = sq[(ra + 1) % 5][ca];
            eb = sq[(rb + 1) % 5][cb];
            // Die Anzahl der Operationen wird erhöht, da eine Verschlüsselung stattfindet
            operationsCount++;
        } else {                        // REGEL 3 – Rechteck
            ea = sq[ra][cb];
            eb = sq[rb][ca];
            // Die Anzahl der Operationen wird erhöht, da eine Verschlüsselung stattfindet
            operationsCount++;
        }

        // Die Anzahl der Operationen wird erhöht, da die Ergebniszeichen zusammengefügt werden.
        operationsCount++;
        return "" + ea + eb;
    }
}


