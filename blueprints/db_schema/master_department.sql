
-- Create Table
CREATE TABLE master_department (
	id serial PRIMARY KEY,
	name varchar(100),
	central boolean,
	state boolean,
	active smallint
);

-- Insert data
INSERT INTO public.master_department(name, central, state, active)
	VALUES 
		('Cabinet Secretariat', 		TRUE, FALSE, 1), 
	 	('Department of Atomic Energy', TRUE, FALSE, 1),
	 	('Ministry of Agriculture', 	FALSE, TRUE, 1),
	 	('Ministry of Coal', 			FALSE, TRUE, 1);