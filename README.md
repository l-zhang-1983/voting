```shell
$ curl -X POST localhost:8080/common/getProvinceForPage \
    -H "Content-Type: application/json" \
    -d '{"pagingConfig": {"page": 1, "size": 5}, "param": {}}' \
    -w '\n' | jq
```

```shell
$ curl -X POST localhost:8080/common/getProvince \
    -H "Content-Type: application/json" \
    -d '{"param": {}}' \
    -w '\n' | jq
```

```shell
$ curl -X POST 127.0.0.1:8080/collect/blankBallot \
    -H "Content-Type: application/json" \
    -d '{"param": {}}' \
    -w '\n' | jq | more
```

```shell
$ curl -X POST localhost:8080/collect/saveBallot \
    -H "Content-Type: application/json" \
    -d '{"param": {"serialNo": 100, "ballotItemList": [{"candidateId": 1, "candidateType": 0, "checked": 0}, {"candidateId": 5, "candidateType": 0, "checked": 1}, {"candidateId": 8, "candidateType": 0, "checked": 0}, {"candidateId": 10, "candidateType": 1, "checked": 0}, {"candidateId": 1, "candidateType": 15, "checked": 0}, {"candidateId": 20, "candidateType": 0, "checked": 1}]}}' \
    -w '\n' | jq | more
```
