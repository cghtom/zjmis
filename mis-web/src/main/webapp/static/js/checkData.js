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

                $('#query-button').on('click',function() {
                    var serviceName = "MISGETCHECKRESULT";
                    var CHECK_RESULT = $("#check_result").val();
                    var RPT_ID = $("#rpt_id").val();
                    var OP_TIME = $("#op_time").val();
                    var CITY_ID = $("#city_id").val();
                    httpUrl = (httpUrlQuery+"&serviceName="+serviceName+"&CHECK_RESULT="+CHECK_RESULT+"&OP_TIME="+OP_TIME+"&CITY_ID="+CITY_ID+"&RPT_ID=" + RPT_ID )
                    console.log(httpUrl);
                    var oSettings = dataTab.fnSettings();
                    oSettings.sAjaxSource = httpUrl;//重新设置url
                    dataTab.fnDraw(false);
                });

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