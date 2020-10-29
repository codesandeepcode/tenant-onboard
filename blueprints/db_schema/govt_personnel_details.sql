
-- Create Table

CREATE TABLE govt_personnel_details (
    id SERIAL PRIMARY KEY,
    application_id INTEGER NOT NULL REFERENCES publisher_application_details(application_id),
    title_id SMALLINT NOT NULL REFERENCES rank_title(id),
    employee_code VARCHAR(99),
    employee_name VARCHAR(99),
    designation VARCHAR(99),
    email_id VARCHAR(99),
    mobile_number varchar(10),
    landline_number varchar(15),
    active SMALLINT
);