package com.dcmis.app.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DealFormulaUtil {
    public static boolean dealFormulaMap(Map<String, String> formulaMap){
        boolean flag = true;
        BigDecimal leftFormula = new BigDecimal(formulaMap.get("leftFormula"));
        BigDecimal rightFormula = new BigDecimal(formulaMap.get("rightFormula"));
        BigDecimal result = leftFormula.subtract(rightFormula);
        int i = result.compareTo(BigDecimal.ZERO) ;
        if("=".equals(formulaMap.get("compareChar"))){
            if(i==0){
                flag = true;
            }else {
                flag = false;
            }
        }else if(">".equals(formulaMap.get("compareChar"))){
            if(i>0){
                flag = true;
            }else {
                flag = false;
            }
        }else if("<".equals(formulaMap.get("compareChar"))){
            if(i<0){
                flag = true;
            }else {
                flag = false;
            }
        }else if(">=".equals(formulaMap.get("compareChar"))){
            if(i>=0){
                flag = true;
            }else {
                flag = false;
            }
        }else if("<=".equals(formulaMap.get("compareChar"))){
            if(i<=0){
                flag = true;
            }else {
                flag = false;
            }
        }
        return flag;

    }

    public static Map<String, String> dealFormula(String formula){
        String[] formulaArr = null;
        Map<String,String> formulaMap = new HashMap<String, String>();
        if(!"".equals(formula)&&formula!=null){
            if(formula.contains(">")&&!formula.contains(">=")){
                formulaArr = formula.split(">");
                formulaMap.put("leftFormula",formulaArr[0]);
                formulaMap.put("rightFormula",formulaArr[1]);
                formulaMap.put("compareChar",">");
            }else if(formula.contains("<")&&!formula.contains("<=")){
                formulaArr = formula.split("<");
                formulaMap.put("leftFormula",formulaArr[0]);
                formulaMap.put("rightFormula",formulaArr[1]);
                formulaMap.put("compareChar","<");
            }else if(formula.contains("=")&&!formula.contains("<")&&!formula.contains(">")){
                formulaArr = formula.split("=");
                formulaMap.put("leftFormula",formulaArr[0]);
                formulaMap.put("rightFormula",formulaArr[1]);
                formulaMap.put("compareChar","=");
            }else if(formula.contains(">=")){
                formulaArr = formula.split(">=");
                formulaMap.put("leftFormula",formulaArr[0]);
                formulaMap.put("rightFormula",formulaArr[1]);
                formulaMap.put("compareChar",">=");
            }else if(formula.contains("<=")){
                formulaArr = formula.split("<=");
                formulaMap.put("leftFormula",formulaArr[0]);
                formulaMap.put("rightFormula",formulaArr[1]);
                formulaMap.put("compareChar","<=");
            }
            return formulaMap;
        }else{
            System.out.println("计算公式为空！");
            return new HashMap<String, String>();
        }

    }

    public static boolean isOperateChar(String temp) {
        return temp.matches("['+','[-]','*'，'/']");
    }

    public static boolean isContainsCalcChar(String temp){
        if(!"".equals(temp)&&temp!=null){
            if(temp.contains("+")||temp.contains("-")||temp.contains("*")||temp.contains("/")){
                return true;
            }else{
                return false;
            }
        }else {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println(isContainsCalcChar("("));
    }

    public static  String  dealRangeFormula(String formula){
        String sql = "";
        List<String> lis = new ArrayList<String>();
        String regex = "\\$\\{.*?\\}";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(formula);
        while(m.find()){
            lis.add("VARIABLE_NAME = '" + m.group().replaceAll("\\$|\\{|\\}", "").trim() + "'");
        }
        for(int i = 0; i < lis.size(); i++) {
            if(i == (lis.size() - 1)) {
                sql += lis.get(i);
            }else{
                sql += lis.get(i) + " or ";
            }
        }
        return sql;
    }

    public static String replaceVariable(String formula,PageData pd){
        String strRes = "";
        String globalStr = "\\$\\{" + pd.get("VARIABLE_NAME").toString() + "\\}";
        strRes = formula.replaceAll(globalStr, pd.get("VARIABLE_VALUE").toString());
        return strRes;
    }
}
