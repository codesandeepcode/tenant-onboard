
CREATE TABLE esp_transaction_details (
    id SERIAL PRIMARY KEY,
    application_id INTEGER NOT NULL REFERENCES publisher_application_details(application_id),
    esp_version VARCHAR(3),
    transaction_id VARCHAR(36) NOT NULL,
    signing_person VARCHAR(99),
    ekyc_type CHAR(1),
    authentication_mode CHAR(1),
    sign_type VARCHAR(10),
    request_timestamp TIMESTAMP WITHOUT TIME ZONE,
    response_status CHAR(1),
    response_code VARCHAR(99),
    response_error_code VARCHAR(99),
    response_error_message VARCHAR(254),
    response_x509_cert bytea,
    signed_doc_hash bytea,
    doc_sign_error VARCHAR(254),
    response_timestamp TIMESTAMP WITHOUT TIME ZONE,
    sign_hash_algorithm VARCHAR(255)
);