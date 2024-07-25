package br.com.apollomusic.app.domain.payload.response;

import br.com.apollomusic.app.domain.payload.ObjectUri;

import java.util.Set;

public record RecommendationsResponse(Set<ObjectUri> tracks) {
}

