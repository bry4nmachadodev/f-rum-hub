package br.com.forum_hub.domain.usuario;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;

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


    //criações de campos para token OPACO
    @Column(name = "refresh_token", length = 64)
    private String refreshToken;

    @Column(name = "expiracao_refresh_token")
    private LocalDateTime expiracaoRefreshToken;

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
