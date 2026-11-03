package br.com.novaroupa.repository;

import br.com.novaroupa.model.LoteRetalho;
import br.com.novaroupa.model.StatusLote;
import br.com.novaroupa.model.TipoPerfil;
import br.com.novaroupa.model.TipoTecido;
import br.com.novaroupa.model.Usuario;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
@TestPropertySource(properties = "spring.datasource.url=jdbc:sqlite:target/test-lote-retalho.db")
class LoteRetalhoRepositoryTests {

    @Autowired
    private LoteRetalhoRepository loteRetalhoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void savesLotWithSellerAndDefaultValues() {
        Usuario vendedor = usuarioRepository.saveAndFlush(
                new Usuario("vendedor-lote", TipoPerfil.VENDEDOR));
        LoteRetalho lote = new LoteRetalho(vendedor, TipoTecido.ALGODAO, 4.5,
                "Azul", "Caruaru");
        lote.setMetragemAproximada(12.0);
        lote.setDescricao("Retalhos de algodão azul");
        lote.setFotosUrls("https://example.test/foto1.jpg,https://example.test/foto2.jpg");

        LoteRetalho salvo = loteRetalhoRepository.saveAndFlush(lote);
        LoteRetalho carregado = loteRetalhoRepository.findById(salvo.getId()).orElseThrow();

        assertThat(carregado.getVendedor().getId()).isEqualTo(vendedor.getId());
        assertThat(carregado.getTipoTecido()).isEqualTo(TipoTecido.ALGODAO);
        assertThat(carregado.getPesoKg()).isEqualTo(4.5);
        assertThat(carregado.getMetragemAproximada()).isEqualTo(12.0);
        assertThat(carregado.getStatus()).isEqualTo(StatusLote.DISPONIVEL);
        assertThat(carregado.getDataPublicacao()).isNotNull();
        assertThat(carregado.getFotosUrls()).contains("foto1.jpg", "foto2.jpg");
    }

    @Test
    void rejectsNonPositiveWeight() {
        Usuario vendedor = usuarioRepository.saveAndFlush(
                new Usuario("vendedor-peso-invalido", TipoPerfil.VENDEDOR));
        LoteRetalho lote = new LoteRetalho(vendedor, TipoTecido.JEANS, 0.0,
                "Azul", "Toritama");

        assertThatThrownBy(() -> loteRetalhoRepository.saveAndFlush(lote))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void createsIndexesForRequestedSearchFields() {
        List<String> indexNames = jdbcTemplate.query(
                "PRAGMA index_list('lotes_retalho')",
                (resultSet, rowNumber) -> resultSet.getString("name"));

        assertThat(indexNames).contains(
                "idx_lotes_retalho_tipo_tecido",
                "idx_lotes_retalho_cor_predominante",
                "idx_lotes_retalho_status");
    }
}
