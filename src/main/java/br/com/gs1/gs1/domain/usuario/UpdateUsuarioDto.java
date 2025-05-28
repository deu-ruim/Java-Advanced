package br.com.gs1.gs1.domain.usuario;

import br.com.gs1.gs1.domain.enums.Role;
import br.com.gs1.gs1.domain.enums.UF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.*;

public record UpdateUsuarioDto(
        @Email String email,
        @Size(min = 3, max = 50) String username,
        @Size(min = 6) String password,
        UF uf,
        Boolean ativo,
        Role role
) {}