package com.github.lly835.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static InputStream connectHttp(String urlStr, String method, byte[] bs) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();

        if (bs != null) {
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(bs);
            outputStream.flush();
            outputStream.close();
        }

        return connection.getInputStream();
    }
}
