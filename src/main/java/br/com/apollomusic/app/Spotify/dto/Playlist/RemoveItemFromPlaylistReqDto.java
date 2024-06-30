package br.com.apollomusic.app.Spotify.dto.Playlist;

import java.util.Set;

public record RemoveItemFromPlaylistReqDto(Set<ObjectUri> tracks, String snapshot_id) {
}
