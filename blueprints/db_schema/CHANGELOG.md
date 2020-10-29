# Changelog in SQL

-   [ ] The following below changes are made to production database

## (14-9-2020)

-   Changed **Application Reference no** format by changing the code in **generate_reference_no.sql** file

## (19-9-2020)

-   Add **UNIQUE** and **NOT NULL** constraints to column '_domain_name_' in table '_publisher_application_details_'

## Unknown date

-   Change name of 'government_type' table to 'master_govt_type' table

## (20-10-2020)

-   Rename table '_rank_title_' to '_master_rank_title_' & also its file name

-   Rename table '_application_status_' to '_master_application_status_' & also its file name

-   Correct table name references from '_government_type_' to '_master_govt_type_' in '**govt_office_details**' table

-   Create **INSERT** query in **application_seq_status.sql** file to avoid compilications of blank records

-   Create _application_reference_seq_no_ sequence in a new file
