package br.com.apollomusic.app.domain.payload.response;

import br.com.apollomusic.app.domain.Establishment.Role;
import java.util.Set;

public record OwnerResponse(String name, String email, String refreshToken) {
}
