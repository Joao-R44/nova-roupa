package br.com.novaroupa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Parâmetros não secretos do servidor LDAP, injetados por variáveis de ambiente. */
@ConfigurationProperties(prefix = "app.ldap")
public class LdapProperties {
    private String url;
    private String base;
    private String userDnPattern;
    private String groupSearchBase;
    private String groupSearchFilter;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    public String getUserDnPattern() { return userDnPattern; }
    public void setUserDnPattern(String userDnPattern) { this.userDnPattern = userDnPattern; }
    public String getGroupSearchBase() { return groupSearchBase; }
    public void setGroupSearchBase(String groupSearchBase) { this.groupSearchBase = groupSearchBase; }
    public String getGroupSearchFilter() { return groupSearchFilter; }
    public void setGroupSearchFilter(String groupSearchFilter) { this.groupSearchFilter = groupSearchFilter; }
}
