package br.com.apollomusic.app.Spotify.Services;

import br.com.apollomusic.app.Spotify.Dto.Playlist.AddItemToPlaylistReqDto;
import br.com.apollomusic.app.Spotify.Dto.Playlist.AddItemToPlaylistResDto;
import br.com.apollomusic.app.Spotify.Dto.Playlist.NewPlaylistSpotifyDto;
import br.com.apollomusic.app.Spotify.Dto.Playlist.NewPlaylistSpotifyResDto;
import br.com.apollomusic.app.model.services.ApiService;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

@Service
public class PlaylistSpotifyService {

    private final ApiService apiService;
    private final Gson gson;

    public PlaylistSpotifyService(ApiService apiService, Gson gson) {
        this.apiService = apiService;
        this.gson = gson;
    }

    public NewPlaylistSpotifyResDto createPlaylist(String userid, NewPlaylistSpotifyDto newPlaylistSpotifyDto, String spotifyAccessToken){
        String endpoint = "/users/" + userid + "/playlists";
        String response = apiService.postWithResponse(endpoint, newPlaylistSpotifyDto, spotifyAccessToken);
        return gson.fromJson(response, NewPlaylistSpotifyResDto.class);
    }

    public void addItemToPlaylist(String playlistId, AddItemToPlaylistReqDto addItemToPlaylistReqDto, String spotifyAccessToken){
        String endpoint = "/playlists/" + playlistId + "/tracks";
        apiService.post(endpoint, addItemToPlaylistReqDto, spotifyAccessToken);
    }

//    public AddItemToPlaylistResDto addItemToPlaylist(String playlistId, AddItemToPlaylistReqDto addItemToPlaylistReqDto, String spotifyAccessToken){
//        String endpoint = "/playlists/" + playlistId + "/tracks";
//        String response = apiService.postWithResponse(endpoint, addItemToPlaylistReqDto, spotifyAccessToken);
//        return gson.fromJson(response, AddItemToPlaylistResDto.class);
//    }


}
