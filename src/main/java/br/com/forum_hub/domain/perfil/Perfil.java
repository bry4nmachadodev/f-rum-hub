package br.com.forum_hub.domain.perfil;

import jakarta.persistence.*;

@Entity
@Table(name = "perfis")
public class Perfil {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    private PerfilNome nome;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
