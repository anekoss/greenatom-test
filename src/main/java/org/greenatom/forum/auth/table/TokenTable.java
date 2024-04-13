package org.greenatom.forum.auth.table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.greenatom.forum.auth.model.Token;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TokenTable {
    private final Map<String, Token> tokens = new HashMap<>();

    public Token insert(@NotNull Token token) {
        token.setId(UUID.randomUUID());
        tokens.put(token.getToken(), token);
        return token;
    }

    public Optional<Token> findByToken(@NotBlank String token) {
        return Optional.ofNullable(tokens.get(token));
    }
}
