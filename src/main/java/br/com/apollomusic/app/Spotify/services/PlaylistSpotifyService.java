package br.com.apollomusic.app.Spotify.services;

import br.com.apollomusic.app.Spotify.dto.Playlist.*;
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

    public AddItemToPlaylistResDto addItemsToPlaylist(String playlistId, AddItemToPlaylistReqDto addItemToPlaylistReqDto, String spotifyAccessToken){
        String endpoint = "/playlists/" + playlistId + "/tracks";
        String response = apiService.postWithResponse(endpoint, addItemToPlaylistReqDto, spotifyAccessToken);
        return gson.fromJson(response, AddItemToPlaylistResDto.class);
    }

    public RemoveItemFromPlaylistResDto removeItemsFromPlaylist(String playlistId, RemoveItemFromPlaylistReqDto removeItemFromPlaylistReqDto ,String spotifyAccessToken){
        String endpoint = "/playlists/" + playlistId + "/tracks";
        String response = apiService.delete(endpoint, removeItemFromPlaylistReqDto, spotifyAccessToken);
        return gson.fromJson(response, RemoveItemFromPlaylistResDto.class);
    }

}
