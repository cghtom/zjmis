seajs.use(['laytpl','jquery.dataTables.bootstrap','ace-elements.min','ace.min', 'DataTableServerSideData', 'jquery.tableTools', 'jquery.fileupload'], function(laytpl) {
    var httpUrl = 'http://localhost:8080/misweb/dataJson?callBack=misData';
    var httpUploadUrl = 'http://localhost:8080/misweb/upload';
    var uploadParam = {'serviceName' : 'MISBATCHADDZBMSG'};
    $(function() {
        var dataTab = $('#table_report').dataTable({
            "bServerSide" : "true",
            "pageLength": 10,
            "sAjaxSource" : httpUrl + '&serviceName=MISGETZBMSG',
            "fnServerData": fnDataTablesPipeline,
            "aaSorting": [[1, "desc"]],
            "bStateSave" : true,
            "oLanguage" : {
                "sLengthMenu" : "每页显示 _MENU_ 条记录",
                "sZeroRecords" : "对不起，没有匹配的数据",
                "sInfo" : "第 _START_ - _END_ 条 / 共 _TOTAL_ 条数据",
                "sInfoEmpty" : "没有匹配的数据",
                "sInfoFiltered" : "(数据表中共 _MAX_ 条记录)",
                "sProcessing" : "正在加载中...",
                "sSearch" : "搜索",
                "oPaginate" : {
                    "sFirst" : "第一页",
                    "sPrevious" : " 上一页 ",
                    "sNext" : " 下一页 ",
                    "sLast" : " 最后一页 "
                }
            },
            "bFilter": false,
            "aoColumns" : [
            {
                "mData" : 'RPT_ID',
                "sTitle" : '<div class="center"><label><input type="checkbox" /><span class="lbl"></span></label></div>',
                "bSortable" : false,
                "mRender" : function(data, type, row) {
                    var strTpl = '<div class="center"><label><input type="checkbox" value="'+data+'"/><span class="lbl"></span></label></div>';
                    return strTpl;
                }
            },
            {
                "mData" : 'ZB_ID',
                "sTitle" : "指标编码",
                "bSortable" : false
            },{
                "mData" : 'ZB_NAME',
                "sTitle" : "指标名称"
            },{
                "mData" : 'ZB_DESC',
                "sTitle" : "指标描述",
                "mRender" : function(data, type, row) {
                    return data;
                }
            }, {
                "mData" : "ZB_UNIT",
                "sTitle" : "指标单位"
            }, {
                "mData" : "STATE",
                "sTitle" : "状态",
                "mRender" : function(data, type, row) {
                    if( data == 1) {
                        return "有效";
                    } else {
                        return "无效";
                    }
                }
            }, {
                    "mData" : 'RPT_ID',
                    "sTitle" : '操作',
                    "bSortable" : false,
                    "mRender" : function(data, type, row) {
                        var strTpl = "<div class='hidden-phone visible-desktop btn-group'>"
                                    +"<button class='btn btn-mini btn-info' value='"+data+"'><i class='icon-edit'></i></button>"
                                    +"<button class='btn btn-mini btn-danger' value='"+data+"'><i class='icon-trash'></i></button>"
                                    +"</div>";
                        return strTpl;
                    }
                }
            ],
            "aoRowCallback": function( row, data ) {//添加单击事件，改变行的样式
                if ( $.inArray(data.DT_RowId, selected) !== -1 ) {
                    $(row).addClass('selected');
                }
            },
            "aLengthMenu": [[5, 10, 25, 50, -1], [5, 10, 25, 50, "全部"]],
            "aoColumnDefs" : [{"aTargets":[0,5]}]
        });

        /** add data start..*/

        /** upload excel start... */
        $('#fileupload').fileupload({
                autoUpload: true,
                url:httpUploadUrl,
                formData:uploadParam,
                forceIframeTransport: true,
                done: function (data) {
                    console.log(data)
                }}
        );
         /** upload excel end... */
        var data = [];
        $('#add').on('click',function() {
            data = [{'name': 'ZB_ID', 'cName': '指标编码', 'value' : ''},
                    {'name': 'ZB_NAME', 'cName': '指标名称', 'value' : ''},
                    {'name': 'ZB_DESC', 'cName': '指标描述', 'value' : ''},
                    {'name': 'ZB_UNIT', 'cName': '指标单位', 'value' : ''}
                   ];
            var modelTpl = $("#modelTpl").html().replace("lt;", "<");
            laytpl(modelTpl).render(data, function (html) {
                $('#form-horizontal').html(html);
            });
            $('#modalLabel').text("添加指标");
            $('#zbModel').modal()
        });

        /** add data start.. */
        $('#commit').on('click', function() {
            var param = $("#form-horizontal").serialize();
            param += "&serviceName=MISADDZBMSG";
            $.get(httpUrl + "&" + param, function(data){
                $('#zbModel').modal('hide');
                dataTab.ajax().reload();
            });
        });
        /** add data end.. */

        /** delete data start... */
        /*$('#contentBody').on('click', function(){
            alert("delete");
        });*/
        /** delete data end... */


        $('table th input:checkbox').on('click' , function(){
            var that = this;
            $(this).closest('table').find('tr > td:first-child input:checkbox')
                .each(function(){
                    this.checked = that.checked;
                    $(this).closest('tr').toggleClass('selected');
                });

        });
        $('[data-rel=tooltip]').tooltip();
    })

});