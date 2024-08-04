package br.com.apollomusic.app.domain.payload.request;

import br.com.apollomusic.app.domain.payload.Offset;

public record StartResumePlaybackRequest(String context_uri, Offset offset, int position_ms) {
}
