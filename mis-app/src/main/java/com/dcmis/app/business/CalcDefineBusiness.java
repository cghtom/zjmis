package com.dcmis.app.business;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.util.PageData;
import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
import java.util.List;

public class CalcDefineBusiness {
    /**
     * 计算公式查询
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getCalcDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        PageData reData = new PageData();
        int curPage = (Integer.parseInt(pd.getString("iDisplayStart")) / Integer.parseInt(pd.getString("iDisplayLength")) + 1);
        int pageSize = Integer.parseInt(pd.getString("iDisplayLength"));
        String sEcho = pd.getString("sEcho");
        PageHelper.startPage(curPage, pageSize);
        List<PageData> pds = (List<PageData>)daoSupport.findForList("calcDataMapper.getCalcMsg",pd);
        PageData pdNum = (PageData)daoSupport.findForObject("calcDataMapper.auditCalcTotalCount", pd);
        reData.put("aaData", pds);
        reData.put("sEcho", sEcho);
        reData.put("iTotalRecords", pdNum.get("COUNT").toString());
        reData.put("iTotalDisplayRecords", pdNum.get("COUNT").toString());
        return reData;
    }



    /**
     * 根据CALC_ID查询计算公式信息
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getCalcDefineById(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        pd.put("CALC_ID", pd.getString("idKey"));
        PageData pds = (PageData)daoSupport.findForObject("calcDataMapper.getCalcMsg",pd);
        return pds;
    }

    /**
     * 计算公式修改
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void updCalcDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        String sql = " CALC_PROC = '" +pd.getString("CALC_PROC")+ "', CALC_NUMERIC ='"+pd.getString("CALC_NUMERIC")+"',CALC_TABLE='"+pd.getString("CALC_TABLE")+"',ZB_ID='"+pd.getString("ZB_ID")+"',CALC_CONDITION='"+pd.getString("CALC_CONDITION")+"' ,MODIFY_DATE = sysdate ";
        pd.put("sql", sql);
        List<PageData> rptIdList = new ArrayList<PageData>();
        PageData pds = new PageData();
        pds.put("sql", sql);
        pds.put("CALC_ID", pd.getString("CALC_ID"));
        rptIdList.add(pds);
        pd.put("list", rptIdList);
        daoSupport.update("calcDataMapper.updCalcMsg", pd);
    }
    /**
     *
     * 删除计算公式（state改为0）
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void delCalcDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        String calcIDs = pd.getString("ID");
        String[] calcIDArr = calcIDs.split(",");
        List<PageData> calcIDList = new ArrayList<PageData>();
        for (String calcID : calcIDArr) {
            PageData pds = new PageData();
            pds.put("CALC_ID", calcID);
            pds.put("sql", "state = '0', MODIFY_DATE = sysdate");
            calcIDList.add(pds);
        }
        pd.put("list", calcIDList);
        daoSupport.update("calcDataMapper.updCalcMsg", pd);
    }

    /**
     * 计算公式添加
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void addCalcDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        List<PageData> items = new ArrayList<PageData>();
        PageData pds = new PageData();
        pds.put("CALC_ID", pd.getString("CALC_ID"));
        pds.put("CALC_PROC", pd.getString("CALC_PROC"));
        pds.put("CALC_NUMERIC", pd.getString("CALC_NUMERIC"));
        pds.put("CALC_CONDITION", pd.getString("CALC_CONDITION"));
        pds.put("CALC_TABLE", pd.getString("CALC_TABLE"));
        pds.put("ZB_ID", pd.getString("ZB_ID"));
        items.add(pds);
        pd.put("list", items);
        daoSupport.save("calcDataMapper.addCalcMsg", pd);
    }
}
