package br.com.apollomusic.app.model.services;

import br.com.apollomusic.app.model.entities.Playlist;
import br.com.apollomusic.app.model.entities.Song;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AlgorithmService {
    private  GenreVotesService genreVotesService;

    public AlgorithmService(Playlist playlist) {
        this.genreVotesService = new GenreVotesService(playlist);
    }

    public GenreVotesService getGenreVotesService() {
        return genreVotesService;
    }

    public void runAlgorithm(){
        var votesInEachGenre = genreVotesService.getSongsQuantityPerGenre();
        var songsInPlaylist = genreVotesService.getPlaylist().getSongs();
        int songsQuantity;

        for(var item : votesInEachGenre.entrySet()){
            songsQuantity = getQuantityOfSongInPlaylistByGenre(item.getKey(), songsInPlaylist);

            while (songsQuantity != item.getValue()){
                if(songsQuantity < item.getValue()){
                    // adiciona musica

                    songsQuantity++;
                }else{
                    // remove musica

                    songsQuantity--;
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
}
