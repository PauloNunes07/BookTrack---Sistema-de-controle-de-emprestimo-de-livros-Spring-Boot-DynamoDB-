package com.biblioteca.bookTrack.repository;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.util.List;

public abstract class BaseRepository<T> {
    protected final DynamoDbTable<T> tabela;


    public BaseRepository(DynamoDbTable<T> tabela) {
        this.tabela = tabela;
    }

    public void salvar(T objeto) {
       this.tabela.putItem(objeto);
    }

    public void remover(int id) {
        Key chave = Key.builder().partitionValue(id).build();

        this.tabela.deleteItem(chave);
    }

    public void atualizar(T objeto) {
        tabela.updateItem(objeto);
    }

    public T buscarPorId(int id) {
        Key chave = Key.builder().partitionValue(id).build();

        return tabela.getItem(chave);
    }

    public List<T> listarTodos() {
        return tabela.scan().items().stream().toList();
    }

}
