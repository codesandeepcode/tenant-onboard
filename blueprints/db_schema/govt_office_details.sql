
--Create Table


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