package br.com.destinify.destinify.domain.enums;

public enum TripStatus {

    DRAFT("Rascunho"),
    PUBLISHED("Publicado"),
    CONFIRMED("Confirmado"),
    COMPLETED("Concluído"),
    CANCELLED("Cancelado");

    private final String description;

    TripStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
