
DROP FUNCTION generate_reference_no();

CREATE FUNCTION generate_reference_no()
    RETURNS TABLE(reference_no character varying) 
    LANGUAGE 'plpgsql'
    ROWS 1
AS $BODY$

BEGIN
	IF EXISTS (SELECT 1 FROM application_seq_status WHERE date_of_sequence <> to_char(now(), 'YY')) THEN
		ALTER SEQUENCE application_reference_seq_no RESTART WITH 10000;
		UPDATE application_seq_status SET date_of_sequence = to_char(now(), 'YY');
	END IF;
	RETURN QUERY SELECT ('NAPIX/' || to_char(now(), 'YY') || right(nextval('application_reference_seq_no')::character varying, 3))::character varying;
END;	

$BODY$;

