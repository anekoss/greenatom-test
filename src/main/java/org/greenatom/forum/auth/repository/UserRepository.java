package org.greenatom.forum.auth.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.greenatom.forum.auth.model.User;
import org.greenatom.forum.auth.table.UserTable;
import org.greenatom.forum.controller.exception.ConflictException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserRepository {
    private final UserTable userTable;

    public User findByUsername(@NotBlank String username) throws ConflictException {
        return userTable.findByUsername(username);
    }

    public User add(@NotNull User user) throws ConflictException {
        return userTable.insert(user);
    }

}
