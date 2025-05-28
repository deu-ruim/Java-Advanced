package br.com.gs1.gs1.domain.desastre;

import br.com.gs1.gs1.domain.enums.Severidade;
import br.com.gs1.gs1.domain.enums.UF;
import br.com.gs1.gs1.domain.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "desastres")
@Getter
@Setter
@NoArgsConstructor
public class Desastre {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UF uf;

    @NotBlank
    @Size(max = 100)
    private String titulo;

    @NotBlank
    @Size(max = 1000)
    private String descricao;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Severidade severidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
}
