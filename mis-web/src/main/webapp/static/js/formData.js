seajs.use(['laytpl',
        'jquery.dataTables.bootstrap',
        'ace-elements.min','ace.min',
        'DataTableServerSideData',
        'jquery.tableTools',
        'jquery.fileupload'],
    function(laytpl) {
        var rptId = "";
        var opTime = "";
        $.getJSON( httpUrl + '&serviceName=MISTABLECONFIG',
            {
                pageName:pageName
            },
            function(data) {
                var tableConfig = eval('(' + data.TABLE_CONFIG + ')');
                var dataModel = eval('(' + data.MODEL + ')');
                var dataTab = $('#table_report').dataTable(tableConfig);


                $('table th input:checkbox').on('click' , function(){
                    var that = this;
                    $(this).closest('table').find('tr > td:first-child input:checkbox')
                        .each(function(){
                            this.checked = that.checked;
                            $(this).closest('tr').toggleClass('selected');
                        });

                });
                $('[data-rel=tooltip]').tooltip();

                $('#contentBody').on('click','#rpt_detail',function() {
                    var $this = $(this);
                    rptId = $this.attr('data1');
                    opTime = $this.attr('data2').replace("-","");
                    var url = 'zbData.html?RPT_ID='+rptId + "&OP_TIME="+opTime;
                    $('#zbModel').modal();
                    $('#zbIfr').attr('src',url);
                });

                $("#modalLabel").on("click",function(){
                    var url = 'zbData.html?RPT_ID='+rptId + "&OP_TIME="+opTime;
                    $('#zbModel').modal();
                    $('#zbIfr').attr('src',url);
                    $("#rptHTML").show();
                    $("#checkHTML").hide();
                });
                $("#modalLabelCheck").on("click",function(){
                    var url = 'checkData.html?RPT_ID='+rptId + "&OP_TIME="+opTime;
                    $('#zbModel').modal();
                    $('#zbCheck').attr('src',url);
                    $("#rptHTML").hide();
                    $("#checkHTML").show();
                });


                $("#formReportContent").on("click","#query-button",function(){
                    var serviceName = "MISGETRPTRESULT";
                    var param = ("&RPT_NAME="+$("#RPT_NAME").val()+"&RPT_ID="+$("#RPT_ID").val());
                    httpUrl = (httpUrlQuery+"&serviceName="+serviceName+param);
                    console.log(httpUrl);
                    var oSettings = dataTab.fnSettings();
                    oSettings.sAjaxSource = httpUrl;//重新设置url
                    dataTab.fnDraw(false);
                 });

                $('#formReportContent').on('click', '#batch-download', function(){
                    var downloadParam = {};
                    $.post(httpUrl + "&serviceName=MISDOWNLOADRPTMSG", downloadParam, function(data){
                       /* var downloadDataModel;
                        var rptMsgList = data.rptMsg;
                        var downloadModelTpl = $("#downloadModelTpl").html().replace("lt;", "<");
                        laytpl(downloadModelTpl).render(rptMsgList, function (html) {
                            $('#form-horizontal').html(html);
                        });
                        $('#downloadModalLabel').text(downloadModelTitle);
                        $('#commit').text('修改');
                        $('#downloadModel').modal();*/
                    });
                });


                $("#file-button").on("click",function () {
                    var checkboxArr = $("#table_report").find("td").find("input:checked");
                    var rpt_id = "";
                    if (checkboxArr.length > 0) {
                        for(var i=0;i<checkboxArr.length;i++){
                            if(i==checkboxArr.length-1){
                                rpt_id += $(checkboxArr[i]).val()+"";
                            }
                            else{
                                rpt_id += $(checkboxArr[i]).val()+",";
                            }
                        }
                        var param = ("&serviceName=" + fileServiceName+"&RPT_ID="+rpt_id);
                        $.get(httpUrl + "&" + param, function(data){
                            alert("success!")
                        });
                    }else{
                        alert("未选择要归档的报表！")
                    }
                })

            }
        );
    });