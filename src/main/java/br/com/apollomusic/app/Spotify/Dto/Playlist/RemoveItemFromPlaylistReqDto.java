package br.com.apollomusic.app.Spotify.Dto.Playlist;

import java.util.Set;

public record RemoveItemFromPlaylistReqDto(Set<ObjectUri> tracks, String snapshot_id) {
}
