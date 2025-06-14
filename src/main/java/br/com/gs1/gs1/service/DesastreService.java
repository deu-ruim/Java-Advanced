package br.com.gs1.gs1.service;

import br.com.gs1.gs1.domain.desastre.*;
import br.com.gs1.gs1.domain.enums.Severidade;
import br.com.gs1.gs1.domain.enums.UF;
import br.com.gs1.gs1.domain.usuario.Usuario;
import br.com.gs1.gs1.domain.usuario.UsuarioRepository;
import br.com.gs1.gs1.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DesastreService {

    @Autowired
    private DesastreRepository desastreRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;


//    CREATE
    @CacheEvict(value = "desastres", allEntries = true)
    @Transactional
    public ReadDesastreDto create(CreateDesastreDto dto) {
        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        Desastre desastre = new Desastre();
        desastre.setUf(dto.uf());
        desastre.setTitulo(dto.titulo());
        desastre.setDescricao(dto.descricao());
        desastre.setSeveridade(dto.severidade());
        desastre.setUsuario(usuario);

        return new ReadDesastreDto(desastreRepository.save(desastre));
    }
//  FIND ALL FILTERED
@Cacheable(value = "desastres", key = "{#uf?.name(), #severidade?.name(), #usuarioId}")
public Page<ReadDesastreDto> findAllFiltered(
        UF uf,
        Severidade severidade,
        Long usuarioId,
        Pageable pageable) {

    return desastreRepository.findAllFiltered(
            uf,
            severidade,
            usuarioId,
            pageable
    ).map(ReadDesastreDto::new);
}

//    FINDByID
    @Cacheable(value = "desastre", key = "#id")
    public ReadDesastreDto findById(Long id) {
        return desastreRepository.findById(id)
                .map(ReadDesastreDto::new)
                .orElseThrow(() -> new NotFoundException("Disaster not found with id: " + id));
    }
//    UPDATE
    @Caching(evict = {
            @CacheEvict(value = "desastres", key = "#id"),
            @CacheEvict(value = "desastres", allEntries = true)
    })
    @Transactional
    public ReadDesastreDto update(Long id, UpdateDesastreDto dto) {
        Desastre desastre = desastreRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Disaster not found with id: " + id));

        if (dto.uf() != null) desastre.setUf(dto.uf());
        if (dto.titulo() != null) desastre.setTitulo(dto.titulo());
        if (dto.descricao() != null) desastre.setDescricao(dto.descricao());
        if (dto.severidade() != null) desastre.setSeveridade(dto.severidade());

        return new ReadDesastreDto(desastreRepository.save(desastre));
    }
//  DELETE
    @CacheEvict(value = "desastres", key = "#id")
    @Transactional
    public void delete(Long id) {
        desastreRepository.deleteById(id);
    }

    @CacheEvict(value = {"desastre", "desastres"}, allEntries = true)
    public void clearCache() {
    }
}
