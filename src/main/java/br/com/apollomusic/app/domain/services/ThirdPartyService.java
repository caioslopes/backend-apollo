package br.com.apollomusic.app.domain.services;

import br.com.apollomusic.app.domain.Establishment.Song;
import br.com.apollomusic.app.domain.payload.ObjectUri;
import br.com.apollomusic.app.domain.payload.request.AddSongsToPlaylistRequest;
import br.com.apollomusic.app.domain.payload.request.CreatePlaylistRequest;
import br.com.apollomusic.app.domain.payload.request.RemoveSongsFromPlaylistRequest;
import br.com.apollomusic.app.domain.payload.response.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ThirdPartyService {

    private final ApiService apiService;
    private final Gson gson;

    @Autowired
    public ThirdPartyService(ApiService apiService, Gson gson) {
        this.apiService = apiService;
        this.gson = gson;
    }

    public ThirdPartyUserResponse getUser(String spotifyAccessToken){
        String me = apiService.get("me", new HashMap<>(), spotifyAccessToken);
        return gson.fromJson(me, ThirdPartyUserResponse.class);
    }

    public Map<String, Collection<DeviceResponse>> getDevices(String spotifyAccessToken){
        String endpoint = "/me/player/devices";
        String response = apiService.get(endpoint, null, spotifyAccessToken);
        return new HashMap<>(gson.fromJson(response, new TypeToken<Map<String, Collection<DeviceResponse>>>() {
        }.getType()));
    }

    public AvailableGenresResponse getAvailableGenres(String spotifyAccessToken){
        String response = apiService.get("/recommendations/available-genre-seeds", new HashMap<>(), spotifyAccessToken);
        return gson.fromJson(response, AvailableGenresResponse.class);
    }

    public CreatePlaylistResponse createPlaylist(String name, String description, String spotifyAccessToken){
        ThirdPartyUserResponse user = getUser(spotifyAccessToken);
        String endpoint = "/users/" + user.id() + "/playlists";
        CreatePlaylistRequest request = new CreatePlaylistRequest(name, description);
        String response = apiService.post(endpoint, request, spotifyAccessToken);
        return gson.fromJson(response, CreatePlaylistResponse.class);
    }

    public ChangePlaylistResponse addSongsToPlaylist(String playlistId, Set<Song> songs, String spotifyAccessToken){
        String endpoint = "/playlists/" + playlistId + "/tracks";
        Set<String> uris = new HashSet<>();
        for(Song s : songs){
            uris.add(s.getUri());
        }
        AddSongsToPlaylistRequest addSongsToPlaylistRequest = new AddSongsToPlaylistRequest(uris, 0);
        String response = apiService.post(endpoint, addSongsToPlaylistRequest, spotifyAccessToken);
        return gson.fromJson(response, ChangePlaylistResponse.class);
    }

    public ChangePlaylistResponse removeSongsFromPlaylist(String playlistId, String snapshot_id, Set<Song> songs, String spotifyAccessToken){
        String endpoint = "/playlists/" + playlistId + "/tracks";
        Set<ObjectUri> tracks = new HashSet<>();
        for (Song song : songs) {
            tracks.add(new ObjectUri(song.getUri()));
        }
        RemoveSongsFromPlaylistRequest removeSongsFromPlaylistRequest = new RemoveSongsFromPlaylistRequest(tracks, snapshot_id);
        String response = apiService.delete(endpoint, removeSongsFromPlaylistRequest, spotifyAccessToken);
        return gson.fromJson(response, ChangePlaylistResponse.class);
    }

}
