package de.xcase.filtercase2.backend.entities;

import de.xcase.filtercase2.backend.enums.Department;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EMailAdress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User of the e-mail address
     */
    @Column(name = "user")
    private String user;

    /**
     * E-Mail address that is used
     */
    @Column(name = "emailAddress")
    private String emailAddress;

    /**
     * Department of the user
     */
    @Column(name = "department")
    private String department;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
