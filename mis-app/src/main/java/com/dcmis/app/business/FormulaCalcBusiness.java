package com.dcmis.app.business;

import com.dcmis.app.dao.DaoSupport;
import com.dcmis.app.util.DealFormulaUtil;
import com.dcmis.app.util.Operate;
import com.dcmis.app.util.PageData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FormulaCalcBusiness {

    public void checkTest(PageData pd, String databaseId, DaoSupport daoSupport)throws Exception{
        pd.put("OP_TIME","201707");
        List<PageData> checkDataList = (List<PageData>)daoSupport.findForList("checkDataMapper.getCheckMsg",pd);
        if(checkDataList.size()>0){
            for(int k=0;k<checkDataList.size();k++){
                String formula = checkDataList.get(k).get("CHECK_FORMULA").toString();
                Map<String, String> formulaMap = DealFormulaUtil.dealFormula(formula);
                List<String> formluaList = new ArrayList<String>();
                formluaList.add(formulaMap.get("leftFormula"));
                formluaList.add(formulaMap.get("rightFormula"));
                List<String> formluaListTemp = new ArrayList<String>();
                for(int i=0;i<formluaList.size();i++){
                    String finalFormula = "";
                    String temp;// 用来临时存放读取的字符
                    StringBuffer tempNum = new StringBuffer();// 用来临时存放数字字符串(当为多位数时)
                    StringBuffer string = new StringBuffer().append(formluaList.get(i)+"#");// 用来保存，提高效率
                    while (string.length() != 0) {
                        temp = string.substring(0, 1);
                        string.delete(0, 1);
                        String tempStr = "";
                        if (DealFormulaUtil.isOperateChar(temp)||"#".equals(temp)) {
                            String ZB_ID = tempNum.toString();
                            tempStr = ZB_ID.replace("(","").replace(")","");
                            pd.put("ZB_ID",ZB_ID);
                            PageData pds = (PageData)daoSupport.findForObject("checkDataMapper.getZBDataMsgByID",pd);
                            if(pds==null){
                                finalFormula += (ZB_ID+temp);
                                tempNum.delete(0, tempNum.length());
                            }else{
                                String ZB_DATA = pds.get("ZB_DATA").toString();
                                tempNum.delete(0, tempNum.length());
                                ZB_ID = ZB_ID.replaceAll(tempStr,ZB_DATA);
                                finalFormula += (ZB_ID+temp);
                            }
                        }else{
                            // 当为非操作符时
                            tempNum = tempNum.append(temp);
                        }
                    }
                    formluaListTemp.add(finalFormula.substring(0,finalFormula.length()-1));
                }
                for (int i=0;i<formluaListTemp.size();i++){
                    String result = "";
                    if(DealFormulaUtil.isContainsCalcChar(formluaListTemp.get(i))){
                        result = Operate.parseExp(formluaListTemp.get(i));
                    }else{
                        result = formluaListTemp.get(i);
                    }
                    if(i==0){
                        formulaMap.put("leftFormula",result);
                    }else if(i==1){
                        formulaMap.put("rightFormula",result);
                    }
                }
                System.out.println(formulaMap.get("leftFormula")+"--------"+formulaMap.get("rightFormula"));
                boolean flag = DealFormulaUtil.dealFormulaMap(formulaMap);
                System.out.println(flag);
                String sql = "";
                if(flag==true){
                    sql = " IS_CHECK = 1";

                }else{
                    sql = "IS_CHECK = 0";
                }
                pd.put("sql", sql);
                List<PageData> checkList = new ArrayList<PageData>();
                PageData checkPds = new PageData();
                checkPds.put("sql", sql);
                checkPds.put("CHECK_FORMULA", formula);
                checkPds.put("OP_TIME", "201707");
                checkList.add(checkPds);
                pd.put("list", checkList);
                daoSupport.update("checkDataMapper.updCheckMsg", pd);
            }
        }
    }


    public void rangeFormula(PageData pd, String databaseId, DaoSupport daoSupport) throws Exception{
        String ZB_ID = "zb3";
        String formula = "${current_growth}<10%";
        String sql = DealFormulaUtil.dealRangeFormula(formula);
        pd.put("sql", sql);
        List<PageData> variableList = new ArrayList<PageData>();
        PageData pds = new PageData();
        pds.put("sql", sql);
        variableList.add(pds);
        pd.put("list", variableList);
        List<PageData> variableMsgList = (List<PageData>)daoSupport.findForList("formulaDataMapper.getVariableValue",pd);
        for (int i=0;i<variableMsgList.size();i++){
            PageData variablePdTmp = variableMsgList.get(i);
            formula = DealFormulaUtil.replaceVariable(formula,variablePdTmp);
        }
        Map<String, String> formulaMap = DealFormulaUtil.dealFormula(formula);
        List<String> formluaListTemp = new ArrayList<String>();
        List<String> formluaList = new ArrayList<String>();
        formluaList.add(formulaMap.get("leftFormula"));
        formluaList.add(formulaMap.get("rightFormula"));
        for(int i=0;i<formluaList.size();i++){
            String finalFormula = "";
            String temp;// 用来临时存放读取的字符
            StringBuffer tempNum = new StringBuffer();// 用来临时存放数字字符串(当为多位数时)
            StringBuffer string = new StringBuffer().append(formluaList.get(i)+"#");// 用来保存，提高效率
            if(string.toString().contains("%")){
                formluaListTemp.add(string.toString().substring(0,string.toString().length()-1));
                continue;
            }
            while (string.length() != 0) {
                temp = string.substring(0, 1);
                string.delete(0, 1);
                String tempStr = "";
                if (DealFormulaUtil.isOperateChar(temp)||"#".equals(temp)) {
                    String isCurrOrLast = tempNum.toString();
                    tempStr = isCurrOrLast.replace("(","").replace(")","");
                    if("a".equals(tempStr)){
                        pd.put("OP_TIME","201708");
                    }else if("b".equals(tempStr)){
                        pd.put("OP_TIME","201707");
                    }
                    pd.put("ZB_ID",ZB_ID);
                    PageData zbList = (PageData)daoSupport.findForObject("checkDataMapper.getZBDataMsgByID",pd);
                    if(zbList==null){
                        System.out.println("指标不存在");
                        finalFormula += (isCurrOrLast+temp);
                        tempNum.delete(0, tempNum.length());
                    }else{
                        String ZB_DATA = zbList.get("ZB_DATA").toString();
                        isCurrOrLast = isCurrOrLast.replaceAll(tempStr,ZB_DATA);
                        tempNum.delete(0, tempNum.length());
                        finalFormula += (isCurrOrLast+temp);
                    }
                }else{
                    // 当为非操作符时
                    tempNum = tempNum.append(temp);
                }
            }
            formluaListTemp.add(finalFormula.substring(0,finalFormula.length()-1));
        }
        for (int i=0;i<formluaListTemp.size();i++){
            String result = "";
            if(DealFormulaUtil.isContainsCalcChar(formluaListTemp.get(i))){
                result = Operate.parseExp(formluaListTemp.get(i)).replace("[","").replace("]","".replace("%",""));
                double tmpResult = Double.parseDouble(result);
                tmpResult = tmpResult*100.00;
                result = String.valueOf(tmpResult);
            }else{
                System.out.println(formluaListTemp.get(i)+"+++++=");
                result = formluaListTemp.get(i).replace("%","");
            }
            if(i==0){
                formulaMap.put("leftFormula",result);
            }else if(i==1){
                formulaMap.put("rightFormula",result);
            }
        }
        System.out.println(formulaMap.get("leftFormula")+"--------"+formulaMap.get("rightFormula"));
        boolean flag = DealFormulaUtil.dealFormulaMap(formulaMap);
        System.out.println(flag);
    }
}
