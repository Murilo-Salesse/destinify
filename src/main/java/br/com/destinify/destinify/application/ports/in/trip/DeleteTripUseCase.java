package br.com.destinify.destinify.application.ports.in.trip;

import java.util.UUID;

public interface DeleteTripUseCase {
    void execute(UUID id);
}
