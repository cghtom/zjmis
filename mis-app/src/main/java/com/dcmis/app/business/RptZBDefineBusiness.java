package com.dcmis.app.business;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.entity.Page;
import com.dcmis.app.util.*;
import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄福亮 on 2017/7/18.
 */
public class RptZBDefineBusiness {

    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * 指标查询
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getZBDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        PageData reData = new PageData();
        int curPage = (Integer.parseInt(pd.getString("iDisplayStart")) / Integer.parseInt(pd.getString("iDisplayLength")) + 1);
        int pageSize = Integer.parseInt(pd.getString("iDisplayLength"));
        String sEcho = pd.getString("sEcho");
        PageHelper.startPage(curPage,pageSize);
        List<PageData> pds = (List<PageData>)daoSupport.findForList("zbDataMapper.getZbMsg",pd);
        PageData pdNum = (PageData)daoSupport.findForObject("zbDataMapper.ZbMsgTotalCount", pd);
        reData.put("aaData", pds);
        reData.put("sEcho", sEcho);
        reData.put("iTotalRecords", pdNum.get("COUNT").toString());
        reData.put("iTotalDisplayRecords", pdNum.get("COUNT").toString());
        return reData;
    }

    public void addZBDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        List<PageData> items = new ArrayList<PageData>();
        PageData pds = new PageData();
        pds.put("RPT_ID", Sequence.nextId());
        pds.put("ZB_ID", pd.getString("ZB_ID"));
        pds.put("ZB_NAME", pd.getString("ZB_NAME"));
        pds.put("ZB_DESC", pd.getString("ZB_DESC"));
        pds.put("ZB_UNIT", pd.getString("ZB_UNIT"));
        pds.put("VERSION", "1");
        items.add(pds);
        pd.put("list", items);
        daoSupport.save("zbDataMapper.addZbMsg", pd);
    }

    /**
     * batch add
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void addBatchZBDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        String fileName = pd.getString("FILENAME");
        String remoteFile = pd.getString("REMOTEFILE");
        String localFile = "D:/file/" + fileName;
        new FtpUtils().downLoad(localFile, remoteFile);
        List<String> colParams = new ArrayList<String>();
        colParams.add("RPT_ID");
        colParams.add("ZB_ID");
        colParams.add("ZB_NAME");
        colParams.add("ZB_DESC");
        colParams.add("ZB_UNIT");
        colParams.add("VERSION");
        List<PageData> items = ExcelUtils.excelType(localFile, colParams);
        pd.put("list", items);
        daoSupport.save("zbDataMapper.addZbMsg", pd);
    }

    public void delZbDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {

        /*String rptIds = pd.getString("ID");
        String[] rptIdArr = rptIds.split(",");
        List<String> rptIdList = new ArrayList<String>();
        for (String rptId : rptIdArr) {
            rptIdList.add(rptId);
        }
        pd.put("list", rptIdList);
        daoSupport.delete();*/
    }

    public void downloadExcel(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {

    }
}