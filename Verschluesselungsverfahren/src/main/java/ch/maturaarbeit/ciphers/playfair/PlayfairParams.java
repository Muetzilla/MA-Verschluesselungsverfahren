package ch.maturaarbeit.ciphers.playfair;

import ch.maturaarbeit.ciphers.DecryptParams;
import ch.maturaarbeit.ciphers.EncryptParams;

public record PlayfairParams(String keyword) implements EncryptParams, DecryptParams {}
