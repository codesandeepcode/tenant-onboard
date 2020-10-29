
-- Create Table
CREATE TABLE master_office_category 
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(225) NOT NULL,
    active SMALLINT NOT NULL DEFAULT 0
)


-- Insert Data
INSERT INTO master_office_category(name, active)
    VALUES
        ('Ministries/Departments/Attached Offices of Central Govt', 1),
        ('Subordinate office of Central Govt having no source of income', 1),
        ('Ministries/Departments and Offices of the State Govt', 1),
        ('Statutory bodies fully funded by Center/State Govt with no internal revenue resources', 1),
        ('Public sector undertakings', 1),
        ('Autonomous/Statutory bodies not fully funded by Central Govt/State Govt', 1),
        ('Autonomous/Statutory bodies under Central Govt/State Govt/Societies generating internal revenue apart from the grants they receive', 1),
        ('Others', 1);