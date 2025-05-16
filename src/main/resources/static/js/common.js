const convertMap = {
    "0": "监事候选人",
    "1": "理事候选人",
}

const EXTRA_CANDIDATE_ID = 9999;
const CANDIDATE_TYPE_DIRECTOR_EXCEEDED = 99;
const CANDIDATE_TYPE_SUPERVISOR_EXCEEDED = 7;

function convert(item) {
    return convertMap[item] || '';
}

function showToast(config = {}) {
    // 默认配置
    if (!config) {
        config = {
            title: '通知',
            message: '操作成功！',
            type: 'success', // success | info | warning | danger
            delay: 3000
        };
    }

    // 创建 Toast 元素
    const toastEl = document.createElement('div');
    toastEl.className = `toast align-items-center text-bg-${config.type} border-0`;
    toastEl.setAttribute('role', 'alert');
    toastEl.setAttribute('aria-live', 'assertive');
    toastEl.setAttribute('aria-atomic', 'true');
    toastEl.dataset.bsDelay = config.delay;

    // Toast 内容结构
    toastEl.innerHTML = `
    <div class="d-flex">
      <div class="toast-body">
        <strong>${config.title}</strong>
        <div class="mt-1">${config.message}</div>
      </div>
      <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
    </div>
  `;

    // 添加到容器
    const container = document.querySelector('.toast-container');
    container.appendChild(toastEl);

    // 初始化并显示
    const toast = new bootstrap.Toast(toastEl);
    toast.show();

    // 自动移除（可选）
    toastEl.addEventListener('hidden.bs.toast', () => {
        toastEl.remove();
    });
}
