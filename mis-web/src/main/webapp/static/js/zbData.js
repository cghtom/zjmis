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
                var dataModel = eval('(' + data.MODEL + ')');
                var dataTab = $('#table_report').dataTable(tableConfig);
                $("#rpt_id").val(arr.RPT_ID);
                $("#op_time").val(arr.OP_TIME);

                /*query data start....*/
                $('#query-button').on('click',function() {
                    var serviceName = "MISZBRESULT";
                    var ZB_ID = $("#zb_id").val();
                    var RPT_ID = $("#rpt_id").val();
                    var OP_TIME = $("#op_time").val();
                    var CITY_ID = $("#city_id").val();
                    httpUrl = (httpUrlQuery+"&serviceName="+serviceName+"&ZB_ID="+ZB_ID+"&OP_TIME="+OP_TIME+"&CITY_ID="+CITY_ID+"&RPT_ID=" + RPT_ID )
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