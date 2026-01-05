package ch.maturaarbeit.ciphers;

import java.util.HashMap;
import java.util.Map;

public class CipherRegistry {
    private final Map<String, Cipher<?>> registeredCiphers = new HashMap<>();

    public <E extends EncryptParams>
    void register(Cipher<E> cipher) { registeredCiphers.put(cipher.name(), cipher); }

    public <E extends EncryptParams>
    Cipher<E> get(String name) { return (Cipher<E>) registeredCiphers.get(name); }

    public Map<String, Cipher<?>> getRegisteredCiphers() { return registeredCiphers; }
}
