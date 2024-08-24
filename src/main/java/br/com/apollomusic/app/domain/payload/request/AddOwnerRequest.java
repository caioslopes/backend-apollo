package br.com.apollomusic.app.domain.payload.request;

public record AddOwnerRequest(String email, String password, Long establishmentId) {
}
