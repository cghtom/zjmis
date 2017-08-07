package com.dcmis.app.util;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 计算器工具类
 * @author shuqi
 * @date  2015-7-23
 * @version since 1.0
 */
public class Operate {
    public static void main(String[] args) {
        System.out.println(parseExp("3.11*4+2-5"));
    }

    public static BigDecimal arithmetic(String exp){
        if(!exp.matches("\\d+")){
            String result = parseExp(exp).replaceAll("[\\[\\]]", "");
            return new BigDecimal(result);
        }else{
            return new BigDecimal(exp);
        }
    }
    /**
     * 最小计数单位
     *
     */
    private static String minExp="^((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))[\\+\\-\\*\\/]((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))$";
    /**
     * 不带括号的运算
     */
    private static String noParentheses="^[^\\(\\)]+$";
    /**
     * 匹配乘法或者除法
     */
    private static String priorOperatorExp="(((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))[\\*\\/]((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\])))";
    /**
     * 匹配加法和减法
     */
    private static String operatorExp="(((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\]))[\\+\\-]((\\d+(\\.\\d+)?)|(\\[\\-\\d+(\\.\\d+)?\\])))";
    /**
     * 匹配只带一个括号的
     */
    private static String minParentheses="\\([^\\(\\)]+\\)";

    /**
     * 解析计算四则运算表达式，例：2+((3+4)*2-22)/2*3
     * @param expression
     * @return
     */
    public static String parseExp(String expression){
        //方法进入 先替换空格，在去除运算两边的()号
        expression=expression.replaceAll("\\s+", "").replaceAll("^\\(([^\\(\\)]+)\\)$", "$1");

        //最小表达式计算
        if(expression.matches(minExp)){
            String result=calculate(expression);
            return Double.parseDouble(result)>=0?result:"["+result+"]";
        }
        //计算不带括号的四则运算
        if(expression.matches(noParentheses)){
            Pattern patt=Pattern.compile(priorOperatorExp);
            Matcher mat=patt.matcher(expression);
            if(mat.find()){
                String tempMinExp=mat.group();
                expression=expression.replaceFirst(priorOperatorExp, parseExp(tempMinExp));
            }else{
                patt=Pattern.compile(operatorExp);
                mat=patt.matcher(expression);

                if(mat.find()){
                    String tempMinExp=mat.group();
                    expression=expression.replaceFirst(operatorExp, parseExp(tempMinExp));
                }
            }
            return parseExp(expression);
        }

        //计算带括号的四则运算
        Pattern patt=Pattern.compile(minParentheses);
        Matcher mat=patt.matcher(expression);
        if(mat.find()){
            String tempMinExp=mat.group();
            expression=expression.replaceFirst(minParentheses, parseExp(tempMinExp));
        }
        return parseExp(expression);
    }
    /**
     * 计算最小单位四则运算表达式（两个数字）
     * @param exp
     * @return
     */
    public static String calculate(String exp){
        exp=exp.replaceAll("[\\[\\]]", "");
        String number[]=exp.replaceFirst("(\\d)[\\+\\-\\*\\/]", "$1,").split(",");
        BigDecimal number1=new BigDecimal(number[0]);
        BigDecimal number2=new BigDecimal(number[1]);
        BigDecimal result=null;

        String operator=exp.replaceFirst("^.*\\d([\\+\\-\\*\\/]).+$", "$1");
        if("+".equals(operator)){
            result=number1.add(number2);
        }else if("-".equals(operator)){
            result=number1.subtract(number2);
        }else if("*".equals(operator)){
            result=number1.multiply(number2);
        }else if("/".equals(operator)){
            //第二个参数为精度，第三个为四色五入的模式
            result=number1.divide(number2,5,BigDecimal.ROUND_CEILING);
        }

        return result!=null?result.toString():null;
    }

}