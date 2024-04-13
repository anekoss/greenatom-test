package org.greenatom.forum.auth.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class Token {
    private UUID id;
    private String token;
    private boolean expired;
    private boolean revoked;
}
