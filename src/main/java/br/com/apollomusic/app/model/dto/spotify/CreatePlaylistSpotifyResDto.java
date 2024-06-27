package br.com.apollomusic.app.model.dto.spotify;

import java.util.ArrayList;

public record CreatePlaylistSpotifyResDto(boolean collaborative, String description, ExternalUrls external_urls, Followers followers, String href, String id, ArrayList<Images> images, String name, OwnerSpotifyDto owner, boolean isPublic, String snapshot_id, String type, String uri) {
}
