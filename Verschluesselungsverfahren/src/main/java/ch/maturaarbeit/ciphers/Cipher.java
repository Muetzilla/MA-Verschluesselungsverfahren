package ch.maturaarbeit.ciphers;

public interface Cipher<E extends EncryptParams, D extends DecryptParams> {
    String name();
    String encrypt(String plaintext);
    String decrypt(String ciphertext, D params);
}