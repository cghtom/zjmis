package com.dcmis.app.business;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.util.PageData;
import com.github.pagehelper.PageHelper;

import java.util.List;

public class checkDataBusiness {
    /**
     * 查询稽核结果页面
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getCheckResult(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        PageData reData = new PageData();
        int curPage = (Integer.parseInt(pd.getString("iDisplayStart")) / Integer.parseInt(pd.getString("iDisplayLength")) + 1);
        int pageSize = Integer.parseInt(pd.getString("iDisplayLength"));
        String sEcho = pd.getString("sEcho");
        PageHelper.startPage(curPage, pageSize);
        List<PageData> pds = (List<PageData>)daoSupport.findForList("checkDataMapper.getCheckMsg",pd);
        PageData pdNum = (PageData)daoSupport.findForObject("checkDataMapper.checkMsgTotalCount", pd);
        reData.put("aaData", pds);
        reData.put("sEcho", sEcho);
        reData.put("iTotalRecords", pdNum.get("COUNT").toString());
        reData.put("iTotalDisplayRecords", pdNum.get("COUNT").toString());
        return reData;
    }


}
