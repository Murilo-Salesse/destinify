package br.com.destinify.destinify.infrastucture.adapters.out.entity;

import br.com.destinify.destinify.domain.enums.DocumentType;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "passengers")
public class PassengerEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private ReservationEntity reservation;

    @Column(name = "full_name", length = 150, nullable = false)
    private String fullName;

    @Column(name = "document_number", length = 30, nullable = false)
    private String documentNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", length = 20, nullable = false)
    private DocumentType documentType;

    @Column(name = "age", nullable = false)
    private Integer age;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "ticket_code", length = 50, nullable = false, unique = true)
    private String ticketCode;

    @Column(name = "boarded", nullable = false)
    private boolean boarded;

    @Column(name = "boarded_at")
    private OffsetDateTime boardedAt;

    public PassengerEntity() {
    }

    public PassengerEntity(UUID id, ReservationEntity reservation, String fullName, String documentNumber,
                           DocumentType documentType, Integer age, String city, String ticketCode,
                           boolean boarded, OffsetDateTime boardedAt) {
        this.id = id;
        this.reservation = reservation;
        this.fullName = fullName;
        this.documentNumber = documentNumber;
        this.documentType = documentType;
        this.age = age;
        this.city = city;
        this.ticketCode = ticketCode;
        this.boarded = boarded;
        this.boardedAt = boardedAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public ReservationEntity getReservation() { return reservation; }
    public void setReservation(ReservationEntity reservation) { this.reservation = reservation; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getTicketCode() { return ticketCode; }
    public void setTicketCode(String ticketCode) { this.ticketCode = ticketCode; }

    public boolean isBoarded() { return boarded; }
    public void setBoarded(boolean boarded) { this.boarded = boarded; }

    public OffsetDateTime getBoardedAt() { return boardedAt; }
    public void setBoardedAt(OffsetDateTime boardedAt) { this.boardedAt = boardedAt; }
}
