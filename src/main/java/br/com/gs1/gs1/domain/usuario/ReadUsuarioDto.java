package br.com.gs1.gs1.domain.usuario;


import br.com.gs1.gs1.domain.enums.Role;
import br.com.gs1.gs1.domain.enums.UF;

import java.time.LocalDateTime;

public record ReadUsuarioDto(
        Long id,
        String email,
        String username,
        UF uf,
        Role role
) {
    public ReadUsuarioDto(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getUsername(),
                usuario.getUf(),
                usuario.getRole()
        );
    }
}