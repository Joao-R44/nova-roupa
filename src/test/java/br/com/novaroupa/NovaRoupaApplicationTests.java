package br.com.novaroupa;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class NovaRoupaApplicationTests {

    @Test
    void contextLoads() {
        // A subida bem-sucedida do contexto é a verificação deste teste.
    }
}
