package br.com.destinify.destinify.infrastucture.adapters.in.rest;

import br.com.destinify.destinify.application.ports.in.CreateTripUseCase;
import br.com.destinify.destinify.application.ports.in.GetAllTripsUseCase;
import br.com.destinify.destinify.application.ports.in.GetTripByIdUseCase;
import br.com.destinify.destinify.domain.model.Trip;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.CreateTripRequest;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.ResponseAPIDefault;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.TripResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

// Caminho: Controller -> UseCase -> Service -> RepositoryPort -> PersistenceAdapter
@RestController
@RequestMapping("/api/v1/trips")
public class TripController {

    private final CreateTripUseCase createTripUseCase;
    private final GetTripByIdUseCase getTripByIdUseCase;
    private final GetAllTripsUseCase getAllTripsUseCase;

    public TripController(CreateTripUseCase createTripUseCase, GetTripByIdUseCase getTripByIdUseCase, GetAllTripsUseCase getAllTripsUseCase) {
        this.createTripUseCase = createTripUseCase;
        this.getTripByIdUseCase = getTripByIdUseCase;
        this.getAllTripsUseCase = getAllTripsUseCase;
    }

    @PostMapping
    public ResponseEntity<ResponseAPIDefault<TripResponse>> create(@RequestBody @Valid CreateTripRequest request) {
        Trip createdTrip = createTripUseCase.execute(request.toCommand());
        var location = URI.create("/trips/" + createdTrip.getId());
        return ResponseEntity.created(location)
                .body(new ResponseAPIDefault<>("Viagem criada com sucesso.", TripResponse.fromDomain(createdTrip)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPIDefault<TripResponse>> getById(@PathVariable("id") UUID id) {
        Trip trip = getTripByIdUseCase.execute(id);
        return ResponseEntity.ok(new ResponseAPIDefault<>("Viagem encontrada com sucesso.", TripResponse.fromDomain(trip)));
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseAPIDefault<List<TripResponse>>> getAll() {
        List<Trip> trips = getAllTripsUseCase.execute();
        List<TripResponse> tripResponses = trips.stream().map(TripResponse::fromDomain).toList();
        return ResponseEntity.ok(new ResponseAPIDefault<>("Viagens encontradas com sucesso.", tripResponses));
    }
}
