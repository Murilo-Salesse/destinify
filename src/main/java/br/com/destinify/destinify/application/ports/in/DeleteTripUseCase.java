package br.com.destinify.destinify.application.ports.in;

import java.util.UUID;

public interface DeleteTripUseCase {
    void execute(UUID id);
}
