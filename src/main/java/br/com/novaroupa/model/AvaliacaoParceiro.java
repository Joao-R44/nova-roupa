package br.com.novaroupa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/** Avaliação emitida por um parceiro após uma negociação concluída. */
@Entity
@Table(name = "avaliacoes_parceiro", indexes = @Index(name = "uk_avaliacao_proposta_avaliador", columnList = "proposta_id, avaliador_id", unique = true))
public class AvaliacaoParceiro {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "proposta_id", nullable = false)
    private PropostaNegociacao proposta;
    @NotNull @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "avaliador_id", nullable = false)
    private Usuario avaliador;
    @NotNull @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "avaliado_id", nullable = false)
    private Usuario avaliado;
    @NotNull @Min(1) @Max(5) @Column(nullable = false)
    private Integer nota;
    @Size(max = 500) @Column(length = 500)
    private String comentario;
    @NotNull @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    protected AvaliacaoParceiro() {}
    public AvaliacaoParceiro(PropostaNegociacao proposta, Usuario avaliador, Usuario avaliado, Integer nota, String comentario) {
        this.proposta = proposta; this.avaliador = avaliador; this.avaliado = avaliado; this.nota = nota; this.comentario = comentario;
    }
    @PrePersist void inicializarDataCriacao() { if (dataCriacao == null) dataCriacao = LocalDateTime.now(); }
    public Long getId() { return id; }
    public PropostaNegociacao getProposta() { return proposta; }
    public void setProposta(PropostaNegociacao proposta) { this.proposta = proposta; }
    public Usuario getAvaliador() { return avaliador; }
    public void setAvaliador(Usuario avaliador) { this.avaliador = avaliador; }
    public Usuario getAvaliado() { return avaliado; }
    public void setAvaliado(Usuario avaliado) { this.avaliado = avaliado; }
    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }
    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
}
