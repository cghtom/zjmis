package com.dcmis.web.test;

import com.dcmis.web.service.HttpService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 黄福亮 on 2017/7/19.
 */
public class TestApp {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:spring/ApplicationContext-*.xml");
        System.out.println(context);

        HttpService httpService = context.getBean("httpService",
                HttpService.class);
        System.out.println(httpService);
        try {
            Map<String, Object> maps = new HashMap<String, Object>();
            maps.put("serviceName", "MISGETZBMSG");
            String string = httpService.doPost(
                    "http://localhost:8080/mis/mis/data", maps);
            System.out.println(string);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
