
package com.biblioteca.bookTrack.repository;

import com.biblioteca.bookTrack.model.Emprestimo;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EmprestimoRepository extends BaseRepository<Emprestimo> {

    public EmprestimoRepository(DynamoDbEnhancedClient client) {
        super(client.table("Emprestimo", TableSchema.fromBean(Emprestimo.class)));
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
