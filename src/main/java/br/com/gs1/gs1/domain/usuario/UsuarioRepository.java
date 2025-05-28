package br.com.gs1.gs1.domain.usuario;

import br.com.gs1.gs1.domain.enums.Role;
import br.com.gs1.gs1.domain.enums.UF;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmailAndIdNot(String email, Long id);
    boolean existsByUsernameAndIdNot(String username, Long id);

    @Query("SELECT u FROM Usuario u WHERE " +
            "(:uf IS NULL OR u.uf = :uf) AND " +
            "(:role IS NULL OR u.role = :role) AND " +
            "(:ativo IS NULL OR u.ativo = :ativo)")
    Page<Usuario> findAllFiltered(
            @Param("uf") UF uf,
            @Param("role") Role role,
            @Param("ativo") Boolean ativo,
            Pageable pageable);
}