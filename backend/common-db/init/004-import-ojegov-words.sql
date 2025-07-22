\connect words;

COPY public.ojegov FROM '/ojegov-source/prepared_voc.txt' WITH ( FORMAT CSV, DELIMITER '#', HEADER false, ENCODING 'UTF8', QUOTE '`', ESCAPE '\');