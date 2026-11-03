
package com.biblioteca.bookTrack.service;

import com.biblioteca.bookTrack.model.Emprestimo;
import com.biblioteca.bookTrack.model.Livro;

import com.biblioteca.bookTrack.model.Usuario;
import com.biblioteca.bookTrack.repository.EmprestimoRepository;
import com.biblioteca.bookTrack.repository.LivroRepository;

import com.biblioteca.bookTrack.exception.emprestimoExceptions.EmprestimoNaoEncontradoException;
import com.biblioteca.bookTrack.exception.emprestimoExceptions.EmprestimoInvalidoException;

import com.biblioteca.bookTrack.exception.livroExceptions.LivroNaoEncontradoException;
import com.biblioteca.bookTrack.exception.livroExceptions.LivroIndisponivelException;

import com.biblioteca.bookTrack.repository.UsuarioRepository;
import com.biblioteca.bookTrack.utils.StatusEmprestimo;
import com.biblioteca.bookTrack.utils.TipoStatus;

import com.biblioteca.bookTrack.utils.Validator;
import org.springframework.stereotype.Service;
import com.biblioteca.bookTrack.utils.IdGenerator;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroRepository livroRepository;
    private final UsuarioService usuarioService;
    private final IdGenerator idGenerator;


    private static final int PRAZO_DIAS = 7;

    public EmprestimoService(EmprestimoRepository emprestimoRepository, LivroRepository livroService,
                             UsuarioService usuarioService, IdGenerator idGenerator) {

        this.emprestimoRepository = emprestimoRepository;
        this.livroRepository = livroService;
        this.usuarioService = usuarioService;
        this.idGenerator = idGenerator;
    }

    public void registrarEmprestimo(Emprestimo emprestimo) {

        validarEmprestimo(emprestimo);

        validarUsuarioParaEmprestimo(emprestimo.getUsuarioID());
        Livro livro = buscarLivro(emprestimo.getLivroID());

        validarDisponibilidade(livro);

        emprestimo.setId(idGenerator.gerarId("Emprestimo"));
        emprestimo.setDataInicial(LocalDate.now());
        emprestimo.setDataDevolucao(null);
        emprestimo.setStatusEmprestimo(StatusEmprestimo.ATIVO);

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() - 1);

        if (livro.getQuantidadeDisponivel() == 0) {
            livro.setStatus(TipoStatus.EMPRESTADO);
        } else {
            livro.setStatus(TipoStatus.DISPONIVEL);
        }

        livroRepository.atualizar(livro);
        emprestimoRepository.salvar(emprestimo);
    }

    public Emprestimo consultarEmprestimo(Integer id) {

        validarId(id);
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(id);

        if (emprestimo == null) {
            throw new EmprestimoNaoEncontradoException("Empréstimo com ID " + id + " não encontrado.");
        }

        return emprestimo;
    }

    public List<Emprestimo> listarEmprestimos() {

        return emprestimoRepository.listarTodos();
    }

    public void registrarDevolucao(Integer id) {

        Emprestimo emprestimo = consultarEmprestimo(id);

        validarEmprestimoAtivo(emprestimo);

        Livro livro = buscarLivro(emprestimo.getLivroID());

        validarQuantidadeLivro(livro);

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimo.setStatusEmprestimo(StatusEmprestimo.DEVOLVIDO);

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        livro.setStatus(TipoStatus.DISPONIVEL);

        livroRepository.atualizar(livro);
        emprestimoRepository.atualizar(emprestimo);
    }

    public void cancelarEmprestimo(Integer id) {

        Emprestimo emprestimo = consultarEmprestimo(id);

        validarEmprestimoAtivo(emprestimo);

        Livro livro = buscarLivro(emprestimo.getLivroID());

        validarQuantidadeLivro(livro);

        emprestimo.setStatusEmprestimo(StatusEmprestimo.CANCELADO);

        livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        livro.setStatus(TipoStatus.DISPONIVEL);

        livroRepository.atualizar(livro);
        emprestimoRepository.atualizar(emprestimo);
    }

    public boolean verificarAtraso(Integer id) {

        Emprestimo emprestimo = consultarEmprestimo(id);

        if (emprestimo.getStatusEmprestimo() != StatusEmprestimo.ATIVO) {
            return emprestimo.getStatusEmprestimo() == StatusEmprestimo.ATRASADO;
        }

        if (emprestimo.getDataInicial() == null) {
            throw new EmprestimoInvalidoException("O empréstimo não possui data inicial.");
        }

        LocalDate dataLimite = emprestimo.getDataInicial().plusDays(PRAZO_DIAS);

        if (LocalDate.now().isAfter(dataLimite)) {

            emprestimo.setStatusEmprestimo(StatusEmprestimo.ATRASADO);

            emprestimoRepository.atualizar(emprestimo);

            return true;
        }

        return false;
    }

    public List<Emprestimo> listarPorEstudante(Integer usuarioID) {

        validarId(usuarioID);

        return emprestimoRepository.listarPorEstudante(usuarioID);
    }

    public List<Emprestimo> listarEmprestimosPorLivro(Integer livroID) {

        validarId(livroID);

        return emprestimoRepository.listarPorLivro(livroID);
    }

    private void validarEmprestimo(Emprestimo emprestimo) {

        if (emprestimo == null) {
            throw new EmprestimoInvalidoException("Dados do empréstimo inválidos.");
        }

        validarId(emprestimo.getLivroID());
        validarId(emprestimo.getUsuarioID());
    }

    private void validarId(Integer id) {

        if (id == null || id <= 0) {
            throw new EmprestimoInvalidoException("ID inválido.");
        }
    }

    private void validarEmprestimoAtivo(Emprestimo emprestimo) {

        if (emprestimo.getStatusEmprestimo() != StatusEmprestimo.ATIVO && emprestimo.getStatusEmprestimo() != StatusEmprestimo.ATRASADO) {
            throw new EmprestimoInvalidoException("Este empréstimo não pode ser devolvido ou cancelado.");
        }
    }

    private Livro buscarLivro(Integer livroID) {

        validarId(livroID);

        Livro livro = livroRepository.buscarPorId(livroID);

        if (livro == null) {
            throw new LivroNaoEncontradoException("Livro não encontrado.");
        }

        return livro;
    }

    private void validarDisponibilidade(Livro livro) {

        if (livro.getQuantidadeDisponivel() == null || livro.getQuantidadeDisponivel() <= 0) {
            throw new LivroIndisponivelException("Livro indisponível para empréstimo.");
        }
    }

    private void validarQuantidadeLivro(Livro livro) {

        if (livro.getQuantidadeDisponivel() == null || livro.getQuantidadeDisponivel() < 0) {
            throw new EmprestimoInvalidoException("Quantidade disponível do livro inválida.");
        }
    }


    private void validarUsuarioParaEmprestimo(Integer usuarioID) {

        Usuario usuario = usuarioService.buscarPorID(usuarioID);

        if (!usuario.isAtivo()) {
            throw new EmprestimoInvalidoException("Usuário desativado não pode realizar empréstimos.");
        }

        if (usuario.isPenalidade()) {
            throw new EmprestimoInvalidoException("Usuário penalizado. Não pode realizar empréstimos.");
        }
    }
}
