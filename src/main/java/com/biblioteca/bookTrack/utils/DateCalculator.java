package com.biblioteca.bookTrack.utils;


import java.time.LocalDate;
import java.time.Period;

public class DateCalculator {

    public static int calcularIdade(LocalDate dataNascimento) {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }
}
