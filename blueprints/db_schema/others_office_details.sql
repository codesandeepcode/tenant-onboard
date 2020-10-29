
-- Create Table

CREATE TABLE others_office_details (
  application_id INTEGER PRIMARY KEY REFERENCES publisher_application_details(application_id),
  state_id VARCHAR(2) REFERENCES master_state(state_code),
  company_name VARCHAR(99),
  company_address VARCHAR(99),
  active smallint
);