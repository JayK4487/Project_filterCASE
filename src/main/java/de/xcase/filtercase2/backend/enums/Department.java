package de.xcase.filtercase2.backend.enums;

public enum Department {

    GESCHÄFTSFÜHRUNG("Geschäftsführung"),
    GESCHÄFTSFELDLEITUNG("Geschäftsfeldleitung"),
    MITARBEITER("Mitarbeiter");


    private String name;

    Department(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
