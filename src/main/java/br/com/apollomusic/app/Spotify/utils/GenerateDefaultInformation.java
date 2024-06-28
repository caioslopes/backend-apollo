package br.com.apollomusic.app.Spotify.utils;

import br.com.apollomusic.app.Spotify.Dto.Playlist.NewPlaylistSpotifyDto;
import org.springframework.stereotype.Service;

public class GenerateDefaultInformation {
    public GenerateDefaultInformation() {}

    public static NewPlaylistSpotifyDto generateDefaultPlaylist(String name){
        String playlistName = "Playlist do " + name;
        String playlistDescription = "Playlist configurada para tocar em " + name;
        return new NewPlaylistSpotifyDto(playlistName, playlistDescription);
    }

}
