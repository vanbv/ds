CREATE TABLE usr (
    id serial NOT NULL,
    email character varying (254) NOT NULL CHECK (email <> ''),
    mobile character varying(10) NOT NULL CHECK (mobile <> ''),
    password character varying(60) NOT NULL CHECK (password <> ''),
    role integer NOT NULL CHECK (role = ANY(ARRAY[0, 1, 2])),
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ON usr (email);
CREATE UNIQUE INDEX ON usr (mobile);

COMMENT ON COLUMN usr.role IS '0 - админ, 1 - курьер, 2 - оператор';

CREATE TABLE client (
    id serial NOT NULL,
    email character varying(254) NOT NULL CHECK (email <> ''),
    mobile character varying(10) NOT NULL CHECK (mobile <> ''),
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX ON client (email);
CREATE UNIQUE INDEX ON client (mobile);

CREATE TABLE ord (
    id serial NOT NULL,
    number character varying(10) NOT NULL CHECK (number <> ''),
    state integer NOT NULL DEFAULT 0 CHECK (state = ANY(ARRAY[0, 1, 2, 3, 4])),
    date_delivery timestamp with time zone NOT NULL,
    id_client integer NOT NULL,
    address character varying(500) NOT NULL CHECK (address <> ''),
    description character varying(500) NOT NULL CHECK (description <> ''),
    PRIMARY KEY (id),
    FOREIGN KEY (id_client) REFERENCES client (id) ON DELETE CASCADE
);

COMMENT ON COLUMN ord.state IS '0 - новый, 1 - в обработке, 2 - готов к доставке, 3 - доставляется, 4 - доставлен, 5 - отменён';

CREATE UNIQUE INDEX ON ord (number);
CREATE INDEX ON ord (id_client);

CREATE TABLE task_call (
    id serial NOT NULL,
    state integer NOT NULL DEFAULT 0 CHECK (state = ANY(ARRAY[0, 1, 2])),
    description character varying(200) NOT NULL CHECK (description <> ''),
    date_create timestamp with time zone DEFAULT NOW() NOT NULL,
    id_ord integer NOT NULL,
    id_courier integer NOT NULL,
    id_operator integer,
    PRIMARY KEY (id),
    FOREIGN KEY (id_ord) REFERENCES ord (id) ON DELETE CASCADE,
    FOREIGN KEY (id_courier) REFERENCES usr (id) ON DELETE CASCADE,
    FOREIGN KEY (id_operator) REFERENCES usr (id) ON DELETE CASCADE
);

CREATE INDEX ON task_call (id_ord);
CREATE INDEX ON task_call (id_courier);
CREATE INDEX ON task_call (id_operator);

COMMENT ON COLUMN task_call.state IS '0 - новая, 1 - обработана, 2 - отменена';