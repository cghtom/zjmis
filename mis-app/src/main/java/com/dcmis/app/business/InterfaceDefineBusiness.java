package com.dcmis.app.business;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.util.PageData;
import com.github.pagehelper.PageHelper;

import java.util.ArrayList;
import java.util.List;

public class InterfaceDefineBusiness {
    /**
     * 报表查询
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getInterfaceDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        PageData reData = new PageData();
        int curPage = (Integer.parseInt(pd.getString("iDisplayStart")) / Integer.parseInt(pd.getString("iDisplayLength")) + 1);
        int pageSize = Integer.parseInt(pd.getString("iDisplayLength"));
        String sEcho = pd.getString("sEcho");
        PageHelper.startPage(curPage, pageSize);
        List<PageData> pds = (List<PageData>)daoSupport.findForList("interfaceDataMapper.getInterfaceMsg",pd);
        PageData pdNum = (PageData)daoSupport.findForObject("interfaceDataMapper.interfaceMsgTotalCount", pd);
        reData.put("aaData", pds);
        reData.put("sEcho", sEcho);
        reData.put("iTotalRecords", pdNum.get("COUNT").toString());
        reData.put("iTotalDisplayRecords", pdNum.get("COUNT").toString());
        return reData;
    }



    /**
     * 根据INTERFACE_CODE查询接口信息 MISGETINTERFACEMSGBYID
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @return
     * @throws Exception
     */
    public PageData getInterfaceDefineById(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        pd.put("INTERFACE_CODE", pd.getString("idKey"));
        PageData pds = (PageData)daoSupport.findForObject("interfaceDataMapper.getInterfaceMsg",pd);
        return pds;
    }

    /**
     * 报表修改 MISUPDINTERFACEMSG
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void updInterfaceDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        String sql = " INTERFACE_NAME = '" +pd.getString("INTERFACE_NAME")+ "', UPLOAD_TIME ='"+pd.getString("UPLOAD_TIME")+"', UPDATE_TIME = sysdate ";
        pd.put("sql", sql);
        List<PageData> rptIdList = new ArrayList<PageData>();
        PageData pds = new PageData();
        pds.put("sql", sql);
        pds.put("INTERFACE_CODE", pd.getString("INTERFACE_CODE"));
        rptIdList.add(pds);
        pd.put("list", rptIdList);
        daoSupport.update("interfaceDataMapper.updInterfaceMsg", pd);
    }
    /**
     *
     * 删除接口（state改为0）
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void delInterfaceDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        String interfaceCodes = pd.getString("ID");
        String[] interfaceCodeArr = interfaceCodes.split(",");
        List<PageData> interfaceCodeList = new ArrayList<PageData>();
        for (String interfaceCode : interfaceCodeArr) {
            PageData pds = new PageData();
            pds.put("INTERFACE_CODE", interfaceCode);
            pds.put("sql", "state = '0', update_time = sysdate");
            interfaceCodeList.add(pds);
        }
        pd.put("list", interfaceCodeList);
        daoSupport.update("interfaceDataMapper.updInterfaceMsg", pd);
    }

    /**
     * 接口添加
     * @param pd
     * @param databaseId
     * @param daoSupport
     * @throws Exception
     */
    public void addInterfaceDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        List<PageData> items = new ArrayList<PageData>();
        PageData pds = new PageData();
        pds.put("INTERFACE_CODE", pd.getString("INTERFACE_CODE"));
        pds.put("INTERFACE_NAME", pd.getString("INTERFACE_NAME"));
        pds.put("UPLOAD_TIME", pd.getString("UPLOAD_TIME"));
        items.add(pds);
        pd.put("list", items);
        daoSupport.save("interfaceDataMapper.addInterfaceMsg", pd);
    }
}
