seajs.use(['laytpl'],function(laytpl){
    $('#contentBody').on('click','#rpt_detail',function() {
        var $this = $(this);
        var rptId = $this.attr('data1');
        var opTime = $this.attr('data2').replace("-","");
        var url = 'zbData.html?RPT_ID='+rptId + "&OP_TIME="+opTime;
        $('#zbModel').modal();
        $('#zbIfr').attr('src',url);
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
})