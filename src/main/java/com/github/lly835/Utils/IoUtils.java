package com.github.lly835.Utils;

import java.io.IOException;
import java.io.InputStream;

public class IoUtils {
    public static String inputStreamToString(InputStream inputStream) throws IOException{
        int size =inputStream.available();
        byte[] bs =new byte[size];
        inputStream.read(bs);
        String message=new String(bs,"UTF-8");
        return message;
    }
}
