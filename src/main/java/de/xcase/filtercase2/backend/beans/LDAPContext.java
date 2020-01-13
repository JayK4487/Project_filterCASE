package de.xcase.filtercase2.backend.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class LDAPContext {

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();

        contextSource.setUrl("ldap://192.168.108.21:389");
        contextSource.setUserDn("ldap@x-case.local");
        contextSource.setPassword("Glt0wXydrclps");
        contextSource.setBase("DC=x-case,DC=local");
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }
}
