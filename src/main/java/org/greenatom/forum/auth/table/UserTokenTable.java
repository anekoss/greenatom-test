package org.greenatom.forum.auth.table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.NoArgsConstructor;
import org.greenatom.forum.auth.model.Token;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class UserTokenTable {
    private final Map<String, Set<Token>> userTokens = new HashMap<>();

    public Set<Token> insert(@NotBlank String username, @NotNull Token token) {
        Set<Token> tokens = userTokens.getOrDefault(username, new HashSet<>());
        tokens.add(token);
        userTokens.put(username, tokens);
        return tokens;
    }

    public Set<Token> findByUsername(@NotBlank String username) {
        return userTokens.getOrDefault(username, Set.of());
    }
}
