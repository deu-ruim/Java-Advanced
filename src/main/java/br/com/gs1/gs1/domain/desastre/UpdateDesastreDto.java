package br.com.gs1.gs1.domain.desastre;
import br.com.gs1.gs1.domain.enums.Severidade;
import br.com.gs1.gs1.domain.enums.UF;
import jakarta.validation.constraints.Size;

public record UpdateDesastreDto(
        UF uf,

        @Size(max = 100, message = "Title cannot exceed 100 characters")
        String titulo,

        @Size(max = 1000, message = "Description cannot exceed 1000 characters")
        String descricao,

        Severidade severidade
) {

}