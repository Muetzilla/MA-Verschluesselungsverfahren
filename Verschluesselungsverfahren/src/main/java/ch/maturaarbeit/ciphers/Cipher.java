package ch.maturaarbeit.ciphers;

/**
 * @Author
 * Interface für Chiffren
 */
public interface Cipher<E extends EncryptParams> {
    long getOperationCount();
    void setOperationCount(long operationCount);
    String name();
    String encrypt(String plaintext);
}