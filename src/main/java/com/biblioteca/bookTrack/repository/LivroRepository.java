
package com.biblioteca.bookTrack.repository;

import com.biblioteca.bookTrack.model.Livro;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LivroRepository extends BaseRepository<Livro> {

    public LivroRepository(DynamoDbEnhancedClient client) {
        super(client.table("Livro", TableSchema.fromBean(Livro.class)));
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
            if (livro.getAutor() != null && livro.getAutor().equalsIgnoreCase(autor)) {
                livros.add(livro);
            }
        }

        return livros;
    }
}
