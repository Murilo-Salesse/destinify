CREATE TABLE reservations (
    id UUID PRIMARY KEY,
    trip_id UUID NOT NULL,
    contact_name VARCHAR(150) NOT NULL,
    contact_email VARCHAR(150) NOT NULL,
    contact_phone VARCHAR(30) NOT NULL,
    total_amount NUMERIC(10, 2) NOT NULL,
    seats_count INTEGER NOT NULL,
    boarding_location VARCHAR(200) NOT NULL,
    status VARCHAR(30) NOT NULL,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    confirmed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT fk_reservations_trip FOREIGN KEY (trip_id) REFERENCES trips(id) ON DELETE RESTRICT
);

CREATE TABLE passengers (
    id UUID PRIMARY KEY,
    reservation_id UUID NOT NULL,
    full_name VARCHAR(150) NOT NULL,
    document_number VARCHAR(30) NOT NULL,
    document_type VARCHAR(20) NOT NULL,
    age INTEGER NOT NULL,
    city VARCHAR(100),
    ticket_code VARCHAR(50) NOT NULL UNIQUE,
    boarded BOOLEAN NOT NULL DEFAULT FALSE,
    boarded_at TIMESTAMP WITH TIME ZONE,

    CONSTRAINT fk_passengers_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(id) ON DELETE CASCADE
);

-- Índices para buscas rápidas
CREATE INDEX idx_reservations_trip_status ON reservations(trip_id, status);
CREATE INDEX idx_passengers_ticket_code ON passengers(ticket_code);