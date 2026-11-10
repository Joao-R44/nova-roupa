package br.com.novaroupa.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityLifecycleTests {

    @Test
    void usuarioInicializaValoresPadraoEExponeCamposDePerfil() {
        Usuario usuario = new Usuario("ana", TipoPerfil.COMPRADOR);
        usuario.setNomeCompleto(" ");
        usuario.setReputacaoMedia(null);
        usuario.setTotalAvaliacoes(null);
        usuario.inicializarValoresPadrao();

        assertThat(usuario.getNomeCompleto()).isEqualTo("ana");
        assertThat(usuario.getReputacaoMedia()).isZero();
        assertThat(usuario.getTotalAvaliacoes()).isZero();
        assertThat(usuario.getDataCadastro()).isNotNull();

        usuario.setUsername("ana-nova");
        usuario.setNomeCompleto("Ana Silva");
        usuario.setEmail("ana@example.org");
        usuario.setTelefoneContato("+55 81 99999-0000");
        usuario.setTipoPerfil(TipoPerfil.VENDEDOR);
        usuario.setReputacaoMedia(4.5);
        usuario.setTotalAvaliacoes(2);
        assertThat(usuario.getUsername()).isEqualTo("ana-nova");
        assertThat(usuario.getNomeCompleto()).isEqualTo("Ana Silva");
        assertThat(usuario.getEmail()).isEqualTo("ana@example.org");
        assertThat(usuario.getTelefoneContato()).isEqualTo("+55 81 99999-0000");
        assertThat(usuario.getTipoPerfil()).isEqualTo(TipoPerfil.VENDEDOR);
        assertThat(usuario.getReputacaoMedia()).isEqualTo(4.5);
        assertThat(usuario.getTotalAvaliacoes()).isEqualTo(2);
    }

    @Test
    void loteInicializaStatusEDadosDePublicacao() {
        Usuario vendedor = new Usuario("loja", TipoPerfil.VENDEDOR);
        LoteRetalho lote = new LoteRetalho(vendedor, TipoTecido.ALGODAO, 12.0, "Azul", "Recife");
        lote.setStatus(null);
        lote.inicializarValoresPadrao();

        assertThat(lote.getStatus()).isEqualTo(StatusLote.DISPONIVEL);
        assertThat(lote.getDataPublicacao()).isNotNull();
        lote.setVendedor(vendedor);
        lote.setTipoTecido(TipoTecido.JEANS);
        lote.setPesoKg(20.0);
        lote.setMetragemAproximada(30.0);
        lote.setCorPredominante("Preto");
        lote.setDescricao("Retalhos selecionados");
        lote.setFotosUrls("https://example.org/foto.jpg");
        lote.setLocalizacao("Olinda");
        lote.setStatus(StatusLote.RESERVADO);
        assertThat(lote.getVendedor()).isSameAs(vendedor);
        assertThat(lote.getTipoTecido()).isEqualTo(TipoTecido.JEANS);
        assertThat(lote.getPesoKg()).isEqualTo(20.0);
        assertThat(lote.getMetragemAproximada()).isEqualTo(30.0);
        assertThat(lote.getCorPredominante()).isEqualTo("Preto");
        assertThat(lote.getDescricao()).isEqualTo("Retalhos selecionados");
        assertThat(lote.getFotosUrls()).isEqualTo("https://example.org/foto.jpg");
        assertThat(lote.getLocalizacao()).isEqualTo("Olinda");
        assertThat(lote.getStatus()).isEqualTo(StatusLote.RESERVADO);
    }

    @Test
    void propostaInicializaDatasEAtualizaDataDeModificacao() {
        Usuario vendedor = new Usuario("loja", TipoPerfil.VENDEDOR);
        Usuario comprador = new Usuario("comprador", TipoPerfil.COMPRADOR);
        LoteRetalho lote = new LoteRetalho(vendedor, TipoTecido.VISCOSE, 8.0, "Verde", "Recife");
        PropostaNegociacao proposta = new PropostaNegociacao(lote, comprador, "Tenho interesse", "81999990000");
        proposta.setStatus(null);
        proposta.inicializarValoresPadrao();

        assertThat(proposta.getStatus()).isEqualTo(StatusPropostaNegociacao.PENDENTE);
        assertThat(proposta.getDataCriacao()).isNotNull();
        assertThat(proposta.getDataAtualizacao()).isEqualTo(proposta.getDataCriacao());
        proposta.atualizarDataAtualizacao();
        assertThat(proposta.getDataAtualizacao()).isNotNull();

        proposta.setLote(lote);
        proposta.setComprador(comprador);
        proposta.setMensagem("Mensagem atualizada");
        proposta.setTelefoneContato("81900000000");
        proposta.setStatus(StatusPropostaNegociacao.CONCLUIDA);
        assertThat(proposta.getLote()).isSameAs(lote);
        assertThat(proposta.getComprador()).isSameAs(comprador);
        assertThat(proposta.getMensagem()).isEqualTo("Mensagem atualizada");
        assertThat(proposta.getTelefoneContato()).isEqualTo("81900000000");
        assertThat(proposta.getStatus()).isEqualTo(StatusPropostaNegociacao.CONCLUIDA);
    }

    @Test
    void avaliacaoInicializaDataEPermiteConsultarDados() {
        Usuario avaliador = new Usuario("comprador", TipoPerfil.COMPRADOR);
        Usuario avaliado = new Usuario("vendedor", TipoPerfil.VENDEDOR);
        PropostaNegociacao proposta = new PropostaNegociacao(
                new LoteRetalho(avaliado, TipoTecido.JEANS, 5.0, "Cinza", "Recife"),
                avaliador, null, null);
        AvaliacaoParceiro avaliacao = new AvaliacaoParceiro(proposta, avaliador, avaliado, 5, "Ótimo parceiro");
        avaliacao.inicializarDataCriacao();

        assertThat(avaliacao.getDataCriacao()).isNotNull();
        assertThat(avaliacao.getProposta()).isSameAs(proposta);
        assertThat(avaliacao.getAvaliador()).isSameAs(avaliador);
        assertThat(avaliacao.getAvaliado()).isSameAs(avaliado);
        assertThat(avaliacao.getNota()).isEqualTo(5);
        assertThat(avaliacao.getComentario()).isEqualTo("Ótimo parceiro");

        avaliacao.setProposta(null);
        avaliacao.setAvaliador(avaliado);
        avaliacao.setAvaliado(avaliador);
        avaliacao.setNota(4);
        avaliacao.setComentario(null);
        assertThat(avaliacao.getProposta()).isNull();
        assertThat(avaliacao.getAvaliador()).isSameAs(avaliado);
        assertThat(avaliacao.getAvaliado()).isSameAs(avaliador);
        assertThat(avaliacao.getNota()).isEqualTo(4);
        assertThat(avaliacao.getComentario()).isNull();
    }
}
