package com.dcmis.app.business;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.util.PageData;
import com.dcmis.app.util.Sequence;
import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
import java.util.List;

public class FormulaDefineBusiness {
    /**
     * 稽核公式查询
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getFormulaDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        PageData reData = new PageData();
        int curPage = (Integer.parseInt(pd.getString("iDisplayStart")) / Integer.parseInt(pd.getString("iDisplayLength")) + 1);
        int pageSize = Integer.parseInt(pd.getString("iDisplayLength"));
        String sEcho = pd.getString("sEcho");
        PageHelper.startPage(curPage, pageSize);
        List<PageData> pds = (List<PageData>)daoSupport.findForList("formulaDataMapper.getFormulaMsg",pd);
        PageData pdNum = (PageData)daoSupport.findForObject("formulaDataMapper.formulaMsgTotalCount", pd);
        reData.put("aaData", pds);
        reData.put("sEcho", sEcho);
        reData.put("iTotalRecords", pdNum.get("COUNT").toString());
        reData.put("iTotalDisplayRecords", pdNum.get("COUNT").toString());
        return reData;
    }



    /**
     * 根据ID查询稽核公式信息
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getFormulaDefineById(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        pd.put("AUDIT_ID", pd.getString("idKey"));
        PageData pds = (PageData)daoSupport.findForObject("formulaDataMapper.getFormulaMsg",pd);
        return pds;
    }

    /**
     * 稽核公式修改
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void updFormulaDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        String sql = " CHECK_FORMULA = '" +pd.getString("CHECK_FORMULA")+ "', RPT_ID ='"+pd.getString("RPT_ID")+"',CITY_ID='"+pd.getString("CITY_ID")+"' ,UPDATE_TIME = sysdate ";
        pd.put("sql", sql);
        List<PageData> rptIdList = new ArrayList<PageData>();
        PageData pds = new PageData();
        pds.put("sql", sql);
        pds.put("ID", pd.getString("ID"));
        rptIdList.add(pds);
        pd.put("list", rptIdList);
        daoSupport.update("formulaDataMapper.updFormulaMsg", pd);
    }
    /**
     *
     * 删除稽核公式（state改为0）
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void delFormulaDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        String formulaIDs = pd.getString("ID");
        String[] formulaIDArr = formulaIDs.split(",");
        List<PageData> formulaIDList = new ArrayList<PageData>();
        for (String ID : formulaIDArr) {
            PageData pds = new PageData();
            pds.put("ID", ID);
            pds.put("sql", "state = '0', UPDATE_TIME = sysdate");
            formulaIDList.add(pds);
        }
        pd.put("list", formulaIDList);
        daoSupport.update("formulaDataMapper.updFormulaMsg", pd);
    }

    /**
     * 稽核公式添加
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void addFormulaDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        List<PageData> items = new ArrayList<PageData>();
        PageData pds = new PageData();
        pds.put("ID", Sequence.nextId());
        pds.put("CHECK_FORMULA", pd.getString("CHECK_FORMULA"));
        pds.put("RPT_ID", pd.getString("RPT_ID"));
        pds.put("CITY_ID", pd.getString("CITY_ID"));
        items.add(pds);
        pd.put("list", items);
        daoSupport.save("formulaDataMapper.addFormulaMsg", pd);
    }
}
