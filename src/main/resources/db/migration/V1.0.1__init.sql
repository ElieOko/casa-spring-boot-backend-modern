SET search_path TO public;

CREATE TABLE IF NOT EXISTS public.users (
    id BIGSERIAL PRIMARY KEY,
    city VARCHAR(100),
    password TEXT,
    email VARCHAR(150) UNIQUE,
    username VARCHAR(100),
    is_premium BOOLEAN DEFAULT FALSE,
    is_certified BOOLEAN DEFAULT FALSE,
    phone VARCHAR(30),
    country VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS public.typecards (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.persons (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    full_name VARCHAR(200) NOT NULL,
    address TEXT,
    images TEXT,
    card_front TEXT,
    card_back TEXT,
    number_card TEXT,
    user_id BIGINT,
    parrain_id BIGINT,
    type_card_id BIGINT,
    CONSTRAINT fk_person_user
        FOREIGN KEY (user_id) REFERENCES public.users(id),

    CONSTRAINT fk_person_parrain
        FOREIGN KEY (parrain_id) REFERENCES public.users(id),

    CONSTRAINT fk_person_typecard
        FOREIGN KEY (type_card_id) REFERENCES public.typecards(id)
);

CREATE TABLE IF NOT EXISTS public.type_accounts (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.type_account_users (
    id BIGSERIAL PRIMARY KEY,
    type_account_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_tau_type_account
        FOREIGN KEY (type_account_id)
        REFERENCES public.type_accounts(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_tau_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id)
        ON DELETE CASCADE,

    CONSTRAINT uq_type_account_user
        UNIQUE (type_account_id, user_id)
);

CREATE TABLE IF NOT EXISTS public.accounts (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type_account_id BIGINT NOT NULL,

    CONSTRAINT fk_account_type_account
        FOREIGN KEY (type_account_id)
        REFERENCES public.type_accounts(id)
        ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS public.refresh_tokens (
    user_id BIGINT PRIMARY KEY,
    expires_at TIMESTAMP,
    hashed_token TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_refresh_token_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.countries (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.cities (
    id BIGSERIAL PRIMARY KEY,
    country_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT fk_city_country
        FOREIGN KEY (country_id)
        REFERENCES public.countries(id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.districts (
    id BIGSERIAL PRIMARY KEY,
    city_id BIGINT,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT fk_district_city
        FOREIGN KEY (city_id)
        REFERENCES public.cities(id)
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS public.communes (
    id BIGSERIAL PRIMARY KEY,
    district_id BIGINT,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT fk_commune_district
        FOREIGN KEY (district_id)
        REFERENCES public.districts(id)
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS public.quartiers (
    id BIGSERIAL PRIMARY KEY,
    commune_id BIGINT,
    name VARCHAR(100) NOT NULL,
    CONSTRAINT fk_quartier_commune
        FOREIGN KEY (commune_id)
        REFERENCES public.communes(id)
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS public.devises (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(20) NOT NULL,
    taux_local DOUBLE PRECISION DEFAULT 22500.0
);

CREATE TABLE IF NOT EXISTS public.publicities (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    image_path TEXT,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATE DEFAULT CURRENT_DATE,

    CONSTRAINT fk_publicity_user
        FOREIGN KEY (user_id)
        REFERENCES public.users(id)
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS public.features (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS public.property_types (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT DEFAULT ''
);

CREATE TABLE IF NOT EXISTS public.properties (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT DEFAULT '',
    price DOUBLE PRECISION NOT NULL,
    surface DOUBLE PRECISION,
    rooms INT DEFAULT 0,
    bedrooms INT DEFAULT 0,
    kitchen INT DEFAULT 0,
    living_room INT DEFAULT 0,
    bathroom INT DEFAULT 0,
    electric INT DEFAULT 0,
    water INT DEFAULT 0,
    floor INT DEFAULT 0,
    address TEXT NOT NULL,
    commune_value TEXT DEFAULT '',
    quartier_value TEXT DEFAULT '',
    city_value TEXT DEFAULT '',
    country_value TEXT DEFAULT '',
    city_id BIGINT,
    postal_code VARCHAR(20) DEFAULT '',
    commune_id BIGINT,
    quartier_id BIGINT,
    sold BOOLEAN DEFAULT FALSE,
    transaction_type VARCHAR(50) NOT NULL,
    guarantee TEXT DEFAULT '',
    property_type_id BIGINT NOT NULL,
    user_id BIGINT,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    is_available BOOLEAN DEFAULT TRUE,
    created_at DATE DEFAULT CURRENT_DATE,
    updated_at DATE DEFAULT CURRENT_DATE,

    CONSTRAINT fk_property_city
        FOREIGN KEY (city_id) REFERENCES public.cities(id) ON DELETE SET NULL,
    CONSTRAINT fk_property_commune
        FOREIGN KEY (commune_id) REFERENCES public.communes(id) ON DELETE SET NULL,
    CONSTRAINT fk_property_quartier
        FOREIGN KEY (quartier_id) REFERENCES public.quartiers(id) ON DELETE SET NULL,
    CONSTRAINT fk_property_user
        FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE SET NULL,
    CONSTRAINT fk_property_property_type
        FOREIGN KEY (property_type_id) REFERENCES public.property_types(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS public.favorites (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    property_id BIGINT,
    created_at DATE DEFAULT CURRENT_DATE,
    CONSTRAINT fk_favorite_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_favorite_property FOREIGN KEY (property_id) REFERENCES public.properties(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.property_features (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT NOT NULL,
    feature_id BIGINT NOT NULL,
    CONSTRAINT fk_pf_property FOREIGN KEY (property_id) REFERENCES public.properties(id) ON DELETE CASCADE,
    CONSTRAINT fk_pf_feature FOREIGN KEY (feature_id) REFERENCES public.features(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.property_images (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT,
    name VARCHAR(200) NOT NULL,
    path TEXT NOT NULL,
    CONSTRAINT fk_pi_property FOREIGN KEY (property_id) REFERENCES public.properties(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.property_image_kitchens (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT,
    name VARCHAR(200) NOT NULL,
    path TEXT NOT NULL,
    CONSTRAINT fk_pik_property FOREIGN KEY (property_id) REFERENCES public.properties(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.property_image_living_rooms (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT NOT NULL,
    name VARCHAR(200) NOT NULL,
    path TEXT NOT NULL,
    CONSTRAINT fk_pilr_property FOREIGN KEY (property_id) REFERENCES public.properties(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.property_image_rooms (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT,
    name VARCHAR(200) NOT NULL,
    path TEXT NOT NULL,
    CONSTRAINT fk_pir_property FOREIGN KEY (property_id) REFERENCES public.properties(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.property_visits (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    visit_date DATE NOT NULL,
    visit_hour VARCHAR(20) NOT NULL,
    status VARCHAR(50) NOT NULL,
    ip_address VARCHAR(50) NOT NULL,
    cancellation_reason TEXT NOT NULL,
    CONSTRAINT fk_pv_property FOREIGN KEY (property_id) REFERENCES public.properties(id) ON DELETE CASCADE,
    CONSTRAINT fk_pv_user FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.reservations (
    id BIGSERIAL PRIMARY KEY,
    property_id BIGINT NOT NULL,
    user_id BIGINT,
    message TEXT DEFAULT '',
    reservation_heure VARCHAR(50) DEFAULT '',
    status VARCHAR(50) DEFAULT 'PENDING',
    type VARCHAR(50) DEFAULT 'STANDARD',
    is_active BOOLEAN DEFAULT TRUE,
    cancellation_reason TEXT DEFAULT '',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at DATE DEFAULT CURRENT_DATE,

    CONSTRAINT fk_reservation_property
        FOREIGN KEY (property_id) REFERENCES public.properties(id) ON DELETE CASCADE,

    CONSTRAINT fk_reservation_user
        FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS public.notification_reservations (
    id BIGSERIAL PRIMARY KEY,
    reservation_id BIGINT NOT NULL,
    guest_user_id BIGINT NOT NULL,
    host_user_id BIGINT NOT NULL,
    host_user_state BOOLEAN,
    guest_user_state BOOLEAN,
    guest_user_deal_concluded BOOLEAN,
    host_user_deal_concluded BOOLEAN,
    date_updated DATE DEFAULT CURRENT_DATE,
    date_created DATE DEFAULT CURRENT_DATE,

    CONSTRAINT fk_notification_reservation
        FOREIGN KEY (reservation_id) REFERENCES public.reservations(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_guest_user
        FOREIGN KEY (guest_user_id) REFERENCES public.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_host_user
        FOREIGN KEY (host_user_id) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.notification_systems (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    date_created DATE DEFAULT CURRENT_DATE
);

