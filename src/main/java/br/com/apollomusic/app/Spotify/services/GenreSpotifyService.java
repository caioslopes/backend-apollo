package br.com.apollomusic.app.Spotify.services;

import br.com.apollomusic.app.Spotify.dto.Genre.AvailableGenresDto;
import br.com.apollomusic.app.model.services.ApiService;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GenreSpotifyService {

    private final ApiService apiService;
    private final Gson gson;

    public GenreSpotifyService(ApiService apiService, Gson gson) {
        this.apiService = apiService;
        this.gson = gson;
    }

    public AvailableGenresDto getAvailableGenres(String spotifyAccessToken){
        String response = apiService.get("/recommendations/available-genre-seeds", new HashMap<>(), spotifyAccessToken);
        return gson.fromJson(response, AvailableGenresDto.class);
    }

}
