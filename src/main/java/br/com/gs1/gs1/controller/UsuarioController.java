package br.com.gs1.gs1.controller;

import br.com.gs1.gs1.domain.usuario.CreateUsuarioDto;
import br.com.gs1.gs1.domain.usuario.ReadUsuarioDto;
import br.com.gs1.gs1.domain.usuario.UpdateUsuarioDto;
import br.com.gs1.gs1.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ReadUsuarioDto> create(
            @RequestBody @Valid CreateUsuarioDto dto,
            UriComponentsBuilder uriBuilder) {
        ReadUsuarioDto usuario = usuarioService.create(dto);
        URI uri = uriBuilder.path("/api/usuarios/{id}").buildAndExpand(usuario.id()).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    @GetMapping
    public ResponseEntity<Page<ReadUsuarioDto>> findAll(
            @PageableDefault(size = 10, sort = {"username"}) Pageable pageable) {
        Page<ReadUsuarioDto> usuarios = usuarioService.findAll(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadUsuarioDto> findById(@PathVariable Long id) {
        ReadUsuarioDto usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReadUsuarioDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateUsuarioDto dto) {
        ReadUsuarioDto updatedUsuario = usuarioService.update(id, dto);
        return ResponseEntity.ok(updatedUsuario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<ReadUsuarioDto> register(
            @RequestBody @Valid CreateUsuarioDto dto,
            UriComponentsBuilder uriBuilder) {
        // For now, just alias the create method
        return create(dto, uriBuilder);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login() {
        // Temporary implementation - will be replaced with JWT later
        return ResponseEntity.ok("Login endpoint - authentication will be implemented later");
    }
}
