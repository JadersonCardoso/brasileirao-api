package br.com.brasileirao.api.controller;

import br.com.brasileirao.api.dto.EquipeDTO;
import br.com.brasileirao.api.dto.EquipeResponseDTO;
import br.com.brasileirao.api.entity.Equipe;
import br.com.brasileirao.api.exception.StandardError;
import br.com.brasileirao.api.service.EquiperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@Api("API de equipes")
@RestController
@RequestMapping("/api/v1/equipes")
public class EquipeController {

    @Autowired
    private EquiperService equiperService;

    @ApiOperation(value = "Buscar equipe por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Equipe.class),
            @ApiResponse(code = 400, message = "Bad request", response = StandardError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
            @ApiResponse(code = 403, message = "Forbidden", response = StandardError.class),
            @ApiResponse(code = 404, message = "Not found", response = StandardError.class),
            @ApiResponse(code = 500, message = "Internal server error", response = StandardError.class),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Equipe> buscarEquipeId(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.equiperService.buscarEquipeId(id));
    }

    @ApiOperation(value = "Buscar todas as equipes")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = EquipeResponseDTO.class),
            @ApiResponse(code = 400, message = "Bad request", response = StandardError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
            @ApiResponse(code = 403, message = "Forbidden", response = StandardError.class),
            @ApiResponse(code = 404, message = "Not found", response = StandardError.class),
            @ApiResponse(code = 500, message = "Internal server error", response = StandardError.class),
    })
    @GetMapping
    public ResponseEntity<EquipeResponseDTO> listarEquipes() {
        return ResponseEntity.ok(this.equiperService.listarEquipes());
    }

    @ApiOperation(value = "Inserir equipe")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = Equipe.class),
            @ApiResponse(code = 400, message = "Bad request", response = StandardError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
            @ApiResponse(code = 403, message = "Forbidden", response = StandardError.class),
            @ApiResponse(code = 404, message = "Not found", response = StandardError.class),
            @ApiResponse(code = 500, message = "Internal server error", response = StandardError.class),
    })
    @PostMapping
    public ResponseEntity<Equipe> inserirEquipe(@Valid @RequestBody EquipeDTO dto) {
        Equipe equipe = this.equiperService.inserirEquipe(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(equipe.getId()).toUri();
        return ResponseEntity.created(location).body(equipe);
    }
    @ApiOperation(value = "Alterar equipe")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Created", response = Void.class),
            @ApiResponse(code = 400, message = "Bad request", response = StandardError.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = StandardError.class),
            @ApiResponse(code = 403, message = "Forbidden", response = StandardError.class),
            @ApiResponse(code = 404, message = "Not found", response = StandardError.class),
            @ApiResponse(code = 500, message = "Internal server error", response = StandardError.class),
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> alterarEquipe(@PathVariable("id") Long id,
                                              @RequestBody EquipeDTO dto) {
        this.equiperService.alterarEquipe(id, dto);
        return ResponseEntity.noContent().build();
    }

}
