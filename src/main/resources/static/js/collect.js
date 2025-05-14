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

const add_ballot_btn = document.getElementById('add_ballot');

add_ballot_btn.addEventListener('click', () => {
    func_add_ballot('add_ballot');
});

const func_add_ballot = (ballotId) => {
    const options = {
        size: BootstrapDialog.SIZE_EXTRA_LARGE,
        type: BootstrapDialog.TYPE_LIGHT,
        message: 'Hi Apple!',
        buttons: [{
            id: 'btn-ok',
            icon: 'bi bi-check',
            label: 'OK',
            cssClass: 'btn btn-primary',
            data: {
                js: 'btn-confirm',
                'user-id': '3'
            },
            autospin: false,
            action: function (dialogRef) {
                dialogRef.close();
            }
        }, {
            label: 'Button 2',
            cssClass: 'btn btn-primary',
            action: function(){
                alert('Hi Orange!');
            }
        }, {
            label: 'Close',
            cssClass: 'btn btn-danger',
            action: function(dialogItself){
                dialogItself.close();
            }
        }]
    };

    BootstrapDialog.show(options);

};

const build_ballot = (name) => {
    let checkboxGroup = document.getElementById('dynamic');
    let newCheck = document.createElement("div");
    newCheck.className = "col";
    newCheck.innerHTML = '8adfdafdsdadsfdfd';
    checkboxGroup.appendChild(newCheck);
}

const clear_ballot = () => {
    let checkboxGroup = document.getElementById('dynamic');
    checkboxGroup._getChildren().remove();
}