package br.com.apollomusic.app.model.dto.spotify;

public record OwnerSpotifyDto(ExternalUrls external_urls, Followers followers, String href, String id, String type, String uri, String display_name) {
}
