package com.biblioteca.bookTrack.service;

import com.biblioteca.bookTrack.dto.UsuarioDTO;
import com.biblioteca.bookTrack.model.Usuario;
import com.biblioteca.bookTrack.repository.UsuarioRepository;
import com.biblioteca.bookTrack.usuariosException.RecursoJaCadastradoException;
import com.biblioteca.bookTrack.usuariosException.UsuarioNaoEncontradoException;
import com.biblioteca.bookTrack.utils.IdGenerator;
import com.biblioteca.bookTrack.utils.IdOperator;
import com.biblioteca.bookTrack.utils.TipoUsuario;
import com.biblioteca.bookTrack.utils.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdGenerator idGenerator;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, IdGenerator idGenerator) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.idGenerator = idGenerator;
    }


    public void salvarUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = criarUsuario(usuarioDTO);
        usuarioRepository.salvar(usuario);

    }

    public void removerUsuario(int id) {
        Usuario usuarioEncontrado = buscarPorID(id);

       usuarioRepository.remover(id);
    }

    public void atualizarUsuario(int id, String nome, String email, LocalDate dataNascimento) {
        Usuario usuarioAtualizar = validarAtualizacaoUsuario(id, nome, email, dataNascimento);
        nome = nome.trim().toLowerCase();
        email = email.trim().toLowerCase();

        usuarioAtualizar = fazerAtualizacaoUsuario(usuarioAtualizar, nome, email, dataNascimento);

        usuarioRepository.atualizar(usuarioAtualizar);
    }

    public void ativarUsuario(int id) {
        Usuario modificarUsuario = buscarPorID(id);

        if(modificarUsuario.isAtivo()) {
            throw new IllegalStateException("O usuário já está ativado.");
        }
        modificarUsuario.setAtivo(true);

        usuarioRepository.atualizar(modificarUsuario);
    }

    public void alterarSenha(int id, String senhaAtual, String novaSenha) {
        Usuario usuario = buscarPorID(id);

        Validator.validarString(senhaAtual, "Senha atual");
        Validator.validarString(novaSenha, "Nova senha");

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new IllegalArgumentException("A senha atual está incorreta.");
        }

        if (novaSenha.length() < 8 || novaSenha.length() > 64) {
            throw new IllegalArgumentException("A nova senha deve ter entre 8 e 64 caracteres.");
        }

        if (passwordEncoder.matches(novaSenha, usuario.getSenha())) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da atual.");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));

        usuarioRepository.atualizar(usuario);
    }

    public void desativarUsuario(int id) {
        Usuario modificarUsuario = buscarPorID(id);

        if(!modificarUsuario.isAtivo()) {
            throw new IllegalStateException("O usuário já está desativado.");
        }

        modificarUsuario.setAtivo(false);
        usuarioRepository.atualizar(modificarUsuario);


    }

    public void adicionarPenalidade(int id) {
        Usuario usuarioPenalizar = buscarPorID(id);
        if(usuarioPenalizar.isPenalidade()) {
            throw new IllegalStateException("O usuário já está penalizado.");

        }
        usuarioPenalizar.setPenalidade(true);
        usuarioRepository.atualizar(usuarioPenalizar);
    }

    public void removerPenalidade(int id) {
        Usuario usuarioPenalizado = buscarPorID(id);
        if(!usuarioPenalizado.isPenalidade()) {
            throw new IllegalStateException("O usuário não penalizado.");

        }
        usuarioPenalizado.setPenalidade(false);
        usuarioRepository.atualizar(usuarioPenalizado);
    }
    public Usuario buscarPorID(int id) {
        IdOperator.validarId(id);
        Usuario usuarioEncontrado = usuarioRepository.buscarPorId(id);

        if(usuarioEncontrado == null) {
            throw new UsuarioNaoEncontradoException("Usuário com ID " + id + " não encontrado.");
        }

        return usuarioEncontrado;
    }

    public Usuario buscarPorEmail(String email) {
        Validator.validarString(email, "email");
        Usuario usuarioEncontrado = usuarioRepository.buscarPorEmail(email.trim());

        if(usuarioEncontrado == null) {
            throw new UsuarioNaoEncontradoException("Usuário com email " + email+ " não encontrado.");
        }

        return usuarioEncontrado;

    }

    public Usuario buscarPorMatricula(String matricula) {
        Validator.validarString(matricula, "matricula");
        Usuario usuarioEncontrado = usuarioRepository.buscarPorMatricula(matricula.trim());

        if(usuarioEncontrado == null) {
            throw new UsuarioNaoEncontradoException("Usuário com matricula " + matricula+ " não encontrado.");
        }

        return usuarioEncontrado;

    }

    public List<Usuario> filtrarPorNome(String nome) {
        Validator.validarString(nome, "nome");

        return usuarioRepository.filtrarPorNome(nome.trim());

    }

    public List<Usuario> filtrarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        Validator.validarPeriodo(dataInicio, dataFim);

        return usuarioRepository.filtrarPorPeriodo(dataInicio, dataFim);
    }

    public List<Usuario> filtrarPorTipoUsuario(String tipoUsuario) {
        TipoUsuario tipo= Validator.converterTipoUsuario(tipoUsuario);

        return usuarioRepository.filtrarPorTipoUsuario(tipo);
    }
    public List<Usuario> filtrarPorIdade(int idade) {
        Validator.validarIdade(idade);

        return usuarioRepository.filtrarPorIdade(idade);
    }

    public List<Usuario> listarAtivos() {
        return usuarioRepository.buscarAtivos();
    }

    public List<Usuario> listarDesativados() {
        return usuarioRepository.buscarDesativados();
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.listarTodos();
    }


    private Usuario validarAtualizacaoUsuario(int id, String nome, String email, LocalDate dataNascimento) {
        Usuario usuarioAtualizar = buscarPorID(id);

        Validator.validarString(nome, "Nome");
        Validator.validarString(email, "Email");
        Validator.validarDataNascimento(dataNascimento);

        return usuarioAtualizar;

    }

    private Usuario fazerAtualizacaoUsuario(Usuario usuario, String nome, String email, LocalDate dataNascimento) {
        usuario.setNome(nome.trim());

        Usuario encontrado = usuarioRepository.buscarPorEmail(email.trim());

        if (encontrado != null && encontrado.getId() != usuario.getId()) {
            throw new RecursoJaCadastradoException("Este email já está cadastrado por outro usuário.");
        }

        usuario.setEmail(email.trim());
        usuario.setDataNascimento(dataNascimento);

        return usuario;
    }

    public Usuario criarUsuario(UsuarioDTO usuarioDTO) {
        Validator.validarObjeto(usuarioDTO, "Usuário");
        Validator.validarString(usuarioDTO.getNome(), "Nome");
        Validator.validarString(usuarioDTO.getEmail(), "Email");
        Validator.validarString(usuarioDTO.getSenha(), "Senha");
        Validator.validarString(usuarioDTO.getMatricula(), "Matrícula");
        Validator.validarObjeto(usuarioDTO.getTipoUsuario(), "Tipo de usuário");
        Validator.validarDataNascimento(usuarioDTO.getDataNascimento());

        String email = usuarioDTO.getEmail().trim().toLowerCase();
        String matricula = usuarioDTO.getMatricula().trim();

        Usuario emailEncontrado = usuarioRepository.buscarPorEmail(email);

        if (emailEncontrado != null) {
            throw new RecursoJaCadastradoException("Este email já está cadastrado.");
        }

        Usuario matriculaEncontrada = usuarioRepository.buscarPorMatricula(matricula);

        if (matriculaEncontrada != null) {
            throw new RecursoJaCadastradoException("Esta matrícula já está cadastrada.");
        }

        Usuario usuario = new Usuario();

        usuario.setId(idGenerator.gerarId("usuario"));
        usuario.setNome(usuarioDTO.getNome().trim());
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        usuario.setDataNascimento(usuarioDTO.getDataNascimento());
        usuario.setMatricula(matricula);
        usuario.setTipoUsuario(usuarioDTO.getTipoUsuario());
        usuario.setAtivo(true);
        usuario.setDataCadastro(LocalDate.now());

        return usuario;
    }

}
