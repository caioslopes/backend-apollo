package br.com.apollomusic.app.domain.payload.response;

import java.util.Map;

public record AvailableGenresVotesResponse(Map<String, Integer> genresAvailable, Integer totalVotes) {
}
