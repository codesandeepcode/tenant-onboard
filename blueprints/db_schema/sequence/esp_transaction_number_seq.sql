

DROP SEQUENCE IF EXISTS esp_transaction_number_seq;

CREATE SEQUENCE IF NOT EXISTS esp_transaction_number_seq
	AS BIGINT
	INCREMENT BY 1
	START WITH 1;

