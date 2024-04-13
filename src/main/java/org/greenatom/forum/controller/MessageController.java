package org.greenatom.forum.controller;

import api.MessageApi;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.greenatom.forum.controller.exception.NotFoundException;
import org.greenatom.forum.controller.exception.ValidationException;
import org.greenatom.forum.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import static org.greenatom.forum.auth.service.UserDetailsServiceImpl.getUsernameCurrentUser;
import static org.greenatom.forum.auth.service.UserDetailsServiceImpl.isAdmin;

@RestController
@AllArgsConstructor
public class MessageController implements MessageApi {
    private final MessageService messageService;

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN')")
    public ResponseEntity<Void> deleteMessage(UUID messageId) throws ValidationException, NotFoundException {
        if (isAdmin()) {
            messageService.deleteMessage(messageId);
        } else {
            messageService.deleteUserMessage(messageId, getUsernameCurrentUser());
        }
        return ResponseEntity.ok().build();
    }
}
