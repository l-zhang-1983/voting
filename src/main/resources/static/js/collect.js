const collect_table = $('table#collect_table');

function initTable() {
    collect_table.bootstrapTable('destroy').bootstrapTable({
        idField: 'ballotId',
        dataField: 'rows',
        columns: [{
            checkbox: true
        }, {
            field: 'ballotId'
            , width: '75'
            , align: 'center'
            , valign: 'middle'
        }, {
            title: '编号',
            field: 'serialNo',
            align: 'center',
            valign: 'middle',
            sortable: true
        }, {
            title: '监事候选人人数',
            field: 'supervisorCount',
            align: 'center',
            valign: 'middle',
        }, {
            title: '理事候选人人数',
            field: 'directorCount',
            align: 'center',
            valign: 'middle',
            sortable: true,
        }, {
            field: 'operate',
            title: '',
            align: 'center',
            valign: 'middle',
            clickToSelect: false,
            events: rowHandler,
            formatter: operateFormatter
        }],
        // , search: true
        // , searchable: true
        // , showSearchButton: true
        queryParams: function (params) {
            params.pagingConfig = {"page": params.offset, "size": params.limit}
            params.param = {search: params.search ? params.search : ''};
            delete params.search;
            delete params.limit;
            delete params.offset;
            delete params.order;
            delete params.sort;
            console.log(params);
            return JSON.stringify(params);
        },
        responseHandler: function (res) {
            res.rows = res.data.content;
            res.total = res.data.totalElements;
            delete res.data;
            return res;
        },
        onLoadSuccess: function (data) {
            collect_table.bootstrapTable('resetView');
        },
        onLoadError: function (status, res) {
        }
    });
}

function operateFormatter(value, row) {
    return '<i class="bi bi-pencil-square"></i>';
}

// document.getElementsByClassName('bi-pencil-square')
//     .addEventListener('click', function (e) {
//         alert(JSON.stringify(e));
// });

rowHandler = {
    'click .bi-pencil-square': (e, value, row) => {
//        alert(`You click like action, row: ${JSON.stringify(row)}`)
        if (row) {
            openModal(row.ballotId);
        }
    }
}

$(document).ready(function () {
    initTable();
});

function openModal(ballotId = null) {
    const modalEl = document.getElementById('myModal');
    const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
    const groupSupervisor = document.getElementById('groupSupervisor');
    const groupDirector = document.getElementById('groupDirector');
    const countSupervisor = document.getElementById('countSupervisor');
    const countDirector = document.getElementById('countDirector');
    const countExtra = document.getElementById('countExtra');
    const extraItems = document.getElementById('extraItems');
    const serialNo = document.getElementById('serialNo');

    // 清空旧内容
    groupSupervisor.innerHTML = '';
    groupDirector.innerHTML = '';
    extraItems.innerHTML = '';
    document.getElementById('extraInput').value = '';
    countSupervisor.textContent = '0';
    countDirector.textContent = '0';
    countExtra.textContent = '0';
    serialNo.value = '';

    // 后端请求
    fetchCheckboxData(ballotId).then(data => {
        data = data.data;
        if (ballotId) {
            serialNo.value = data.serialNo;
            serialNo.readOnly = true;
            serialNo.dataset.ballotId = ballotId;
        } else {
            serialNo.value = '';
            serialNo.readOnly = false;
            serialNo.dataset.ballotId = '';
        }

        generateCheckboxes(groupSupervisor, data.supervisorList, '0');
        generateCheckboxes(groupDirector, data.directorList, '1', data.supervisorList.length);
        if (data.extraItem) {
            data.extraItem.forEach(item => addExtraTag(item.candidateName, item.candidateType));
        }
        updateCounts();
    });

    modal.show();
}

function fetchCheckboxData(ballotId) {
//    alert(ballotId);
    let url = 'js/response.json';
    if (ballotId) {
        url = '/collect/getBallotContents';
    } else {
        url = '/collect/blankBallot';
    }
    return new Promise((resolve, reject) => {
        fetch(url, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json' // 必须指定 JSON 类型
            },
            body: JSON.stringify({'param': ballotId})
        }).then(response => {
            if (!response.ok) {
                throw new Error('加载 JSON 失败');
            }
            return response.json();
        }).then(data => {
            // 模拟后端处理延迟
            setTimeout(() => {
                resolve(data);
            }, 500); // 模拟 500ms 延迟
        }).catch(error => reject(error));
    });
}

function generateCheckboxes(container, items, groupName, startIndex = 0) {
    let counter = 0;
    items.forEach(item => {
        const wrapper = document.createElement('div');
        wrapper.className = 'form-check';

        const checkbox = document.createElement('input');
        checkbox.className = 'form-check-input';
        checkbox.type = 'checkbox';
        checkbox.id = item.candidateId;
        checkbox.name = groupName;
        checkbox.value = item.candidateName;
        checkbox.checked = item.checked;

        const label = document.createElement('label');
        label.className = 'form-check-label';
        label.htmlFor = item.candidateId;
        label.textContent = item.candidateName;

        wrapper.appendChild(checkbox);
        wrapper.appendChild(label);

        const colContainer = document.createElement('div');
        if (counter % 8 == 0) {
            const lineNo = document.createElement('div');
            lineNo.className = 'col-lg-1 offset-lg-1 line-no';
            const lineLabel = document.createElement('label');
            lineLabel.className = 'form-check-label';
            lineLabel.htmlFor = '';
            lineLabel.textContent = String(startIndex / 8 + 1);
            lineNo.appendChild(lineLabel)
            container.appendChild(lineNo);

            colContainer.className = 'col-lg-1';
        } else if ((counter + 1) % 8 == 0) {
            colContainer.className = 'col-lg-3';
        } else {
            colContainer.className = 'col-lg-1';
        }
        colContainer.appendChild(wrapper);
        colContainer.className += ' container-extra';

        container.appendChild(colContainer);
        counter++;
        startIndex++;
    });
}

function generateExtraItem(container, items) {
    items.forEach(item => {
        console.info(JSON.stringify(item));
        const container = document.getElementById('extraItems');
        const wrapper = document.createElement('div');
        wrapper.className = 'form-check';

        const checkbox = document.createElement('input');
        checkbox.className = 'form-check-input';
        checkbox.type = 'checkbox';
        checkbox.id = EXTRA_CANDIDATE_ID;
        checkbox.name = 'extra';
        checkbox.value = JSON.stringify({ label: item.candidateName, type: item.candidateType });
        checkbox.checked = true;
        checkbox.style.visibility = 'hidden';

        const label = document.createElement('label');
        label.className = 'form-check-label';
        label.htmlFor = '';
        label.textContent = item.candidateName + ' [' + convert(item.candidateType) + ']';

        const delBtn = document.createElement('button');
        delBtn.type = 'button';
        delBtn.className = 'btn btn-danger btn-close btn-sm ms-2';
        delBtn.onclick = () => {
            colContainer.remove();
            updateCounts();
        };

        checkbox.addEventListener('change', updateCounts);

        wrapper.appendChild(checkbox);
        wrapper.appendChild(label);
        wrapper.appendChild(delBtn);

        const colContainer = document.createElement('div');
        colContainer.className = 'col-lg-4';
        colContainer.appendChild(wrapper);

        container.appendChild(colContainer);
    })
}

document.querySelector('.modal-body').addEventListener('change', function (e) {
    if (e.target.matches('input[type="checkbox"]')) {
        updateCounts();
    }
});

function updateCounts() {
    const supervisorChecked = document.querySelectorAll('#groupSupervisor input:checked').length;
    const directorChecked = document.querySelectorAll('#groupDirector input:checked').length;
    const extraItem = document.querySelectorAll('#extraItems .tag-btn');
//    alert(JSON.stringify(extraItem));
    const extraChecked = extraItem.length;
    const extraType = [];
    extraItem.forEach(item => extraType.push(item.dataset.type));
//    alert(JSON.stringify(extraType));
    const extraSupervisor = extraType.filter(item => item == '0')
    const extraDirector = extraType.filter(item => item == '1')

    if (supervisorChecked + extraSupervisor.length > CANDIDATE_TYPE_SUPERVISOR_EXCEEDED) {
        document.getElementById('countSupervisor').style = 'color: red';
    } else {
        document.getElementById('countSupervisor').style = '';
    }
    document.getElementById('countSupervisor').textContent = supervisorChecked + extraSupervisor.length;
    document.getElementById('countDirector').textContent = directorChecked;
    document.getElementById('countExtra').textContent = extraChecked;

    document.getElementById('addExtraBtn').disabled = extraChecked == 3;
    document.getElementById('extraInput').disabled = extraChecked == 3;
    document.getElementById('extraType').disabled = extraChecked == 3;
}

function addExtraItem() {
    const name = document.getElementById('extraInput').value.trim();
    const type = document.getElementById('extraType').value;
    const count = document.querySelectorAll('#extraItems .tag-btn').length;
    if (!name) {
        // alert('请输入姓名');
        showToast({
            title: '提示信息',
            message: '请输入姓名',
            type: 'danger',
            delay: 3000
        });
        return;
    }

    // if (count >= MAX_EXTRA) {
    //     alert(`最多只能添加 ${MAX_EXTRA} 项`);
    //     return;
    // }

    addExtraTag(name, type);
    document.getElementById('extraInput').value = '';
    updateCounts();
}

function addExtraTag(name, type) {
    const btn = document.createElement('button');
    btn.type = 'button';
    btn.className = 'btn btn-sm btn-outline-primary tag-btn';
    btn.textContent = name + '[' + convert(type) + ']';
    btn.dataset.name = name;
    btn.dataset.type = type;
    btn.onclick = () => {
        btn.remove();
        updateCounts();
    };
    document.getElementById('extraItems').appendChild(btn);
}

document.getElementById('submitBtn').addEventListener('click', function () {
    const selected = [];
    const checkboxes = document.querySelectorAll('.modal-body input[type="checkbox"]:checked');
    checkboxes.forEach(cb => selected.push({'candidateId': cb.id, 'candidateName': cb.value, 'candidateType': cb.name, 'checked': 1}));

    const extra = document.querySelectorAll('#extraItems .tag-btn');
    extra.forEach(label => {
        selected.push({'candidateId': EXTRA_CANDIDATE_ID, 'candidateName': label.dataset.name, 'candidateType': label.dataset.type});
    });

    const serialNo = document.getElementById('serialNo');

    if (!serialNo.value) {
        // alert('请输入姓名');
        showToast({
            title: '提示信息',
            message: '请输入选票编号',
            type: 'danger',
            delay: 3000
        });
        return;
    }

    const param = {};
    param.supervisorList = selected.filter(item => item.candidateType == '0' && item.candidateId != EXTRA_CANDIDATE_ID);
    param.directorList = selected.filter(item => item.candidateType == '1' && item.candidateId != EXTRA_CANDIDATE_ID);

    if (!param.supervisorList.length || !param.directorList.length) {
        showToast({
            title: '提示信息',
            message: '请选择监事候选人预备人选或<br>理事候选人预备人选',
            type: 'danger',
            delay: 3000
        });
        return;
    }

    param.extraList = selected.filter(item => item.candidateId == EXTRA_CANDIDATE_ID);
    param.serialNo = serialNo.value;
    param.ballotId = serialNo.dataset.ballotId;

    // console.log(param);

    submitToBackend(param).then(success => {
        if (success) {
            const modal = bootstrap.Modal.getInstance(document.getElementById('myModal'));
            modal.hide();
            showToast({
                title: '提交成功',
                message: '页面列表将被刷新',
                type: 'info',
                delay: 3000
            });
            collect_table.bootstrapTable('refresh');
        } else {
            showToast({
                title: '提交失败',
                message: '请重试',
                type: 'danger',
                delay: 3000
            });
        }
    });
});

function submitToBackend(data) {
    // console.log('提交给后端：', data);
    // alert(JSON.stringify(data));
//    return new Promise(resolve => {
//        setTimeout(() => resolve(true), 800);
//    });

//    data.serialNo = Math.floor(Math.random() * 100) + 1;
    return new Promise((resolve, reject) => {
        fetch('/collect/saveBallotContents', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json' // 必须指定 JSON 类型
            },
            body: JSON.stringify({'param': data})
        }).then(response => {
            if (!response.ok) {
                throw new Error('加载 JSON 失败');
            }
            collect_table.bootstrapTable('refresh');
            return response.json();
        }).then(data => {
            // 模拟后端处理延迟
            setTimeout(() => {
                resolve(data);
            }, 500); // 模拟 500ms 延迟
        }).catch(error => reject(error));
    });
}
