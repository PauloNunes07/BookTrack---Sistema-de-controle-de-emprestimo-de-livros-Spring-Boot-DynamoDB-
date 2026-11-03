
package com.biblioteca.bookTrack.dto;

import com.biblioteca.bookTrack.utils.StatusEmprestimo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class EmprestimoDTO {

    @NotNull(message = "ID do empréstimo obrigatório.")
    @Positive(message = "O ID deve ser maior que zero.")
    private Integer id;

    @NotNull(message = "ID do livro obrigatório.")
    @Positive(message = "O ID do livro deve ser maior que zero.")
    private Integer livroID;

    @NotNull(message = "ID do usuário obrigatório.")
    @Positive(message = "O ID do usuário deve ser maior que zero.")
    private Integer usuarioID;

    private LocalDate dataInicial;

    private LocalDate dataDevolucao;

    private StatusEmprestimo statusEmprestimo;
}
