package br.com.destinify.destinify.infrastucture.adapters.in.rest;

import br.com.destinify.destinify.application.dto.request.TripFilterQuery;
import br.com.destinify.destinify.application.ports.in.*;
import br.com.destinify.destinify.domain.model.Trip;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request.CreateTripRequest;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request.UpdateTripRequest;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.ResponseAPIDefault;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.TripResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

// Caminho: Controller -> UseCase -> Service -> RepositoryPort -> PersistenceAdapter
@RestController
@RequestMapping("/api/v1/trips")
public class TripController {

    private final CreateTripUseCase createTripUseCase;
    private final GetTripByIdUseCase getTripByIdUseCase;
    private final SearchTripUseCase searchTripUseCase;
    private final UpdateTripUseCase updateTripUseCase;
    private final DeleteTripUseCase deleteTripUseCase;

    public TripController(CreateTripUseCase createTripUseCase, GetTripByIdUseCase getTripByIdUseCase, SearchTripUseCase searchTripUseCase, UpdateTripUseCase updateTripUseCase, DeleteTripUseCase deleteTripUseCase) {
        this.createTripUseCase = createTripUseCase;
        this.getTripByIdUseCase = getTripByIdUseCase;
        this.searchTripUseCase = searchTripUseCase;
        this.updateTripUseCase = updateTripUseCase;
        this.deleteTripUseCase = deleteTripUseCase;
    }

    @PostMapping
    public ResponseEntity<ResponseAPIDefault<TripResponse>> create(@RequestBody @Valid CreateTripRequest request) {
        Trip createdTrip = createTripUseCase.execute(request.toCommand());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTrip.getId())
                .toUri();
        return ResponseEntity.created(location)
                .body(new ResponseAPIDefault<>("Viagem criada com sucesso.", TripResponse.fromDomain(createdTrip)));
    }

    @GetMapping
    public ResponseEntity<ResponseAPIDefault<Page<TripResponse>>> getAll(
            @ModelAttribute TripFilterQuery filter,
            @PageableDefault(page = 0, size = 10, sort = "departureAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<Trip> trips = searchTripUseCase.execute(filter, pageable);
        Page<TripResponse> response = trips.map(TripResponse::fromDomain);
        return ResponseEntity.ok(new ResponseAPIDefault<>("Viagens filtradas com sucesso.", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPIDefault<TripResponse>> getById(@PathVariable("id") UUID id) {
        Trip trip = getTripByIdUseCase.execute(id);
        return ResponseEntity.ok(new ResponseAPIDefault<>("Viagem encontrada com sucesso.", TripResponse.fromDomain(trip)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseAPIDefault<TripResponse>> update(@PathVariable("id") UUID id,
                                                                   @RequestBody @Valid UpdateTripRequest request) {
        Trip updatedTrip = updateTripUseCase.execute(request.toCommand(id));
        return ResponseEntity.ok(new ResponseAPIDefault<>("Viagem atualizada com sucesso.",
                TripResponse.fromDomain(updatedTrip)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
        deleteTripUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}