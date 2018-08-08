package com.github.lly835.Utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.github.lly835.controller.AuthController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Tools {
    private static Logger logger = LoggerFactory.getLogger(Tools.class);

    public static String getMenuStr() throws JSONException{
        JSONObject firstLevelMenu = new JSONObject();//一级菜单
        JSONArray firstLevelMenuArray = new JSONArray();//一级菜单列表


        //一级菜单内容1
        JSONObject firstLevelMenuContext1 = new JSONObject();
        firstLevelMenuContext1.put("type", "click");
        firstLevelMenuContext1.put("name", "music");
        firstLevelMenuContext1.put("key", "V1001_TODAY_MUSIC");

        //一级菜单内容2
        JSONObject firstLevelMenuContext2 = new JSONObject();
        //一级菜单内容2的二级菜单列表
        JSONArray firstLevelMenuContext2Array = new JSONArray();
        //一级菜单内容2的二级菜单内容1
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("type", "click");
        jsonObject1.put("name", "music");
        jsonObject1.put("key", "V1001_TODAY_MUSIC");
        //一级菜单内容2的二级菜单内容2
        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("type", "view");
        jsonObject2.put("name", "video");
        jsonObject2.put("url", "http://www.randzh.cn");
        firstLevelMenuContext2Array.add(jsonObject1);
        firstLevelMenuContext2Array.add(jsonObject2);//put
        firstLevelMenuContext2.put("name", "menu");
        firstLevelMenuContext2.put("sub_button", firstLevelMenuContext2Array);

        //一级菜单内容3
        JSONObject firstLevelMenuContext3 = new JSONObject();
        firstLevelMenuContext3.put("type", "click");
        firstLevelMenuContext3.put("name", "video");
        firstLevelMenuContext3.put("key", "V1001_TODAY_MOVIE");


        firstLevelMenuArray.add(firstLevelMenuContext1);//put
        firstLevelMenuArray.add(firstLevelMenuContext2);
        firstLevelMenuArray.add(firstLevelMenuContext3);///


        firstLevelMenu.put("button", firstLevelMenuArray);
        logger.info("menu={}",firstLevelMenu.toString());
        return firstLevelMenu.toString();
    }

    public static String createCustomMenu() throws Exception{
        String custmMenuUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=";

        //获取access_token
//      String accessToken = AccessToken.getAccessToken();//
        String accessToken = getAccessToken();//
        logger.info("token={}",accessToken);
        custmMenuUrl = custmMenuUrl + accessToken;

        URL url = new URL(custmMenuUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(getMenuStr().getBytes("UTF-8"));
        outputStream.flush();
        outputStream.close();

        InputStream inputStream = connection.getInputStream();
        int size =inputStream.available();
        byte[] bs =new byte[size];
        inputStream.read(bs);
        String message=new String(bs,"UTF-8");
        logger.info("msg={}",message);
        return message;
    }

    public static String getAccessToken() throws Exception{
        //String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        String accessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxa6058139d641d146&secret=93c22b6c0be85eae7fa326e77363e947";
        URL url = new URL(accessTokenUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.connect();

        //获取返回的字符
        InputStream inputStream = connection.getInputStream();
        int size =inputStream.available();
        byte[] bs =new byte[size];
        inputStream.read(bs);
        String message=new String(bs,"UTF-8");
        logger.info("message={}",message);
        //获取access_token
        //JSONObject jsonObject = new JSONObject(message);///?
        JSONObject jsonObject = JSONObject.parseObject(message);///?
        logger.info("json_msg_token={}",jsonObject);
        return jsonObject.getString("access_token");
    }


}
