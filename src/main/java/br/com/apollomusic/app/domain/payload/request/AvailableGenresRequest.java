package br.com.apollomusic.app.domain.payload.request;

import java.util.Set;

public record AvailableGenresRequest(Set<String> genres) {
}
