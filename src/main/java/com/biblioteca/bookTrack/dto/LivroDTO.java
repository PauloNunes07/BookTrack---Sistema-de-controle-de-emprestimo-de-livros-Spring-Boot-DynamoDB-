
package com.biblioteca.bookTrack.dto;

import com.biblioteca.bookTrack.utils.TipoStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LivroDTO {

    @NotBlank(message = "Nome do livro obrigatório.")
    @Size(min = 2, max = 150, message = "O nome deve ter entre 2 e 150 caracteres.")
    private String nome;

    @NotBlank(message = "Autor obrigatório.")
    @Size(min = 3, max = 100, message = "O nome do autor deve ter entre 3 e 100 caracteres.")
    private String autor;

    @NotBlank(message = "Categoria obrigatória.")
    @Size(max = 50, message = "A categoria deve ter no máximo 50 caracteres.")
    private String categoria;

    @NotNull(message = "Faixa etária obrigatória.")
    @Min(value = 0, message = "A faixa etária não pode ser negativa.")
    @Max(value = 18, message = "A faixa etária deve ser de até 18 anos.")
    private Integer faixaEtaria;

    @NotNull(message = "Ano de publicação obrigatório.")
    @Min(value = 1, message = "Ano de publicação inválido.")
    @Max(value = 2026, message = "Ano de publicação não pode ser futuro.")
    private Integer anoPublicacao;

    @NotNull(message = "Status do livro obrigatório.")
    private TipoStatus status;

    @NotBlank(message = "Idioma obrigatório.")
    @Size(max = 50, message = "O idioma deve ter no máximo 50 caracteres.")
    private String idioma;

    @NotBlank(message = "Sinopse obrigatória.")
    @Size(max = 2000, message = "A sinopse deve ter no máximo 2000 caracteres.")
    private String sinopse;

    @NotNull(message = "Quantidade disponível obrigatória.")
    @PositiveOrZero(message = "A quantidade disponível não pode ser negativa.")
    private Integer quantidadeDisponivel;

}
