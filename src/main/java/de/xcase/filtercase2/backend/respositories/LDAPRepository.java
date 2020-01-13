package de.xcase.filtercase2.backend.respositories;

import de.xcase.filtercase2.backend.entities.LDAPUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;

@Repository
public class LDAPRepository {

    public static final String USER_BASE = "OU=SBSUsers,OU=Users,OU=MyBusiness";

    @Autowired
    private LdapTemplate ldapTemplate;

    public List<LDAPUser> findAll() {
        return ldapTemplate
                .search(
                        USER_BASE,
                        "(objectclass=organizationalPerson)",
                        new LDAPUserAttributesMapper());
    }

    public List<LDAPUser> findBy(final String attribute, final String accountName) {
        return ldapTemplate
                .search(
                    USER_BASE,
                        "(" + attribute + "=" + accountName + ")",
                        new LDAPUserAttributesMapper()
                );
    }

    public LDAPUser findByAccountName(final String accountName) {
        List<LDAPUser> results =  this.findBy("sAMAccountName", accountName);
        if (!results.isEmpty()){
            if (results.size() > 1) {
                //TODO: Log that more than one results have been found with same AccountName or throw error
            }
            return results.get(0);
        }
        return null;
    }

    private static class LDAPUserAttributesMapper implements AttributesMapper<LDAPUser> {
        public LDAPUser mapFromAttributes(Attributes attrs) throws NamingException {
            LDAPUser user = new LDAPUser();
            if (attrs.get("sAMAccountName") != null) user.setUserName(String.valueOf(attrs.get("sAMAccountName").get()));
            if (attrs.get("mail") != null) user.setEMail(String.valueOf(attrs.get("mail").get()));
            if (attrs.get("displayName") != null) user.setFullName(String.valueOf(attrs.get("displayName").get()));
            return user;
        }
    }
}
