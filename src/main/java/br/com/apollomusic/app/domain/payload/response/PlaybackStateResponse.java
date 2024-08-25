package br.com.apollomusic.app.domain.payload.response;

public record PlaybackStateResponse(int progress_ms, boolean is_playing) {
}
