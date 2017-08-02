seajs.use(['laytpl'],function(laytpl){
    $('#contentBody').on('click','#rpt_detail',function() {
        var $this = $(this);
        var rptId = $this.attr('data1');
        var opTime = $this.attr('data2').replace("-","");
        var url = 'zbData.html?RPT_ID='+rptId + "&OP_TIME="+opTime;
        $('#zbModel').modal();
        $('#zbIfr').attr('src',url);
    });
})