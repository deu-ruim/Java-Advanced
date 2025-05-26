package br.com.gs1.gs1.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUsuarioDto(
        @Email String email,
        @Size(min = 3, max = 50) String username,
        @Size(min = 6) String password
) {}