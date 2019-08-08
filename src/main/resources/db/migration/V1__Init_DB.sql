CREATE TABLE usr (
    id bigserial NOT NULL,
    email character varying (254) NOT NULL CHECK (email <> ''),
    password character varying(60) NOT NULL CHECK (password <> ''),
    role integer NOT NULL CHECK (role = ANY(ARRAY[0, 1, 2])),
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ON usr (email);

COMMENT ON COLUMN usr.role IS '0 - админ, 1 - курьер, 2 - оператор';

CREATE TABLE client (
    id bigserial NOT NULL,
    email character varying(254) NOT NULL CHECK (email <> ''),
    mobile character varying(10) NOT NULL CHECK (mobile <> ''),
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ON client (email);
CREATE UNIQUE INDEX ON client (mobile);

CREATE TABLE ord (
    id bigserial NOT NULL,
    number character varying(10) NOT NULL CHECK (number <> ''),
    state integer NOT NULL CHECK (state = ANY(ARRAY[0, 1])),
    date_delivery timestamp with time zone NOT NULL,
    id_client bigint NOT NULL,
    address character varying(500) NOT NULL CHECK (address <> ''),
    description character varying(500) NOT NULL CHECK (description <> ''),
    PRIMARY KEY (id),
    FOREIGN KEY (id_client) REFERENCES client (id) ON DELETE CASCADE
);

COMMENT ON COLUMN ord.state IS '0 - новый, 1 - в обработке, 2 - готов к доставке, 3 - доставляется, 4 - доставлен, 5 - отменён';

CREATE UNIQUE INDEX ON ord (number);
CREATE INDEX ON ord (id_client);

CREATE TABLE task_call (
    id bigserial NOT NULL,
    state integer NOT NULL CHECK (state = ANY(ARRAY[0, 1, 2])),
    id_ord bigint NOT NULL,
    id_courier bigint NOT NULL,
    id_operator bigint,
    PRIMARY KEY (id),
    FOREIGN KEY (id_ord) REFERENCES ord (id) ON DELETE CASCADE,
    FOREIGN KEY (id_courier) REFERENCES usr (id) ON DELETE CASCADE,
    FOREIGN KEY (id_operator) REFERENCES usr (id) ON DELETE CASCADE
);

COMMENT ON COLUMN task_call.state IS '0 - новая, 1 - обработана, 2 - отменена';