package com.biblioteca.bookTrack.controller;

import com.biblioteca.bookTrack.dto.UsuarioDTO;
import com.biblioteca.bookTrack.model.Usuario;
import com.biblioteca.bookTrack.service.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários da biblioteca")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

// Metodo de cadastrar os usuarios
    @PostMapping
    @Operation(summary = "Cadastrar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Dados informados inválidos."),
            @ApiResponse(responseCode = "409", description = "Usuário já cadastrado.")
    })
    public ResponseEntity<String> cadastrar(@Valid @RequestBody UsuarioDTO usuarioDTO) {

        usuarioService.salvarUsuario(usuarioDTO);

        return ResponseEntity.status(201).body("Usuário cadastrado com sucesso!");
    }

    // Metodos de buscar usuarios
    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuário por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    public ResponseEntity<Usuario> buscarPorId(@PathVariable int id) {

        Usuario usuario = usuarioService.buscarPorID(id);

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar usuário por email")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    public ResponseEntity<Usuario> buscarPorEmail(@PathVariable String email) {

        Usuario usuario = usuarioService.buscarPorEmail(email);

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/matricula/{matricula}")
    @Operation(summary = "Buscar usuário por matrícula")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    public ResponseEntity<Usuario> buscarPorMatricula(@PathVariable String matricula) {

        Usuario usuario = usuarioService.buscarPorMatricula(matricula);

        return ResponseEntity.ok(usuario);
    }

    @GetMapping("/nome")
    @Operation(summary = "Filtrar usuários por nome")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.")
    })
    public ResponseEntity<List<Usuario>> filtrarPorNome(@RequestParam String nome) {

        return ResponseEntity.ok(usuarioService.filtrarPorNome(nome));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Filtrar usuários por período de cadastro")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.")
    })
    public ResponseEntity<List<Usuario>> filtrarPorPeriodo(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataInicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataFim) {

        return ResponseEntity.ok(
                usuarioService.filtrarPorPeriodo(dataInicio, dataFim)
        );
    }

    @GetMapping("/tipo")
    @Operation(summary = "Filtrar usuários por tipo")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.")
    })
    public ResponseEntity<List<Usuario>> filtrarPorTipoUsuario(
            @RequestParam String tipoUsuario) {

        return ResponseEntity.ok(usuarioService.filtrarPorTipoUsuario(tipoUsuario));
    }

    @GetMapping("/idade")
    @Operation(summary = "Filtrar usuários por idade")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca realizada com sucesso.")
    })
    public ResponseEntity<List<Usuario>> filtrarPorIdade(@RequestParam int idade) {

        return ResponseEntity.ok(usuarioService.filtrarPorIdade(idade));
    }

    @GetMapping("/ativos")
    @Operation(summary = "Listar usuários ativos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários ativos retornados com sucesso.")
    })
    public ResponseEntity<List<Usuario>> listarAtivos() {

        return ResponseEntity.ok(usuarioService.listarAtivos());
    }

    @GetMapping("/desativados")
    @Operation(summary = "Listar usuários desativados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários desativados retornados com sucesso.")
    })
    public ResponseEntity<List<Usuario>> listarDesativados() {

        return ResponseEntity.ok(usuarioService.listarDesativados());
    }

    @GetMapping
    @Operation(summary = "Listar todos os usuários")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso.")
    })
    public ResponseEntity<List<Usuario>> listarTodos() {

        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    //Metodos de atualizar o usuario
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "409", description = "Email já cadastrado.")
    })
    public ResponseEntity<String> atualizar(
            @PathVariable int id,
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate dataNascimento) {

        usuarioService.atualizarUsuario(id, nome, email, dataNascimento);

        return ResponseEntity.ok("Usuário atualizado com sucesso!");
    }

    @PatchMapping("/{id}/ativar")
    @Operation(summary = "Ativar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário ativado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "409", description = "Usuário já está ativado.")
    })
    public ResponseEntity<String> ativar(@PathVariable int id) {

        usuarioService.ativarUsuario(id);

        return ResponseEntity.ok("Usuário ativado com sucesso!");
    }

    @PatchMapping("/{id}/desativar")
    @Operation(summary = "Desativar usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário desativado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "409", description = "Usuário já está desativado.")
    })
    public ResponseEntity<String> desativar(@PathVariable int id) {

        usuarioService.desativarUsuario(id);

        return ResponseEntity.ok("Usuário desativado com sucesso!");
    }

    @PatchMapping("/{id}/penalidade/adicionar")
    @Operation(summary = "Adicionar penalidade")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Penalidade adicionada com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "409", description = "Usuário já possui penalidade.")
    })
    public ResponseEntity<String> adicionarPenalidade(@PathVariable int id) {

        usuarioService.adicionarPenalidade(id);

        return ResponseEntity.ok("Penalidade adicionada ao usuário com sucesso!");
    }

    @PatchMapping("/{id}/penalidade/remover")
    @Operation(summary = "Remover penalidade")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Penalidade removida com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado."),
            @ApiResponse(responseCode = "409", description = "Usuário não possui penalidade.")
    })
    public ResponseEntity<String> removerPenalidade(@PathVariable int id) {

        usuarioService.removerPenalidade(id);

        return ResponseEntity.ok("Penalidade removida do usuário com sucesso!");
    }

    @PatchMapping("/{id}/senha")
    @Operation(summary = "Alterar senha")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Senha alterada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Senha atual incorreta ou nova senha inválida."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    public ResponseEntity<String> alterarSenha(
            @PathVariable int id,
            @RequestParam String senhaAtual,
            @RequestParam String novaSenha) {

        usuarioService.alterarSenha(id, senhaAtual, novaSenha);

        return ResponseEntity.ok("Senha alterada com sucesso!");
    }

    // Metodo de deletar o usuario

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário removido com sucesso."),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    })
    public ResponseEntity<String> remover(@PathVariable int id) {

        usuarioService.removerUsuario(id);

        return ResponseEntity.ok("Usuário removido com sucesso!");
    }
}