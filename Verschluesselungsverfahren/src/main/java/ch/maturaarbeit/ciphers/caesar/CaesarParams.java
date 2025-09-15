package ch.maturaarbeit.ciphers.caesar;

import ch.maturaarbeit.ciphers.DecryptParams;
import ch.maturaarbeit.ciphers.EncryptParams;

public record CaesarParams(int key) implements EncryptParams, DecryptParams {}
