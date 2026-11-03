
package com.biblioteca.bookTrack.service;

import com.biblioteca.bookTrack.exception.livroExceptions.*;
import com.biblioteca.bookTrack.model.Livro;
import com.biblioteca.bookTrack.model.Emprestimo;
import com.biblioteca.bookTrack.repository.LivroRepository;
import com.biblioteca.bookTrack.repository.EmprestimoRepository;
import com.biblioteca.bookTrack.utils.TipoStatus;
import com.biblioteca.bookTrack.utils.StatusEmprestimo;

import org.springframework.stereotype.Service;
import com.biblioteca.bookTrack.utils.IdGenerator;

import java.time.Year;
import java.util.List;

@Service
public class LivroService {

    private final LivroRepository livroRepository;
    private final EmprestimoRepository emprestimoRepository;
    private final IdGenerator idGenerator;

    public LivroService(LivroRepository livroRepository, EmprestimoRepository emprestimoRepository, IdGenerator idGenerator) {

        this.livroRepository = livroRepository;
        this.emprestimoRepository = emprestimoRepository;
        this.idGenerator = idGenerator;
    }

    public void cadastrarLivro(Livro livro) {

        validarAnoPublicacao(livro.getAnoPublicacao());
        validarQuantidadeDisponivel(livro.getQuantidadeDisponivel());

        if (livro.getStatus() == null) {
            if (livro.getQuantidadeDisponivel() > 0) {
                livro.setStatus(TipoStatus.DISPONIVEL);
            } else {
                livro.setStatus(TipoStatus.INDISPONIVEL);
            }
        }
        livro.setId(idGenerator.gerarId("Livro"));

        livroRepository.salvar(livro);
    }

    public Livro consultarLivro(Integer id) {

        validarLivroExistente(id);

        return livroRepository.buscarPorId(id);
    }

    public List<Livro> listarLivros() {
        return livroRepository.listarTodos();
    }

    public void atualizarLivro(Integer id, Livro livro) {

        validarLivroExistente(id);
        validarAnoPublicacao(livro.getAnoPublicacao());
        validarQuantidadeDisponivel(livro.getQuantidadeDisponivel());

        livro.setId(id);

        if (livro.getQuantidadeDisponivel() == 0) {
            livro.setStatus(TipoStatus.INDISPONIVEL);
        } else {
            livro.setStatus(TipoStatus.DISPONIVEL);
        }

        livroRepository.atualizar(livro);
    }

    public void excluirLivro(Integer id) {

        validarLivroExistente(id);
        validarSemEmprestimoAtivo(id);

        livroRepository.remover(id);
    }

    public List<Livro> buscarPorTitulo(String titulo) {

        return livroRepository.buscarPorTitulo(titulo);
    }

    public List<Livro> buscarPorAutor(String autor) {

        return livroRepository.buscarPorAutor(autor);
    }

    public boolean verificarDisponibilidade(Integer id) {

        Livro livro = consultarLivro(id);

        return livro.getQuantidadeDisponivel() > 0;
    }

    private void validarLivroExistente(Integer id) {

        validarId(id);

        if (livroRepository.buscarPorId(id) == null) {
            throw new LivroNaoEncontradoException("Livro com ID " + id + " não encontrado.");
        }
    }

    private void validarId(Integer id) {

        if (id == null || id <= 0) {
            throw new LivroIdInvalidoException("ID do livro inválido.");
        }
    }

    private void validarAnoPublicacao(Integer ano) {

        if (ano == null || ano <= 0 || ano > Year.now().getValue()) {
            throw new AnoPublicacaoInvalidoException("Ano de publicação inválido.");
        }
    }

    private void validarQuantidadeDisponivel(Integer quantidade) {

        if (quantidade == null || quantidade < 0) {
            throw new IllegalArgumentException("Quantidade disponível inválida.");
        }
    }

    private void validarSemEmprestimoAtivo(Integer livroID) {

        List<Emprestimo> emprestimos = emprestimoRepository.listarTodos();

        for (Emprestimo emprestimo : emprestimos) {
            if (emprestimo.getLivroID().equals(livroID) && (emprestimo.getStatusEmprestimo() == StatusEmprestimo.ATIVO || emprestimo.getStatusEmprestimo() == StatusEmprestimo.ATRASADO)) {
                throw new LivroComEmprestimoAtivoException("Não é possível excluir um livro com empréstimo ativo.");
            }
        }
    }
}
