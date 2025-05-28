package br.com.gs1.gs1.domain.desastre;
import br.com.gs1.gs1.domain.enums.Severidade;
import br.com.gs1.gs1.domain.enums.UF;
import br.com.gs1.gs1.domain.usuario.ReadUsuarioDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record ReadDesastreDto(
        Long id,
        UF uf,
        String titulo,
        String descricao,
        LocalDateTime createdAt,
        Severidade severidade,
        ReadUsuarioDto usuario
) {
    public ReadDesastreDto(Desastre desastre) {
        this(
                desastre.getId(),
                desastre.getUf(),
                desastre.getTitulo(),
                desastre.getDescricao(),
                desastre.getCreatedAt(),
                desastre.getSeveridade(),
                new ReadUsuarioDto(desastre.getUsuario())
        );
    }
}
