use recipe;

create table hibernate_sequence(
    next_val INTEGER NOT null
);

INSERT INTO hibernate_sequence (next_val) VALUES (1);

CREATE TABLE IF NOT EXISTS recipe (
    id bigint(20) primary key auto_increment,
    creation_date_time timestamp,
    name varchar(50) not null unique,
    vegetarian smallint(1) not null,
    servings int not null,
    cooking_instructions varchar(2000) not null
);

CREATE TABLE IF NOT EXISTS ingredients_list (
    id bigint(20) NOT NULL,
    ingredients_list varchar(255) DEFAULT NULL,
    FOREIGN KEY (id) REFERENCES recipe(id)
);