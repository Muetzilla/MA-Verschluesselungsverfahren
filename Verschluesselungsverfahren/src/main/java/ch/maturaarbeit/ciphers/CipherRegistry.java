package ch.maturaarbeit.ciphers;

import java.util.HashMap;
import java.util.Map;

public class CipherRegistry {
    private final Map<String, Cipher<?,?>> registeredCiphers = new HashMap<>();

    public <E extends EncryptParams, D extends DecryptParams>
    void register(Cipher<E,D> cipher) { registeredCiphers.put(cipher.name(), cipher); }

    @SuppressWarnings("unchecked")
    public <E extends EncryptParams, D extends DecryptParams>
    Cipher<E,D> get(String name) { return (Cipher<E,D>) registeredCiphers.get(name); }

    public Map<String, Cipher<?,?>> getRegisteredCiphers() { return registeredCiphers; }
}
