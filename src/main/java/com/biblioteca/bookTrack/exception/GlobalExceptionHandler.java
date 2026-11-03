
package com.biblioteca.bookTrack.exception;

import com.biblioteca.bookTrack.exception.emprestimoExceptions.EmprestimoInvalidoException;
import com.biblioteca.bookTrack.exception.emprestimoExceptions.EmprestimoNaoEncontradoException;
import com.biblioteca.bookTrack.exception.livroExceptions.*;
import com.biblioteca.bookTrack.exception.usuariosException.RecursoJaCadastradoException;
import com.biblioteca.bookTrack.exception.usuariosException.UsuarioNaoEncontradoException;

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

    @ExceptionHandler(RecursoJaCadastradoException.class)
    public ResponseEntity<String> recursoJaCadastrado(RecursoJaCadastradoException erro) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro.getMessage());
    }

    @ExceptionHandler(EmprestimoNaoEncontradoException.class)
    public ResponseEntity<String> emprestimoNaoEncontrado(EmprestimoNaoEncontradoException erro) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro.getMessage());
    }

    @ExceptionHandler(EmprestimoInvalidoException.class)
    public ResponseEntity<String> emprestimoInvalido(EmprestimoInvalidoException erro) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro.getMessage());
    }

    @ExceptionHandler(LivroIndisponivelException.class)
    public ResponseEntity<String> livroIndisponivel(LivroIndisponivelException erro) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro.getMessage());
    }

    @ExceptionHandler(LivroNaoEncontradoException.class)
    public ResponseEntity<String> livroNaoEncontrado(LivroNaoEncontradoException erro) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro.getMessage());
    }

    @ExceptionHandler(LivroIdInvalidoException.class)
    public ResponseEntity<String> livroIdInvalido(LivroIdInvalidoException erro) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro.getMessage());
    }

    @ExceptionHandler(AnoPublicacaoInvalidoException.class)
    public ResponseEntity<String> anoPublicacaoInvalido(AnoPublicacaoInvalidoException erro) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro.getMessage());
    }

    @ExceptionHandler(LivroComEmprestimoAtivoException.class)
    public ResponseEntity<String> livroComEmprestimoAtivo(LivroComEmprestimoAtivoException erro) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> argumentoInvalido(IllegalArgumentException erro) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro.getMessage());
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
    public ResponseEntity<String> erroLeituraJson(
            HttpMessageNotReadableException erro) {

        String mensagem = "Erro ao interpretar o JSON. Verifique os valores enviados.";

        return ResponseEntity.badRequest().body(mensagem);}
    }
