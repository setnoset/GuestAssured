CREATE EXTENSION btree_gist;

CREATE TABLE guests (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    name varchar(500) NOT NULL,
    document varchar(11) UNIQUE NOT NULL,
    phone varchar(13)
);

CREATE TABLE check_in(
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    guest int NOT NULL REFERENCES guests,
    date_in timestamp NOT NULL,
    date_out timestamp NOT NULL,
    parking boolean NOT NULL,
    price numeric CHECK (price > 0),
    CHECK (date_out > date_in),
    EXCLUDE USING gist (guest WITH =, tsrange(date_in, date_out) WITH &&)
);
