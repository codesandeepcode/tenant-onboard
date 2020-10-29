
-- Create Table

CREATE TABLE master_govt_type (
    id SMALLINT PRIMARY KEY,
    name VARCHAR(10),
    active  SMALLINT
);


-- Insert Data

INSERT INTO master_govt_type(id, name, active)
    VALUES 
        (1, 'Central', 1),
        (2, 'State', 1);