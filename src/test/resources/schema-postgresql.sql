

-- Tables

CREATE TABLE application_seq_status (
    date_of_sequence VARCHAR
);


@@


INSERT INTO application_seq_status VALUES (0);


@@


CREATE TABLE master_application_status (
    id smallint PRIMARY KEY,
    name varchar(20),
    active smallint NOT NULL DEFAULT 0 
);


@@


CREATE TABLE master_state (
    state_code VARCHAR(2) PRIMARY KEY,
    state_name_en VARCHAR(50),
    state_name_ll VARCHAR(50),
    active SMALLINT
);


@@


CREATE TABLE master_department (
    id serial PRIMARY KEY,
    name varchar(100),
    central boolean,
    state boolean,
    active smallint
);


@@


CREATE TABLE master_govt_type (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(10),
    active  SMALLINT
);


@@


CREATE TABLE master_office_category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(225) NOT NULL,
    active SMALLINT NOT NULL DEFAULT 0
);


@@


CREATE TABLE master_rank_title (
    id SMALLINT PRIMARY KEY,
    position VARCHAR(30),
    active SMALLINT
);


@@


CREATE TABLE publisher_application_details (
    application_id SERIAL PRIMARY KEY,
    application_reference_no VARCHAR(18) NOT NULL UNIQUE,
    domain_name VARCHAR(30) NOT NULL UNIQUE,
    project_name VARCHAR(30),
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    application_remarks VARCHAR(255),
    application_status_id SMALLINT REFERENCES master_application_status(id),
    active SMALLINT
);


@@


CREATE TABLE nic_office_details (
    application_id INTEGER PRIMARY KEY REFERENCES publisher_application_details(application_id),
    group_name VARCHAR(99),
    active SMALLINT
);


@@


CREATE TABLE nic_personnel_details (
    id SERIAL PRIMARY KEY,
    application_id INTEGER NOT NULL REFERENCES publisher_application_details(application_id),
    title_id SMALLINT NOT NULL REFERENCES master_rank_title(id),
    employee_code INTEGER NOT NULL,
    employee_name VARCHAR(99),
    designation VARCHAR(99),
    department VARCHAR(99),
    address VARCHAR(99),
    email_id VARCHAR(99),
    mobile_number varchar(10),
    landline_number varchar(15),
    active SMALLINT
);


@@


CREATE TABLE govt_office_details (
    application_id INTEGER PRIMARY KEY REFERENCES publisher_application_details(application_id),
    govt_type_id SMALLINT NOT NULL REFERENCES master_govt_type(id),
    department_id INTEGER REFERENCES master_department(id),
    state_id VARCHAR(2) REFERENCES master_state(state_code),
    office_category_id INTEGER REFERENCES master_office_category(id),
    office_name VARCHAR(99),
    office_address VARCHAR(99),
    group_name VARCHAR(99),
    active SMALLINT
);


@@


CREATE TABLE govt_personnel_details (
    id SERIAL PRIMARY KEY,
    application_id INTEGER NOT NULL REFERENCES publisher_application_details(application_id),
    title_id SMALLINT NOT NULL REFERENCES master_rank_title(id),
    employee_code VARCHAR(99),
    employee_name VARCHAR(99),
    designation VARCHAR(99),
    email_id VARCHAR(99),
    mobile_number varchar(10),
    landline_number varchar(15),
    active SMALLINT
);


@@


CREATE TABLE others_office_details (
  application_id INTEGER PRIMARY KEY REFERENCES publisher_application_details(application_id),
  state_id VARCHAR(2) REFERENCES master_state(state_code),
  company_name VARCHAR(99),
  company_address VARCHAR(99),
  active smallint
);


@@


CREATE TABLE others_personnel_details (
    id SERIAL PRIMARY KEY,
    application_id INTEGER NOT NULL REFERENCES publisher_application_details(application_id),
    title_id SMALLINT NOT NULL REFERENCES master_rank_title(id),
    employee_code VARCHAR(99),
    employee_name VARCHAR(99),
    designation VARCHAR(99),
    email_id VARCHAR(99),
    mobile_number varchar(10),
    landline_number varchar(15),
    active SMALLINT
);


@@


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


@@


-- Sequences

CREATE SEQUENCE IF NOT EXISTS esp_transaction_number_seq
    AS BIGINT
    INCREMENT BY 1
    START WITH 1;


@@


CREATE SEQUENCE IF NOT EXISTS application_reference_seq_no
    AS INTEGER
    INCREMENT BY 1
    START WITH 10000;


@@


-- Stored procedures

CREATE FUNCTION generate_reference_no()
    RETURNS TABLE(reference_no character varying) 
    LANGUAGE 'plpgsql'
    ROWS 1
AS $BODY$

BEGIN
    IF EXISTS (SELECT 1 FROM application_seq_status WHERE date_of_sequence <> to_char(now(), 'YY')) THEN
        ALTER SEQUENCE application_reference_seq_no RESTART WITH 10000;
        UPDATE application_seq_status SET date_of_sequence = to_char(now(), 'YY');
    END IF;
    RETURN QUERY SELECT ('NAPIX/' || to_char(now(), 'YY') || right(nextval('application_reference_seq_no')::character varying, 3))::character varying;
END;    

$BODY$;

@@