package com.dcmis.app.util;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class UploadExcelUtil {
    private static List<PageData> readXlsx(String path, List<String> colParams) throws Exception {
        InputStream is = new FileInputStream(path);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        List<PageData> result = new ArrayList<PageData>();
        for (Sheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null) continue;
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                Row row = xssfSheet.getRow(rowNum);
                int minColIx = row.getFirstCellNum();
                int maxColIx = row.getLastCellNum();
                PageData data = new PageData();
                int colIndex = 0;
                for (int colIx = minColIx; colIx < maxColIx; colIx++){
                    Cell cell = row.getCell(colIx);
                    if (cell == null) continue;
                    if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        DecimalFormat df = new DecimalFormat("0");
                        /*rowList.add(df.format(cell.getNumericCellValue()));*/
                        data.put(colParams.get(colIndex), df.format(cell.getNumericCellValue()));
                    } else {
                        /*rowList.add(cell.toString());*/
                        data.put(colParams.get(colIndex), cell.toString());
                    }
                    colIndex++;
                }
                result.add(data);
            }

        }
        return result;
    }


    private static List<PageData> readXls(String path, List<String> colParams) throws Exception {
        InputStream is = new FileInputStream(path);
        HSSFWorkbook xssfWorkbook = new HSSFWorkbook(is);
        List<PageData> result = new ArrayList<PageData>();
        for (Sheet xssfSheet : xssfWorkbook) {
            if (xssfSheet == null) continue;
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                Row row = xssfSheet.getRow(rowNum);
                int minColIx = row.getFirstCellNum();
                int maxColIx = row.getLastCellNum();
                PageData data = new PageData();
                int colIndex = 0;
                for (int colIx = minColIx; colIx < maxColIx; colIx++){
                    Cell cell = row.getCell(colIx);
                    if (cell == null) continue;
                    if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        DecimalFormat df = new DecimalFormat("0");
                        /*rowList.add(df.format(cell.getNumericCellValue()));*/
                        data.put(colParams.get(colIndex), df.format(cell.getNumericCellValue()));
                    } else {
                        /*rowList.add(cell.toString());*/
                        data.put(colParams.get(colIndex), cell.toString());
                    }
                    colIndex++;
                }
                result.add(data);
            }

        }
        return result;

    }

    private static boolean isExcel2003(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xls)$");
    }

    //@描述：是否是2007的excel，返回true是2007
    private static boolean isExcel2007(String filePath)  {
        return filePath.matches("^.+\\.(?i)(xlsx)$");
    }

    public static List<PageData> excelType(String filePath, List<String> colParams) throws Exception {
        if(isExcel2003(filePath)){
            return readXls(filePath, colParams);
        } else {
            return readXlsx(filePath, colParams);
        }
    }



    public static void main(String[] args) throws Exception {
        List<String> colParams = new ArrayList<String>();
        colParams.add("ZB_DATA_1");
        colParams.add("ZB_DATA_2");
        colParams.add("OP_TIME");
        colParams.add("ZB_NAME");
        colParams.add("CITY_ID");
        colParams.add("RPT_ID");
        colParams.add("ZB_ID");
        List<PageData> list = excelType("D://upload//fb--201708.xlsx", colParams);
        for (PageData li : list) {

            System.out.println(li);
        }
    }
}
