Ext.onReady(function () {
    Ext.BLANK_IMAGE_URL = '../../js/ext/resources/images/default/s.gif';
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    var record = new Ext.data.Record.create([
        {name:'ip', mapping:'ip'} ,
        {name:'port', mapping:'port'} ,
        {name:'eth', mapping:'eth'},
        {name:'adm', mapping:'adm'},
        {name:'pwd', mapping:'pwd'},
        {name:'type', mapping:'type'}
    ]);

    var proxy = new Ext.data.HttpProxy({
        url:"../../UnifyConfigAction_find.action"
    });

    var reader = new Ext.data.JsonReader({
        totalProperty:"totalCount",
        root:"root"
    }, record);

    var store = new Ext.data.GroupingStore({
        id:"store.info",
        proxy:proxy,
        reader:reader
    });

    store.load();
    store.on('load',function(){
        var ip = store.getAt(0).get('ip');
        var port = store.getAt(0).get('port');
        var eth = store.getAt(0).get('eth');
        var adm = store.getAt(0).get('adm');
        var pwd = store.getAt(0).get('pwd');
        var type = store.getAt(0).get('type');
        Ext.getCmp('unify.ip').setValue(ip);
        Ext.getCmp('unify.port').setValue(port);
        Ext.getCmp('unify.adm').setValue(adm);
        Ext.getCmp('unify.eth').setValue(eth);
        Ext.getCmp('unify.pwd').setValue(pwd);
        Ext.getCmp('unify.type').setValue(type);
    });

    var types = [
        ['华为', 'HuaWei'],
        ['华三', 'H3c']
    ];

    var formPanel = new Ext.form.FormPanel({
        plain:true,
        width:500,
        labelAlign:'right',
        labelWidth:200,
        defaultType:'textfield',
        defaults:{
            width:250,
            allowBlank:false,
            blankText:'该项不能为空!'
        },
        items:[
            new Ext.form.ComboBox({
                fieldLabel:'交换机类型',
                emptyText:'交换机类型',
                typeAhead:true,
                triggerAction:'all',
                forceSelection:true,
                id:"unify.type",
                mode:'local',
                hiddenName:"type",
                store:new Ext.data.ArrayStore({
                    fields:[
                        'id',
                        'name'
                    ],
                    data:types
                }),
                valueField:'name', //下拉框具体的值（例如值为SM，则显示的内容即为‘短信’）
                displayField:'id'//下拉框显示内容
            }),
            new Ext.form.TextField({
                fieldLabel : '交换机管理IP',
                name : 'ip',
                regex:/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/,
                regexText:'请输入正确的IP地址',
                id:"unify.ip",
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            }),
            new Ext.form.TextField({
                fieldLabel : '交换机管理端口',
                id:"unify.port",
                regex:/^(6553[0-6]|655[0-2][0-9]|65[0-4][0-9]{2}|6[0-4][0-9]{3}|[1-5][0-9]{4}|[1-9][0-9]{3}|[1-9][0-9]{2}|[1-9][0-9]|[1-9])$/,
                regexText:'请输入正确的端口',
                name : 'port',
                allowBlank : false,
                blankText : "不能为空，请正确填写"
            }),

            new Ext.form.TextField({
                fieldLabel:'交换机管理员',
                name:'adm',
                allowBlank:false,
                id:"unify.adm",
                blankText:"交换机管理员"
            }) ,
            new Ext.form.TextField({
                fieldLabel:'交换机管理员密码',
                name:'pwd',
                id:"unify.pwd",
                inputType:'password',
                allowBlank:false,
                blankText:"交换机管理员"
            }),
            new Ext.form.TextField({
                fieldLabel:'交换机接口(绑定、解绑)',
                name:'eth',
                id:"unify.eth",
                allowBlank:false,
                blankText:"交换机接口"
            })
        ],
        buttons:[
           '->',
            {
                id:'insert_win.info',
                text:'保存配置',
                handler:function () {
                    if (formPanel.form.isValid()) {
                        formPanel.getForm().submit({
                            url:"../../UnifyConfigAction_save.action",
                            method:'POST',
                            waitTitle:'系统提示',
                            waitMsg:'正在连接...',
                            success:function () {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:'保存成功,点击返回页面!',
                                    buttons:Ext.MessageBox.OK,
                                    buttons:{'ok':'确定'},
                                    icon:Ext.MessageBox.INFO,
                                    closable:false
                                });
                            },
                            failure:function () {
                                Ext.MessageBox.show({
                                    title:'信息',
                                    width:250,
                                    msg:'保存失败，请与管理员联系!',
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
            },
            {
                text:'开启IP+MAC绑定',
                handler:function () {
                    formPanel.getForm().submit({
                        url:"../../UnifyConfigAction_openBind.action",
                        method:'POST',
                        waitTitle:'系统提示',
                        waitMsg:'正在连接...',
                        params:{
                            "ip":Ext.getCmp("unify.ip").getValue(),
                            "port":Ext.getCmp("unify.port").getValue(),
                            "eth":Ext.getCmp("unify.eth").getValue(),
                            "adm":Ext.getCmp("unify.adm").getValue(),
                            "pwd":Ext.getCmp("unify.pwd").getValue(),
                            "type":Ext.getCmp("unify.type").getValue()
                        },
                        success:function () {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:250,
                                msg:'已开启IP+MAC绑！',
                                buttons:Ext.MessageBox.OK,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO
                            });
                        },
                        failure:function () {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:250,
                                msg:'开启IP+MAC绑定失败!',
                                buttons:Ext.MessageBox.OK,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.ERROR,
                                closable:false
                            });
                        }
                    });
                }
            },
            {
                text:'关闭IP+MAC绑定',
                handler:function () {
                    formPanel.getForm().submit({
                        url:"../../UnifyConfigAction_closeBind.action",
                        method:'POST',
                        waitTitle:'系统提示',
                        waitMsg:'正在连接...',
                        params:{
                            "ip":Ext.getCmp("unify.ip").getValue(),
                            "port":Ext.getCmp("unify.port").getValue(),
                            "eth":Ext.getCmp("unify.eth").getValue(),
                            "adm":Ext.getCmp("unify.adm").getValue(),
                            "pwd":Ext.getCmp("unify.pwd").getValue(),
                            "type":Ext.getCmp("unify.type").getValue()
                        },
                        success:function () {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:250,
                                msg:'已关闭IP+MAC绑定!',
                                buttons:Ext.MessageBox.OK,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.INFO
                            });
                        },
                        failure:function () {
                            Ext.MessageBox.show({
                                title:'信息',
                                width:250,
                                msg:'关闭IP+MAC绑定失败!',
                                buttons:Ext.MessageBox.OK,
                                buttons:{'ok':'确定'},
                                icon:Ext.MessageBox.ERROR,
                                closable:false
                            });
                        }
                    });
                }
            }
        ]
    });

    var panel = new Ext.Panel({
        plain:true,
        width:600,
        border:false,
        items:[{
            id:'panel.info',
            xtype:'fieldset',
            title:'交换机配置',
            width:530,
            items:[formPanel]
        }]
    });
    new Ext.Viewport({
        layout :'fit',
        renderTo:Ext.getBody(),
        autoScroll:true,
        items:[{
            frame:true,
            autoScroll:true,
            items:[panel]
        }]
    });

});


