package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.Spotify.dto.Playlist.ObjectUri;
import br.com.apollomusic.app.Spotify.dto.Playlist.RecommendationsResDto;
import br.com.apollomusic.app.model.entities.Playlist;
import br.com.apollomusic.app.model.entities.Song;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlgorithmService {
    private  GenreVotesService genreVotesService;
    private final ApiService apiService;
    private final Gson gson;

    private final PlaylistService playlistService;

    public AlgorithmService(Playlist playlist, ApiService apiService, Gson gson, PlaylistService playlistService) {
        this.genreVotesService = new GenreVotesService(playlist);
        this.apiService = apiService;
        this.gson = gson;
        this.playlistService = playlistService;
    }

    public GenreVotesService getGenreVotesService() {
        return genreVotesService;
    }

    public void runAlgorithm(String accessToken){
        var votesInEachGenre = genreVotesService.getSongsQuantityPerGenre();
        var songsInPlaylist = genreVotesService.getPlaylist().getSongs();
        int songsQuantity;
        Set<Song> songs;

        for(var item : votesInEachGenre.entrySet()){
            songsQuantity = getQuantityOfSongInPlaylistByGenre(item.getKey(), songsInPlaylist);

            if(songsQuantity != item.getValue()){
                if(songsQuantity < item.getValue()){
                    songs = getRecommendations(item.getValue() - songsQuantity, item.getKey(), accessToken);

                    playlistService.addSongsToPlaylist(
                            genreVotesService.getPlaylist().getPlaylistId(),
                            songs,
                            accessToken
                    );
                }else{
                    songs = getRandomSongsInPlaylistByGenre(songsQuantity - item.getValue(), item.getKey(), songsInPlaylist);

                    playlistService.removeSongsFromPlaylist(
                            genreVotesService.getPlaylist().getPlaylistId(),
                            songs,
                            genreVotesService.getPlaylist().getLastSnapshotId(),
                            accessToken
                    );
                }
            }
        }
    }

    private int getQuantityOfSongInPlaylistByGenre(String genre, Set<Song> songs){
        int quantity = 0;

        for(Song s : songs){
            if(s.getGenre().equals(genre)){
                quantity++;
            }
        }

        return quantity;
    }

    private Set<Song> getRecommendations(Integer quantity, String genre, String accessToken){
        Set<Song> songs = new HashSet<>();
        String endpoint = "/recommendations";

        Map<String, String> queryParams = new HashMap<>();

        queryParams.put("limit", quantity.toString());
        queryParams.put("market", "BR");
        queryParams.put("seed_genres", genre);

        String response = apiService.get(endpoint, queryParams, accessToken);
        var recommendationsResponse = gson.fromJson(response, RecommendationsResDto.class).tracks();

        for(var item : recommendationsResponse){
            songs.add(new Song(item.uri(), genre));
        }

        return songs;
    }

    private Set<Song> getRandomSongsInPlaylistByGenre(Integer quantity, String genre, Set<Song> playlist){
        Set<Song> result = new HashSet<>();
        List<Song> songsInPlaylistOfGenre = new ArrayList<>();
        Random random = new Random();

        for(var item : playlist){
            if(item.getGenre().equals(genre)){
                songsInPlaylistOfGenre.add(item);
            }
        }

        for(int i = 0; i < quantity; i++){
            songsInPlaylistOfGenre.remove(random.nextInt(songsInPlaylistOfGenre.size()));
        }

        for(var item : songsInPlaylistOfGenre){
            result.add(new Song(item.getUri(), item.getGenre()));
        }

        return result;
    }
}
