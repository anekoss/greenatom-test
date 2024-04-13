package org.greenatom.forum.auth.table;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.NoArgsConstructor;
import org.greenatom.forum.auth.model.User;
import org.greenatom.forum.controller.exception.ConflictException;
import org.springframework.stereotype.Component;
import static org.greenatom.forum.controller.exception.ExceptionMessage.USER_ALREADY_REGISTER;
import static org.greenatom.forum.controller.exception.ExceptionMessage.USER_NOT_FOUND_MESSAGE;

@Component
@NoArgsConstructor
public class UserTable {

    private final Map<String, User> userMap = new HashMap<>();

    public User insert(@NotNull User user) throws ConflictException {
        if (existByUsername(user.getUsername())) {
            throw new ConflictException(USER_ALREADY_REGISTER);
        }
        user.setId(UUID.randomUUID());
        userMap.put(user.getUsername(), user);
        return user;
    }

    public User delete(@NotNull User user) throws ConflictException {
        if (!existByUsername(user.getUsername())) {
            throw new ConflictException(USER_NOT_FOUND_MESSAGE);
        }
        return userMap.remove(user.getUsername());
    }

    public User findByUsername(@NotBlank String username) throws ConflictException {
        if (!existByUsername(username)) {
            throw new ConflictException(USER_NOT_FOUND_MESSAGE);
        }
        return userMap.get(username);
    }

    public boolean existByUsername(@NotBlank String username) {
        return userMap.get(username) != null;
    }
}
