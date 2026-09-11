package br.com.destinify.destinify.infrastucture.adapters.in.rest.controller;

import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request.CreateReservationRequest;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request.CreateTripRequest;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.ReservationResponse;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.ResponseAPIDefault;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.TripResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Caminho: Controller -> UseCase -> Service -> RepositoryPort -> PersistenceAdapter
@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @PostMapping
    public ResponseEntity<ResponseAPIDefault<ReservationResponse>> create(@RequestBody @Valid CreateReservationRequest request){
        return  null;
    }
}
