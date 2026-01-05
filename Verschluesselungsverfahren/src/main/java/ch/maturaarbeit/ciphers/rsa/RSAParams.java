package ch.maturaarbeit.ciphers.rsa;

import ch.maturaarbeit.ciphers.EncryptParams;

/**
 * RSA Verschlüsselungsparameter
 * @param publicKey Das öffentliche Schlüsselpaar für die Verschlüsselung der RSA Chiffre
 */
public record RSAParams(int[] publicKey) implements EncryptParams {}
