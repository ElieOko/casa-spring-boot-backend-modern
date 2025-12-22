SET search_path TO public;

CREATE TABLE IF NOT EXISTS public.sectors(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS public.agencies(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT DEFAULT '',
    phone_1 VARCHAR(50) DEFAULT '',
    phone_2 VARCHAR(50) DEFAULT '',
    address VARCHAR(255) DEFAULT '',
    logo VARCHAR(255),
    CONSTRAINT fk_agencies_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.bureau (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    devise_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT DEFAULT '',
    price DOUBLE PRECISION,
    room_meet INTEGER DEFAULT 0,
    number_piece INTEGER DEFAULT 0,
    is_equip BOOLEAN NOT NULL DEFAULT FALSE,
    address VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bureau_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id),

    CONSTRAINT fk_bureau_devise
        FOREIGN KEY (devise_id)
        REFERENCES public.devises(id)
);

CREATE TABLE IF NOT EXISTS public.bureau_images (
    id BIGSERIAL PRIMARY KEY,
    bureau_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(255),
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bureau_images_bureau
        FOREIGN KEY (bureau_id)
        REFERENCES public.bureau(id)
);

CREATE TABLE IF NOT EXISTS public.vacances (
    id BIGSERIAL PRIMARY KEY,
    agence_id BIGINT,
    user_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT DEFAULT '',
    note TEXT DEFAULT '',
    date_vacance VARCHAR(50),
    heure_vacance VARCHAR(50),
    price DOUBLE PRECISION,
    price_combo TEXT DEFAULT '',
    offre TEXT DEFAULT '',
    capacity INTEGER DEFAULT 0,
    address VARCHAR(255) NOT NULL,
    city_value VARCHAR(255) DEFAULT '',
    country_value VARCHAR(255) DEFAULT '',
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_vacances_agence
        FOREIGN KEY (agence_id)
        REFERENCES public.agencies(id),

    CONSTRAINT fk_vacances_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id)
);

CREATE TABLE IF NOT EXISTS public.vacance_images (
    id BIGSERIAL PRIMARY KEY,
    vacance_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(255),
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_vacance_images_vacance
        FOREIGN KEY (vacance_id)
        REFERENCES public.vacances(id)
);

CREATE TABLE IF NOT EXISTS public.salle_funeraires (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    devise_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT DEFAULT '',
    capacity_people VARCHAR(100) DEFAULT '',
    price DOUBLE PRECISION,
    usage TEXT DEFAULT 'conference, etc',
    address VARCHAR(255) NOT NULL,
    commune_value VARCHAR(255) DEFAULT '',
    quartier_value VARCHAR(255) DEFAULT '',
    city_value VARCHAR(255) DEFAULT '',
    country_value VARCHAR(255) DEFAULT '',
    city_id BIGINT NULL,
    electric INTEGER DEFAULT 0,
    postal_code VARCHAR(50) DEFAULT '',
    commune_id BIGINT NULL,
    quartier_id BIGINT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,
    updated_at DATE NOT NULL DEFAULT CURRENT_DATE,

    CONSTRAINT fk_salle_funeraires_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id),

    CONSTRAINT fk_salle_funeraires_devise
        FOREIGN KEY (devise_id)
        REFERENCES public.devises(id),

    CONSTRAINT fk_salle_funeraires_city
        FOREIGN KEY (city_id)
        REFERENCES public.cities(id),

    CONSTRAINT fk_salle_funeraires_commune
        FOREIGN KEY (commune_id)
        REFERENCES public.communes(id),

    CONSTRAINT fk_salle_funeraires_quartier
        FOREIGN KEY (quartier_id)
        REFERENCES public.quartiers(id)
);

CREATE TABLE IF NOT EXISTS public.salle_funeraire_images (
    id BIGSERIAL PRIMARY KEY,
    salle_funeraire_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(255),
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_salle_funeraire_images_salle_funeraire
        FOREIGN KEY (salle_funeraire_id)
        REFERENCES public.salle_funeraires(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.salle_festives (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    devise_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT DEFAULT '',
    usage TEXT DEFAULT 'conference, mariage, etc',
    capacity_people BIGINT,
    electric INTEGER DEFAULT 0,
    price DOUBLE PRECISION,
    address VARCHAR(255) NOT NULL,
    commune_value VARCHAR(255) DEFAULT '',
    quartier_value VARCHAR(255) DEFAULT '',
    city_value VARCHAR(255) DEFAULT '',
    country_value VARCHAR(255) DEFAULT '',
    city_id BIGINT NULL,
    postal_code VARCHAR(50) DEFAULT '',
    commune_id BIGINT NULL,
    quartier_id BIGINT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    is_decore BOOLEAN NOT NULL DEFAULT FALSE,
    piste_danse BOOLEAN NOT NULL DEFAULT FALSE,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,
    updated_at DATE NOT NULL DEFAULT CURRENT_DATE,

    CONSTRAINT fk_salle_festives_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id),

    CONSTRAINT fk_salle_festives_devise
        FOREIGN KEY (devise_id)
        REFERENCES public.devises(id),

    CONSTRAINT fk_salle_festives_city
        FOREIGN KEY (city_id)
        REFERENCES public.cities(id),

    CONSTRAINT fk_salle_festives_commune
        FOREIGN KEY (commune_id)
        REFERENCES public.communes(id),

    CONSTRAINT fk_salle_festives_quartier
        FOREIGN KEY (quartier_id)
        REFERENCES public.quartiers(id)
);

CREATE TABLE IF NOT EXISTS public.salle_festive_images (
    id BIGSERIAL PRIMARY KEY,
    salle_festive_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(255),
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_salle_festive_images_salle_festive
        FOREIGN KEY (salle_festive_id)
        REFERENCES public.salle_festives(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.hotels (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT DEFAULT '',
    address VARCHAR(255) NOT NULL,
    image VARCHAR(255) NOT NULL,
    commune_value VARCHAR(255) DEFAULT '',
    quartier_value VARCHAR(255) DEFAULT '',
    city_value VARCHAR(255) DEFAULT '',
    country_value VARCHAR(255) DEFAULT '',
    city_id BIGINT NULL,
    postal_code VARCHAR(50) DEFAULT '',
    commune_id BIGINT NULL,
    quartier_id BIGINT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,
    updated_at DATE NOT NULL DEFAULT CURRENT_DATE,

    CONSTRAINT fk_hotels_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id),

    CONSTRAINT fk_hotels_city
        FOREIGN KEY (city_id)
        REFERENCES public.cities(id),

    CONSTRAINT fk_hotels_commune
        FOREIGN KEY (commune_id)
        REFERENCES public.communes(id),

    CONSTRAINT fk_hotels_quartier
        FOREIGN KEY (quartier_id)
        REFERENCES public.quartiers(id)
);

CREATE TABLE IF NOT EXISTS public.hotel_chambres (
    id BIGSERIAL PRIMARY KEY,
    hotel_id BIGINT,
    title VARCHAR(255) NOT NULL,
    description TEXT DEFAULT '',
    number_piece BIGINT,
    price DOUBLE PRECISION,
    price_heure TEXT DEFAULT '',
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,
    updated_at DATE NOT NULL DEFAULT CURRENT_DATE,

    CONSTRAINT fk_hotel_chambres_hotel
        FOREIGN KEY (hotel_id)
        REFERENCES public.hotels(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.hotel_chambre_images (
    id BIGSERIAL PRIMARY KEY,
    hotel_chambre_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(255),
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,

    CONSTRAINT fk_hotel_chambre_images_hotel_chambre
        FOREIGN KEY (hotel_chambre_id)
        REFERENCES public.hotel_chambres(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.prestations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    service_id BIGINT NOT NULL,
    devise_id BIGINT,
    title VARCHAR(255) NOT NULL DEFAULT '',
    description TEXT DEFAULT '',
    experience TEXT NOT NULL DEFAULT '',
    plage_jour_prestation TEXT NOT NULL DEFAULT '',
    plage_heure_prestation TEXT NOT NULL DEFAULT '',
    min_price DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    max_price DOUBLE PRECISION NOT NULL DEFAULT 0.0,
    address VARCHAR(255),
    commune_value VARCHAR(255) DEFAULT '',
    quartier_value VARCHAR(255) DEFAULT '',
    city_value VARCHAR(255) DEFAULT '',
    country_value VARCHAR(255) DEFAULT '',
    city_id BIGINT NULL,
    commune_id BIGINT NULL,
    quartier_id BIGINT NULL,
    is_certified BOOLEAN NOT NULL DEFAULT TRUE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    date_created TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_prestations_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id),

    CONSTRAINT fk_prestations_service
        FOREIGN KEY (service_id)
        REFERENCES public.type_accounts(id),

    CONSTRAINT fk_prestations_devise
        FOREIGN KEY (devise_id)
        REFERENCES public.devises(id)
);

CREATE TABLE IF NOT EXISTS public.prestation_images (
    id BIGSERIAL PRIMARY KEY,
    prestation_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,

    CONSTRAINT fk_prestation_images_prestation
        FOREIGN KEY (prestation_id)
        REFERENCES public.prestations(id)
        ON DELETE CASCADE
);
