package br.com.apollomusic.app.domain.payload.response;

import br.com.apollomusic.app.domain.payload.Image;

import java.util.List;

public record PlaybackStateAlbumResponse(List<Image> images) {
}
