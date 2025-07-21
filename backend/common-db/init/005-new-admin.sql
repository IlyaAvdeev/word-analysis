\connect words;

CREATE USER p_user WITH PASSWORD 'p_password';
ALTER DATABASE words OWNER TO p_user;