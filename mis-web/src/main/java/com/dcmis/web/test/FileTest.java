package com.dcmis.web.test;

import java.io.*;

/**
 * Created by 黄福亮 on 2017/7/25.
 */
public class FileTest {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\yjgk\\bak\\1.txt");
        InputStreamReader isr = new InputStreamReader(new FileInputStream("D:\\yjgk\\bak\\1.txt"), "UTF-8");
        BufferedReader reader = null;
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("D:\\yjgk\\bak\\2.txt")));
        try {
            reader = new BufferedReader(isr);
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                tempString = new String(tempString.getBytes(), "utf-8");
                tempString = tempString.trim();
                writer.write(tempString);
            }
            writer.flush();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

    }
}
