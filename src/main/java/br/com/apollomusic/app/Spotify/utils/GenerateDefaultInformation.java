package br.com.apollomusic.app.Spotify.utils;

import br.com.apollomusic.app.Spotify.dto.Playlist.NewPlaylistSpotifyDto;

public class GenerateDefaultInformation {
    public GenerateDefaultInformation() {}

    public static NewPlaylistSpotifyDto generateDefaultPlaylist(String name){
        String playlistName = "Playlist do " + name;
        String playlistDescription = "Playlist configurada para tocar em " + name;
        return new NewPlaylistSpotifyDto(playlistName, playlistDescription);
    }

}
