package br.com.gs1.gs1.domain.desastre;

import br.com.gs1.gs1.domain.enums.Severidade;
import br.com.gs1.gs1.domain.enums.UF;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DesastreRepository extends JpaRepository<Desastre, Long> {
    List<Desastre> findByUf(UF uf);
    List<Desastre> findBySeveridade(Severidade severidade);

    @Query("SELECT d FROM Desastre d WHERE " +
            "(:uf IS NULL OR d.uf = :uf) AND " +
            "(:severidade IS NULL OR d.severidade = :severidade) AND " +
            "(:usuarioId IS NULL OR d.usuario.id = :usuarioId)")
    Page<Desastre> findAllFiltered(
            @Param("uf") UF uf,
            @Param("severidade") Severidade severidade,
            @Param("usuarioId") Long usuarioId,
            Pageable pageable);
}