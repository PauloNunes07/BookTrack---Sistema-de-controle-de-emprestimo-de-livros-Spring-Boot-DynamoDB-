
package com.biblioteca.bookTrack;

import com.biblioteca.bookTrack.model.Livro;
import com.biblioteca.bookTrack.service.LivroService;
import com.biblioteca.bookTrack.utils.TipoStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LivroServiceTeste {

    @Autowired
    private LivroService livroService;

    @Test
    void testarMetodosLivro() {

        // 1. Criar livro
        Livro livro = new Livro();

        livro.setNome("Livro Teste DynamoDB");
        livro.setAutor("Autor Teste");
        livro.setCategoria("Tecnologia");
        livro.setFaixaEtaria(12);
        livro.setAnoPublicacao(2024);
        livro.setIdioma("Português");
        livro.setSinopse("Livro utilizado para testar o DynamoDB.");
        livro.setQuantidadeDisponivel(5);
        livro.setStatus(null);

        // 2. Cadastrar livro
        livroService.cadastrarLivro(livro);

        assertNotNull(livro.getId());
        assertTrue(livro.getId() > 0);

        Integer id = livro.getId();

        System.out.println("Livro cadastrado. ID: " + id);

        // 3. Consultar livro
        Livro encontrado = livroService.consultarLivro(id);

        assertEquals("Livro Teste DynamoDB", encontrado.getNome());

        System.out.println("Consulta realizada com sucesso.");

        // 4. Listar livros
        assertFalse(livroService.listarLivros().isEmpty());

        System.out.println("Listagem realizada com sucesso.");

        // 5. Buscar por título e autor
        assertFalse(livroService.buscarPorTitulo("Livro Teste DynamoDB").isEmpty());
        assertFalse(livroService.buscarPorAutor("Autor Teste").isEmpty());

        System.out.println("Buscas por título e autor realizadas.");

        // 6. Verificar disponibilidade
        assertTrue(livroService.verificarDisponibilidade(id));

        System.out.println("Livro disponível.");

        // 7. Atualizar livro
        livro.setNome("Livro Teste Atualizado");
        livro.setQuantidadeDisponivel(3);

        livroService.atualizarLivro(id, livro);

        Livro atualizado = livroService.consultarLivro(id);

        assertEquals("Livro Teste Atualizado", atualizado.getNome());
        assertEquals(3, atualizado.getQuantidadeDisponivel());

        System.out.println("Atualização realizada com sucesso.");

        // 8. Excluir livro
        livroService.excluirLivro(id);

        System.out.println("Livro excluído com sucesso.");

    }
}
