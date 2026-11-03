package br.com.novaroupa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/** Proposta de contato e negociação associada a um lote de retalhos. */
@Entity
@Table(name = "propostas_negociacao")
public class PropostaNegociacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lote_id", nullable = false)
    private LoteRetalho lote;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "comprador_id", nullable = false)
    private Usuario comprador;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    @Column(name = "telefone_contato")
    private String telefoneContato;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPropostaNegociacao status = StatusPropostaNegociacao.PENDENTE;

    @NotNull
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @NotNull
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    /** Construtor exigido pelo JPA. */
    protected PropostaNegociacao() {
    }

    public PropostaNegociacao(LoteRetalho lote, Usuario comprador, String mensagem,
                              String telefoneContato) {
        this.lote = lote;
        this.comprador = comprador;
        this.mensagem = mensagem;
        this.telefoneContato = telefoneContato;
        this.status = StatusPropostaNegociacao.PENDENTE;
    }

    @PrePersist
    void inicializarValoresPadrao() {
        if (status == null) {
            status = StatusPropostaNegociacao.PENDENTE;
        }
        LocalDateTime agora = LocalDateTime.now();
        if (dataCriacao == null) {
            dataCriacao = agora;
        }
        if (dataAtualizacao == null) {
            dataAtualizacao = dataCriacao;
        }
    }

    @PreUpdate
    void atualizarDataAtualizacao() {
        dataAtualizacao = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public LoteRetalho getLote() {
        return lote;
    }

    public void setLote(LoteRetalho lote) {
        this.lote = lote;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public StatusPropostaNegociacao getStatus() {
        return status;
    }

    public void setStatus(StatusPropostaNegociacao status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
}
