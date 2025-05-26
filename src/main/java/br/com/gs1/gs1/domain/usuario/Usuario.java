package br.com.gs1.gs1.domain.usuario;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email"),
                @UniqueConstraint(columnNames = "username")
        })
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Deve ser um endereço de e-mail válido", regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false)
    private String email;

    @NotBlank(message = "Username é obrigatório")
    @Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres")
    @Column(nullable = false)
    private String username;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter pelo menos 6 caracteres")
    @Column(nullable = false)
    private String password;
}