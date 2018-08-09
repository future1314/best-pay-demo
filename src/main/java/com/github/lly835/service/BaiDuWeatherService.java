package com.github.lly835.service;

import com.github.lly835.bean.baidu.*;
import com.github.lly835.controller.AuthController;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

public class BaiDuWeatherService {
    private static String api = "http://api.map.baidu.com/telematics/v3/weather?";
    private static String output = "json";
    private static String ak = "29AV0OtxV1XbQR3rXrusN0a7b83BMSBU";
    private static Logger logger = LoggerFactory.getLogger(BaiDuWeatherService.class);

    public static String getSend(String str) {
        // 将传进来的城市转码
        try {
            str = URLEncoder.encode(str, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 拼接url
        StringBuffer buf = new StringBuffer();
        buf.append("location=");
        buf.append(str);
        buf.append("&output=");
        buf.append(output);
        buf.append("&ak=");
        buf.append(ak);
        String param = buf.toString();
        // 这是接收百度API返回的内容
        String result = "";
        String urlName = api + param;
        URL realUrl;
        try {
            realUrl = new URL(urlName);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            conn.setDoInput(true);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream(),"UTF-8"));

            String line = br.readLine();
            while (line != null) {
                result += line;
                line = br.readLine();
            }
            br.close();
            Gson gson = new Gson();
            logger.info("--result--"+result);
            Status status = gson.fromJson(result, Status.class);
            List<Results> results=status.getResults();
            StringBuffer bf=new StringBuffer();
            for(int i=0;i<results.size();i++){///null??
                Results res=results.get(i);
                List<Weather> weather_data=res.getWeather_data();
                for(int j=0;j<weather_data.size();j++){
                    Weather weather=weather_data.get(j);
                    String date=weather.getDate();
                    String weath=weather.getWeather();
                    String temp=weather.getTemperature();
                    bf.append(date+" ");
                    bf.append(weath+" ");
                    bf.append(temp+" ");
                    bf.append("\n");
                }
            }
            return bf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
        String json=getSend("广州");
        System.out.println(json);
    }
}