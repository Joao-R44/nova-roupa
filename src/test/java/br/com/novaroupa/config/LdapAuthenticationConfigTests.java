package br.com.novaroupa.config;

import br.com.novaroupa.security.ProjectLdapAuthoritiesPopulator;
import org.junit.jupiter.api.Test;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.authentication.AuthenticationManager;

import static org.assertj.core.api.Assertions.assertThat;

class LdapAuthenticationConfigTests {

    @Test
    void criaFonteDeContextoEAuthenticationManagerSemAbrirConexaoRemota() {
        LdapProperties properties = new LdapProperties();
        properties.setUrl("ldap://127.0.0.1:1389");
        properties.setBase("dc=example,dc=org");
        properties.setUserDnPattern("uid={0},ou=people");
        properties.setGroupSearchBase("ou=groups");
        properties.setGroupSearchFilter("(member={0})");

        LdapAuthenticationConfig configuration = new LdapAuthenticationConfig();
        LdapContextSource source = configuration.ldapContextSource(properties);
        AuthenticationManager authenticationManager = configuration.ldapAuthenticationManager(source, properties);
        ProjectLdapAuthoritiesPopulator authoritiesPopulator = new ProjectLdapAuthoritiesPopulator(
                source, properties.getGroupSearchBase(), properties.getGroupSearchFilter());

        assertThat(source).isNotNull();
        assertThat(authenticationManager).isNotNull();
        assertThat(authoritiesPopulator).isNotNull();
    }
}
