INSERT INTO TB_CATEGORY (ID, DESCRIPTION) VALUES (1000, 'Comic Books');
INSERT INTO TB_CATEGORY (ID, DESCRIPTION) VALUES (1001, 'Movies');
INSERT INTO TB_CATEGORY (ID, DESCRIPTION) VALUES (1002, 'Books');

INSERT INTO TB_SUPPLIER (ID, NAME) VALUES (1000, 'Panini Comics');
INSERT INTO TB_SUPPLIER (ID, NAME) VALUES (1001, 'Amazon');

INSERT INTO TB_PRODUCT (ID, NAME, FK_SUPPLIER, FK_CATEGORY, QUANTITY_AVAILABLE, CREATED_AT) VALUES (1001, 'Crise nas Infinitas Terras', 1000, 1000, 10, CURRENT_TIMESTAMP);
INSERT INTO TB_PRODUCT (ID, NAME, FK_SUPPLIER, FK_CATEGORY, QUANTITY_AVAILABLE, CREATED_AT) VALUES (1002, 'Interestelar', 1001, 1001, 5, CURRENT_TIMESTAMP);
INSERT INTO TB_PRODUCT (ID, NAME, FK_SUPPLIER, FK_CATEGORY, QUANTITY_AVAILABLE, CREATED_AT) VALUES (1003, 'Harry Potter E A Pedra Filosofal', 1001, 1002, 3, CURRENT_TIMESTAMP);