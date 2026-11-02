package com.biblioteca.bookTrack.dto;

import com.biblioteca.bookTrack.utils.TipoUsuario;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UsuarioDTO {

    @NotBlank(message = "Nome obrigatório.")
    @Size(min = 4, max = 100, message = "O nome deve ter entre 4 e 100 caracteres.")
    @Pattern(
            regexp = "^\\p{L}+([ '\\-]\\p{L}+)+$",
            message = "Informe nome e sobrenome, apenas com letras."
    )
    private String nome;

    @NotBlank(message = "Email obrigatório.")
    @Email(message = "Email informado em formato inválido.")
    @Size(max = 150, message = "O email deve ter no máximo 150 caracteres.")
    private String email;

    @NotBlank(message = "Senha obrigatória.")
    @Size(min = 8, max = 64, message = "A senha deve ter entre 8 e 64 caracteres.")
    private String senha;

    @NotNull(message = "Data de nascimento obrigatória.")
    @Past(message = "A data de nascimento deve estar no passado.")
    private LocalDate dataNascimento;

    @NotBlank(message = "Matricula obrigatoria.")
    @Pattern(regexp = "^\\d{12}$",
            message = "A matrícula deve conter exatamente 12 números.")
    private String matricula;

    @NotNull(message = "O tipo de usuário é obrigatório.")
    private TipoUsuario tipoUsuario;

}