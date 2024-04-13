package org.greenatom.forum.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(@NotBlank @Size(min = 5, max = 20) String username,
                          @NotBlank @Size(min = 5, max = 20) String password) {
}
