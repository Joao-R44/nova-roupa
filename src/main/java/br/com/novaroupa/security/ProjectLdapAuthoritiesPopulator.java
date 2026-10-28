package br.com.novaroupa.security;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;

import java.util.Collection;
import java.util.Locale;

/** Traduz grupos LDAP terminados em _admin, _vendedor ou _comprador para roles do backend. */
public class ProjectLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

    private final DefaultLdapAuthoritiesPopulator delegate;

    public ProjectLdapAuthoritiesPopulator(BaseLdapPathContextSource contextSource,
                                           String groupSearchBase,
                                           String groupSearchFilter) {
        this.delegate = new DefaultLdapAuthoritiesPopulator(contextSource, groupSearchBase);
        this.delegate.setGroupSearchFilter(groupSearchFilter);
        this.delegate.setRolePrefix("");
        this.delegate.setConvertToUpperCase(false);
    }

    @Override
    public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations userData, String username) {
        return delegate.getGrantedAuthorities(userData, username).stream()
                .map(GrantedAuthority::getAuthority)
                .map(String::toLowerCase)
                .map(this::roleForGroup)
                .filter(role -> !role.isEmpty())
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    private String roleForGroup(String groupName) {
        String normalized = groupName.toLowerCase(Locale.ROOT);
        if (normalized.endsWith("_admin")) return "ROLE_ADMIN";
        if (normalized.endsWith("_vendedor")) return "ROLE_VENDEDOR";
        if (normalized.endsWith("_comprador")) return "ROLE_COMPRADOR";
        return "";
    }
}
