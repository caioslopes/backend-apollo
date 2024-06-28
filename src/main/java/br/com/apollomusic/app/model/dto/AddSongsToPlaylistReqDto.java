package br.com.apollomusic.app.model.dto;

import java.util.Set;

public record AddSongsToPlaylistReqDto(String spotifyAccessToken, Set<String> uris, int position) {
}
