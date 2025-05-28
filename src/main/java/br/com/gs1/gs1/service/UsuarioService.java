package br.com.gs1.gs1.service;

import br.com.gs1.gs1.domain.enums.Role;
import br.com.gs1.gs1.domain.usuario.*;
import br.com.gs1.gs1.exception.DuplicateEntryException;
import br.com.gs1.gs1.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public ReadUsuarioDto create(CreateUsuarioDto dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new DuplicateEntryException("Email already in use");
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


    public Page<ReadUsuarioDto> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(ReadUsuarioDto::new);
    }

    public ReadUsuarioDto findById(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return new ReadUsuarioDto(usuario);
    }

    @Transactional
    public ReadUsuarioDto update(Long id, UpdateUsuarioDto dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

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

    @Transactional
    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        usuarioRepository.deleteById(id);
    }

    public boolean validateCredentials(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return passwordEncoder.matches(password, usuario.getPassword());
    }
}