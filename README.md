```shell
$ curl -X POST 127.0.0.1:8080/common/getProvinceForPage \
    -H "Content-Type: application/json" \
    -d '{"pagingConfig": {"page": 1, "size": 5}, "extraParam": {}}' \
    -w '\n' | jq
```
```shell
$ curl -X POST 127.0.0.1:8080/common/getProvince \
  -H "Content-Type: application/json" \
  -d '{"pagingConfig": null, "param": {}}' \
  -w '\n' | jq
```