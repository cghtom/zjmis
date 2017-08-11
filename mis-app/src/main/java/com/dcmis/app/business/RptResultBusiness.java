package com.dcmis.app.business;

import com.alibaba.fastjson.JSONArray;
import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.entity.RptEntity;
import com.dcmis.app.util.PageData;
import com.github.pagehelper.PageHelper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄福亮 on 2017/7/28.
 */
public class RptResultBusiness {

    /**
     * 查询报表结果信息 MISGETRPTRESULT
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getRptResult(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        PageData reData = new PageData();
        int curPage = (Integer.parseInt(pd.getString("iDisplayStart")) / Integer.parseInt(pd.getString("iDisplayLength")) + 1);
        int pageSize = Integer.parseInt(pd.getString("iDisplayLength"));
        String sEcho = pd.getString("sEcho");
        PageHelper.startPage(curPage, pageSize);
        List<PageData> pds = (List<PageData>)daoSupport.findForList("rptDataMapper.getRptResult",pd);
        PageData pdNum = (PageData)daoSupport.findForObject("rptDataMapper.rptResultMsgTotalCount", pd);
        reData.put("aaData", pds);
        reData.put("sEcho", sEcho);
        reData.put("iTotalRecords", pdNum.get("COUNT").toString());
        reData.put("iTotalDisplayRecords", pdNum.get("COUNT").toString());
        return reData;
    }


    /**
     * 报表归档 MISFILERPTMSG
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void fileMisRptMsg (PageData pd, String databaseId, DaoSupport daoSupport) throws Exception{
        Object update = daoSupport.update("zbDataMapper.fileDataByTmp", pd);
    }

    public PageData getAllRptMsg(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception{
        PageData reData = new PageData();
        List<PageData> pds = (List<PageData>)daoSupport.findForList("rptDataMapper.getRptResult",pd);
        reData.put("rptMsg", pds);
        return reData;
    }

    public PageData  exportExcel(PageData pd, String databaseId, DaoSupport daoSupport) throws  Exception{
        List<PageData> pds = (List<PageData>)daoSupport.findForList("rptDataMapper.getRptResult",pd);
        String rptID = "";
        String rptName = "";
        JSONArray ja = new JSONArray();
        for(int i=0;i<pds.size();i++){
            PageData rptMsg = pds.get(i);
            rptID = rptMsg.getString("RPT_ID");
            rptName = rptMsg.getString("RPT_NAME");
            RptEntity entity = new RptEntity(rptID,rptName);
            ja.add(entity);
        }
        Map<String,String> headMap = new LinkedHashMap<String,String>();
        headMap.put("rptID","指标编码");
        headMap.put("rptName","只表名称");
        String title = "测试";
       /* String curFile = constant.getLocPath()+"test.xlsx";
        OutputStream outXlsx = new FileOutputStream(curFile);
        System.out.println("正在导出xlsx....");
        ExportExcelUtil.exportExcelX(title,headMap,ja,null,0,outXlsx);
        outXlsx.close();
        String remoteFile = constant.getPath()+"test.xlsx";
        ftpUtil.put(curFile, remoteFile);*/
        return null;
    }

}
