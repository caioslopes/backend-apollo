package br.com.apollomusic.app.domain.payload.request;

import br.com.apollomusic.app.domain.payload.ObjectUri;

import java.util.Set;

public record RemoveSongsFromPlaylistRequest(Set<ObjectUri> tracks, String snapshot_id) {
}
