package com.dcmis.app.business;

import com.alibaba.fastjson.JSONArray;
import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.entity.RptEntity;
import com.dcmis.app.util.PageData;
import com.dcmis.app.util.UploadExcelUtil;
import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
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
        /*pd.put("RPT_ID",pd.getString("RPT_ID").replaceAll(" ",""));*/
        String[] rptArray = pd.getString("RPT_ID").split(",");
        String[] opTimeArray = pd.getString("OP_TIME").split(",");
        PageData finalPage = new PageData();
        for(int i=0;i<rptArray.length;i++){
            pd.put("RPT_ID",rptArray[i]);
            pd.put("OP_TIME",opTimeArray[i]);
            List<PageData> pds = (List<PageData>)daoSupport.findForList("rptDataMapper.getRptZbMsg",pd);
            String rptID = "";
            String opTime = "";
            String  zbID = "";
            String zbName = "";
            String cityID = "";
            String zbData1 = "";
            String zbData2 = "";
            JSONArray ja = new JSONArray();
            for(int j=0;j<pds.size();j++){
                PageData rptMsg = pds.get(j);
                rptID = rptMsg.getString("RPT_ID");
                opTime = rptMsg.getString("OP_TIME");
                zbID = rptMsg.getString("ZB_ID");
                zbName = rptMsg.getString("ZB_NAME");
                cityID = rptMsg.getString("CITY_ID");
                Object zb_data_1 = rptMsg.get("ZB_DATA_1");
                Object zb_data_2 = rptMsg.get("ZB_DATA_2");
                if(zb_data_1==null){
                    zbData1 = "";
                }else{
                    zbData1 = zb_data_1.toString();
                }
                if(zb_data_2==null){
                    zbData2 = "";
                }else{
                    zbData2 = zb_data_2.toString();
                }

                /*zbData1 = rptMsg.getString("ZB_DATA_1");*/
                /*zbData2 = rptMsg.getString("ZB_DATA_2");*/
                RptEntity entity = new RptEntity(rptID,opTime,zbID,zbName,cityID,zbData1,zbData2);
                ja.add(entity);
            }
            Map<String,String> headMap = new LinkedHashMap<String,String>();
            headMap.put("rptID","报表编码");
            headMap.put("opTime","时间");
            headMap.put("zbID","指标编码");
            headMap.put("zbName","指标名称");
            headMap.put("cityID","城市编码");
            headMap.put("zbData1","原始指标值");
            headMap.put("zbData2","更新指标值");
            String title = "报表详情";
            PageData returnPage = new PageData();
            returnPage.put("BODYMSG",ja);
            returnPage.put("HEADMSG",headMap);
            returnPage.put("FILENAME",rptArray[i]+"--"+opTimeArray[i]+".xlsx");
            finalPage.put(i,returnPage);
        }

        return finalPage;
    }

    public void batchUpload(PageData pd, String databaseId, DaoSupport daoSupport)throws  Exception{
        String localFile = pd.getString("DOWNLOADFILE");
        List<String> colParams = new ArrayList<String>();
        colParams.add("ZB_DATA_1");
        colParams.add("ZB_DATA_2");
        colParams.add("OP_TIME");
        colParams.add("ZB_NAME");
        colParams.add("CITY_ID");
        colParams.add("RPT_ID");
        colParams.add("ZB_ID");
        List<PageData> list = UploadExcelUtil.excelType(localFile, colParams);
        for (PageData li : list) {
            String sql = " ZB_DATA = '" +li.getString("ZB_DATA_1")+ "'";
            li.put("sql", sql);
            List<PageData> rptIdList = new ArrayList<PageData>();
            PageData pds = new PageData();
            pds.put("sql", sql);
            pds.put("ZB_ID", li.getString("ZB_ID"));
            pds.put("OP_TIME", li.getString("OP_TIME"));
            rptIdList.add(pds);
            li.put("list", rptIdList);
            daoSupport.update("rptDataMapper.updZbDataMsg", li);
        }
    }

}
