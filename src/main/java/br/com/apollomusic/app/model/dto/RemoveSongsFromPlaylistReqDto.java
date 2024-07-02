package br.com.apollomusic.app.model.dto;

import br.com.apollomusic.app.model.entities.Song;

import java.util.Set;

public record RemoveSongsFromPlaylistReqDto(String spotifyAccessToken, Set<Song> songs, String snapshot_id) {
}
