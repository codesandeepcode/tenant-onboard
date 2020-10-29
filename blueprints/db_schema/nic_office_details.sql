
--Create Table 

CREATE TABLE nic_office_details (
    application_id INTEGER PRIMARY KEY REFERENCES publisher_application_details(application_id),
    group_name VARCHAR(99),
    active SMALLINT
);