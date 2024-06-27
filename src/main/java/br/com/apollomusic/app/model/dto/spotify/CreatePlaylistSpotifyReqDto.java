package br.com.apollomusic.app.model.dto.spotify;

public record CreatePlaylistSpotifyReqDto(String name, String description, boolean isPublic) {
}
