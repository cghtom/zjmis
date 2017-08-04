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

                $('#add').on('click',function() {
                    var modelTpl = $("#modelTpl").html().replace("lt;", "<");
                    laytpl(modelTpl).render(dataModel, function (html) {
                        $('#form-horizontal').html(html);
                    });
                    $('#modalLabel').text(modelTitle);
                    $("#commit").html("提交")
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
                    param += "&serviceName=" + serviceName+"&CALC_ID="+$("#form-horizontal").find("input").eq(0).val();
                    $.get(httpUrl + "&" + param, function(data){
                        $('#zbModel').modal('hide');
                        dataTab.fnDraw(false);
                    });
                });

                $('#contentBody').on('click', '.btn_delete', function(){
                    var $this = $(this);
                    var value = $this.attr('value');
                    var params = {'ID': value};
                    $.post(httpUrl + "&serviceName=" + delServiceName,params,function(data){
                        dataTab.fnDraw(false);
                    });

                });

                $('#contentBody').on('click', '.btn_edit', function(){
                    var $this = $(this);
                    var idValue = $this.attr('value');
                    var editParam = {idKey : idValue};
                    $.post(httpUrl + "&serviceName=MISGETCALCBYID", editParam, function(data){
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
                        $("#form-horizontal").find("input").eq(0).attr("disabled", true);
                        $('#modalLabel').text(editModelTitle);
                        $('#commit').text('修改');
                        $('#zbModel').modal();
                    });
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