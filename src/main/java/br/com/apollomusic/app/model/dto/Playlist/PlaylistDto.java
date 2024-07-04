package br.com.apollomusic.app.model.dto.Playlist;

import java.util.Map;
import java.util.Set;

public record PlaylistDto(String id, String name, String description, Map<String, Integer> genres, Set<String> blockedGenres) {
}
