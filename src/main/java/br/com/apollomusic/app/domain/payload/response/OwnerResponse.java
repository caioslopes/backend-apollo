package br.com.apollomusic.app.domain.payload.response;

import br.com.apollomusic.app.domain.entities.Role;
import java.util.Set;

public record OwnerResponse(String name, String email, Set<Role> roles, String refreshToken) {
}
