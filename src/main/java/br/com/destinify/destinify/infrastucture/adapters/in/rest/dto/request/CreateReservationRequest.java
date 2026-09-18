package br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request;

import br.com.destinify.destinify.application.dto.request.CreateReservationCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.UUID;

public record CreateReservationRequest(
        @NotNull(message = "O ID da viagem é obrigatório")
        UUID tripId,

        @NotBlank(message = "O nome do contato é obrigatório")
        String contactName,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "O email precisa ser válido")
        String contactEmail,

        @NotBlank(message = "O telefone é obrigatório")
        String contactPhone,

        @NotNull(message = "A quantidade de assentos é obrigatória")
        @Positive(message = "A quantidade de assentos deve ser maior que zero")
        Integer seatsCount,

        @NotBlank(message = "O local de embarque é obrigatório")
        String boardingLocation,

        @Valid
        java.util.List<CreatePassengerRequest> passengers
) {
    public CreateReservationCommand toCommand() {
        java.util.List<br.com.destinify.destinify.application.dto.request.CreatePassengerCommand> passengerCommands = 
                passengers != null ? passengers.stream().map(CreatePassengerRequest::toCommand).toList() : java.util.List.of();

        return new CreateReservationCommand(
                tripId,
                contactName,
                contactEmail,
                contactPhone,
                seatsCount,
                boardingLocation,
                passengerCommands
        );
    }
}