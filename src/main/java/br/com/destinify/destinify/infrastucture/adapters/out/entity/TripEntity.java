package br.com.destinify.destinify.infrastucture.adapters.out.entity;

import br.com.destinify.destinify.domain.enums.TripStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "trips")
public class TripEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "destination", length = 150, nullable = false)
    private String destination;

    @Column(name = "departure_at", nullable = false)
    private OffsetDateTime departureAt;

    @Column(name = "return_at", nullable = false)
    private OffsetDateTime returnAt;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "included_items")
    private String includedItems;

    @Column(name = "cover_image_url",columnDefinition = "TEXT")
    private String coverImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TripStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private OffsetDateTime updatedAt;

    public TripEntity() {
    }

    public TripEntity(UUID id, String title, String destination, OffsetDateTime departureAt, OffsetDateTime returnAt, BigDecimal price, Integer totalSeats, Integer availableSeats, String description, String includedItems, String coverImageUrl, TripStatus status, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.destination = destination;
        this.departureAt = departureAt;
        this.returnAt = returnAt;
        this.price = price;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        this.description = description;
        this.includedItems = includedItems;
        this.coverImageUrl = coverImageUrl;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public OffsetDateTime getDepartureAt() {
        return departureAt;
    }

    public void setDepartureAt(OffsetDateTime departureAt) {
        this.departureAt = departureAt;
    }

    public OffsetDateTime getReturnAt() {
        return returnAt;
    }

    public void setReturnAt(OffsetDateTime returnAt) {
        this.returnAt = returnAt;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(Integer availableSeats) {
        this.availableSeats = availableSeats;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIncludedItems() {
        return includedItems;
    }

    public void setIncludedItems(String includedItems) {
        this.includedItems = includedItems;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
