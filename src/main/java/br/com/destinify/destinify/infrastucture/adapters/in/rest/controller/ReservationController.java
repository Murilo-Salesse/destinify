package br.com.destinify.destinify.infrastucture.adapters.in.rest.controller;

import br.com.destinify.destinify.application.ports.in.reservation.CancelReservationUseCase;
import br.com.destinify.destinify.application.ports.in.reservation.ConfirmReservationUseCase;
import br.com.destinify.destinify.application.ports.in.reservation.CreateReservationUseCase;
import br.com.destinify.destinify.application.ports.in.reservation.GetReservationByIdUseCase;
import br.com.destinify.destinify.domain.model.Reservation;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request.CreateReservationRequest;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.ReservationResponse;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.ResponseAPIDefault;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

// Caminho: Controller -> UseCase -> Service -> RepositoryPort -> PersistenceAdapter
@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final CreateReservationUseCase createReservationUseCase;
    private final GetReservationByIdUseCase getReservationByIdUseCase;
    private final ConfirmReservationUseCase confirmReservationUseCase;
    private final CancelReservationUseCase cancelReservationUseCase;

    public ReservationController(CreateReservationUseCase createReservationUseCase,
                                 GetReservationByIdUseCase getReservationByIdUseCase,
                                 ConfirmReservationUseCase confirmReservationUseCase,
                                 CancelReservationUseCase cancelReservationUseCase) {
        this.createReservationUseCase = createReservationUseCase;
        this.getReservationByIdUseCase = getReservationByIdUseCase;
        this.confirmReservationUseCase = confirmReservationUseCase;
        this.cancelReservationUseCase = cancelReservationUseCase;
    }

    @PostMapping
    public ResponseEntity<ResponseAPIDefault<ReservationResponse>> create(@RequestBody @Valid CreateReservationRequest request) {
        Reservation createdReservation = createReservationUseCase.execute(request.toCommand());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdReservation.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(new ResponseAPIDefault<>("Reserva efetuada com sucesso.", ReservationResponse.fromDomain(createdReservation)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPIDefault<ReservationResponse>> getById(@PathVariable("id") UUID id) {
        Reservation reservation = getReservationByIdUseCase.execute(id);
        return ResponseEntity.ok(new ResponseAPIDefault<>("Reserva encontrada com sucesso.", ReservationResponse.fromDomain(reservation)));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<ResponseAPIDefault<ReservationResponse>> confirm(@PathVariable("id") UUID id) {
        Reservation confirmedReservation = confirmReservationUseCase.execute(id);
        return ResponseEntity.ok(new ResponseAPIDefault<>("Reserva confirmada com sucesso.", ReservationResponse.fromDomain(confirmedReservation)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable("id") UUID id) {
        cancelReservationUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
