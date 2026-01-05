package ch.maturaarbeit;

/**
 * @Author
 * Hauptklasse der Applikation
 */
public class Main {
    public Main(){
        CipherManager cp = new CipherManager();
        cp.manager();
    }
    public static void main(String[] args) {
        new Main();
    }
}