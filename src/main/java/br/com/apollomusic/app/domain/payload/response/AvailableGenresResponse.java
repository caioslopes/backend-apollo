package br.com.apollomusic.app.domain.payload.response;

import java.util.Set;

public record AvailableGenresResponse(Set<String> genres) {
}
