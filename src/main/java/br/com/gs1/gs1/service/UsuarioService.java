package br.com.gs1.gs1.service;

import br.com.gs1.gs1.domain.enums.Role;
import br.com.gs1.gs1.domain.enums.UF;
import br.com.gs1.gs1.domain.usuario.*;
import br.com.gs1.gs1.exception.AuthenticationException;
import br.com.gs1.gs1.exception.DuplicateEntryException;
import br.com.gs1.gs1.exception.NotFoundException;
import br.com.gs1.gs1.infra.security.TokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    @Autowired
    private  UsuarioRepository usuarioRepository;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired TokenService tokenService;


//    CREATE
    @CacheEvict(value = "usuarios", key = "#result.id", condition = "#result != null")
    @Transactional
    public ReadUsuarioDto create(CreateUsuarioDto dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new DuplicateEntryException("Email already in use");
        }
        if (usuarioRepository.existsByUsername(dto.username())) {
            throw new DuplicateEntryException("Username already in use");
        }


        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email());
        usuario.setUsername(dto.username());
        usuario.setPassword(passwordEncoder.encode(dto.password()));
        usuario.setUf(dto.uf());
        usuario.setAtivo(dto.ativo() != null ? dto.ativo() : true);

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return new ReadUsuarioDto(savedUsuario);
    }

// FIND ALL FILTERED
    public Page<ReadUsuarioDto> findAllFiltered(
            UF uf,
            Role role,
            Boolean ativo,
            Pageable pageable) {

        return usuarioRepository.findAllFiltered(
                uf,
                role,
                ativo,
                pageable
        ).map(ReadUsuarioDto::new);
    }
//  FIND BY ID
    @Cacheable(value = "usuario", key = "#id")
    public ReadUsuarioDto findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));
        return new ReadUsuarioDto(usuario);
    }

//    UPDATE
    @Caching(evict = {
            @CacheEvict(value = "usuarios", key = "#id"),
            @CacheEvict(value = "usuarios", key = "#result.email", condition = "#result != null")
    })
    @Transactional
    public ReadUsuarioDto update(Long id, UpdateUsuarioDto dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + id));

        if (dto.email() != null && !dto.email().isBlank() && !dto.email().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmailAndIdNot(dto.email(), id)) {
                throw new DuplicateEntryException("Email already in use by another user");
            }
            usuario.setEmail(dto.email());
        }

        if (dto.username() != null && !dto.username().isBlank() && !dto.username().equals(usuario.getUsername())) {
            if (usuarioRepository.existsByUsernameAndIdNot(dto.username(), id)) {
                throw new DuplicateEntryException("Username already taken by another user");
            }
            usuario.setUsername(dto.username());
        }

        if (dto.password() != null && !dto.password().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(dto.password()));
        }

        if (dto.uf() != null) {
            usuario.setUf(dto.uf());
        }

        if (dto.ativo() != null) {
            usuario.setAtivo(dto.ativo());
        }

        if (dto.role() != null && !dto.role().equals(usuario.getRole())) {
            usuario.setRole(dto.role());
        }

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return new ReadUsuarioDto(updatedUsuario);
    }

//    DELETE
    @CacheEvict(value = "usuarios", key = "#id")
    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("User not found with id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

//    VALIDATE (MOCK/LEGACY)
    public boolean validateCredentials(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User with email "+ email + " not found.") {
                });

        if (!usuario.getAtivo()) {
            throw new IllegalStateException("User account is inactive");
        }

        boolean valid =  passwordEncoder.matches(password, usuario.getPassword());
        if (!valid) throw new AuthenticationException("Password incorrect");
        return valid;
    }

//    LOGIN (REAL AUTH)
    public ResponseEntity<TokenResponse> loginUser(LoginDto request) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            var authentication = authenticationManager.authenticate(authToken);

            var usuario = (Usuario) authentication.getPrincipal();
            var token = tokenService.generateToken(usuario);

            return ResponseEntity.ok(new TokenResponse(token));
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new br.com.gs1.gs1.exception.AuthenticationException("Invalid email or password");
        }
    }
//  CLEAR-CACHE
    @CacheEvict(value = {"usuario", "usuarios"}, allEntries = true)
    public void clearCache() {
    }
}