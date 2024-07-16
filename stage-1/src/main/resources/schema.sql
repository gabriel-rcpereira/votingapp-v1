CREATE TABLE pool
(
    id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    version INT NOT NULL,
    expired_at TIMESTAMP NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE pool_option
(
    id BIGINT NOT NULL,
    pool_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    version BIGINT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (pool_id) REFERENCES pool(id)
);

CREATE TABLE vote
(
    id BIGINT NOT NULL,
    pool_option_id BIGINT NOT NULL,
    version BIGINT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY (pool_option_id) REFERENCES pool_option(id)
);