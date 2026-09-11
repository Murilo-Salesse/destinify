package br.com.destinify.destinify.domain.enums;

public enum ReservationStatus {

    PENDING("Pendente"),
    CONFIRMED("Confirmada"),
    CANCELLED("Cancelada"),
    EXPIRED("Expirada");

    private final String description;

    ReservationStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}