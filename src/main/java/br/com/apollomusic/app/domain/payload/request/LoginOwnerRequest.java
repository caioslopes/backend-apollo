package br.com.apollomusic.app.domain.payload.request;

public record LoginOwnerRequest(String email, String password, Long establishmentId) {
}
