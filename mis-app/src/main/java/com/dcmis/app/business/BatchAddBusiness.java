package com.dcmis.app.business;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.util.FtpUtils;
import com.dcmis.app.util.PageData;
import com.enterprisedt.net.ftp.FTPException;

import java.io.IOException;

/**
 * Created by 黄福亮 on 2017/7/25.
 */
public class BatchAddBusiness {

    public void addZBDefine(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception {
        String fileName = pd.getString("FILENAME");
        String remoteFile = pd.getString("REMOTEFILE");
        String localFile = "D:/file/" + fileName;
        ftpFile(localFile, remoteFile);

    }

    public void ftpFile(String localFile, String remoteFile) throws FTPException, IOException {

        new FtpUtils().downLoad(localFile, remoteFile);
    }

}
