package org.greenatom.forum.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.greenatom.forum.auth.model.Role;
import org.greenatom.forum.auth.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public static boolean isAdmin() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(Role.ROLE_ADMIN);
    }

    public static String getUsernameCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @SneakyThrows @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
