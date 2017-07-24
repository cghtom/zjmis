package com.dcmis.app.controller;

import com.dcmis.app.service.MisDataService;
import com.dcmis.app.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄福亮 on 2017/7/17.
 */
@Controller
@RequestMapping("mis")
public class DataController extends BaseController{

    @Resource(name = "misDataService")
    private MisDataService misDataService;

    @RequestMapping(value = "/data")
    @ResponseBody
    public PageData misData() throws Exception {

        PageData pd = this.getPageData();
        return misDataService.getData(pd);

    }
}
