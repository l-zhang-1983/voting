```shell
$ curl -X POST 127.0.0.1:8080/voting/getProvince \
    -H "Content-Type: application/json" \
    -d '{"pagingConfig": {"page": 1, "size": 5}, "extraParam": {}}' \
    -w '\n' | jq
```
