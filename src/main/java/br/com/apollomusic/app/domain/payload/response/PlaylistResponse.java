package br.com.apollomusic.app.domain.payload.response;

import br.com.apollomusic.app.domain.payload.Image;

import java.util.Collection;
import java.util.Map;

public record PlaylistResponse(String id, String name, String description, Collection<Image> images, Collection<String> blockedGenres, Map<String, Integer> genres, boolean hasIncrementedGenre ) {
}
