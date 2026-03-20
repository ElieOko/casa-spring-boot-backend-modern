SET search_path TO public;

CREATE TABLE IF NOT EXISTS public.paiements (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    reference VARCHAR(255) NOT NULL,
    amount VARCHAR(100) NOT NULL,
    devise VARCHAR(50) DEFAULT 'USD',
    description TEXT,
    type_payment VARCHAR(50) DEFAULT 'MOBILE_MONEY',
    status VARCHAR(50) DEFAULT 'PENDING',
    date_created DATE DEFAULT CURRENT_DATE,
    date_updated DATE DEFAULT CURRENT_DATE,

    CONSTRAINT fk_paiement_user
        FOREIGN KEY (user_id) REFERENCES public.users(id) ON DELETE CASCADE
);