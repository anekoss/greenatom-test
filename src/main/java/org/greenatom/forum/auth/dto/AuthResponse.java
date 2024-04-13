package org.greenatom.forum.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthResponse(@NotBlank String accessToken, @NotBlank String refreshToken) {
}
