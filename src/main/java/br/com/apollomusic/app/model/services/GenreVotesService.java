package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.model.entities.Playlist;
import br.com.apollomusic.app.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GenreVotesService {
    private PlaylistRepository playlistRepository;

    public GenreVotesService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public HashMap<String, Integer> getSongsQuantityPerGenre(String id){
        int totalVotes = 0;
        HashMap<String, Integer> genresPercent = new HashMap<>();
        HashMap<String, Integer> songQuantityPerGenre = new HashMap<>();
        var playlist = playlistRepository.findById(id);

        if(playlist == null){
            return null;
        }

        // foreach to get the total votes
        for(Integer item : playlist.get().getGenres().values()){
            totalVotes += item;
        }

        // foreach to get the percentage of each genre in playlist
        for(var item : playlist.get().getGenres().entrySet()){
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
