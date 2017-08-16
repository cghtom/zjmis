package com.dcmis.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.dcmis.web.constant.SysConstant;
import com.dcmis.web.service.FtpUtil;
import com.dcmis.web.service.HttpService;
import com.dcmis.web.util.ExportExcelUtil;
import com.dcmis.web.util.Logger;
import com.dcmis.web.util.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by 黄福亮 on 2017/7/19.
 */
@Controller
public class MisDataController {

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

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public String uploadDataJson(MultipartFile files,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map properties = request.getParameterMap();
        String curFile = sysConstant.getLocPath() + files.getOriginalFilename();
        String remoteFile = sysConstant.getPath() + files.getOriginalFilename();
        File f = new File(curFile);
        logger.info(sysConstant.getFileHost());
        try {
            FileUtils.copyInputStreamToFile(files.getInputStream(), f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ftpUtil.put(curFile, remoteFile);
        System.out.println(remoteFile);
        properties.put("REMOTEFILE", remoteFile);
        /*properties.put("FILENAME", files.getOriginalFilename());*/
        properties.put("DOWNLOADFILE", curFile);
        String backDate = httpService.doPost("http://localhost:8080/mis/mis/data", properties);
        f.delete();
        return "sucess";
    }


    @ResponseBody
    @RequestMapping("download")
    public ResponseEntity<byte[]> download(HttpServletRequest request) throws Exception {
        Map properties = request.getParameterMap();
        String backDate = httpService.doPost("http://localhost:8080/mis/mis/data", properties);
        JSONObject job = JSONObject.parseObject(backDate);
        System.out.println(job.size());
        String curFile = sysConstant.getLocPath();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<job.size();i++){
            JSONObject jo = (JSONObject)job.get(i+"");
            JSONArray ja = (JSONArray) jo.get("BODYMSG");
            Map<String, String> headMap = (Map<String, String>) jo.get("HEADMSG");
            String name = jo.getString("FILENAME");
            OutputStream outXlsx = new FileOutputStream(curFile+name);
            ExportExcelUtil.exportExcelX("",headMap,ja,null,0,outXlsx);
            outXlsx.close();
            sb.append(curFile+name+",");
        }
        ZipUtil.writeZip(sb,curFile+"test.zip");

        HttpHeaders headers = new HttpHeaders();
        File file=new File(curFile+"test.zip");
        String fileName=new String("test.zip".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        ResponseEntity<byte[]> responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
        file.delete();
        return responseEntity;
    }

}
