package ch.maturaarbeit.ciphers.caesar;

import ch.maturaarbeit.ciphers.EncryptParams;

/**
 * Cäsar Verschlüsselungsparameter
 * @param key Der Schlüssel für die Cäsar Chiffre
 */
public record CaesarParams(int key) implements EncryptParams {}
