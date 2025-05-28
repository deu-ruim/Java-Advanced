package br.com.gs1.gs1.controller;

import br.com.gs1.gs1.domain.desastre.CreateDesastreDto;
import br.com.gs1.gs1.domain.desastre.ReadDesastreDto;
import br.com.gs1.gs1.domain.desastre.UpdateDesastreDto;
import br.com.gs1.gs1.domain.enums.Severidade;
import br.com.gs1.gs1.domain.enums.UF;
import br.com.gs1.gs1.service.DesastreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/desastres")
@Tag(name = "Desastres", description = "CRUD completo de desastres naturais")
public class DesastreController {

    @Autowired
    private DesastreService desastreService;

    @Operation(summary = "Cadastrar novo desastre")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Desastre criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados")
    })
    @PostMapping
    public ResponseEntity<ReadDesastreDto> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do novo desastre",
                    required = true
            )
            @RequestBody @Valid CreateDesastreDto dto,
            UriComponentsBuilder uriBuilder) {
        ReadDesastreDto desastre = desastreService.create(dto);
        URI uri = uriBuilder.path("/api/desastres/{id}").buildAndExpand(desastre.id()).toUri();
        return ResponseEntity.created(uri).body(desastre);
    }

    @Operation(
            summary = "Listar desastres",
            description = "Retorna desastres paginados com filtros opcionais"
    )
    @ApiResponse(responseCode = "200", description = "Lista de desastres retornada com sucesso")
    @GetMapping
    public ResponseEntity<Page<ReadDesastreDto>> findAll(
            @Parameter(description = "UF do desastre (opcional)", example = "SP")
            @RequestParam(required = false) UF uf,

            @Parameter(description = "Severidade do desastre (opcional)", example = "CRITICAL")
            @RequestParam(required = false) Severidade severidade,

            @Parameter(description = "ID do usuário (opcional)", example = "1")
            @RequestParam(required = false) Long usuarioId,

            @Parameter(hidden = true)
            @PageableDefault(size = 10, sort = {"createdAt"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(desastreService.findAllFiltered(uf, severidade, usuarioId, pageable));
    }

    @Operation(summary = "Buscar desastre por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Desastre encontrado"),
            @ApiResponse(responseCode = "404", description = "Desastre não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReadDesastreDto> findById(
            @Parameter(description = "ID do desastre", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(desastreService.findById(id));
    }

    @Operation(summary = "Atualizar desastre existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Desastre atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Desastre não encontrado"),
            @ApiResponse(responseCode = "409", description = "Conflito de dados")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReadDesastreDto> update(
            @Parameter(description = "ID do desastre", example = "1")
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados atualizados do desastre",
                    required = true
            )
            @RequestBody @Valid UpdateDesastreDto dto) {
        return ResponseEntity.ok(desastreService.update(id, dto));
    }

    @Operation(summary = "Excluir desastre")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Desastre excluído"),
            @ApiResponse(responseCode = "404", description = "Desastre não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do desastre", example = "1")
            @PathVariable Long id) {
        desastreService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Limpar cache de Desastres")
    @ApiResponse(responseCode = "204", description = "Cache limpo")
    @PostMapping("/clear-cache")
    public ResponseEntity<Void> clearCache() {
        desastreService.clearCache();
        return ResponseEntity.noContent().build();
    }
}
