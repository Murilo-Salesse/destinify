package br.com.destinify.destinify.infrastucture.adapters.in.rest.dto.request;

import br.com.destinify.destinify.application.dto.request.CreatePassengerCommand;
import br.com.destinify.destinify.domain.enums.DocumentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePassengerRequest(
        @NotBlank(message = "O nome completo do passageiro é obrigatório")
        String fullName,

        @NotBlank(message = "O número do documento é obrigatório")
        String documentNumber,

        @NotNull(message = "O tipo do documento é obrigatório")
        DocumentType documentType,

        @NotNull(message = "A idade é obrigatória")
        @Min(value = 0, message = "A idade não pode ser negativa")
        Integer age,

        String city
) {
    public CreatePassengerCommand toCommand() {
        return new CreatePassengerCommand(fullName, documentNumber, documentType, age, city);
    }
}
