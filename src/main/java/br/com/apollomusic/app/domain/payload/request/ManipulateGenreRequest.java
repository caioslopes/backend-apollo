package br.com.apollomusic.app.domain.payload.request;

import java.util.Set;

public record ManipulateGenreRequest(Set<String> genres) {
}
