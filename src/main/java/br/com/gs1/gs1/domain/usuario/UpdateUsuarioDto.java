package br.com.gs1.gs1.domain.usuario;

import br.com.gs1.gs1.domain.enums.Role;
import br.com.gs1.gs1.domain.enums.UF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUsuarioDto(
        @Email(message = "Please provide a valid email address")
        String email,

        @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
        String username,

        @Size(min = 6, message = "Password must have at least 6 characters")
        String password,

        UF uf,
        Boolean ativo,
        Role role
) {

}