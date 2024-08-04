package br.com.apollomusic.app.domain.payload.request;

import java.util.Set;

public record AddSongsToPlaylistRequest(Set<String> uris, int position) {
}
