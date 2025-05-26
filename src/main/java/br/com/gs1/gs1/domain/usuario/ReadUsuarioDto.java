package br.com.gs1.gs1.domain.usuario;


import java.time.LocalDateTime;

public record ReadUsuarioDto(
        Long id,
        String email,
        String username
) {
    public ReadUsuarioDto(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getUsername()
        );
    }
}