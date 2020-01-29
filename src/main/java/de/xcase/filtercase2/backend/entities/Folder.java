package de.xcase.filtercase2.backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Destination folder
     */
    @Column(name = "destinationFolder")
    private String destinationFolder;

    /**
     * Source folder that is used
     */
    @Column(name = "sourceFolder")
    private String sourceFolder;

    /**
     * User that set the folders
     */
    @Column(name = "user")
    private String user;

    @OneToMany(mappedBy = "folder", fetch = FetchType.EAGER)
    private List<Keyword> keywords = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceFolder() {
        return sourceFolder;
    }

    public void setSourceFolder(String sourceFolder) {
        this.sourceFolder = sourceFolder;
    }

    public String getDestinationFolder() {
        return destinationFolder;
    }

    public void setDestinationFolder(String destinationFolder) {
        this.destinationFolder = destinationFolder;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<Keyword> keywords) {
        this.keywords = keywords;
    }

    public void addKeyword(final Keyword keyword) {
        if (!keywords.contains(keyword)){
            keywords.add(keyword);
        }
    }

    public void removeKeyword(final Keyword keyword) {
        keywords.remove(keyword);
    }
}
