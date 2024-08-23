package br.com.apollomusic.app.domain.payload.response;

public record EstablishmentResponse(Long id, String name, String deviceId, boolean isOff, PlaylistResponse playlist) {
}
