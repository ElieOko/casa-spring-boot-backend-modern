SET search_path TO public;

CREATE TABLE IF NOT EXISTS public.hotel_images (
    id BIGSERIAL PRIMARY KEY,
    hotel_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(255),

    CONSTRAINT fk_hotel_images_hotel
        FOREIGN KEY (hotel_id)
        REFERENCES public.hotels(id)
        ON DELETE CASCADE
);

