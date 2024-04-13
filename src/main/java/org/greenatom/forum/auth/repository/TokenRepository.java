package org.greenatom.forum.auth.repository;

import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.greenatom.forum.auth.model.Token;
import org.greenatom.forum.auth.table.TokenTable;
import org.greenatom.forum.auth.table.UserTokenTable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenRepository {
    private final TokenTable tokenTable;
    private final UserTokenTable userTokenTable;

    public Token add(String username, String token) {
        Token accessToken = new Token().setToken(token).setRevoked(false).setExpired(false);
        accessToken = tokenTable.insert(accessToken);
        userTokenTable.insert(username, accessToken);
        return accessToken;
    }

    public Set<Token> findByUsername(String username) {
        return userTokenTable.findByUsername(username);
    }

    public void saveAll(String username, Set<Token> tokens) {
        tokens.stream().map(tokenTable::insert).forEach(token -> userTokenTable.insert(username, token));
    }

    public Optional<Token> findByToken(String token) {
        return tokenTable.findByToken(token);
    }
}
