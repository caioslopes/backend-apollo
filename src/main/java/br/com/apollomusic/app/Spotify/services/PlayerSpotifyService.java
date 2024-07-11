package br.com.apollomusic.app.Spotify.services;

import br.com.apollomusic.app.Spotify.dto.Player.DeviceSpotifyDto;
import br.com.apollomusic.app.model.services.ApiService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlayerSpotifyService {

    private final ApiService apiService;
    private final Gson gson;

    public PlayerSpotifyService(ApiService apiService, Gson gson) {
        this.apiService = apiService;
        this.gson = gson;
    }

    public Map<String, Collection<DeviceSpotifyDto>> getAvailableDevices(String spotifyAccessToken){
        String endpoint = "/me/player/devices";
        String response = apiService.get(endpoint, null, spotifyAccessToken);
        return new HashMap<>(gson.fromJson(response, new TypeToken<Map<String, Collection<DeviceSpotifyDto>>>() {
        }.getType()));
    }

}
