package br.com.apollomusic.app.domain.payload.response;

public record OwnerResponse(String name, String email, boolean hasThirdPartyAccess) {
}
