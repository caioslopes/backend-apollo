package br.com.apollomusic.app.domain.payload.response;

import java.util.Map;

public record AvailableGenresVotesResponses(Map<String, Integer> genresAvailable, Integer votesTotal, Integer genresTotal) {
}
