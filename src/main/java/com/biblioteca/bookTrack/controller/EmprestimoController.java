
package com.biblioteca.bookTrack.controller;

import com.biblioteca.bookTrack.dto.EmprestimoDTO;
import com.biblioteca.bookTrack.model.Emprestimo;
import com.biblioteca.bookTrack.service.EmprestimoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
@Tag(name = "Empréstimo", description = "Gerenciamento de empréstimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    @Operation(summary = "Registrar empréstimo")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Empréstimo registrado com sucesso"), @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<String> registrarEmprestimo(@Valid @RequestBody EmprestimoDTO emprestimoDTO) {

        Emprestimo emprestimo = converterParaEmprestimo(emprestimoDTO);

        emprestimoService.registrarEmprestimo(emprestimo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Empréstimo registrado com sucesso!");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar empréstimo por ID")
    public ResponseEntity<Emprestimo> consultarEmprestimo(@PathVariable Integer id) {

        Emprestimo emprestimo = emprestimoService.consultarEmprestimo(id);

        return ResponseEntity.ok(emprestimo);
    }

    @GetMapping
    @Operation(summary = "Listar todos os empréstimos")
    public ResponseEntity<List<Emprestimo>> listarEmprestimos() {

        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimos();

        return ResponseEntity.ok(emprestimos);
    }

    @PostMapping("/{id}/devolucao")
    @Operation(summary = "Registrar devolução de empréstimo")
    public ResponseEntity<String> registrarDevolucao(@PathVariable Integer id) {

        emprestimoService.registrarDevolucao(id);

        return ResponseEntity.ok("Devolução registrada com sucesso!");
    }

    @PostMapping("/{id}/cancelamento")
    @Operation(summary = "Cancelar empréstimo")
    public ResponseEntity<String> cancelarEmprestimo(@PathVariable Integer id) {

        emprestimoService.cancelarEmprestimo(id);

        return ResponseEntity.ok("Empréstimo cancelado com sucesso!");
    }

    @GetMapping("/{id}/atraso")
    @Operation(summary = "Verificar atraso de empréstimo")
    public ResponseEntity<Boolean> verificarAtraso(@PathVariable Integer id) {

        boolean atrasado = emprestimoService.verificarAtraso(id);

        return ResponseEntity.ok(atrasado);
    }

    @GetMapping("/estudante/{usuarioID}")
    @Operation(summary = "Listar empréstimos por estudante")
    public ResponseEntity<List<Emprestimo>> listarPorEstudante(@PathVariable Integer usuarioID) {

        List<Emprestimo> emprestimos =
                emprestimoService.listarPorEstudante(usuarioID);

        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/livro/{livroID}")
    @Operation(summary = "Listar empréstimos por livro")
    public ResponseEntity<List<Emprestimo>> listarEmprestimosPorLivro(
            @PathVariable Integer livroID) {

        List<Emprestimo> emprestimos =
                emprestimoService.listarEmprestimosPorLivro(livroID);

        return ResponseEntity.ok(emprestimos);
    }

    private Emprestimo converterParaEmprestimo(EmprestimoDTO emprestimoDTO) {

        Emprestimo emprestimo = new Emprestimo();

        emprestimo.setLivroID(emprestimoDTO.getLivroID());
        emprestimo.setUsuarioID(emprestimoDTO.getUsuarioID());
        emprestimo.setDataInicial(emprestimoDTO.getDataInicial());
        emprestimo.setDataDevolucao(emprestimoDTO.getDataDevolucao());
        emprestimo.setStatusEmprestimo(emprestimoDTO.getStatusEmprestimo());

        return emprestimo;
    }
}
