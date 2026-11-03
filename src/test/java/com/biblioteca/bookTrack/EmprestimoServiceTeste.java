
package com.biblioteca.bookTrack;

import com.biblioteca.bookTrack.model.Emprestimo;
import com.biblioteca.bookTrack.model.Livro;
import com.biblioteca.bookTrack.service.EmprestimoService;
import com.biblioteca.bookTrack.service.LivroService;
import com.biblioteca.bookTrack.utils.StatusEmprestimo;
import com.biblioteca.bookTrack.utils.TipoStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EmprestimoServiceTeste {

    @Autowired
    private EmprestimoService emprestimoService;

    @Autowired
    private LivroService livroService;

    @Test
    void testarMetodosEmprestimo() {

        // 1. Cadastrar um livro para o empréstimo
        Livro livro = new Livro();

        livro.setNome("Livro Para Emprestimo");
        livro.setAutor("Autor Teste");
        livro.setCategoria("Tecnologia");
        livro.setFaixaEtaria(12);
        livro.setAnoPublicacao(2024);
        livro.setIdioma("Português");
        livro.setSinopse("Livro para testar empréstimos.");
        livro.setQuantidadeDisponivel(5);
        livro.setStatus(TipoStatus.DISPONIVEL);

        livroService.cadastrarLivro(livro);

        Integer livroID = livro.getId();

        System.out.println("Livro cadastrado. ID: " + livroID);

        // 2. Criar empréstimo
        Emprestimo emprestimo = new Emprestimo();

        emprestimo.setLivroID(livroID);
        emprestimo.setUsuarioID(1);

        // 3. Registrar empréstimo
        emprestimoService.registrarEmprestimo(emprestimo);

        assertNotNull(emprestimo.getId());
        assertTrue(emprestimo.getId() > 0);
        assertEquals(StatusEmprestimo.ATIVO, emprestimo.getStatusEmprestimo());

        Integer emprestimoID = emprestimo.getId();

        System.out.println("Empréstimo registrado. ID: " + emprestimoID);

        // 4. Consultar empréstimo
        Emprestimo encontrado = emprestimoService.consultarEmprestimo(emprestimoID);

        assertEquals(livroID, encontrado.getLivroID());
        assertEquals(1, encontrado.getUsuarioID());

        System.out.println("Consulta realizada com sucesso.");

        // 5. Listar empréstimos
        assertFalse(emprestimoService.listarEmprestimos().isEmpty());

        System.out.println("Listagem realizada com sucesso.");

        // 6. Buscar por estudante
        assertFalse(emprestimoService.listarPorEstudante(1).isEmpty());

        System.out.println("Busca por estudante realizada.");

        // 7. Buscar por livro
        assertFalse(emprestimoService.listarEmprestimosPorLivro(livroID).isEmpty());

        System.out.println("Busca por livro realizada.");

        // 8. Verificar disponibilidade do livro após empréstimo
        Livro livroAtualizado = livroService.consultarLivro(livroID);

        assertEquals(4, livroAtualizado.getQuantidadeDisponivel());

        System.out.println("Quantidade disponível atualizada corretamente.");

        // 9. Verificar atraso
        assertFalse(emprestimoService.verificarAtraso(emprestimoID));

        System.out.println("Verificação de atraso realizada.");

        // 10. Registrar devolução
        emprestimoService.registrarDevolucao(emprestimoID);

        Emprestimo devolvido = emprestimoService.consultarEmprestimo(emprestimoID);

        assertEquals(StatusEmprestimo.DEVOLVIDO, devolvido.getStatusEmprestimo());
        assertNotNull(devolvido.getDataDevolucao());

        System.out.println("Devolução registrada com sucesso.");

        // 11. Verificar quantidade após devolução
        livroAtualizado = livroService.consultarLivro(livroID);

        assertEquals(5, livroAtualizado.getQuantidadeDisponivel());

        System.out.println("Quantidade restaurada corretamente.");

        // 12. Excluir livro de teste
        livroService.excluirLivro(livroID);

        System.out.println("Livro de teste excluído com sucesso.");
    }
}
