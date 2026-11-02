package com.biblioteca.bookTrack.exception.livroExceptions;

public class LivroJaExistenteException extends RuntimeException {
    public LivroJaExistenteException(String message) {
        super(message);
    }
}
