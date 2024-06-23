package br.com.apollomusic.app.model.dto;


public record ResponseTokenDto(String access_token, String token_type, Integer expires_in, String refresh_token, String scope) {
}
