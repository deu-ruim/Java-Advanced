package br.com.gs1.gs1.domain.desastre;
import br.com.gs1.gs1.domain.enums.Severidade;
import br.com.gs1.gs1.domain.enums.UF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateDesastreDto(
        UF uf,
        @Size(max = 100) String titulo,
        @Size(max = 1000) String descricao,
        Severidade severidade
) {}