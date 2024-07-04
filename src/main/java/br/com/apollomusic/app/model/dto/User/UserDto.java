package br.com.apollomusic.app.model.dto.User;

import java.util.Set;

public record UserDto(Long id, String name, Set<String> genres) {
}
