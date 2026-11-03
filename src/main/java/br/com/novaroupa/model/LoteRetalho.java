package br.com.novaroupa.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/** Lote de retalhos têxteis publicado por um vendedor. */
@Entity
@Table(name = "lotes_retalho", indexes = {
        @Index(name = "idx_lotes_retalho_tipo_tecido", columnList = "tipo_tecido"),
        @Index(name = "idx_lotes_retalho_cor_predominante", columnList = "cor_predominante"),
        @Index(name = "idx_lotes_retalho_status", columnList = "status")
})
public class LoteRetalho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Usuario vendedor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_tecido", nullable = false, length = 30)
    private TipoTecido tipoTecido;

    @NotNull
    @Positive
    @Column(name = "peso_kg", nullable = false)
    private Double pesoKg;

    @Positive
    @Column(name = "metragem_aproximada")
    private Double metragemAproximada;

    @NotBlank
    @Size(max = 60)
    @Column(name = "cor_predominante", nullable = false, length = 60)
    private String corPredominante;

    @Size(max = 1000)
    @Column(name = "descricao", length = 1000)
    private String descricao;

    /** URLs de fotos separadas por vírgula, sem criar uma tabela extra nesta etapa. */
    @Size(max = 2000)
    @Column(name = "fotos_urls", length = 2000)
    private String fotosUrls;

    @NotBlank
    @Size(max = 120)
    @Column(name = "localizacao", nullable = false, length = 120)
    private String localizacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusLote status = StatusLote.DISPONIVEL;

    @NotNull
    @Column(name = "data_publicacao", nullable = false, updatable = false)
    private LocalDateTime dataPublicacao;

    /** Construtor exigido pelo JPA. */
    protected LoteRetalho() {
    }

    /** Cria um lote com os dados obrigatórios; valores opcionais podem ser definidos depois. */
    public LoteRetalho(Usuario vendedor, TipoTecido tipoTecido, Double pesoKg,
                       String corPredominante, String localizacao) {
        this.vendedor = vendedor;
        this.tipoTecido = tipoTecido;
        this.pesoKg = pesoKg;
        this.corPredominante = corPredominante;
        this.localizacao = localizacao;
        this.status = StatusLote.DISPONIVEL;
    }

    @PrePersist
    void inicializarValoresPadrao() {
        if (status == null) {
            status = StatusLote.DISPONIVEL;
        }
        if (dataPublicacao == null) {
            dataPublicacao = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public TipoTecido getTipoTecido() {
        return tipoTecido;
    }

    public void setTipoTecido(TipoTecido tipoTecido) {
        this.tipoTecido = tipoTecido;
    }

    public Double getPesoKg() {
        return pesoKg;
    }

    public void setPesoKg(Double pesoKg) {
        this.pesoKg = pesoKg;
    }

    public Double getMetragemAproximada() {
        return metragemAproximada;
    }

    public void setMetragemAproximada(Double metragemAproximada) {
        this.metragemAproximada = metragemAproximada;
    }

    public String getCorPredominante() {
        return corPredominante;
    }

    public void setCorPredominante(String corPredominante) {
        this.corPredominante = corPredominante;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFotosUrls() {
        return fotosUrls;
    }

    public void setFotosUrls(String fotosUrls) {
        this.fotosUrls = fotosUrls;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public StatusLote getStatus() {
        return status;
    }

    public void setStatus(StatusLote status) {
        this.status = status;
    }

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }
}
