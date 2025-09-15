package ch.maturaarbeit.ciphers.hill;

import ch.maturaarbeit.ciphers.DecryptParams;
import ch.maturaarbeit.ciphers.EncryptParams;

public record HillParams(String key, int blockSize)
        implements EncryptParams, DecryptParams {}