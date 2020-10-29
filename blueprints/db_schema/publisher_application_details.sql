
-- Create Table 

CREATE TABLE publisher_application_details (
    application_id SERIAL PRIMARY KEY,
    application_reference_no VARCHAR(18) NOT NULL UNIQUE,
    domain_name VARCHAR(30) NOT NULL UNIQUE,
    project_name VARCHAR(30),
    creation_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    application_remarks VARCHAR(255),
    application_status_id SMALLINT REFERENCES application_status(id),
    active SMALLINT
);