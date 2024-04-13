package org.greenatom.forum.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.greenatom.forum.auth.dto.AuthRequest;
import org.greenatom.forum.auth.dto.AuthResponse;
import org.greenatom.forum.auth.dto.RegisterRequest;
import org.greenatom.forum.auth.model.Token;
import org.greenatom.forum.auth.model.User;
import org.greenatom.forum.auth.repository.TokenRepository;
import org.greenatom.forum.auth.repository.UserRepository;
import org.greenatom.forum.controller.exception.ConflictException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final int TOKEN_START_INDEX = 7;
    private static final String BEARER_HEADER = "Bearer ";
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) throws ConflictException {
        User user = new User().setUsername(request.username())
            .setPassword(passwordEncoder.encode(request.password()))
            .setRoles(request.roles());
        user = userRepository.add(user);
        AuthResponse authResponse = jwtService.generateTokens(user);
        tokenRepository.add(user.getUsername(), authResponse.accessToken());
        return authResponse;
    }

    public AuthResponse authenticate(AuthRequest request) throws ConflictException {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.username(),
                request.password()
            )
        );
        User user = userRepository.findByUsername(request.username());
        AuthResponse authResponse = jwtService.generateTokens(user);
        tokenRepository.add(user.getUsername(), authResponse.accessToken());
        return authResponse;
    }

    public void revokeAllUserTokens(User user) {
        Set<Token> userTokens = tokenRepository.findByUsername(user.getUsername());
        if (userTokens.isEmpty()) {
            return;
        }
        userTokens.forEach(token -> token.setRevoked(false).setExpired(false));
        tokenRepository.saveAll(user.getUsername(), userTokens);
    }

    public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException, ConflictException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        if (authHeader == null || !authHeader.startsWith(BEARER_HEADER)) {
            return;
        }
        refreshToken = authHeader.substring(TOKEN_START_INDEX);
        String username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = userRepository.findByUsername(username);
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                tokenRepository.add(user.getUsername(), accessToken);
                var authResponse = new AuthResponse(accessToken, refreshToken);
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }


}
