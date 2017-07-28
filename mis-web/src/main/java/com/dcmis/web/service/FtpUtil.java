package com.dcmis.web.service;

import com.dcmis.web.constant.SysConstant;
import com.dcmis.web.util.Logger;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * Created by 黄福亮 on 2017/7/24.
 */
@Service
public class FtpUtil {

    @Resource
    private SysConstant sysConstant;

    private Logger logger = Logger.getLogger(this.getClass());

    private FileTransferClient conn;

    public void initFtp() {
        conn = new FileTransferClient();
        try {
            conn.setRemoteHost("20.26.26.45");
            conn.setRemotePort(21);
            conn.setUserName("yjgk");
            conn.setPassword("yjgk#Pass");
            conn.setTimeout(1000 * 60 * 10);
        } catch (Exception e) {
            System.out.println("ftp connect failed!");
            e.printStackTrace();
        }
    }

    public void put(String localFilePath, String remoteFilePath) throws Exception{
        initFtp();
        conn.connect();
        logger.debug(localFilePath + " " + remoteFilePath);
        conn.uploadFile(localFilePath, remoteFilePath);
    }
}