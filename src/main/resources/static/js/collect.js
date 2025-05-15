let collect_table = $('#collect_table');

// 通用表格配置
function initBootstrapTable() {
    collect_table.bootstrapTable({
        url: '/collect/ballotList',
        method: 'post',
        sidePagination: 'server',
        pagination: true,
        idField: 'ballotId',
        pageNumber: 1,
        pageSize: 10,
        pageList: '[10, 20, 50, all]',
        dataField: 'rows',
        sortOrder: 'asc',
        showRefresh: true,
        showFullscreen: true,
        idTable: 'collect_table',
        toolbar: '#collect_toolbar',
        queryParams: function(params) {
            params.pagingConfig = {"page": params.offset, "size": params.limit}
            params.param = {search: params.search ? params.search : ''};
            return params;
        },
        responseHandler: function(res) {
            res.rows = res.data.content;
            res.total = res.data.totalElements;
            delete res.data;
            return res;
        },
        onLoadSuccess: function(data) {
            collect_table.bootstrapTable('resetView');
        },
        onLoadError: function(status, res) {}
    });
}

// 通用DOM创建函数
function createCheckboxElement(config) {
    const wrapper = document.createElement('div');
    wrapper.className = 'form-check';

    const checkbox = document.createElement('input');
    checkbox.className = 'form-check-input';
    checkbox.type = 'checkbox';
    checkbox.id = config.id;
    checkbox.name = config.groupName;
    checkbox.value = config.value;
    checkbox.checked = config.checked;
    if (config.hidden) checkbox.style.visibility = 'hidden';

    const label = document.createElement('label');
    label.className = 'form-check-label';
    label.htmlFor = config.id;
    label.textContent = config.labelText;

    if (config.withDelete) {
        const delBtn = document.createElement('button');
        delBtn.type = 'button';
        delBtn.className = 'btn btn-danger btn-close btn-sm ms-2';
        delBtn.onclick = () => {
            config.container.removeChild(config.colContainer);
            updateCounts();
        };
        wrapper.appendChild(delBtn);
    }

    wrapper.prepend(label);
    wrapper.prepend(checkbox);

    const colContainer = document.createElement('div');
    colContainer.className = config.colClass;
    colContainer.appendChild(wrapper);

    return colContainer;
}

// 通用复选框生成器
function generateCheckboxGroup(container, items, groupConfig) {
    container.innerHTML = '';
    items.forEach(item => {
        const colContainer = createCheckboxElement({
            id: item.candidateId,
            groupName: groupConfig.groupName,
            value: groupConfig.valueSerializer(item),
            checked: item.checked,
            labelText: groupConfig.labelFormatter(item),
            colClass: groupConfig.colClass,
            withDelete: groupConfig.withDelete,
            hidden: groupConfig.hidden,
            container: container
        });
        container.appendChild(colContainer);
    });
}

// 初始化模态框内容
function initModalContent() {
    const containers = [
        'groupSupervisor', 'groupDirector', 'extraItems'
    ].map(id => document.getElementById(id));
    
    containers.forEach(container => container.innerHTML = '');
    document.getElementById('extraInput').value = '';
    ['countSupervisor', 'countDirector', 'countExtra'].forEach(id => {
        document.getElementById(id).textContent = '0';
    });
}

// 统一数据加载
async function loadCheckboxData() {
    try {
        const response = await fetch('js/response.json');
        if (!response.ok) throw new Error('加载 JSON 失败');
        return response.json();
    } catch (error) {
        console.error('加载数据失败:', error);
        return { supervisorList: [], directorList: [], extraItem: [] };
    }
}

// 增强的更新计数方法
function updateCounts() {
    const counters = {
        supervisor: '#groupSupervisor input:checked',
        director: '#groupDirector input:checked',
        extra: '#extraItems input:checked'
    };

    Object.entries(counters).forEach(([key, selector]) => {
        document.getElementById(`count${key.charAt(0).toUpperCase() + key.slice(1)}`)
            .textContent = document.querySelectorAll(selector).length;
    });
}

// 重构后的主要功能函数
function openModal() {
    const modal = bootstrap.Modal.getOrCreateInstance('#myModal');
    initModalContent();

    loadCheckboxData().then(data => {
        generateCheckboxGroup(document.getElementById('groupSupervisor'), data.supervisorList, {
            groupName: 'S',
            valueSerializer: item => item.candidateName,
            labelFormatter: item => item.candidateName,
            colClass: 'col-lg-2'
        });

        generateCheckboxGroup(document.getElementById('groupDirector'), data.directorList, {
            groupName: 'D',
            valueSerializer: item => item.candidateName,
            labelFormatter: item => item.candidateName,
            colClass: 'col-lg-2'
        });

        generateCheckboxGroup(document.getElementById('extraItems'), data.extraItem, {
            groupName: 'extra',
            valueSerializer: item => JSON.stringify({ 
                label: item.candidateName, 
                type: item.candidateType 
            }),
            labelFormatter: item => `${item.candidateName} [${convert(item.candidateType)}]`,
            colClass: 'col-lg-4',
            withDelete: true,
            hidden: true
        });
        
        updateCounts();
    });

    modal.show();
}

// 添加额外项优化版
function addExtraItem() {
    const MAX_EXTRA = 3;
    const input = document.getElementById('extraInput');
    const typeSelect = document.getElementById('extraType');
    const container = document.getElementById('extraItems');
    
    const value = input.value.trim();
    if (!value) return alert("请输入有效内容");
    if (container.children.length >= MAX_EXTRA) return alert(`最多只能添加 ${MAX_EXTRA} 项`);

    container.appendChild(createCheckboxElement({
        id: `extra_${Date.now()}`,
        groupName: 'extra',
        value: JSON.stringify({ label: value, type: typeSelect.value }),
        checked: true,
        labelText: `${value} [${convert(typeSelect.value)}]`,
        colClass: 'col-lg-4',
        withDelete: true,
        hidden: true,
        container: container
    }));

    input.value = '';
    updateCounts();
}

// 初始化事件监听
function initEventListeners() {
    $('body').on('click', '#btnRebuildTesteeList', () => {
        buildTable({ planId, gradeType, encryptType, itemType });
    });

    document.querySelector('.modal-body').addEventListener('change', e => {
        if (e.target.matches('input[type="checkbox"]')) updateCounts();
    });

    document.getElementById('submitBtn').addEventListener('click', () => {
        const selected = Array.from(document.querySelectorAll('.modal-body input:checked'))
            .map(cb => ({ id: cb.id, value: cb.value, type: cb.name }));
        
        submitToBackend(selected).then(success => {
            success && bootstrap.Modal.getInstance('#myModal').hide();
        });
    });
}

// 初始化入口
window.onload = function() {
    initBootstrapTable();
    initEventListeners();
};

// 剩余未修改的辅助函数保持原样
const submitToBackend = data => {
    console.log('提交给后端：', data);
    return new Promise(resolve => setTimeout(() => resolve(true), 800));
};