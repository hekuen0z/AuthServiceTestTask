CREATE TABLE IF NOT EXISTS users(
    id IDENTITY PRIMARY KEY,
    login varchar(50) NOT NULL UNIQUE,
    password varchar NOT NULL,
    access_code varchar NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS roles(
    id IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS user_role(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    CONSTRAINT pk_user_id_role_id PRIMARY KEY (user_id, role_id)
);
