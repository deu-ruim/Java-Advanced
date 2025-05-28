package br.com.gs1.gs1.domain.desastre;

import br.com.gs1.gs1.domain.enums.Severidade;
import br.com.gs1.gs1.domain.enums.UF;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DesastreRepository extends JpaRepository<Desastre, Long> {
    List<Desastre> findByUf(UF uf);
    List<Desastre> findBySeveridade(Severidade severidade);
}