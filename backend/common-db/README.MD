# Создание докер образа внутри которого будут сидеть словари (без привязки к внешних хранилищам данных)

1. Создание образа PG с подготовленной структурой БД для словарей

```shell
docker-compose build --no-cache
```

```shell
docker-compose up
```

```shell
docker-compose -f ./docker-compose-final.yaml build
```

```shell
docker-compose -f ./docker-compose-final.yaml up
```