package com.dcmis.app.business;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.util.PageData;
import com.github.pagehelper.PageHelper;

import java.util.List;

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
        System.out.println(update+"--------");
        System.out.println("123");
    }

}
