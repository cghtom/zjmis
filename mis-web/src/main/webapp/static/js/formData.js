seajs.use(['laytpl'],function(laytpl){
    $('#contentBody').on('click','#rpt_detail',function() {
        var $this = $(this);
        var rptId = $this.attr('data1');
        var opTime = $this.attr('data2');
        var url = 'zbData.html?rptId='+rptId + "&opTime="+opTime;
        $('#zbModel').modal();
        $('#zbIfr').attr('src',url);
    });
})