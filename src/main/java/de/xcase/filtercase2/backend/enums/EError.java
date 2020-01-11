package de.xcase.filtercase2.backend.enums;

/**
 * This enum contains all causes for errors.
 */
public enum EError {
    AUTHENTICATION("Es wurde ein nicht authentifizierter Zugriff durchgeführt. Bist du eingeloggt?", "authError"),
    AUTHORIZATION("Es wurde ein nicht authorisierter Zugriff durchgeführt. Besitzt du die nötigen Rechte?", "roleError");

    private String text;
    private String name;

    EError(String text, String name) {
        this.text = text;
        this.name = name;
    }

    public static EError getByName(final String name) {
        for (EError error : EError.values()) {
            if (error.getName().equals(name)) {
                return error;
            }
        }

        return null;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }
}

