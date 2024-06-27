package br.com.apollomusic.app.model.dto.spotify;

import java.util.ArrayList;

public record SpotifyMeResDto(String country, String display_name, ExplicitContent explicit_content, ExternalUrls external_urls, Followers followers, String href, String id, ArrayList<Images> images, String product, String type, String uri) {
}
