package com.dcmis.app.business;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.util.PageData;

import java.util.List;

/**
 * Created by 黄福亮 on 2017/7/27.
 */
public class TableConfigBusiness {

    /** serviceName:MISTABLECONFIG */
    public PageData getConfMsg(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        PageData pds = (PageData)daoSupport.findForObject("misdataMapper.getTableConfigMsg", pd);
        return pds;
    }
}
