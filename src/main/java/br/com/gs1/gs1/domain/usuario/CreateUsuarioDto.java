package br.com.gs1.gs1.domain.usuario;

import br.com.gs1.gs1.domain.enums.UF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record CreateUsuarioDto(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 3, max = 50) String username,
        @NotBlank @Size(min = 6) String password,
        @NotNull UF uf,
        Boolean ativo
) {}