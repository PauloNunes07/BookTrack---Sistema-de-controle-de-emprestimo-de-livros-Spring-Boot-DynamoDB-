package com.biblioteca.bookTrack.repository;

import com.biblioteca.bookTrack.model.Usuario;
import com.biblioteca.bookTrack.utils.DateCalculator;
import com.biblioteca.bookTrack.utils.TipoUsuario;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.time.LocalDate;
import java.util.List;

@Repository
public class UsuarioRepository extends BaseRepository <Usuario> {
    public UsuarioRepository(DynamoDbEnhancedClient client) {
        super(client.table("Usuario", TableSchema.fromBean(Usuario.class)));
    }

    public Usuario buscarPorEmail(String email) {
        return tabela.index("email-index")
                .query(r -> r.queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(email)
                        .build()))).stream()
                .flatMap(pagina -> pagina.items().stream())
                .findFirst()
                .orElse(null);
    }


    public Usuario buscarPorMatricula(String matricula) {
        return tabela.index("matricula-index")
                .query(r -> r.queryConditional(QueryConditional.keyEqualTo(Key.builder().partitionValue(matricula)
                        .build()))).stream()
                .flatMap(pagina -> pagina.items().stream())
                .findFirst()
                .orElse(null);
    }

    public List<Usuario> filtrarPorIdade(int idade) {
        return tabela.scan().items().stream()
                .filter(usuario -> DateCalculator.calcularIdade(usuario.getDataNascimento()) == idade).toList();
    }

    public List<Usuario> filtrarPorTipoUsuario(TipoUsuario tipoUsuario) {

        System.out.println("Pesquisando por: " + tipoUsuario);

        List<Usuario> usuarios = tabela.scan().items().stream().toList();

        for (Usuario usuario : usuarios) {
            System.out.println(
                    "ID: " + usuario.getId()
                            + " | Nome: " + usuario.getNome()
                            + " | Tipo: " + usuario.getTipoUsuario()
            );
        }

        return usuarios.stream()
                .filter(usuario -> usuario.getTipoUsuario() == tipoUsuario)
                .toList();
    }

    public List<Usuario> buscarAtivos() {
        return tabela.scan().items().stream()
                .filter(usuario -> usuario.isAtivo()).toList();
    }

    public List<Usuario> buscarDesativados() {
        return tabela.scan().items().stream()
                .filter(usuario -> !usuario.isAtivo()).toList();
    }

    public List<Usuario> filtrarPorNome(String nome) {
        return tabela.scan().items().stream()
                .filter(usuario -> usuario.getNome().toLowerCase().contains(nome.toLowerCase())).toList();
    }

    public List<Usuario> filtrarPorPeriodo(LocalDate inicio, LocalDate fim) {
        return tabela.scan().items().stream()
                .filter(usuario -> !usuario.getDataCadastro()
                        .isBefore(inicio) && !usuario.getDataCadastro().isAfter(fim)).toList();
    }

}
