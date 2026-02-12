package br.com.forum_hub.domain.autenticacao;

import br.com.forum_hub.domain.usuario.Usuario;
import br.com.forum_hub.domain.usuario.UsuarioRepository;
import br.com.forum_hub.infra.exception.RegraDeNegocioException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
public class TokenService {

    private final UsuarioRepository usuarioRepository;

    public TokenService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String gerarToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256("12345678");
            return JWT.create()
                    .withIssuer("Forum Hub")
                    .withSubject(usuario.getUsername())
                    .withExpiresAt(expiracao(30))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RegraDeNegocioException("Erro ao gerar token JWT de acesso!");
        }
    }

    public String gerarRefreshToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("12345678");
            return JWT.create()
                    .withIssuer("Forum Hub")
                    .withSubject(usuario.getId().toString())
                    .withExpiresAt(expiracao(120))
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RegraDeNegocioException("Erro ao gerar token JWT de acesso!");
        }
    }

    private Instant expiracao(Integer minutos) {
        return LocalDateTime.now().plusMinutes(minutos).toInstant(ZoneOffset.of("-03:00"));
    }

    //direto da doc
    public String verificarToken(String token){
        DecodedJWT decodedJWT;
        try {
            Algorithm algorithm = Algorithm.HMAC256("12345678");
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Forum Hub")
                    .build();

            decodedJWT = verifier.verify(token);
            return decodedJWT.getSubject();
        } catch (JWTVerificationException exception){
            throw new RegraDeNegocioException("Erro ao verificar token JWT de acesso!");
        }
    }


    /// desafio - token refresh - opaco

    public String gerarRefreshTokenOpaco(Usuario usuario){

        String refreshToken = UUID.randomUUID().toString();

        usuario.setRefreshToken(refreshToken);
        usuario.setExpiracaoRefreshToken(LocalDateTime.now().plusDays(7));

        usuarioRepository.save(usuario);

        return refreshToken;
    }

    public Usuario validarRefreshTokenOpaco(String refreshToken){

        Usuario usuario = usuarioRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if(usuario.getExpiracaoRefreshToken().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Refresh token expirado");
        }

        return usuario;
    }
}
