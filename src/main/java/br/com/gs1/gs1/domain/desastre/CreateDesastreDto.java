package br.com.gs1.gs1.domain.desastre;

import br.com.gs1.gs1.domain.enums.Severidade;
import br.com.gs1.gs1.domain.enums.UF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateDesastreDto(
        @NotNull(message = "State (UF) is required")
        UF uf,

        @NotBlank(message = "Title is required")
        @Size(max = 100, message = "Title cannot exceed 100 characters")
        String titulo,

        @NotBlank(message = "Description is required")
        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String descricao,

        @NotNull(message = "Severity level is required")
        Severidade severidade,

        @NotNull(message = "User ID is required")
        Long usuarioId
) {

}