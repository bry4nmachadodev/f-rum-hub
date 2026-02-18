package br.com.forum_hub.domain.usuario;

import br.com.forum_hub.infra.email.EmailService;
import jakarta.validation.Valid;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder encriptador;

    private final EmailService emailService;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encriptador, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.encriptador = encriptador;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmailIgnoreCaseAndVerificadoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("O usuário não foi encontrado!"));
    }


    ///  cadastra no db - criptografa a senha antes de salvar
    public Usuario cadastrar(@Valid DadosCadastroUsuario dados) {
        String senhaCriptografada = encriptador.encode(dados.senha());
        var usuario  = new Usuario(dados, senhaCriptografada);

        emailService.enviarEmailVerificacao(usuario);

        return usuarioRepository.save(usuario);
    }
}
