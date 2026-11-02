package br.com.novaroupa.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectLdapAuthoritiesPopulatorTests {

    @Test
    void mapsLdapGroupSuffixesToSpringSecurityRoles() {
        assertThat(ProjectLdapAuthoritiesPopulator.roleForGroup("cn=projeto_admin"))
                .isEqualTo("ROLE_ADMIN");
        assertThat(ProjectLdapAuthoritiesPopulator.roleForGroup("cn=projeto_vendedor"))
                .isEqualTo("ROLE_VENDEDOR");
        assertThat(ProjectLdapAuthoritiesPopulator.roleForGroup("cn=projeto_comprador"))
                .isEqualTo("ROLE_COMPRADOR");
    }

    @Test
    void ignoresGroupsWithoutAConfiguredRoleSuffix() {
        assertThat(ProjectLdapAuthoritiesPopulator.roleForGroup("cn=projeto_visitante"))
                .isEmpty();
    }
}
