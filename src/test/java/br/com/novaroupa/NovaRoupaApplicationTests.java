package br.com.novaroupa;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("dev")
@TestPropertySource(properties = "spring.datasource.url=jdbc:sqlite:target/test-extensao.db")
@Transactional
class NovaRoupaApplicationTests {

    @Autowired
    private EntityManager entityManager;

    @Test
    void contextLoadsAndJpaCanWriteAndReadSqlite() {
        entityManager.createNativeQuery("CREATE TABLE IF NOT EXISTS sanity_check (id INTEGER PRIMARY KEY, label TEXT)")
                .executeUpdate();
        entityManager.createNativeQuery("DELETE FROM sanity_check WHERE id = 1").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO sanity_check (id, label) VALUES (1, 'ok')")
                .executeUpdate();

        Object label = entityManager.createNativeQuery("SELECT label FROM sanity_check WHERE id = 1")
                .getSingleResult();
        assertThat(label).isEqualTo("ok");
    }
}
