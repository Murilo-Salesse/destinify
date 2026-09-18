package br.com.destinify.destinify.infrastucture.adapters.in.rest.controller;

import br.com.destinify.destinify.application.ports.in.passenger.BoardPassengerUseCase;
import br.com.destinify.destinify.domain.model.Passenger;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.PassengerResponse;
import br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.response.ResponseAPIDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/passengers")
public class PassengerController {

    private final BoardPassengerUseCase boardPassengerUseCase;

    public PassengerController(BoardPassengerUseCase boardPassengerUseCase) {
        this.boardPassengerUseCase = boardPassengerUseCase;
    }

    @PatchMapping("/{ticketCode}/board")
    public ResponseEntity<ResponseAPIDefault<PassengerResponse>> board(@PathVariable("ticketCode") String ticketCode) {
        Passenger passenger = boardPassengerUseCase.execute(ticketCode);
        return ResponseEntity.ok(new ResponseAPIDefault<>("Embarque confirmado com sucesso.", PassengerResponse.fromDomain(passenger)));
    }
}
