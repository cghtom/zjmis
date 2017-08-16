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

                $("#downloadModel").on("click","#checkboxControl",function () {
                    var that = this;
                    $(this).closest('form').find('div > div > label:first-child input:checkbox')
                        .each(function(){
                            this.checked = that.checked;
                            $(this).toggleClass('selected');
                        });
                });

                $("#downloadModel").on("click","#commit",function () {
                    var that = this;
                    var param = "&RPT_ID=";
                    var opTime = "&OP_TIME=";
                    $('form div div input:checkbox').each(function () {
                        if(this.checked==true){
                            if($(this).attr("id")!="checkboxControl"){
                                param += ($(this).parent().parent().siblings("div").children("span").html().replace(/(^\s*)|(\s*$)/g,"")+",");
                                opTime += ($(this).parent().parent().siblings("div").children("input").val()+",");
                            }
                        }
                    });
                    param = param.substring(0,param.length-1);
                    opTime = opTime.substring(0,opTime.length-1);
                    window.location.href = httpUrlDownload + "&serviceName=MISDOWNLOADRPTMSG" + param+opTime;
                });

                /*$('#formReportContent').on("click","#batch-upload",function () {
                    
                });*/

                $('#fileupload').fileupload({
                    autoUpload: true,
                    url:httpUploadUrl,
                    formData:uploadParam,
                    forceIframeTransport: true,
                    done: function (data) {
                        console.log(data)
                    }}
                );

                //日期框添加默认值
                function  setdate(){
                    var sd=new Date();
                    sd.setDate(sd.getDate()-1);
                    var sy=sd.getFullYear();
                    var sm = sd.getMonth()+1;
                    if (sm >= 1 && sm <= 9) {
                        sm = "0" + sm;
                    }else {
                        sm = ""+sm;
                    }
                    $(".Wdate").val(sy+sm);
                }

                $('#formReportContent').on('click', '#batch-download', function(){
                    var downloadParam = {};

                    $.post(httpUrlQuery + "&serviceName=MISGETALLRPTMSG", downloadParam, function(data){
                        var downloadDataModel;
                        var rptMsgList = data.rptMsg;
                        var downloadModelTpl = $("#downloadModelTpl").html().replace("lt;", "<");
                        laytpl(downloadModelTpl).render(rptMsgList, function (html) {
                            $('#form-horizontal').html(html);
                        });
                        $('#downloadModalLabel').text(downloadModelTitle);
                        $('#commit').text('下载');
                        $('#downloadModel').modal();
                        setdate();
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