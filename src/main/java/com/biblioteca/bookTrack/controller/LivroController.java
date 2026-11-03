
package com.biblioteca.bookTrack.controller;

import com.biblioteca.bookTrack.dto.LivroDTO;
import com.biblioteca.bookTrack.model.Livro;
import com.biblioteca.bookTrack.service.LivroService;

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
@RequestMapping("/livros")
@Tag(name = "Livro", description = "Gerenciamento de livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<String> cadastrarLivro(@Valid @RequestBody LivroDTO livroDTO) {

        Livro livro = converterParaLivro(livroDTO);

        livroService.cadastrarLivro(livro);

        return ResponseEntity.status(HttpStatus.CREATED).body("Livro cadastrado com sucesso!");
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar livro por ID")
    public ResponseEntity<Livro> consultarLivro(@PathVariable Integer id) {

        Livro livro = livroService.consultarLivro(id);

        return ResponseEntity.ok(livro);
    }

    @GetMapping("/titulo")
    @Operation(summary = "Buscar livros por título")
    public ResponseEntity<List<Livro>> buscarPorTitulo(@RequestParam String titulo) {

        List<Livro> livros = livroService.buscarPorTitulo(titulo);

        return ResponseEntity.ok(livros);
    }

    @GetMapping("/autor")
    @Operation(summary = "Buscar livros por autor")
    public ResponseEntity<List<Livro>> buscarPorAutor(@RequestParam String autor) {

        List<Livro> livros = livroService.buscarPorAutor(autor);

        return ResponseEntity.ok(livros);
    }

    @GetMapping("/disponibilidade/{id}")
    @Operation(summary = "Verificar disponibilidade do livro")
    public ResponseEntity<Boolean> verificarDisponibilidade(@PathVariable Integer id) {

        boolean disponivel = livroService.verificarDisponibilidade(id);

        return ResponseEntity.ok(disponivel);
    }

    @GetMapping
    @Operation(summary = "Listar todos os livros")
    public ResponseEntity<List<Livro>> listarLivros() {

        List<Livro> livros = livroService.listarLivros();

        return ResponseEntity.ok(livros);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    public ResponseEntity<String> atualizarLivro(@PathVariable Integer id, @Valid @RequestBody LivroDTO livroDTO) {

        Livro livro = converterParaLivro(livroDTO);

        livroService.atualizarLivro(id, livro);

        return ResponseEntity.ok("Livro atualizado com sucesso!");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir livro")
    public ResponseEntity<String> excluirLivro(@PathVariable Integer id) {

        livroService.excluirLivro(id);

        return ResponseEntity.ok("Livro excluído com sucesso!");
    }

    private Livro converterParaLivro(LivroDTO livroDTO) {

        Livro livro = new Livro();

        livro.setNome(livroDTO.getNome());
        livro.setAutor(livroDTO.getAutor());
        livro.setCategoria(livroDTO.getCategoria());
        livro.setFaixaEtaria(livroDTO.getFaixaEtaria());
        livro.setAnoPublicacao(livroDTO.getAnoPublicacao());
        livro.setStatus(livroDTO.getStatus());
        livro.setIdioma(livroDTO.getIdioma());
        livro.setSinopse(livroDTO.getSinopse());
        livro.setQuantidadeDisponivel(livroDTO.getQuantidadeDisponivel());

        return livro;
    }
}
