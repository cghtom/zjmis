package com.dcmis.web.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by 黄福亮 on 2017/7/24.
 */
@Component("sysConstant")
public class SysConstant {

    @Value("${file.service.host}")
    private String fileHost;

    @Value("${file.service.port}")
    private String filePort;

    @Value("${file.service.username}")
    private String fileUserName;

    @Value("${file.service.password}")
    private String filePassword;

    @Value("${file.service.path}")
    private String path;

    @Value("${local.service.path}")
    private String locPath;

    public String getFileHost() {
        return fileHost;
    }

    public String getFilePort() {
        return filePort;
    }

    public String getFileUserName() {
        return fileUserName;
    }

    public String getFilePassword() {
        return filePassword;
    }

    public String getPath() {
        return path;
    }

    public String getLocPath() {
        return locPath;
    }
}
