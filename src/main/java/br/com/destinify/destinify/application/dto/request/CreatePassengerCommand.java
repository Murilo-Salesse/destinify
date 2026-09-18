package br.com.destinify.destinify.application.dto.request;

import br.com.destinify.destinify.domain.enums.DocumentType;

public record CreatePassengerCommand(
        String fullName,
        String documentNumber,
        DocumentType documentType,
        Integer age,
        String city
) {}
