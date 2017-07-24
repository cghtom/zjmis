package com.dcmis.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.dcmis.web.constant.SysConstant;
import com.dcmis.web.service.HttpService;
import com.dcmis.web.service.FtpUtil;
import com.dcmis.web.util.Logger;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by 黄福亮 on 2017/7/19.
 */
@Controller
public class MisDataController{

    private Logger logger = Logger.getLogger(this.getClass());

    @Resource
    private HttpService httpService;

    @Resource
    private SysConstant sysConstant;

    @Resource
    private FtpUtil ftpUtil;

    @RequestMapping(value = "dataJsonP")
    @ResponseBody
    public JSONPObject misDataJsonp(HttpServletRequest request, String callBack) throws Exception {
        Map properties = request.getParameterMap();
        String backDate = httpService.doPost("http://localhost:8080/mis/mis/data", properties);
        JSONPObject returnJsonp = new JSONPObject(callBack);
        returnJsonp.addParameter(backDate);
        return returnJsonp;
    }

    @RequestMapping(value = "dataJson")
    @ResponseBody
    public JSONObject misDataJson(HttpServletRequest request) throws Exception {
        Map properties = request.getParameterMap();
        String backDate = httpService.doPost("http://localhost:8080/mis/mis/data", properties);
        JSONObject jo = JSONObject.parseObject(backDate);
        return jo;
    }

    @RequestMapping(value="/upload", method = RequestMethod.POST)
    @ResponseBody
    public String uploadDataJson(MultipartFile files,
                                     HttpServletRequest request,HttpServletResponse response) throws Exception {
        String curFile = sysConstant.getLocPath()+files.getOriginalFilename();
        String remoteFile = sysConstant.getPath()+files.getOriginalFilename();
        File f=new File(curFile);
        logger.info(sysConstant.getFileHost());
        try {
            FileUtils.copyInputStreamToFile(files.getInputStream(), f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ftpUtil.put(curFile, remoteFile);
        return "fileuploaddone";
    }
}
