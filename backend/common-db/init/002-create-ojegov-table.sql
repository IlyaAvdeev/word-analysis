\connect words;

CREATE TABLE public.ojegov (
    id BIGINT PRIMARY KEY GENERATED always as identity,
    word VARCHAR(80) NOT NULL,
    explanation TEXT NOT NULL
);

CREATE INDEX IF NOT EXISTS word_idx ON ojegov(word) WITH (deduplicate_items = off);