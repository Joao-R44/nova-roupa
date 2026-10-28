package br.com.novaroupa.config;

import br.com.novaroupa.security.ProjectLdapAuthoritiesPopulator;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;

/** Conecta ao diretório institucional apenas quando a aplicação é iniciada com o profile ldap. */
@Configuration
@Profile("ldap")
@EnableConfigurationProperties(LdapProperties.class)
public class LdapAuthenticationConfig {

    @Bean
    LdapContextSource ldapContextSource(LdapProperties properties) {
        LdapContextSource source = new LdapContextSource();
        source.setUrl(properties.getUrl());
        source.setBase(properties.getBase());
        source.afterPropertiesSet();
        return source;
    }

    @Bean
    AuthenticationManager ldapAuthenticationManager(LdapContextSource source, LdapProperties properties) {
        LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(source);
        factory.setUserDnPatterns(properties.getUserDnPattern());
        factory.setLdapAuthoritiesPopulator(new ProjectLdapAuthoritiesPopulator(
                source, properties.getGroupSearchBase(), properties.getGroupSearchFilter()));
        return factory.createAuthenticationManager();
    }
}
