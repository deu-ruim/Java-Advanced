package br.com.gs1.gs1.controller;

import br.com.gs1.gs1.domain.enums.Role;
import br.com.gs1.gs1.domain.enums.UF;
import br.com.gs1.gs1.domain.usuario.*;
import br.com.gs1.gs1.infra.security.TokenResponse;
import br.com.gs1.gs1.service.TokenService;
import br.com.gs1.gs1.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "CRUD completo de usuários")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

//    LEGACY POST
    @Operation(summary = "(LEGACY ROUTE) Cadastrar novo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Email/username já em uso")
    })
    @PostMapping
    public ResponseEntity<ReadUsuarioDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do novo usuário",
                    required = true
            )
            @RequestBody @Valid CreateUsuarioDto dto,
            UriComponentsBuilder uriBuilder) {
        ReadUsuarioDto usuario = usuarioService.create(dto);
        URI uri = uriBuilder.path("/api/usuarios/{id}").buildAndExpand(usuario.id()).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

//    GET
    @Operation(
            summary = "Listar usuários",
            description = "Retorna usuários paginados com filtros opcionais"
    )
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<ReadUsuarioDto>> findAll(
            @Parameter(description = "UF do usuário (opcional)", example = "SP")
            @RequestParam(required = false) UF uf,

            @Parameter(description = "Role do usuário (opcional)", example = "ADMIN")
            @RequestParam(required = false) Role role,

            @Parameter(description = "Status ativo (opcional)", example = "true")
            @RequestParam(required = false) Boolean ativo,

            @Parameter(hidden = true)
            @PageableDefault(size = 10, sort = {"username"}) Pageable pageable) {
        return ResponseEntity.ok(usuarioService.findAllFiltered(uf, role, ativo, pageable));
    }

//    GETByID
    @Operation(summary = "Buscar usuário por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReadUsuarioDto> findById(
            @Parameter(description = "ID do usuário", example = "1")
            @PathVariable Long id) {
        ReadUsuarioDto usuario = usuarioService.findById(id);
        return ResponseEntity.ok(usuario);
    }

//      PUT
    @Operation(summary = "Atualizar usuário existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "409", description = "Email/username já em uso")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReadUsuarioDto> update(
            @Parameter(description = "ID do usuário", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados atualizados do usuário",
                    required = true
            )
            @RequestBody @Valid UpdateUsuarioDto dto) {
        ReadUsuarioDto updatedUsuario = usuarioService.update(id, dto);
        return ResponseEntity.ok(updatedUsuario);
    }

//      DELETE
    @Operation(summary = "Excluir usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuário excluído"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do usuário", example = "1")
            @PathVariable Long id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

//      REGISTER
    @Operation(summary = "Registrar novo usuário (alias para POST /api/usuarios)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Email/username já em uso")
    })
    @PostMapping("/register")
    public ResponseEntity<ReadUsuarioDto> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do novo usuário",
                    required = true
            )
            @RequestBody @Valid CreateUsuarioDto dto,
            UriComponentsBuilder uriBuilder) {
        return create(dto, uriBuilder);
    }

//    LEGACY LOGIN
    @Operation(summary = "(SIMULAÇÃO/LEGACY) Login de usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/mocklogin")
    public ResponseEntity<Void> mockLogin(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de login",
                    required = true
            )
            @RequestBody LoginDto request) {
        boolean valid = usuarioService.validateCredentials(request.email(), request.password());
        return valid ? ResponseEntity.ok().build() : ResponseEntity.status(401).build();
    }
//      LOGIN
    @Operation(summary = "Login de usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Credenciais de login",
                    required = true
            )
            @RequestBody @Valid LoginDto request) {
                return usuarioService.loginUser(request);
    }
//      CLEAR-CACHE
    @Operation(summary = "Limpar cache de Usuários")
    @ApiResponse(responseCode = "204", description = "Cache limpo")
    @PostMapping("/clear-cache")
    public ResponseEntity<Void> clearCache() {
        usuarioService.clearCache();
        return ResponseEntity.noContent().build();
    }
}
