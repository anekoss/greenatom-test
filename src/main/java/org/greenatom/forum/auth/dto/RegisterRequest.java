package org.greenatom.forum.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import org.greenatom.forum.auth.model.Role;

public record RegisterRequest(@NotBlank @Size(min = 5, max = 20) String username,
                              @NotBlank @Size(min = 5, max = 20) String password,
                              @NotNull Set<Role> roles) {
}
