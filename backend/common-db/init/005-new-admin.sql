\connect words;

CREATE USER p_user WITH PASSWORD 'p_password';
GRANT SELECT on public.all_words, public.ojegov TO p_user;
ALTER DATABASE words OWNER TO p_user;