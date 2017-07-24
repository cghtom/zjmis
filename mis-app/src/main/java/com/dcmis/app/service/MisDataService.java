package com.dcmis.app.service;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.util.Logger;
import com.dcmis.app.util.PageData;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄福亮 on 2017/7/17.
 */
@Service("misDataService")
public class MisDataService {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    private Logger logger = Logger.getLogger(this.getClass());

    /**
     * 传入 dao 进行业务处理
     * @param pd 传入参数
     * @return
     * @throws Exception
     */
    public PageData getData(PageData pd) throws Exception
    {
        logger.info("interfaceId...start...");
        /** 查询出对应的接口信息 */
        List<PageData> ins = (List<PageData>)dao.findForList("misdataMapper.getInterfaceDefMsg",pd);
        PageData rePds = null;
        if(ins.size() > 1) {
            logger.info("接口信息过多");
        } else {
            PageData interfaceNames = ins.get(0);
            String className = interfaceNames.getString("CLASS_NAME");
            String methodName = interfaceNames.getString("METHOD_NAME");
            String databaseId = interfaceNames.getString("DATABASE_ID");
            Class classType = Class.forName(className);
            Method checkFileMethod = classType.getDeclaredMethod(methodName, new Class[]{PageData.class, String.class, DaoSupport.class});
            rePds = (PageData)checkFileMethod.invoke(classType.newInstance(), pd, databaseId, dao);
        }
        logger.info("interfaceId...end...");
        return rePds;
    }

}
