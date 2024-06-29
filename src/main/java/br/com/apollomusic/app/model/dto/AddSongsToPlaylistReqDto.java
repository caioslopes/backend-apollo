package br.com.apollomusic.app.model.dto;

import br.com.apollomusic.app.model.entities.Song;

import java.util.Set;

public record AddSongsToPlaylistReqDto(String spotifyAccessToken, Set<Song> songs) {
}
