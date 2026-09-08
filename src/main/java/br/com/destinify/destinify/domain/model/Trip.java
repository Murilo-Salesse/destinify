package br.com.destinify.destinify.domain.model;

import br.com.destinify.destinify.domain.enums.TripStatus;
import br.com.destinify.destinify.domain.exception.BusinessException;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public class Trip {

    private UUID id;
    private String title;
    private String destination;
    private OffsetDateTime departureAt;
    private OffsetDateTime returnAt;
    private BigDecimal price;
    private Integer totalSeats;
    private Integer availableSeats;
    private String description;
    private String includedItems;
    private String coverImageUrl;
    private TripStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public Trip(UUID id, String title, String destination, OffsetDateTime departureAt, OffsetDateTime returnAt, BigDecimal price, Integer totalSeats, Integer availableSeats, String description, String includedItems, String coverImageUrl, TripStatus status, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
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

    // Método fábrica (Factory Method) para criar uma NOVA viagem com validação
    public static Trip createNew(String title, String destination, OffsetDateTime departureAt, OffsetDateTime returnAt,
                                 BigDecimal price, Integer totalSeats, String description,
                                 String includedItems, String coverImageUrl) {

        if (departureAt.isAfter(returnAt)) {
            throw new IllegalArgumentException("A data de ida não pode ser posterior à data de volta.");
        }
        if (totalSeats <= 0) {
            throw new IllegalArgumentException("A capacidade de assentos deve ser maior que zero.");
        }
        OffsetDateTime now = OffsetDateTime.now();

        return new Trip(
                UUID.randomUUID(),
                title,
                destination,
                departureAt,
                returnAt,
                price,
                totalSeats,
                totalSeats, // availableSeats começa igual ao total
                description,
                includedItems,
                coverImageUrl,
                TripStatus.DRAFT,
                now,
                now
        );
    }

    public void updateTrip(String title,
                           OffsetDateTime departureAt,
                           OffsetDateTime returnAt,
                           BigDecimal price,
                           Integer totalSeats,
                           String description,
                           String includedItems,
                           String coverImageUrl) {


        if (this.status == TripStatus.COMPLETED || this.status == TripStatus.CANCELLED) {
            throw new BusinessException("Não é possível alterar uma viagem finalizada ou cancelada.");
        }
        if (price != null && price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("O preço da viagem deve ser maior que zero.");
        }
        if (departureAt != null && returnAt != null && departureAt.isAfter(returnAt)) {
            throw new BusinessException("A data de ida não pode ser posterior à data de volta.");
        }

        if (totalSeats != null) {
            if (totalSeats <= 0) {
                throw new BusinessException("A capacidade de assentos deve ser maior que zero.");
            }

            // Se não há reservas feitas (todas as vagas ainda estão livres)
            if (this.availableSeats.equals(this.totalSeats)) {
                this.availableSeats = totalSeats;
            } else {
                int reservedSeats = this.totalSeats - this.availableSeats;
                if (totalSeats < reservedSeats) {
                    throw new BusinessException("A nova capacidade não pode ser menor que as vagas já reservadas (" + reservedSeats + ").");
                }
                this.availableSeats = totalSeats - reservedSeats;
            }

            this.totalSeats = totalSeats;
        }

        // Atualiza os campos desta instância (this)
        this.title = title;
        this.departureAt = departureAt;
        this.returnAt = returnAt;
        this.price = price;
        this.totalSeats = totalSeats;
        this.description = description;
        this.includedItems = includedItems;
        this.coverImageUrl = coverImageUrl;
        this.updatedAt = OffsetDateTime.now();
    }


//    public void reserveSeats(int seatsToReserve) {
//        if (this.status != TripStatus.PUBLISHED) {
//            throw new BusinessException("Não é possível reservar vagas em uma viagem que não está aberta.");
//        }
//        if (seatsToReserve <= 0) {
//            throw new BusinessException("A quantidade de assentos deve ser positiva.");
//        }
//        if (seatsToReserve > this.availableSeats) {
//            throw new BusinessException("Vagas insuficientes. Restam apenas: " + this.availableSeats);
//        }
//        this.availableSeats -= seatsToReserve;
//        this.updatedAt = OffsetDateTime.now();
//    }

//    public void releaseSeats(int seatsToRelease) {
//        if (this.availableSeats + seatsToRelease > this.totalSeats) {
//            this.availableSeats = this.totalSeats;
//        } else {
//            this.availableSeats += seatsToRelease;
//        }
//        this.updatedAt = OffsetDateTime.now();
//    }

//    public void publish() {
//        if (this.status != TripStatus.DRAFT) {
//            throw new BusinessException("Apenas viagens em rascunho podem ser publicadas.");
//        }
//        this.status = TripStatus.PUBLISHED;
//        this.updatedAt = OffsetDateTime.now();
//    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDestination() { return destination; }
    public OffsetDateTime getDepartureAt() { return departureAt; }
    public OffsetDateTime getReturnAt() { return returnAt; }
    public BigDecimal getPrice() { return price; }
    public Integer getTotalSeats() { return totalSeats; }
    public Integer getAvailableSeats() { return availableSeats; }
    public String getDescription() { return description; }
    public String getIncludedItems() { return includedItems; }
    public String getCoverImageUrl() { return coverImageUrl; }
    public TripStatus getStatus() { return status; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public OffsetDateTime getUpdatedAt() { return updatedAt; }
}
