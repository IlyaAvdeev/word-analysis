Создаём основную базу данных на основе Postgresql. Как запускать docker image описано [здесь|https://github.com/docker-library/docs/blob/master/postgres/README.md]. 
Командная строка следующая:

```shell
docker build -t ilya.avdeev/all-words-db .
```

```shell
docker run -p 5432:5432 -v $(pwd)/db:/var/lib/postgresql/data \
	-v $(pwd)/init:/docker-entrypoint-initdb.d \
	-v /Users/ilya/Documents/Java/russian-words/russian.utf-8:/init_voc/russian.utf-8:ro \
	-e POSTGRES_PASSWORD=password \
	ilya.avdeev/all-words-db
```

Запустить PgAdmin4
```shell
docker run --rm -p 5050:5050 thajeztah/pgadmin4
```