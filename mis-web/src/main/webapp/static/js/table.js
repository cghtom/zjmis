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
                console.log(dataModel);
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