package br.com.destinify.destinify.infrastucture.adapters.out.entity;

import br.com.destinify.destinify.domain.enums.ReservationStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class ReservationEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private TripEntity trip;

    @Column(name = "contact_name", length = 150, nullable = false)
    private String contactName;

    @Column(name = "contact_email", length = 150, nullable = false)
    private String contactEmail;

    @Column(name = "contact_phone", length = 30, nullable = false)
    private String contactPhone;

    @Column(name = "total_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "seats_count", nullable = false)
    private Integer seatsCount;

    @Column(name = "boarding_location", length = 200, nullable = false)
    private String boardingLocation;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private ReservationStatus status;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "confirmed_at")
    private OffsetDateTime confirmedAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<PassengerEntity> passengers = new java.util.ArrayList<>();

    public ReservationEntity() {
    }

    public ReservationEntity(UUID id, TripEntity trip, String contactName, String contactEmail, String contactPhone, BigDecimal totalAmount, Integer seatsCount, String boardingLocation, ReservationStatus status, OffsetDateTime expiresAt, OffsetDateTime confirmedAt, OffsetDateTime createdAt) {
        this.id = id;
        this.trip = trip;
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

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public TripEntity getTrip() { return trip; }
    public void setTrip(TripEntity trip) { this.trip = trip; }

    public UUID getTripId() {
        return trip != null ? trip.getId() : null;
    }

    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Integer getSeatsCount() { return seatsCount; }
    public void setSeatsCount(Integer seatsCount) { this.seatsCount = seatsCount; }

    public String getBoardingLocation() { return boardingLocation; }
    public void setBoardingLocation(String boardingLocation) { this.boardingLocation = boardingLocation; }

    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }

    public OffsetDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(OffsetDateTime expiresAt) { this.expiresAt = expiresAt; }

    public OffsetDateTime getConfirmedAt() { return confirmedAt; }
    public void setConfirmedAt(OffsetDateTime confirmedAt) { this.confirmedAt = confirmedAt; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public java.util.List<PassengerEntity> getPassengers() { return passengers; }
    public void setPassengers(java.util.List<PassengerEntity> passengers) { this.passengers = passengers; }
}
