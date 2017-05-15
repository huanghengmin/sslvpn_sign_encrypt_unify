Ext.onReady(function () {

    Ext.BLANK_IMAGE_URL = '../../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';

    var start = 0;
    var pageSize = 15;
    /*var toolbar = new Ext.Toolbar({
        plain: true,
        items: [
            {
                id: 'add_key.info',
                xtype: 'button',
                text: '添加',
                iconCls: 'add',
                handler: function () {
                    addsourceNet(grid_panel, store);
                }
            }
        ]
    });*/
    var record = new Ext.data.Record.create([
        {name: 'mac', mapping: 'mac'},
        {name: 'id', mapping: 'id'},
        //{name: 'status', mapping: 'status'},
        {name: 'inet', mapping: 'inet'},
        {name: 'vlan', mapping: 'vlan'},
        {name: 'aging', mapping: 'aging'},
        {name: 'type', mapping: 'type'},
        {name: 'ip', mapping: 'ip'}
    ]);
    var proxy = new Ext.data.HttpProxy({
        url: "../../UnifyAction_find.action"
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
        //{header:"状态", dataIndex:"status", align:'center',sortable:true, menuDisabled:true,renderer:show_enabled},
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
    /*if(parseInt(r.get("status"))==0){
        return String.format(
            '<a id="disableUser.info" href="javascript:void(0);" onclick="disableUser();return false;"style="color: green;">阻断</a>&nbsp;&nbsp;&nbsp;'  +
            '<a id="update_private.info" href="javascript:void(0);" onclick="update_private();return false;" style="color: green;">修改</a>&nbsp;&nbsp;&nbsp;'+
            '<a id="delete_private.info" href="javascript:void(0);" onclick="delete_private();return false;" style="color: green;">解绑</a>&nbsp;&nbsp;&nbsp;'
        );*/
    //}else if(parseInt(r.get("status"))==1){
        return String.format(
           // '<a id="enableUser.info" href="javascript:void(0);" onclick="enableUser();return false;"style="color: green;">恢复</a>&nbsp;&nbsp;&nbsp;'  +
            //'<a id="update_private.info" href="javascript:void(0);" onclick="update_private();return false;" style="color: green;">修改</a>&nbsp;&nbsp;&nbsp;'+
            '<a id="delete_private.info" href="javascript:void(0);" onclick="delete_private();return false;" style="color: green;">解绑</a>&nbsp;&nbsp;&nbsp;'
        );
    //}
}

/*function show_enabled(value, p, r){
    if(r.get("status")=="0"){
        return String.format('<img src="../../img/icon/ok.png" alt="启用" title="启用" />');
    }else if(r.get("status")=="1"){
        return String.format('<img src="../../img/icon/off.gif" alt="禁用" title="禁用" />');
    }
}*/

/*
function disableUser(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "阻断终端？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../UnifyAction_disable.action",
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{id:recode.get("id")},
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
};

function enableUser(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "恢复终端？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../UnifyAction_enable.action",
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{id:recode.get("id")},
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
};
*/


/*
function addsourceNet(grid,store){
    var formPanel = new Ext.form.FormPanel({
        frame:true,
        autoScroll:true,
        labelWidth:150,
        labelAlign:'right',
        defaultWidth:300,
        autoWidth:true,
        layout:'form',
        border:false,
        defaults : {
            width : 250,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            new Ext.form.TextField({
                fieldLabel : 'MAC地址',
                name : 'unify.mac',
                id:  'unify.mac',
                allowBlank : false,
                emptyText:"网络MAC地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : 'IP地址',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入IP地址',
                name : 'unify.ip',
                id:  'unify.ip',
                allowBlank : false,
                emptyText:"IP地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '网络接口',
                name : 'unify.inet',
                id:  'unify.inet',
                allowBlank : false,
                emptyText:"网络接口",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : 'Vlan',
                name : 'unify.vlan',
                id:  'unify.vlan',
                allowBlank : false,
                emptyText:"Vlan",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : 'Aging',
                name : 'unify.aging',
                id:  'unify.aging',
                allowBlank : false,
                emptyText:"Aging",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '类型',
                name : 'unify.type',
                id:  'unify.type',
                allowBlank : false,
                emptyText:"类型",
                blankText : "不能为空，请正确填写"
            })
        ]
    });
    var win = new Ext.Window({
        title:"添加",
        width:500,
        layout:'fit',
        height:180,
        modal:true,
        items:formPanel,
        bbar:[
            '->',
            {
                id:'add_win.info',
                text:'确定',
                width:50,
                handler:function(){
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url :'../../UnifyAction_add.action',
                            timeout: 20*60*1000,
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在连接...',
                            success : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        store.reload();
                                        win.close();
                                    }
                                });
                            },
                            failure : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.ERROR,
                                    closable:false
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'请填写完成再提交!',
                            buttons:Ext.MessageBox.OK,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            },{
                text:'重置',
                width:50,
                handler:function(){
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}

function update_private(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    var formPanel = new Ext.form.FormPanel({
        frame:true,
        autoScroll:true,
        labelWidth:150,
        labelAlign:'right',
        defaultWidth:300,
        autoWidth:true,
        layout:'form',
        border:false,
        defaults : {
            width : 250,
            allowBlank : false,
            blankText : '该项不能为空！'
        },
        items:[
            new Ext.form.TextField({
                fieldLabel : 'MAC地址',
                name : 'unify.mac',
                id:  'unify.mac',
                readOnly:true,
                value:recode.get('mac'),
                allowBlank : false,
                emptyText:"MAC地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : 'IP地址',
                regex:/^(((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9])\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]|[0-9]))$/,
                regexText:'请输入IP地址',
                value:recode.get('ip'),
                name : 'unify.ip',
                id:  'unify.ip',
                allowBlank : false,
                emptyText:"IP地址",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '网络接口',
                value:recode.get('inet'),
                name : 'unify.inet',
                readOnly:true,
                id:  'unify.inet',
                allowBlank : false,
                emptyText:"网络接口",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : 'Vlan',
                readOnly:true,
                value:recode.get('vlan'),
                name : 'unify.vlan',
                id:  'unify.vlan',
                allowBlank : false,
                emptyText:"Vlan",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : 'Aging',
                readOnly:true,
                value:recode.get('aging'),
                name : 'unify.aging',
                id:  'unify.aging',
                allowBlank : false,
                emptyText:"Aging",
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '类型',
                readOnly:true,
                value:recode.get('type'),
                name : 'unify.type',
                id:  'unify.type',
                allowBlank : false,
                emptyText:"类型",
                blankText : "不能为空，请正确填写"
            })
        ]
    });
    var win = new Ext.Window({
        title:"修改",
        width:500,
        layout:'fit',
        height:180,
        modal:true,
        items:formPanel,
        bbar:[
            '->',
            {
                id:'add_win.info',
                text:'确定',
                width:50,
                handler:function(){
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url :'../../UnifyAction_modify.action',
                            timeout: 20*60*1000,
                            params:{id:recode.get('id')},
                            method :'POST',
                            waitTitle :'系统提示',
                            waitMsg :'正在连接...',
                            success : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false,
                                    fn:function(e){
                                        grid_panel.getStore().reload();
                                        win.close();
                                    }
                                });
                            },
                            failure : function(form, action) {
                                var msg = action.result.msg;
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:msg,
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.ERROR,
                                    closable:false
                                });
                            }
                        });
                    } else {
                        Ext.MessageBox.show({
                            title:'信息',
                            width:200,
                            msg:'请填写完成再提交!',
                            buttons:Ext.MessageBox.OK,
                            buttons:{'ok':'确定'},
                            icon:Ext.MessageBox.ERROR,
                            closable:false
                        });
                    }
                }
            },{
                text:'重置',
                width:50,
                handler:function(){
                    formPanel.getForm().reset();
                }
            }
        ]
    }).show();
}
*/

function delete_private(){
    var grid_panel = Ext.getCmp("grid.info");
    var recode = grid_panel.getSelectionModel().getSelected();
    if(!recode){
        Ext.Msg.alert("提示", "请选择一条记录!");
    }else{
        Ext.Msg.confirm("提示", "确定删除这条记录？", function(sid) {
            if (sid == "yes") {
                Ext.Ajax.request({
                    url : "../../UnifyAction_remove.action",
                    timeout: 20*60*1000,
                    method : "POST",
                    params:{id:recode.get("id")},
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