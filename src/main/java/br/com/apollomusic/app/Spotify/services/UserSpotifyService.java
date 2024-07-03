package br.com.apollomusic.app.Spotify.services;

import br.com.apollomusic.app.Spotify.dto.Me.UserSpotifyDto;
import br.com.apollomusic.app.model.services.ApiService;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserSpotifyService {

    private final ApiService apiService;
    private final Gson gson;

    public UserSpotifyService(ApiService apiService, Gson gson) {
        this.apiService = apiService;
        this.gson = gson;
    }

    public UserSpotifyDto getUserOnSpotify(String spotifyAccessToken){
        String me = apiService.get("me", new HashMap<>(), spotifyAccessToken);
        return gson.fromJson(me, UserSpotifyDto.class);
    }
}
