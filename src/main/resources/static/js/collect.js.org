let collect_table = $('#collect_table');

collect_table.bootstrapTable({
    url: '/collect/ballotList'
    , method: 'post'
    , sidePagination: 'server'
    , pagination: true
    , idField: 'ballotId'
    , pageNumber: 1
    , pageSize: 10
    , pageList: '[10, 20, 50, all]'
    , dataField: 'rows'
    , sortOrder : 'asc'
    , showRefresh: true
    , showFullscreen: true
    // , search: true
    // , searchable: true
    // , showSearchButton: true
    , idTable: 'collect_table'
    , toolbar: '#collect_toolbar'
    , queryParams: function(params) {
        // alert(JSON.stringify(params, null, 2));
        params.pagingConfig = {"page": params.offset, "size": params.limit}
        params.param = {search: params.search ? params.search : ''};
        return params;
    }
    , responseHandler: function(res) {
        // console.log(res);
        res.rows = res.data.content;
        res.total = res.data.totalElements;
        delete res.data;
        return res;
    }
    , onLoadSuccess: function(data) {
        collect_table.bootstrapTable('resetView');
    }
    , onLoadError: function(status, res) {
    }
});

$('body').on('click', 'button#btnRebuildTesteeList', function(event) {
    buildTable({planId: planId, gradeType: gradeType, encryptType: encryptType, itemType: itemType});
});

window.onload = function() {
};

const MAX_EXTRA = 3;

function openModal() {
    const modalEl = document.getElementById('myModal');
    const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
    const groupSupervisor = document.getElementById('groupSupervisor');
    const groupDirector = document.getElementById('groupDirector');
    const countSupervisor = document.getElementById('countSupervisor');
    const countDirector = document.getElementById('countDirector');
    const countExtra = document.getElementById('countExtra');
    const extraItems = document.getElementById('extraItems');

    // 清空旧内容
    groupSupervisor.innerHTML = '';
    groupDirector.innerHTML = '';
    extraItems.innerHTML = '';
    document.getElementById('extraInput').value = '';
    countSupervisor.textContent = '0';
    countDirector.textContent = '0';
    countExtra.textContent = '0';

    // 模拟后端请求
    fetchCheckboxData().then(data => {
        generateCheckboxes(groupSupervisor, data.supervisorList, 'S');
        generateCheckboxes(groupDirector, data.directorList, 'D');
        generateExtraItem(extraItems, data.extraItem);
        updateCounts();
    });

    modal.show();
}

function fetchCheckboxData() {
    return new Promise((resolve, reject) => {
        fetch('js/response.json')
            .then(response => {
                if (!response.ok) {
                    throw new Error('加载 JSON 失败');
                }
                return response.json();
            })
            .then(data => {
                // 模拟后端处理延迟
                setTimeout(() => {
                    resolve(data);
                }, 500); // 模拟 500ms 延迟
            })
            .catch(error => reject(error));
    });
}

function generateCheckboxes(container, items, groupName) {
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
        colContainer.className = 'col-lg-2';
        colContainer.appendChild(wrapper);

        container.appendChild(colContainer);
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
        checkbox.id = '';
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
    const extraChecked = document.querySelectorAll('#extraItems input:checked').length;

    document.getElementById('countSupervisor').textContent = supervisorChecked;
    document.getElementById('countDirector').textContent = directorChecked;
    document.getElementById('countExtra').textContent = extraChecked;
}

function addExtraItem() {
    const input = document.getElementById('extraInput');
    const typeSelect = document.getElementById('extraType');
    const container = document.getElementById('extraItems');
    const currentCount = container.querySelectorAll('input[type="checkbox"]').length;

    const value = input.value.trim();
    const type = typeSelect.value;
    if (!value) {
        alert("请输入有效内容");
        return;
    }

    if (currentCount >= MAX_EXTRA) {
        alert("最多只能添加 " + MAX_EXTRA + " 项");
        return;
    }

    const id = 'extra_' + Date.now();

    const wrapper = document.createElement('div');
    wrapper.className = 'form-check';

    const checkbox = document.createElement('input');
    checkbox.className = 'form-check-input';
    checkbox.type = 'checkbox';
    checkbox.id = id;
    checkbox.name = 'extra';
    checkbox.value = JSON.stringify({ label: value, type: type });
    checkbox.checked = true;
    checkbox.style.visibility = 'hidden';

    const label = document.createElement('label');
    label.className = 'form-check-label';
    label.htmlFor = id;
    label.textContent = value + ' [' + convert(type) + ']';

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

    input.value = '';
    updateCounts();
}

document.getElementById('submitBtn').addEventListener('click', function () {
    const selected = [];
    const checkboxes = document.querySelectorAll('.modal-body input[type="checkbox"]:checked');
    checkboxes.forEach(cb => selected.push({'id': cb.id, 'value': cb.value, 'type': cb.name}));

    // console.log('选中项：', selected);
    // alert(JSON.stringify(selected, null, 2));

    submitToBackend(selected).then(success => {
        if (success) {
            const modal = bootstrap.Modal.getInstance(document.getElementById('myModal'));
            modal.hide();
        } else {
            alert('提交失败，请重试');
        }
    });
});

function submitToBackend(data) {
    console.log('提交给后端：', data);
    return new Promise(resolve => {
        setTimeout(() => resolve(true), 800);
    });
}
