package ch.maturaarbeit.ciphers.hill;

import ch.maturaarbeit.ciphers.EncryptParams;

/**
 * Hill Verschlüsselungsparameter
 * @param key Der Schlüssel für die Hill Chiffre
 * @param blockSize Die Blockgrösse für die Hill Chiffre
 */
public record HillParams(String key, int blockSize)  implements EncryptParams{}