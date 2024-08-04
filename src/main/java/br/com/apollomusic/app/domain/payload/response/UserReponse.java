package br.com.apollomusic.app.domain.payload.response;

import java.util.Collection;

public record UserReponse(Long id, String username, Collection<String> genres, Long establishmentId) {
}
