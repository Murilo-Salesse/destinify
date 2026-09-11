package br.com.destinify.destinify.domain.model;

import br.com.destinify.destinify.domain.enums.ReservationStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Reservation {

    private UUID id;
    private UUID tripId;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private BigDecimal totalAmount;
    private Integer seatsCount;
    private String boardingLocation;
    private ReservationStatus status;
    private OffsetDateTime expiresAt;
    private OffsetDateTime confirmedAt;
    private OffsetDateTime createdAt;

    public Reservation(UUID id, UUID tripId, String contactName, String contactEmail, String contactPhone, BigDecimal totalAmount, Integer seatsCount, String boardingLocation, ReservationStatus status, OffsetDateTime expiresAt, OffsetDateTime confirmedAt, OffsetDateTime createdAt) {
        this.id = id;
        this.tripId = tripId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.totalAmount = totalAmount;
        this.seatsCount = seatsCount;
        this.boardingLocation = boardingLocation;
        this.status = status;
        this.expiresAt = expiresAt;
        this.confirmedAt = confirmedAt;
        this.createdAt = createdAt;
    }

    public static Reservation createNew(UUID tripId, String contactName, String contactEmail, String contactPhone, BigDecimal totalAmount, Integer seatsCount, String boardingLocation, OffsetDateTime expiresAt) {
        OffsetDateTime now = OffsetDateTime.now();
        return new Reservation(
                UUID.randomUUID(),
                tripId,
                contactName,
                contactEmail,
                contactPhone,
                totalAmount,
                seatsCount,
                boardingLocation,
                ReservationStatus.PENDING,
                expiresAt,
                null,
                now
        );
    }

    public UUID getId() { return id; }
    public UUID getTripId() { return tripId; }
    public String getContactName() { return contactName; }
    public String getContactEmail() { return contactEmail; }
    public String getContactPhone() { return contactPhone; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Integer getSeatsCount() { return seatsCount; }
    public String getBoardingLocation() { return boardingLocation; }
    public ReservationStatus getStatus() { return status; }
    public OffsetDateTime getExpiresAt() { return expiresAt; }
    public OffsetDateTime getConfirmedAt() { return confirmedAt; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
