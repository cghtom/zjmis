package com.dcmis.app.util;

import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FileTransferClient;

import java.io.IOException;

/**
 * Created by 黄福亮 on 2017/7/25.
 */
public class FtpUtils {
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

    public void downLoad(String localFilePath, String remoteFilePath) throws FTPException, IOException {
        initFtp();
        conn.connect();
        conn.downloadFile(localFilePath, remoteFilePath);
    }
}
