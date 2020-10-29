
-- Create Table 

CREATE TABLE application_seq_status (
    date_of_sequence VARCHAR
);

-- Required to prevent complications arising from empty records (see 'generate_reference_no.sql')
INSERT INTO application_seq_status VALUES (0);