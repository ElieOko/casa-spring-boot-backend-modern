SET search_path TO public;

ALTER TABLE public.bureau
ADD COLUMN address VARCHAR(255) NOT NULL,
ADD COLUMN commune_value VARCHAR(255) DEFAULT '',
ADD COLUMN quartier_value VARCHAR(255) DEFAULT '',
ADD COLUMN city_value VARCHAR(255) DEFAULT '',
ADD COLUMN country_value VARCHAR(255) DEFAULT '',
ADD COLUMN city_id BIGINT NULL,
ADD COLUMN postal_code VARCHAR(50) DEFAULT '',
ADD COLUMN commune_id BIGINT NULL,
ADD COLUMN quartier_id BIGINT NULL,
ADD COLUMN electric INTEGER DEFAULT 0,
ADD COLUMN water INTEGER DEFAULT 0,
ADD COLUMN latitude DOUBLE PRECISION,
ADD COLUMN longitude DOUBLE PRECISION;

ALTER TABLE public.salle_funeraires
ADD COLUMN water INTEGER DEFAULT 0;

ALTER TABLE public.salle_festives
ADD COLUMN water INTEGER DEFAULT 0;

ALTER TABLE public.bureau
ADD CONSTRAINT fk_bureau_city
    FOREIGN KEY (city_id) REFERENCES public.city(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL;

ALTER TABLE public.bureau
ADD CONSTRAINT fk_bureau_commune
    FOREIGN KEY (commune_id) REFERENCES public.commune(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL;

ALTER TABLE public.bureau
ADD CONSTRAINT fk_bureau_quartier
    FOREIGN KEY (quartier_id) REFERENCES public.quartier(id)
    ON UPDATE CASCADE
    ON DELETE SET NULL;
