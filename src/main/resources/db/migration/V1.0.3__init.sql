SET search_path TO public;

CREATE TABLE IF NOT EXISTS public.notification_reservation_bureaux (
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
        FOREIGN KEY (reservation_id) REFERENCES public.reservation_bureau(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_guest_user
        FOREIGN KEY (guest_user_id) REFERENCES public.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_host_user
        FOREIGN KEY (host_user_id) REFERENCES public.users(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS public.notification_reservation_festives (
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
        FOREIGN KEY (reservation_id) REFERENCES public.reservation_festives(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_guest_user
        FOREIGN KEY (guest_user_id) REFERENCES public.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_host_user
        FOREIGN KEY (host_user_id) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.notification_reservation_funeraires (
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
        FOREIGN KEY (reservation_id) REFERENCES public.reservation_funeraires(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_guest_user
        FOREIGN KEY (guest_user_id) REFERENCES public.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_host_user
        FOREIGN KEY (host_user_id) REFERENCES public.users(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS public.notification_reservation_hotels (
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
        FOREIGN KEY (reservation_id) REFERENCES public.reservation_hotel_chambres(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_guest_user
        FOREIGN KEY (guest_user_id) REFERENCES public.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_host_user
        FOREIGN KEY (host_user_id) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.notification_reservation_terrains (
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
        FOREIGN KEY (reservation_id) REFERENCES public.reservation_terrains(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_guest_user
        FOREIGN KEY (guest_user_id) REFERENCES public.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_host_user
        FOREIGN KEY (host_user_id) REFERENCES public.users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.notification_reservation_vacances (
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
        FOREIGN KEY (reservation_id) REFERENCES public.reservation_vacances(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_guest_user
        FOREIGN KEY (guest_user_id) REFERENCES public.users(id) ON DELETE CASCADE,
    CONSTRAINT fk_notification_host_user
        FOREIGN KEY (host_user_id) REFERENCES public.users(id) ON DELETE CASCADE
);