
-- Create Table
CREATE TABLE master_application_status (
    id smallint PRIMARY KEY,
    name varchar(20),
    active smallint NOT NULL DEFAULT 0 
);

INSERT INTO master_application_status(id, name, active)
    VALUES 
        (1, 'in_process', 1),
        (2, 'completed', 1),
        (3, 'rejected', 1);

        