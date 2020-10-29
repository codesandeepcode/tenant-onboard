
--Create Table

CREATE TABLE master_rank_title (
    id SMALLINT PRIMARY KEY,
    position VARCHAR(30),
    active SMALLINT
);

INSERT INTO master_rank_title(id, position, active)
    VALUES
        (1, 'Head of Group', 1),
        (2, 'Head of Division', 1),
        (3, 'Technical Admin', 1),
        (4, 'Project Head', 1),
        (5, 'Technical Head', 1);