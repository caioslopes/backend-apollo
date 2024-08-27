package br.com.apollomusic.app.domain.payload.request;

public record CreateOwnerRequest(String name, String email, String password) {
}
