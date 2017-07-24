package com.dcmis.app.business;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.util.Logger;
import com.dcmis.app.util.PageData;
import com.dcmis.app.util.Sequence;
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
        int curPage = Integer.parseInt(pd.getString("iDisplayStart"));
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
}