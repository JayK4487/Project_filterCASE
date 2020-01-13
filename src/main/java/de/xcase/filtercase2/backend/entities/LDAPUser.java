package de.xcase.filtercase2.backend.entities;

/**
 * Represents an LDAP user.
 */
public class LDAPUser {

    private String userName;

    private String fullName;

    private String eMail;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEMail() {
        return eMail;
    }

    public void setEMail(String eMail) {
        this.eMail = eMail;
    }
}
