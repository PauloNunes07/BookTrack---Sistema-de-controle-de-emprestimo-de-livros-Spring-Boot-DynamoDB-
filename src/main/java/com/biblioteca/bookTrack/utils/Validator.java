package com.biblioteca.bookTrack.utils;

import java.time.LocalDate;

public class Validator {

    public static <T> void validarObjeto(T objeto, String nome) {
        if (objeto == null) {
            throw new IllegalArgumentException(
                    nome + " não pode ser nulo."
            );
        }
    }

    public static void validarString(String texto, String entidade) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException(
                    entidade + " não pode ser nulo ou vazio."
            );
        }
    }

    public static void validarIdade(int idade) {
        if (idade < 1) {
            throw new IllegalArgumentException(
                    "A idade deve ser maior que zero."
            );
        }
    }

    public static TipoUsuario converterTipoUsuario(String tipoUsuario) {
        validarString(tipoUsuario, "Tipo de usuário");

        try {
            return TipoUsuario.valueOf(tipoUsuario.trim().toUpperCase());

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + tipoUsuario);
        }

    }

    public static void validarData(LocalDate data) {
        if(data == null) {
            throw new IllegalArgumentException("A data não pode ser nula.");
        }
    }

    public static void validarDataNascimento(LocalDate dataNascimento) {
        validarData(dataNascimento);
        if(dataNascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento não pode ser futura.");

        }
    }

    public static void validarPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        validarData(dataInicio);
        validarData(dataFim);

        if(dataInicio.isAfter(dataFim)) {
            throw new IllegalArgumentException("A data inicial não pode ser posterior à data final.");
        }
    }
}