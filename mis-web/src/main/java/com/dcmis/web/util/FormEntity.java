package com.dcmis.web.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 黄福亮 on 2017/7/19.
 */
public class FormEntity {

    public static UrlEncodedFormEntity getFormEntity(HttpServletRequest request) throws UnsupportedEncodingException {
        UrlEncodedFormEntity formEntity = null;
        List<NameValuePair> parameters = null;
        Map properties = request.getParameterMap();
        Iterator entries = properties.entrySet().iterator();
        Map.Entry entry;
        String name = "";
        String value = "";
        if (!properties.isEmpty()) {
            parameters = new ArrayList<NameValuePair>();
            while (entries.hasNext()) {
                entry = (Map.Entry) entries.next();
                parameters.add(new BasicNameValuePair(String.valueOf(entry.getKey()), String.valueOf(entry.getValue())));
            }
            formEntity = new UrlEncodedFormEntity(parameters);
        }
        return formEntity;
    }
}
