
```shell
$ curl -X POST localhost:8080/common/getProvinceForPage \
    -H "Content-Type: application/json" \
    -d '{"pagingConfig": {"page": 0, "size": 5}, "param": {}}' \
    -w '\n' | jq
```

```shell
$ curl -X POST localhost:8080/common/getProvince \
    -H "Content-Type: application/json" \
    -d '{"param": {}}' \
    -w '\n' | jq
```

- ballotListForPage
```shell
$ curl -X POST 127.0.0.1:8080/collect/ballotList \
    -H "Content-Type: application/json" \
    -d '{"pagingConfig": {"page": 0, "size": 5}, "param": {}}' \
    -w '\n' | jq
```

- save
```shell
$ curl -X POST localhost:8080/collect/saveBallotContents \
    -H "Content-Type: application/json" \
    -d '{"param": {"serialNo": 110, "supervisorList": [{"candidateId": 1, "candidateType": 0, "checked": 0}, {"candidateId": 5, "candidateType": 0, "checked": 1}, {"candidateId": 8, "candidateType": 0, "checked": 0}], "directorList": [{"candidateId": 10, "candidateType": 1, "checked": 0}, {"candidateId": 1, "candidateType": 1, "checked": 0}, {"candidateId": 20, "candidateType": 1, "checked": 1}]}}' \
    -w '\n' | jq | more
```

- save by file
```shell
$ curl -X POST localhost:8080/collect/saveBallotContents \
    -H "Content-Type: application/json" \
    -d @mock/save.json \
    -w '\n' | jq | more
```

- update by file
```shell
$ curl -X POST localhost:8080/collect/saveBallotContents \
    -H "Content-Type: application/json" \
    -d @mock/update.json \
    -w '\n' | jq | more
```

- get
```shell
$ curl -X POST localhost:8080/collect/getBallotContents \
    -H "Content-Type: application/json" \
    -d '{"param": 1}' \
    -w '\n' | jq | more
```

- delete
```shell
$ curl -X POST localhost:8080/collect/deleteBallotContents \
    -H "Content-Type: application/json" \
    -d '{"param": 105}' \
    -w '\n' | jq | more
```

