package br.com.apollomusic.app.application;

import br.com.apollomusic.app.domain.Establishment.Establishment;
import br.com.apollomusic.app.domain.Owner.Owner;
import br.com.apollomusic.app.domain.Establishment.Playlist;
import br.com.apollomusic.app.domain.Establishment.Song;
import br.com.apollomusic.app.domain.payload.response.ChangePlaylistResponse;
import br.com.apollomusic.app.domain.payload.response.RecommendationsResponse;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlgorithmService {

    private final ApiService apiService;
    private final Gson gson;
    private final ThirdPartyService thirdPartyService;

    @Autowired
    public AlgorithmService(ApiService apiService, Gson gson, ThirdPartyService thirdPartyService) {
        this.apiService = apiService;
        this.gson = gson;
        this.thirdPartyService = thirdPartyService;
    }

    public void runAlgorithm(Establishment establishment){
        Owner owner = establishment.getOwner();
        Playlist playlist = establishment.getPlaylist();

        HashMap<String, Integer> songsQuantityPerGenre = getSongsQuantityPerGenre(playlist);
        Collection<Song> songsInPlaylist = playlist.getSongs();

        int songsQuantity;
        Set<Song> songs;

        for(var item : songsQuantityPerGenre.entrySet()){
            songsQuantity = getQuantityOfSongInPlaylistByGenre(item.getKey(), songsInPlaylist);

            if(songsQuantity != item.getValue()){
                if(songsQuantity < item.getValue()){
                    songs = getRecommendations(item.getValue() - songsQuantity, item.getKey(), owner.getAccessToken());

                    for (Song s : songs){
                        playlist.addSong(s);
                    }

                    ChangePlaylistResponse changePlaylistResponse = thirdPartyService.addSongsToPlaylist(playlist.getId(), songs, owner.getAccessToken());
                    playlist.setSnapshot(changePlaylistResponse.snapshot_id());
                    establishment.setPlaylist(playlist);
                }else{
                    songs = getRandomSongsInPlaylistByGenre(songsQuantity - item.getValue(), item.getKey(), songsInPlaylist);

                    for (Song s : songs){
                        playlist.removeSong(s);
                    }

                    ChangePlaylistResponse changePlaylistResponse = thirdPartyService.removeSongsFromPlaylist(playlist.getId(), playlist.getSnapshot(), songs, owner.getAccessToken());
                    playlist.setSnapshot(changePlaylistResponse.snapshot_id());
                    establishment.setPlaylist(playlist);
                }
            }
        }
    }

    private int getQuantityOfSongInPlaylistByGenre(String genre, Collection<Song> songs){
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
        var recommendationsResponse = gson.fromJson(response, RecommendationsResponse.class).tracks();

        for(var item : recommendationsResponse){
            songs.add(new Song(item.uri(), genre));
        }

        return songs;
    }

    private Set<Song> getRandomSongsInPlaylistByGenre(Integer quantity, String genre, Collection<Song> playlist){
        Set<Song> result = new HashSet<>();
        List<Song> songsInPlaylistOfGenre = new ArrayList<>();
        Random random = new Random();

        for(var item : playlist){
            if(item.getGenre().equals(genre)){
                songsInPlaylistOfGenre.add(item);
            }
        }

        for(int i = 0; i < quantity; i++){
            result.add(songsInPlaylistOfGenre.remove(random.nextInt(songsInPlaylistOfGenre.size())));
        }

        return result;
    }

    private HashMap<String, Integer> getSongsQuantityPerGenre(Playlist playlist){
        if(playlist == null) return null;

        int totalVotes = playlist.getVotesQuantity();
        if(totalVotes == 0) return null;

        HashMap<String, Integer> genresPercent = new HashMap<>();
        HashMap<String, Integer> songQuantityPerGenre = new HashMap<>();

        // foreach to get the percentage of each genre in playlist
        for(var item : playlist.getGenres().entrySet()){
            int votesInGenre = item.getValue();
            genresPercent.put(item.getKey(), votesInGenre * 100 / totalVotes);
        }

        //  foreach to calculate the quantity of songs for each genre
        for(var item : genresPercent.entrySet()){
            songQuantityPerGenre.put(item.getKey(), item.getValue() * Playlist.SONGLIMIT / 100);
        }

        return songQuantityPerGenre;
    }
}
