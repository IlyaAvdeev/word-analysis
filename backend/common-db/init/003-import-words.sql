\connect words;

COPY public.all_words(word) FROM '/init_voc/russian.utf-8' ( FORMAT TEXT );