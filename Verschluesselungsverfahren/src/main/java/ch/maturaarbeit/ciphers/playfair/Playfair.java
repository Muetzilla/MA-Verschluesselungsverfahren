package ch.maturaarbeit.ciphers.playfair;

import ch.maturaarbeit.ciphers.Cipher;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @Author
 * Implementiert die Playfair Chiffre
 */
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

    /**
     * Name der Chiffre
     * @return Name der Chiffre
     */
    @Override
    public String name() {
        return "Playfair";
    }

    /**
     * Playfair Chiffre Verschlüsselung
     * @param plaintext der Klartext
     * @return der verschlüsselte Text
     */
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

    /**
     * Erstellt das 5x5 Quadrat basierend auf dem Schlüsselwort
     * @param key das Schlüsselwort
     * @param sq das 5x5 Quadrat
     * @param row die Zeilenpositionen der Buchstaben
     * @param col die Spaltenpositionen der Buchstaben
     */
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
            }
        }
    }

    /**
     * Bereitet den Text auf die Verschlüsselung vor und entfernt unerwünschte Zeichen
     * @param s der Eingabetext
     * @return der vorbereitete Text
     */
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

    /**
     * Teilt den Text in Paare auf und fügt bei Bedarf 'X' ein
     * @param t der vorbereitete Text
     * @return die Liste der Zeichenpaare
     */
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

    /**
     * Verschlüsselt ein Zeichenpaar basierend auf den Playfair Regeln
     * @param a das erste Zeichen
     * @param b das zweite Zeichen
     * @param sq das 5x5 Quadrat
     * @param row die Zeilenpositionen der Buchstaben
     * @param col die Spaltenpositionen der Buchstaben
     * @return das verschlüsselte Zeichenpaar
     */
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


