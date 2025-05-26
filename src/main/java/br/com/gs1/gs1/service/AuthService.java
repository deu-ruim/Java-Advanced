package br.com.gs1.gs1.service;

import br.com.gs1.gs1.domain.usuario.CreateUsuarioDto;
import br.com.gs1.gs1.domain.usuario.ReadUsuarioDto;
import br.com.gs1.gs1.domain.usuario.Usuario;
import br.com.gs1.gs1.domain.usuario.UsuarioRepository;
import br.com.gs1.gs1.exception.DuplicateEntryException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReadUsuarioDto register(CreateUsuarioDto dto) {
        if (usuarioRepository.existsByEmail(dto.email())) {
            throw new DuplicateEntryException("Email already in use");
        }

        if (usuarioRepository.existsByUsername(dto.username())) {
            throw new DuplicateEntryException("Username already taken");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(dto.email());
        usuario.setUsername(dto.username());
        usuario.setPassword(passwordEncoder.encode(dto.password()));

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return new ReadUsuarioDto(savedUsuario);
    }
}