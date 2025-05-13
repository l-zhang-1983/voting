curl -X POST localhost:8080/collect/saveBallotContents \
    -H "Content-Type: application/json" \
    -d @update.json \
    -w '\n' | jq | more