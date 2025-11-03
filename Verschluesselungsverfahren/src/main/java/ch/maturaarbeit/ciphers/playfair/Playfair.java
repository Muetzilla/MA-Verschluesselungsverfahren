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
    public String encrypt(String plaintext) {
        char[][] sq = new char[5][5];
        int[] row = new int[26], col = new int[26];
        buildSquare(key, sq, row, col);

        String prep = normalize(plaintext);
        List<char[]> pairs = toPairs(prep);

        StringBuilder out = new StringBuilder();
        for (char[] p : pairs) out.append(encryptPair(p[0], p[1], sq, row, col));
        return out.toString();
    }

    @Override
    public String decrypt(String ciphertext, DecryptParams params) {
        return "";
    }

    private  void buildSquare(String key, char[][] sq, int[] row, int[] col) {
        boolean[] seen = new boolean[26];
        List<Character> stream = new ArrayList<>();

        for (char ch : normalize(key).toCharArray()) {
            int idx = ch - 'A';
            if (!seen[idx] && ch != 'J') { seen[idx] = true; stream.add(ch); }
        }
        for (char c = 'A'; c <= 'Z'; c++) {
            if (c == 'J') continue;
            if (!seen[c - 'A']) { seen[c - 'A'] = true; stream.add(c); }
        }

        Iterator<Character> it = stream.iterator();
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                char ch = it.next();
                sq[r][c] = ch;
                row[ch - 'A'] = r;
                col[ch - 'A'] = c;
            }
        }
    }

    private  String normalize(String s) {
        StringBuilder b = new StringBuilder();
        for (char ch : s.toUpperCase(Locale.ROOT).toCharArray()) {
            if (ch < 'A' || ch > 'Z') continue;
            if (ch == 'J') ch = 'I';
            b.append(ch);
        }
        return b.toString();
    }

    private List<char[]> toPairs(String t) {
        StringBuilder buf = new StringBuilder(t);
        List<char[]> pairs = new ArrayList<>();
        for (int i = 0; i < buf.length(); i += 2) {
            char a = buf.charAt(i);
            char b = (i + 1 < buf.length()) ? buf.charAt(i + 1) : 0;

            if (b == 0) {
                buf.append('X');
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
    private static String encryptPair(char a, char b, char[][] sq, int[] row, int[] col) {
        int ra = row[a - 'A'], ca = col[a - 'A'];
        int rb = row[b - 'A'], cb = col[b - 'A'];

        char ea, eb;
        if (ra == rb) {         // REGEL 1 - gleiche Zeile
            ea = sq[ra][(ca + 1) % 5];
            eb = sq[rb][(cb + 1) % 5];
        } else if (ca == cb) { // REGEL 2 - gleiche Spalte
            ea = sq[(ra + 1) % 5][ca];
            eb = sq[(rb + 1) % 5][cb];
        } else {               // REGEL 3 - Rechteck
            ea = sq[ra][cb];
            eb = sq[rb][ca];
        }
        return "" + ea + eb;
    }
}


