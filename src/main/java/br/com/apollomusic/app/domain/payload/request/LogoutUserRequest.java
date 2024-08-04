package br.com.apollomusic.app.domain.payload.request;

import java.util.Set;

public record LogoutUserRequest(String username, Set<String> genres, Long establishmentId) {
}
