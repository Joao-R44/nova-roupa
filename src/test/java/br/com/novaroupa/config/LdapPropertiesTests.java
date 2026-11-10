package br.com.novaroupa.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LdapPropertiesTests {

    @Test
    void guardaTodosOsParametrosDoDiretorio() {
        LdapProperties properties = new LdapProperties();
        properties.setUrl("ldaps://directory.example.org:636");
        properties.setBase("dc=example,dc=org");
        properties.setUserDnPattern("uid={0},ou=people");
        properties.setGroupSearchBase("ou=groups");
        properties.setGroupSearchFilter("(member={0})");

        assertThat(properties.getUrl()).isEqualTo("ldaps://directory.example.org:636");
        assertThat(properties.getBase()).isEqualTo("dc=example,dc=org");
        assertThat(properties.getUserDnPattern()).isEqualTo("uid={0},ou=people");
        assertThat(properties.getGroupSearchBase()).isEqualTo("ou=groups");
        assertThat(properties.getGroupSearchFilter()).isEqualTo("(member={0})");
    }
}
