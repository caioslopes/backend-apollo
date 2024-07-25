package br.com.apollomusic.app.domain.payload.response;

import java.util.Set;

public record UserReponse(Long id, String username, Set<String> genres, Long establishmentId) {
}
