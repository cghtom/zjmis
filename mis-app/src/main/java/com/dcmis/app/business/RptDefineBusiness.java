package com.dcmis.app.business;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.util.PageData;
import com.dcmis.app.util.Sequence;
import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄福亮 on 2017/7/27.
 */
public class RptDefineBusiness {
    /**
     * 报表查询
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getRptDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        PageData reData = new PageData();
        int curPage = (Integer.parseInt(pd.getString("iDisplayStart")) / Integer.parseInt(pd.getString("iDisplayLength")) + 1);
        int pageSize = Integer.parseInt(pd.getString("iDisplayLength"));
        String sEcho = pd.getString("sEcho");
        PageHelper.startPage(curPage, pageSize);
        List<PageData> pds = (List<PageData>)daoSupport.findForList("rptDataMapper.getRptMsg",pd);
        PageData pdNum = (PageData)daoSupport.findForObject("rptDataMapper.RptMsgTotalCount", pd);
        reData.put("aaData", pds);
        reData.put("sEcho", sEcho);
        reData.put("iTotalRecords", pdNum.get("COUNT").toString());
        reData.put("iTotalDisplayRecords", pdNum.get("COUNT").toString());
        return reData;
    }

    /**
     * 报表添加
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void addRptDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        List<PageData> items = new ArrayList<PageData>();
        PageData pds = new PageData();
        pds.put("RPT_ID", pd.getString("RPT_ID"));
        pds.put("RPT_NAME", pd.getString("RPT_NAME"));
        pds.put("RPT_DESC", pd.getString("RPT_DESC"));
        items.add(pds);
        pd.put("list", items);
        daoSupport.save("rptDataMapper.addRptMsg", pd);
    }

    /**
     *
     * 删除报表（state改为0）
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void delRptDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        String rptIds = pd.getString("ID");
        String[] rptIdArr = rptIds.split(",");
        List<PageData> rptIdList = new ArrayList<PageData>();
        for (String rptId : rptIdArr) {
            PageData pds = new PageData();
            pds.put("RPT_ID", rptId);
            pds.put("sql", "state = '0', modify_date = sysdate");
            rptIdList.add(pds);
        }
        pd.put("list", rptIdList);
        daoSupport.update("rptDataMapper.updRptMsg", pd);
    }

    /**
     * 根据Id报表查询 MISGETRPTMSGBYID
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getRptDefineById(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        pd.put("RPT_ID", pd.getString("idKey"));
        PageData pds = (PageData)daoSupport.findForObject("rptDataMapper.getRptMsg",pd);
        return pds;
    }


    /**
     * 报表修改 MISUPDRPTMSG
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void updRptDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        String sql = " RPT_NAME = '" +pd.getString("RPT_NAME")+ "', RPT_DESC ='"+pd.getString("RPT_DESC")+"', MODIFY_DATE = sysdate ";
        pd.put("sql", sql);
        List<PageData> rptIdList = new ArrayList<PageData>();
        PageData pds = new PageData();
        pds.put("sql", sql);
        pds.put("RPT_ID", pd.getString("RPT_ID"));
        rptIdList.add(pds);
        pd.put("list", rptIdList);
        daoSupport.update("rptDataMapper.updRptMsg", pd);
    }

}
