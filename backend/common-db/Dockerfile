# #RUN localedef -i en_US -c -f UTF-8 -A /usr/share/locale/locale.alias en_US.UTF-8
# ENV LANG en_US.utf8
FROM postgres:17-alpine as builder

COPY ./russian-words/russian.utf-8 /init_voc/
COPY ./init/ /docker-entrypoint-initdb.d/
COPY ./output/prepared_voc.txt /ojegov-source/

RUN ["sed", "-i", "s/exec \"$@\"/echo \"noop...\"/", "/usr/local/bin/docker-entrypoint.sh"]

ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=postgres

RUN ["/usr/local/bin/docker-entrypoint.sh", "postgres"]

# final build stage
FROM postgres:17-alpine

COPY --from=builder $PGDATA $PGDATA
