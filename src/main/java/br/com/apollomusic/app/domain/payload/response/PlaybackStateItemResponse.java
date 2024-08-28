package br.com.apollomusic.app.domain.payload.response;

import br.com.apollomusic.app.domain.payload.Image;

import java.util.List;

public record PlaybackStateItemResponse(String id, String name, PlaybackStateAlbumResponse album, List<PlaybackStateItemArtistResponse> artists, int duration_ms) {
}
