
package com.biblioteca.bookTrack.repository;

import com.biblioteca.bookTrack.model.Emprestimo;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmprestimoRepository {

    private final DynamoDbTable<Emprestimo> tabela;

    public EmprestimoRepository(DynamoDbEnhancedClient enhancedClient) {

        this.tabela = enhancedClient.table(
                "Emprestimo",
                TableSchema.fromBean(Emprestimo.class)
        );
    }



    public void salvar(Emprestimo emprestimo) {

        tabela.putItem(emprestimo);
    }



    public Emprestimo buscarPorId(Integer id) {

        Key chave = Key.builder().partitionValue(id).build();

        return tabela.getItem(chave);
    }



    public List<Emprestimo> listarTodos() {

        List<Emprestimo> emprestimos = new ArrayList<>();

        for (Emprestimo emprestimo : tabela.scan().items()) {
            emprestimos.add(emprestimo);
        }

        return emprestimos;
    }



    public void atualizar(Emprestimo emprestimo) {

        tabela.updateItem(emprestimo);
    }



    public void excluir(Integer id) {

        Key chave = Key.builder().partitionValue(id).build();

        tabela.deleteItem(chave);
    }



    public List<Emprestimo> listarPorEstudante(Integer estudanteId) {

        List<Emprestimo> emprestimos = new ArrayList<>();

        for (Emprestimo emprestimo : tabela.scan().items()) {

            if (emprestimo.getUsuarioID().equals(estudanteId)) {
                emprestimos.add(emprestimo);
            }
        }

        return emprestimos;
    }



    public List<Emprestimo> listarPorLivro(Integer livroId) {

        List<Emprestimo> emprestimos = new ArrayList<>();

        for (Emprestimo emprestimo : tabela.scan().items()) {

            if (emprestimo.getLivroID().equals(livroId)) {
                emprestimos.add(emprestimo);
            }
        }

        return emprestimos;
    }
}
