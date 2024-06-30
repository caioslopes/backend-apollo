package br.com.apollomusic.app.model.dto;

import java.util.Set;

public record UserReqDto(String username, Set<String> genres, Long establishmentId) {
}
