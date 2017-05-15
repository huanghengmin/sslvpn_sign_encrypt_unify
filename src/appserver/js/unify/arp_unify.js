Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;

    var record = new Ext.data.Record.create([
        {name: 'mac', mapping: 'mac'},
        {name: 'inet', mapping: 'inet'},
        {name: 'vlan', mapping: 'vlan'},
        {name: 'aging', mapping: 'aging'},
        {name: 'type', mapping: 'type'},
        {name: 'ip', mapping: 'ip'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../UnifyAction_arg_find.action"
    });
    var reader = new Ext.data.JsonReader({
        totalProperty: "total",
        root: "rows",
        id: 'id'
    }, record);



    var store = new Ext.data.GroupingStore({
        id: "private.store.info",
        proxy: proxy,
        reader: reader
    });

    store.load({
        params:{
            start:start, limit:pageSize
        }
    });
    var rowNumber = new Ext.grid.RowNumberer();         //自动 编号
    var colM = new Ext.grid.ColumnModel([
        rowNumber,
        {header: "MAC地址", dataIndex: "mac", align: 'center', sortable: true, menuDisabled: true},
        {header: "IP地址", dataIndex: "ip", align: 'center', sortable: true, menuDisabled: true},
        {header: "Vlan", dataIndex: "vlan", align: 'center', sortable: true, menuDisabled: true},
        {header: "网络接口", dataIndex: "inet", align: 'center', sortable: true, menuDisabled: true},
        {header: "Aging", dataIndex: "aging", align: 'center', sortable: true, menuDisabled: true},
        {header: "类型", dataIndex: "type", align: 'center', sortable: true, menuDisabled: true},
        {header: '操作标记', dataIndex: "flag", align: 'center', sortable: true, menuDisabled: true, renderer: show_flag, width: 100}
    ]);
    var page_toolbar = new Ext.PagingToolbar({
        pageSize: pageSize,
        store: store,
        displayInfo: true,
        displayMsg: "显示第{0}条记录到第{1}条记录，一共{2}条",
        emptyMsg: "没有记录",
        beforePageText: "当前页",
        afterPageText: "共{0}页"
    });
    var grid_panel = new Ext.grid.GridPanel({
        id: 'grid.info',
        plain: true,
        viewConfig: {
            forceFit: true //让grid的列自动填满grid的整个宽度，不用一列一列的设定宽度。
        },
        bodyStyle: 'width:100%',
        loadMask: {msg: '正在加载数据，请稍后...'},
        cm: colM,
        //sm: boxM,
        store: store,
        //tbar: toolbar,
        bbar: page_toolbar,
        //title: '资源配置',
        columnLines: true,
        autoScroll: true,
        border: false,
        collapsible: false,
        stripeRows: true,
        autoExpandColumn: 'Position',
        enableHdMenu: true,
        enableColumnHide: true,
        selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
        height: 300,
        frame: true,
        iconCls: 'icon-grid'
    });

    var port = new Ext.Viewport({
        layout: 'fit',
        renderTo: Ext.getBody(),
        items: [grid_panel]
    });

});

function show_flag(value, p, r){
    return String.format(
      '<a id="bind_unify.info" href="javascript:void(0);" onclick="bind_unify();return false;" style="color: green;">绑定</a>&nbsp;&nbsp;&nbsp;'
    );
}

function bind_unify(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确定删除这条记录？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../UnifyAction_add.action",
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{
                        "unify.mac":recode.get("mac"),
                        "unify.inet":recode.get("inet"),
                        "unify.vlan":recode.get("vlan"),
                        "unify.aging":recode.get("aging"),
                        "unify.ip":recode.get("ip"),
                        "unify.type":recode.get("type")
                    },
                    success : function(r,o){
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                        grid_panel.getStore().reload();
                    },
                    failure : function(r,o) {
                        var respText = Ext.util.JSON.decode(r.responseText);
                        var msg = respText.msg;
                        Ext.Msg.alert("提示", msg);
                    }
                });
            }
        });
    }
}