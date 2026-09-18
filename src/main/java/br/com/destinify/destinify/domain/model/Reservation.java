package br.com.destinify.destinify.domain.model;

import br.com.destinify.destinify.domain.enums.ReservationStatus;
import br.com.destinify.destinify.domain.exception.BusinessException;

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
    private java.util.List<Passenger> passengers = new java.util.ArrayList<>();

    public Reservation(UUID id, UUID tripId, String contactName, String contactEmail, String contactPhone, BigDecimal totalAmount, Integer seatsCount, String boardingLocation, ReservationStatus status, OffsetDateTime expiresAt, OffsetDateTime confirmedAt, OffsetDateTime createdAt) {
        this(id, tripId, contactName, contactEmail, contactPhone, totalAmount, seatsCount, boardingLocation, status, expiresAt, confirmedAt, createdAt, new java.util.ArrayList<>());
    }

    public Reservation(UUID id, UUID tripId, String contactName, String contactEmail, String contactPhone, BigDecimal totalAmount, Integer seatsCount, String boardingLocation, ReservationStatus status, OffsetDateTime expiresAt, OffsetDateTime confirmedAt, OffsetDateTime createdAt, java.util.List<Passenger> passengers) {
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
        if (passengers != null) {
            this.passengers = passengers;
        }
    }

    public static Reservation createNew(UUID tripId,
                                        String contactName,
                                        String contactEmail,
                                        String contactPhone,
                                        BigDecimal totalAmount,
                                        Integer seatsCount,
                                        String boardingLocation,
                                        OffsetDateTime expiresAt) {
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
                now,
                new java.util.ArrayList<>()
        );
    }

    public void confirm() {
        if (this.status != ReservationStatus.PENDING) {
            throw new BusinessException("Apenas reservas pendentes podem ser confirmadas. Status atual: " + this.status.getDescription());
        }
        if (OffsetDateTime.now().isAfter(this.expiresAt)) {
            this.status = ReservationStatus.EXPIRED;
            throw new BusinessException("Esta reserva expirou e não pode mais ser confirmada.");
        }
        this.status = ReservationStatus.CONFIRMED;
        this.confirmedAt = OffsetDateTime.now();
    }

    public void cancel() {
        if (this.status == ReservationStatus.CANCELLED) {
            throw new BusinessException("A reserva já se encontra cancelada.");
        }
        if (this.status == ReservationStatus.EXPIRED) {
            throw new BusinessException("A reserva já se encontra expirada.");
        }
        this.status = ReservationStatus.CANCELLED;
    }

    public void expire() {
        if (this.status == ReservationStatus.PENDING) {
            this.status = ReservationStatus.EXPIRED;
        }
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
    public java.util.List<Passenger> getPassengers() { return passengers; }
    public void addPassenger(Passenger passenger) {
        if (this.passengers.size() >= this.seatsCount) {
            throw new BusinessException("A quantidade de passageiros não pode exceder o total de assentos reservados (" + this.seatsCount + ").");
        }
        this.passengers.add(passenger);
    }
}
