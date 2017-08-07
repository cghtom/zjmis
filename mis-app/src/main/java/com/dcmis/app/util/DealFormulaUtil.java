package com.dcmis.app.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
}
