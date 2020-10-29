
-- Create Table

CREATE TABLE nic_personnel_details (
    id SERIAL PRIMARY KEY,
    application_id INTEGER NOT NULL REFERENCES publisher_application_details(application_id),
    title_id SMALLINT NOT NULL REFERENCES rank_title(id),
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