package me.redtea.nodropx.libs.message.verifier;

import java.util.Optional;

public interface MessageVerifier {
    Optional<Object> fromDefault(String key);
}
