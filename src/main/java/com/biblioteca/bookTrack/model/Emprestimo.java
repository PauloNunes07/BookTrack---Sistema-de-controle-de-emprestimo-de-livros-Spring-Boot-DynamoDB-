
package com.biblioteca.bookTrack.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import com.biblioteca.bookTrack.utils.StatusEmprestimo;

import java.time.LocalDate;

@DynamoDbBean
public class Emprestimo {

    private Integer id;

    private Integer livroID;

    private Integer usuarioID;

    private LocalDate dataInicial;

    private LocalDate dataDevolucao;

    private StatusEmprestimo statusEmprestimo;

    public Emprestimo() {
    }

    @DynamoDbPartitionKey
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLivroID() {
        return livroID;
    }

    public void setLivroID(Integer livroID) {
        this.livroID = livroID;
    }

    public Integer getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(Integer usuarioID) {
        this.usuarioID = usuarioID;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public StatusEmprestimo getStatusEmprestimo() {
        return statusEmprestimo;
    }

    public void setStatusEmprestimo(StatusEmprestimo statusEmprestimo) {
        this.statusEmprestimo = statusEmprestimo;
    }
}
