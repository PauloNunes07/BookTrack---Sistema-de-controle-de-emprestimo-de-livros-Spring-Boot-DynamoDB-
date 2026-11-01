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
    private String id;

    private String nome;

    private String email;

    private String senha;

    private String matricula;

    private boolean ativo;

    private LocalDate dataNascimento;

    private TipoUsuario tipoUsuario;

   private Penalidade penalidade;

    @DynamoDbPartitionKey
    public String getId() {
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
