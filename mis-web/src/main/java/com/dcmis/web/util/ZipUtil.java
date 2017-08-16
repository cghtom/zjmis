package com.dcmis.web.util;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {

    public static void writeZip(StringBuffer sb,String zipname) throws IOException {
        String[] files = sb.toString().split(",");
        System.out.println(files);
        OutputStream os = new BufferedOutputStream( new FileOutputStream( zipname ) );
        ZipOutputStream zos = new ZipOutputStream( os );
        byte[] buf = new byte[8192];
        int len;
        for (int i=0;i<files.length;i++) {
            File file = new File( files[i] );
            if ( !file.isFile() ) continue;
            ZipEntry ze = new ZipEntry( file.getName() );
            zos.putNextEntry( ze );
            BufferedInputStream bis = new BufferedInputStream( new FileInputStream( file ) );
            while ( ( len = bis.read( buf ) ) > 0 ) {
                zos.write( buf, 0, len );
            }
            zos.closeEntry();
        }
//        zos.setEncoding("GBK");
        zos.closeEntry();
        zos.close();

        for(int i=0;i<files.length;i++){
            System.out.println("------------"+files );
            File file= new File(files[i] );
            file.delete();
        }
    }

}
