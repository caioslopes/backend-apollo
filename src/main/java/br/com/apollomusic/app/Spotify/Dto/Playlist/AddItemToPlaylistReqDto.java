package br.com.apollomusic.app.Spotify.Dto.Playlist;

import java.util.Set;

public record AddItemToPlaylistReqDto(Set<String> uris, int position) {
}
