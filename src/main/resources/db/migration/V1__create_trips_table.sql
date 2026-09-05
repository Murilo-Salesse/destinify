CREATE TABLE trips (
    id UUID PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    destination VARCHAR(150) NOT NULL,
    departure_at TIMESTAMP WITH TIME ZONE NOT NULL,
    return_at TIMESTAMP WITH TIME ZONE NOT NULL,
    price NUMERIC(10, 2) NOT NULL,
    total_seats INTEGER NOT NULL,
    available_seats INTEGER NOT NULL,
    description TEXT,
    included_items TEXT,
    cover_image_url VARCHAR(500),
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE
);

-- Índice para acelerar a busca de viagens disponíveis/publicadas
CREATE INDEX idx_trips_status_departure ON trips(status, departure_at);