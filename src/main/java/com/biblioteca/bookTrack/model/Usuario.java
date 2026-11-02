package com.biblioteca.bookTrack.model;

import com.biblioteca.bookTrack.utils.TipoUsuario;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;

import com.biblioteca.bookTrack.utils.ConversorTipoUsuario;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDate;
import java.time.LocalDateTime;

@DynamoDbBean
@NoArgsConstructor
@Getter
@Setter
public class Usuario {
    private int id;

    private String nome;

    private String email;

    private String senha;

    private LocalDate dataNascimento;

    private String matricula;

    private boolean ativo;

    private TipoUsuario tipoUsuario;

   private boolean penalidade;

   private LocalDate dataCadastro;

    @DynamoDbPartitionKey
    public int getId() {
        return this.id;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "email-index")
    public String getEmail() {
        return this.email;
    }

    @DynamoDbSecondaryPartitionKey(indexNames = "matricula-index")
    public String getMatricula() {
        return this.matricula;
    }


    @DynamoDbConvertedBy(ConversorTipoUsuario.class)
    public TipoUsuario getTipoUsuario() {
        return this.tipoUsuario;
    }
}
