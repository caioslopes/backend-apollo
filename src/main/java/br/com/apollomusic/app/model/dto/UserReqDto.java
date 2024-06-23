package br.com.apollomusic.app.model.dto;

import java.util.List;

public record UserReqDto(String username, List<String> genres, Long establishmentId) {
}
