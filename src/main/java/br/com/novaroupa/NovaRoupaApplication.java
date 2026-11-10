package br.com.novaroupa;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Ponto de entrada da API Nova Roupa. */
@SpringBootApplication
@OpenAPIDefinition(info = @Info(
        title = "Nova Roupa API",
        version = "0.1.0",
        description = "API REST para publicação e pesquisa de lotes de retalhos e serviços de parceiros."))
public class NovaRoupaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaRoupaApplication.class, args);
    }
}
