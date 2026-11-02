package com.biblioteca.bookTrack.utils;

public class IdOperator {

   static int contadorUsuario = 0;
   static int contadorLivro = 0;
   static int contadorEmprestimo = 0;

    String[] possibilidades = {"usuario", "livro", "emprestimo"};

    public static void validarId(int id) {
        if(id < 1) {
            throw new IllegalArgumentException("O ID deve ser maior que zero.");
        }
    }

    public static int gerarId(String texto) {
        switch (texto.toLowerCase()) {
            case "usuario":
                return ++contadorUsuario;

            case "livro":
                return ++contadorLivro;

            case "emprestimo":
                return ++contadorEmprestimo;

            default:
                throw new IllegalArgumentException("Entidade inválida.");
        }
    }
}
