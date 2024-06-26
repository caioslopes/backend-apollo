package br.com.apollomusic.app.model.dto;

import br.com.apollomusic.app.model.entities.Role;

import java.util.Set;

public record OwnerDto(String name, String email, Set<Role> roles, String refreshToken) {
}
