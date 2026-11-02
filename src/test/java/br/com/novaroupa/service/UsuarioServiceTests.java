package br.com.novaroupa.service;

import br.com.novaroupa.model.TipoPerfil;
import br.com.novaroupa.model.Usuario;
import br.com.novaroupa.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(UsuarioService.class)
@ActiveProfiles("dev")
@TestPropertySource(properties = "spring.datasource.url=jdbc:sqlite:target/test-usuario-service.db")
class UsuarioServiceTests {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void createsProfileUsingAuthenticatedRole() {
        Usuario perfil = usuarioService.garantirPerfilLocal("vendedor",
                List.of(new SimpleGrantedAuthority("ROLE_VENDEDOR")));

        assertThat(perfil.getTipoPerfil()).isEqualTo(TipoPerfil.VENDEDOR);
        assertThat(perfil.getUsername()).isEqualTo("vendedor");
        assertThat(usuarioRepository.count()).isEqualTo(1);
    }

    @Test
    void returnsExistingProfileInsteadOfCreatingAnotherOne() {
        Usuario original = usuarioService.garantirPerfilLocal("usuario",
                List.of(new SimpleGrantedAuthority("ROLE_VENDEDOR")));

        Usuario recuperado = usuarioService.garantirPerfilLocal("usuario",
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        assertThat(recuperado.getId()).isEqualTo(original.getId());
        assertThat(recuperado.getTipoPerfil()).isEqualTo(TipoPerfil.VENDEDOR);
        assertThat(usuarioRepository.count()).isEqualTo(1);
    }
}
