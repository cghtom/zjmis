package com.dcmis.app.business;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.util.PageData;
import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
import java.util.List;

public class AuditDefineBusiness {
    /**
     * 稽核公式查询
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getAuditDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        PageData reData = new PageData();
        int curPage = (Integer.parseInt(pd.getString("iDisplayStart")) / Integer.parseInt(pd.getString("iDisplayLength")) + 1);
        int pageSize = Integer.parseInt(pd.getString("iDisplayLength"));
        String sEcho = pd.getString("sEcho");
        PageHelper.startPage(curPage, pageSize);
        List<PageData> pds = (List<PageData>)daoSupport.findForList("auditDataMapper.getAuditMsg",pd);
        PageData pdNum = (PageData)daoSupport.findForObject("auditDataMapper.auditMsgTotalCount", pd);
        reData.put("aaData", pds);
        reData.put("sEcho", sEcho);
        reData.put("iTotalRecords", pdNum.get("COUNT").toString());
        reData.put("iTotalDisplayRecords", pdNum.get("COUNT").toString());
        return reData;
    }



    /**
     * 根据AUDIT_ID查询稽核公式信息
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getAuditDefineById(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        pd.put("AUDIT_ID", pd.getString("idKey"));
        PageData pds = (PageData)daoSupport.findForObject("auditDataMapper.getAuditMsg",pd);
        return pds;
    }

    /**
     * 稽核公式修改
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void updAuditDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        String sql = " AUDIT_TYPE = '" +pd.getString("AUDIT_TYPE")+ "', AUDIT_FORMULA ='"+pd.getString("AUDIT_FORMULA")+"',RPT_ID='"+pd.getString("RPT_ID")+"' ,MODIFY_DATE = sysdate ";
        pd.put("sql", sql);
        List<PageData> rptIdList = new ArrayList<PageData>();
        PageData pds = new PageData();
        pds.put("sql", sql);
        pds.put("AUDIT_ID", pd.getString("AUDIT_ID"));
        rptIdList.add(pds);
        pd.put("list", rptIdList);
        daoSupport.update("auditDataMapper.updAuditMsg", pd);
    }
    /**
     *
     * 删除稽核公式（state改为0）
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void delAuditDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        String auditIDs = pd.getString("ID");
        String[] auditIDArr = auditIDs.split(",");
        List<PageData> auditIDList = new ArrayList<PageData>();
        for (String auditID : auditIDArr) {
            PageData pds = new PageData();
            pds.put("AUDIT_ID", auditID);
            pds.put("sql", "state = '0', MODIFY_DATE = sysdate");
            auditIDList.add(pds);
        }
        pd.put("list", auditIDList);
        daoSupport.update("auditDataMapper.updAuditMsg", pd);
    }

    /**
     * 稽核公式添加
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void addAuditDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        List<PageData> items = new ArrayList<PageData>();
        PageData pds = new PageData();
        pds.put("AUDIT_ID", pd.getString("AUDIT_ID"));
        pds.put("AUDIT_TYPE", pd.getString("AUDIT_TYPE"));
        pds.put("AUDIT_FORMULA", pd.getString("AUDIT_FORMULA"));
        pds.put("RPT_ID", pd.getString("RPT_ID"));
        items.add(pds);
        pd.put("list", items);
        daoSupport.save("auditDataMapper.addAuditMsg", pd);
    }
}
