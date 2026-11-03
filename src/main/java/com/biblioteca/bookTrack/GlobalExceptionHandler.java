
package com.biblioteca.bookTrack;

import com.biblioteca.bookTrack.usuariosException.RecursoJaCadastradoException;
import com.biblioteca.bookTrack.usuariosException.UsuarioNaoEncontradoException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<String> usuarioNaoEncontrado(UsuarioNaoEncontradoException erro) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> argumentoIlegal(
            IllegalArgumentException erro) {

        return ResponseEntity.badRequest().body(erro.getMessage());
    }

    @ExceptionHandler(RecursoJaCadastradoException.class)
    public ResponseEntity<String> recursoJaCadastrado(RecursoJaCadastradoException erro) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity< String> estadoIlegal(IllegalStateException erro) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body( erro.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> erroValidacao(MethodArgumentNotValidException erro) {

        String mensagem = erro.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce((a, b) -> a + "\n" + b).orElse("Erro de validação");

        return ResponseEntity.badRequest().body(mensagem);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> erroLeituraJson(HttpMessageNotReadableException erro) {

        return ResponseEntity.badRequest().body("Erro ao interpretar o JSON: " + erro.getMostSpecificCause().getMessage());
    }
}
