package br.com.novaroupa.repository;

import br.com.novaroupa.model.TipoPerfil;
import br.com.novaroupa.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
@TestPropertySource(properties = "spring.datasource.url=jdbc:sqlite:target/test-usuario-repository.db")
class UsuarioRepositoryTests {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void savesAndFindsUserByUsernameWithDefaultValues() {
        Usuario saved = usuarioRepository.saveAndFlush(new Usuario("comprador", TipoPerfil.COMPRADOR));

        Usuario loaded = usuarioRepository.findByUsername("comprador").orElseThrow();
        assertThat(loaded.getId()).isEqualTo(saved.getId());
        assertThat(loaded.getNomeCompleto()).isEqualTo("comprador");
        assertThat(loaded.getReputacaoMedia()).isEqualTo(0.0);
        assertThat(loaded.getTotalAvaliacoes()).isZero();
        assertThat(loaded.getDataCadastro()).isNotNull();
    }

    @Test
    void rejectsDuplicateUsername() {
        usuarioRepository.saveAndFlush(new Usuario("mesmo-username", TipoPerfil.COMPRADOR));

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(
                new Usuario("mesmo-username", TipoPerfil.VENDEDOR)))
                .hasMessageContaining("UNIQUE constraint failed");
    }
}
