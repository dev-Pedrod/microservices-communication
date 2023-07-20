-- CATEGORY
CREATE TABLE IF NOT EXISTS tb_category
(
    id          bigserial    NOT NULL,
    created_at  timestamp(6),
    disabled_at timestamp(6),
    updated_at  timestamp(6),
    description VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

-- PRODUCT
CREATE TABLE IF NOT EXISTS tb_product
(
    id                 bigserial    NOT NULL,
    created_at         timestamp(6),
    disabled_at        timestamp(6),
    updated_at         timestamp(6),
    name               VARCHAR(255) NOT NULL,
    quantity_available INTEGER      NOT NULL,
    fk_category        BIGINT       NOT NULL,
    fk_supplier        BIGINT       NOT NULL,
    PRIMARY KEY (id)
);

-- SUPPLIER
CREATE TABLE IF NOT EXISTS tb_supplier
(
    id          bigserial    NOT NULL,
    created_at  timestamp(6),
    disabled_at timestamp(6),
    updated_at  timestamp(6),
    name        VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS tb_product
    ADD CONSTRAINT fk_product_category
        FOREIGN KEY (fk_category)
            REFERENCES tb_category;

ALTER TABLE IF EXISTS tb_product
    ADD CONSTRAINT fk_product_supplier
        FOREIGN KEY (fk_supplier)
            REFERENCES tb_supplier