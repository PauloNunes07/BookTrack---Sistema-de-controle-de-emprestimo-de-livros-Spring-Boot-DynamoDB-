
package com.biblioteca.bookTrack.repository;

import com.biblioteca.bookTrack.model.Livro;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LivroRepository {

    private final DynamoDbTable<Livro> tabela;

    public LivroRepository(DynamoDbEnhancedClient enhancedClient) {
        this.tabela = enhancedClient.table(
                "Livro",
                TableSchema.fromBean(Livro.class)
        );
    }

    public void salvar(Livro livro) {
        tabela.putItem(livro);
    }

    public Livro buscarPorId(Integer id) {

        Key chave = Key.builder()
                .partitionValue(id)
                .build();

        return tabela.getItem(chave);
    }

    public List<Livro> listarTodos() {

        List<Livro> livros = new ArrayList<>();

        for (Livro livro : tabela.scan().items()) {
            livros.add(livro);
        }

        return livros;
    }

    public void atualizar(Livro livro) {
        tabela.updateItem(livro);
    }

    public void excluir(Integer id) {

        Key chave = Key.builder().partitionValue(id).build();

        tabela.deleteItem(chave);
    }

    public List<Livro> buscarPorTitulo(String titulo) {

        List<Livro> livros = new ArrayList<>();

        for (Livro livro : tabela.scan().items()) {
            if (livro.getNome() != null &&
                    livro.getNome().equalsIgnoreCase(titulo)) {
                livros.add(livro);
            }
        }

        return livros;
    }

    public List<Livro> buscarPorAutor(String autor) {

        List<Livro> livros = new ArrayList<>();

        for (Livro livro : tabela.scan().items()) {
            if (livro.getAutor() != null &&
                    livro.getAutor().equalsIgnoreCase(autor)) {
                livros.add(livro);
            }
        }

        return livros;
    }
}
