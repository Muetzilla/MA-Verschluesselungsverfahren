package ch.maturaarbeit.ciphers.rsa;

import ch.maturaarbeit.ciphers.DecryptParams;
import ch.maturaarbeit.ciphers.EncryptParams;

public record RSAParams(java.security.PublicKey publicKey) implements EncryptParams, DecryptParams {}
