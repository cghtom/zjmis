seajs.use(['laytpl',
    'jquery.dataTables.bootstrap',
    'ace-elements.min','ace.min',
    'DataTableServerSideData',
    'jquery.tableTools',
    'jquery.fileupload'],
    function(laytpl) {
        $.getJSON( httpUrl + '&serviceName=MISTABLECONFIG',
            {
                pageName:pageName
            },
            function(data) {
                var tableConfig = eval('(' + data.TABLE_CONFIG + ')');
                console.log(data)
                var dataModel = eval('(' + data.MODEL + ')');
                console.log(dataModel+"222");
                var dataTab = $('#table_report').dataTable(tableConfig);


                $('#add').on('click',function() {
                    var modelTpl = $("#modelTpl").html().replace("lt;", "<");
                    laytpl(modelTpl).render(dataModel, function (html) {
                        $('#form-horizontal').html(html);
                    });
                    $('#modalLabel').text(modelTitle);
                    $('#zbModel').modal();
                });

                $('#commit').on('click', function() {
                    var txt = $(this).text();

                    var serviceName;
                    if(txt == '修改') {
                        serviceName = editServiceName;
                    } else {
                        serviceName = addServiceName;
                    }

                    var param = $("#form-horizontal").serialize();
                    param += "&serviceName=" + serviceName;
                    console.log(param);
                    $.get(httpUrl + "&" + param, function(data){
                        $('#zbModel').modal('hide');
                        dataTab.fnDraw(false);
                    });
                });

                /*query data start....*/
                $('#query-button').on('click',function() {
                    var serviceName = "MISZBRESULT";
                    var ZB_ID = $("#report-form-code").val();
                    var ZB_NAME = $("#report-form-name").val();
                    var OP_TIME = $("#data-cycle").val();
                    var STATE = 1;
                    httpUrl = (httpUrlQuery+"&serviceName="+serviceName+"&ZB_ID="+ZB_ID+"&ZB_NAME="+ZB_NAME+"&OP_TIME="+OP_TIME+"&STATE="+STATE+"&RPT_ID=" + arr.RPT_ID)
                    console.log(httpUrl);
                    var oSettings = dataTab.fnSettings();
                    oSettings.sAjaxSource = httpUrl;//重新设置url
                    dataTab.fnDraw(false);
                });
                /*query data end....*/

                /*rpt_edit data start ....*/
                $('#contentBody').on('click', '#rpt_edit', function(){
                    var $this = $(this);
                    var zb_id = $this.attr("data1");
                    var op_time = $this.attr("data2");
                    var zb_data = $this.attr("data3");
                    $('#zb-commit').attr("data1",zb_id);
                    $('#zb-commit').attr("data2",op_time);
                    for (var item in dataModel) {
                        dataModel[item].value = zb_data;
                    }
                    var modelTpl = $("#modelTpl").html().replace("lt;", "<");
                    laytpl(modelTpl).render(dataModel, function (html) {
                        $('#form-horizontal').html(html);
                    });
                    $('#zbModel').modal();
                });
                /*rpt_edit data end ....*/


                /*zb-edit-sure data start ....*/
                $('#zb-commit').on('click', function() {
                    var zb_id = $(this).attr("data1");
                    var op_time = $(this).attr("data2");
                    var serviceName = editServiceName;
                    var param = $("#form-horizontal").serialize();
                    param += ("&serviceName=" + serviceName+"&ZB_ID="+zb_id+"&OP_TIME="+op_time);
                    console.log(httpUrlQuery + "&" + param);
                    $.get(httpUrl + "&" + param, function(data){
                        $('#zbModel').modal('hide');
                        dataTab.fnDraw(false);
                    });
                });


                /*zb-edit-sure data end ....*/




                /** delete data start... */
                $('#contentBody').on('click', '.btn_delete', function(){
                    var $this = $(this);
                    var value = $this.attr('value');
                    var params = {'ID': value};
                    $.post(httpUrl + "&serviceName=" + delServiceName,params,function(data){
                        dataTab.fnDraw(false);
                    });

                });
                /** delete data end... */

                /** edit data start... */

                $('#contentBody').on('click', '.btn_edit', function(){
                    var $this = $(this);
                    var idValue = $this.attr('value');
                    var editParam = {idKey : idValue};
                    $.post(httpUrl + "&serviceName=MISGETRPTMSGBYID", editParam, function(data){
                        var editDataModel;
                        for (var item in dataModel) {
                            var keyName = dataModel[item].name;
                            for (var dataItem in data) {
                                if (keyName == dataItem) {
                                    dataModel[item].value = data[dataItem];
                                }

                            }
                        }
                        var modelTpl = $("#editModelTpl").html().replace("lt;", "<");
                        laytpl(modelTpl).render(dataModel, function (html) {
                            $('#form-horizontal').html(html);
                        });
                        $('#modalLabel').text(editModelTitle);
                        $('#commit').text('修改');
                        $('#zbModel').modal();
                    });

                    /*for(var i = 0; i < nTrs.length; i++){
                        console.log( dataTab.fnGetData(nTrs[i]));//fnGetData获取一行的数据
                    }*/
                });


               /* $('#contentBody').on('click', '.btn_edit', function(){
                    var $this = $(this);
                    var value = $this.attr('value');
                    var params = $("#form-horizontal").serialize();
                    $.post(httpUrl + "&serviceName=" + editServiceName,params,function(data){
                        dataTab.fnDraw(false);
                    });

                });*/
                /** edit data end... */


                $('table th input:checkbox').on('click' , function(){
                    var that = this;
                    $(this).closest('table').find('tr > td:first-child input:checkbox')
                        .each(function(){
                            this.checked = that.checked;
                            $(this).closest('tr').toggleClass('selected');
                        });

                });
                $('[data-rel=tooltip]').tooltip();
            }
        );
});