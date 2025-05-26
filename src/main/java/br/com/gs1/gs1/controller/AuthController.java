package br.com.gs1.gs1.controller;

import br.com.gs1.gs1.domain.usuario.CreateUsuarioDto;
import br.com.gs1.gs1.domain.usuario.LoginDto;
import br.com.gs1.gs1.domain.usuario.ReadUsuarioDto;
import br.com.gs1.gs1.domain.usuario.Usuario;
import br.com.gs1.gs1.infra.security.TokenDto;
import br.com.gs1.gs1.service.TokenService;
import br.com.gs1.gs1.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody @Valid LoginDto dto) {
        logger.info("Attempting login for email: {}", dto.email());
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

       try {
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        logger.info("Authentication successful for email: {}", dto.email());
        var token = tokenService.generateToken((Usuario) authentication.getPrincipal());
        return ResponseEntity.ok(new TokenDto(token));

       } catch (AuthenticationException e) {
           logger.error("Authentication failed for email: {}", dto.email(), e);
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
       }

    }

    @PostMapping("/register")
    public ResponseEntity<ReadUsuarioDto> register(@RequestBody @Valid CreateUsuarioDto dto, UriComponentsBuilder uriBuilder) {
        ReadUsuarioDto usuario = authService.register(dto);
        URI uri = uriBuilder.path("/api/usuarios/{id}").buildAndExpand(usuario.id()).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Just return success - actual "logout" happens client-side
        return ResponseEntity.noContent().build();
    }
}