package br.com.apollomusic.app.domain.payload.response;

import br.com.apollomusic.app.domain.payload.Image;

import java.util.Collection;

public record ThirdPartyPlaylistResponse(String id, String name, String description, Collection<Image> images) {
}
