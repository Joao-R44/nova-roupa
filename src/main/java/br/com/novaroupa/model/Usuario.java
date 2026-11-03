package br.com.novaroupa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** Perfil local associado ao username autenticado no diretório. */
@Entity
@Table(name = "usuarios", uniqueConstraints =
        @UniqueConstraint(name = "uk_usuarios_username", columnNames = "username"))
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String username;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nomeCompleto;

    @Email
    @Size(max = 254)
    @Column(length = 254)
    private String email;

    @Size(max = 30)
    @Column(length = 30)
    private String telefoneContato;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoPerfil tipoPerfil;

    @NotNull
    @Column(nullable = false)
    private Double reputacaoMedia = 0.0;

    @NotNull
    @Column(nullable = false)
    private Integer totalAvaliacoes = 0;

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    /** Construtor exigido pelo JPA. */
    protected Usuario() {
    }

    /** Cria um perfil mínimo, usando o username como nome provisório até haver dados de perfil. */
    public Usuario(String username, TipoPerfil tipoPerfil) {
        this.username = username;
        this.nomeCompleto = username;
        this.tipoPerfil = tipoPerfil;
    }

    @PrePersist
    void inicializarValoresPadrao() {
        if (nomeCompleto == null || nomeCompleto.isBlank()) {
            nomeCompleto = username;
        }
        if (reputacaoMedia == null) {
            reputacaoMedia = 0.0;
        }
        if (totalAvaliacoes == null) {
            totalAvaliacoes = 0;
        }
        if (dataCadastro == null) {
            dataCadastro = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public TipoPerfil getTipoPerfil() {
        return tipoPerfil;
    }

    public void setTipoPerfil(TipoPerfil tipoPerfil) {
        this.tipoPerfil = tipoPerfil;
    }

    public Double getReputacaoMedia() {
        return reputacaoMedia;
    }

    public void setReputacaoMedia(Double reputacaoMedia) {
        this.reputacaoMedia = reputacaoMedia;
    }

    public Integer getTotalAvaliacoes() {
        return totalAvaliacoes;
    }

    public void setTotalAvaliacoes(Integer totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
}
