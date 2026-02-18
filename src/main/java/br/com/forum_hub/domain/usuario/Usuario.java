package br.com.forum_hub.domain.usuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name="usuarios")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nomeCompleto;
    private String email;
    private String senha;

    private String nomeUsuario;
    private String biografia;
    private String miniBiografia;

    private Boolean verificado;
    private String token;
    private LocalDateTime expiracaoToken;

    //criações de campos para token OPACO
    @Column(name = "refresh_token", length = 64)
    private String refreshToken;

    @Column(name = "expiracao_refresh_token")
    private LocalDateTime expiracaoRefreshToken;

    public Usuario(DadosCadastroUsuario dados, String senhaCriptografada) {
        this.nomeCompleto = dados.nomeCompleto();
        this.email = dados.email();
        this.senha = senhaCriptografada;
        this.nomeUsuario = dados.nomeUsuario();
        this.biografia = dados.biografia();
        this.miniBiografia = dados.miniBiografia();
        this.verificado = false;
        this.token = UUID.randomUUID().toString();
        this.expiracaoToken = LocalDateTime.now().plusMinutes(30);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getMiniBiografia() {
        return miniBiografia;
    }

    public Long getId() {
        return id;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public LocalDateTime getExpiracaoRefreshToken() {
        return expiracaoRefreshToken;
    }

    public void setExpiracaoRefreshToken(LocalDateTime expiracaoRefreshToken) {
        this.expiracaoRefreshToken = expiracaoRefreshToken;
    }
}
