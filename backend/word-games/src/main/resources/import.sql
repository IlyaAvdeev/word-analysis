-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
insert into Ojegov (id, word, explanation) values(1, 'смола', 'какое-то пояснение');
insert into Ojegov (id, word, explanation) values(2, 'слива', 'какое-то пояснение2');
insert into Ojegov (id, word, explanation) values(3, 'вышка', 'какое-то пояснение3');
insert into Ojegov (id, word, explanation) values(4, 'пижон', 'какое-то пояснение4');
insert into Ojegov (id, word, explanation) values(5, 'каблук', 'какое-то пояснение5');
insert into Ojegov (id, word, explanation) values(6, 'яйцо', 'какое-то пояснение6');
insert into Ojegov (id, word, explanation) values(7, 'купон', 'какое-то пояснение7');
insert into Ojegov (id, word, explanation) values(8, 'слив…', 'какое-то пояснение8');


insert into all_words (id, word) values(1, 'смола');
insert into all_words (id, word) values(2, 'слива');
insert into all_words (id, word) values(3, 'вышка');
insert into all_words (id, word) values(4, 'пижон');
insert into all_words (id, word) values(5, 'каблук');
insert into all_words (id, word) values(6, 'яйцо');
insert into all_words (id, word) values(7, 'купон');
insert into all_words (id, word) values(8, 'слив…');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;