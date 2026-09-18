package br.com.destinify.destinify.domain.model;

import br.com.destinify.destinify.domain.enums.DocumentType;
import br.com.destinify.destinify.domain.exception.BusinessException;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Passenger {

    private UUID id;
    private UUID reservationId;
    private String fullName;
    private String documentNumber;
    private DocumentType documentType;
    private Integer age;
    private String city;
    private String ticketCode;
    private boolean boarded;
    private OffsetDateTime boardedAt;

    public Passenger(UUID id, UUID reservationId, String fullName, String documentNumber,
                     DocumentType documentType, Integer age, String city, String ticketCode,
                     boolean boarded, OffsetDateTime boardedAt) {
        this.id = id;
        this.reservationId = reservationId;
        this.fullName = fullName;
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.age = age;
        this.city = city;
        this.ticketCode = ticketCode;
        this.boarded = boarded;
        this.boardedAt = boardedAt;
    }

    public static Passenger createNew(UUID reservationId, String fullName, String documentNumber,
                                      DocumentType documentType, Integer age, String city) {
        if (fullName == null || fullName.isBlank()) {
            throw new BusinessException("O nome completo do passageiro é obrigatório.");
        }
        if (documentNumber == null || documentNumber.isBlank()) {
            throw new BusinessException("O documento do passageiro é obrigatório para viagem.");
        }
        if (age == null || age < 0) {
            throw new BusinessException("A idade do passageiro é inválida.");
        }

        String generatedTicket = "DST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        return new Passenger(
                UUID.randomUUID(),
                reservationId,
                fullName,
                documentNumber,
                documentType,
                age,
                city,
                generatedTicket,
                false,
                null
        );
    }

    public void board() {
        if (this.boarded) {
            throw new BusinessException("Passageiro já realizou o embarque em: " + this.boardedAt);
        }
        this.boarded = true;
        this.boardedAt = OffsetDateTime.now();
    }

    public UUID getId() { return id; }
    public UUID getReservationId() { return reservationId; }
    public String getFullName() { return fullName; }
    public String getDocumentNumber() { return documentNumber; }
    public DocumentType getDocumentType() { return documentType; }
    public Integer getAge() { return age; }
    public String getCity() { return city; }
    public String getTicketCode() { return ticketCode; }
    public boolean isBoarded() { return boarded; }
    public OffsetDateTime getBoardedAt() { return boardedAt; }
}
