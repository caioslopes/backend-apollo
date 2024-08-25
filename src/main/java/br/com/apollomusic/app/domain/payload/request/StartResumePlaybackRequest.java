package br.com.apollomusic.app.domain.payload.request;

public record StartResumePlaybackRequest(String context_uri, PlaybackOffSetRequest offset, int position_ms) {
}
