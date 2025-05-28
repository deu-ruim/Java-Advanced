package br.com.gs1.gs1.controller;

import br.com.gs1.gs1.domain.desastre.CreateDesastreDto;
import br.com.gs1.gs1.domain.desastre.ReadDesastreDto;
import br.com.gs1.gs1.domain.desastre.UpdateDesastreDto;
import br.com.gs1.gs1.domain.enums.Severidade;
import br.com.gs1.gs1.domain.enums.UF;
import br.com.gs1.gs1.domain.usuario.CreateUsuarioDto;
import br.com.gs1.gs1.domain.usuario.LoginDto;
import br.com.gs1.gs1.domain.usuario.ReadUsuarioDto;
import br.com.gs1.gs1.domain.usuario.UpdateUsuarioDto;
import br.com.gs1.gs1.service.DesastreService;
import br.com.gs1.gs1.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/desastres")
public class DesastreController {

    @Autowired
    private DesastreService desastreService;

    @PostMapping
    public ResponseEntity<ReadDesastreDto> create(
            @RequestBody @Valid CreateDesastreDto dto,
            UriComponentsBuilder uriBuilder) {
        ReadDesastreDto desastre = desastreService.create(dto);
        URI uri = uriBuilder.path("/api/desastres/{id}").buildAndExpand(desastre.id()).toUri();
        return ResponseEntity.created(uri).body(desastre);
    }

    @GetMapping
    public ResponseEntity<Page<ReadDesastreDto>> findAll(
            @RequestParam(required = false) UF uf,
            @RequestParam(required = false) Severidade severidade,
            @RequestParam(required = false) Long usuarioId,
            @PageableDefault(size = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(desastreService.findAllFiltered(uf, severidade, usuarioId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadDesastreDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(desastreService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadDesastreDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateDesastreDto dto) {
        return ResponseEntity.ok(desastreService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        desastreService.delete(id);
        return ResponseEntity.noContent().build();
    }
}