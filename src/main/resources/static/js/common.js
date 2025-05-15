const convertMap = {
    "0": "监事",
    "1": "理事"
}

function convert(item) {
    return convertMap[item] || '';
}
