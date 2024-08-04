package br.com.apollomusic.app.domain.payload.response;

public record ThirdPartyAccessResponse(String access_token, String token_type, Integer expires_in, String refresh_token, String scope) {
}
