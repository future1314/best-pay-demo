package com.github.lly835.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.lly835.Utils.*;
import com.github.lly835.bean.FirstValidation;
import com.github.lly835.service.BaiDuWeatherService;
import com.github.lly835.service.CoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

@Controller
public class AuthController {

    private static Logger logger = LoggerFactory.getLogger(AuthController.class);

    @ResponseBody
    @RequestMapping(value = "/core", method = RequestMethod.GET)
    public String coreByGet(FirstValidation validation, Model model, HttpServletRequest request) {

        logger.info("微信开始验证服务器地址的有效性..........................");

        // 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (checkSignatureIsTrue(validation)) {
            logger.info("加密后的字符串与Signature一致，接入成功......................");
            return validation.getEchostr();
        } else {
            logger.info("加密后的字符串与Signature不一致，接入失败......................");
            //logger.info("访问者IP：" + HttpUtils.getIpAddr(request));
            return "";
        }
    }
//    @RequestMapping(value = "/core", method = RequestMethod.POST)
//    public String coreByPost(Model model) {//异常怎么统一处理？
//        return "";
//    }
//    @RequestMapping(value = "/core", method = RequestMethod.POST)
//    public String coreByPost(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{//异常怎么统一处理？
//        logger.info("-------");
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//
//        // 调用核心业务类接收消息、处理消息
////      String respMessage = CoreService.processRequest(request);
//        String respMessage = CoreService.processRequest(request,0);
//
//        // 响应消息
//        PrintWriter out = response.getWriter();
//        out.print(respMessage);
//        out.close();
//        return "";
//    }
    @RequestMapping(value = "/core", method = RequestMethod.POST)
    public String coreByPost1(Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{//异常怎么统一处理？
        logger.info("-------");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 调用核心业务类接收消息、处理消息
        //String respMessage = CoreService.processRequest(request);//ok 1-5
        //String respMessage = CoreService.processRequest(request,0);

        String respMsg= weather(request,response);

//        // 响应消息
        PrintWriter out = response.getWriter();
        //out.print(respMessage);//ok 1-5
        out.print(respMsg);
        out.close();
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/createMenu")
    public String menu(Model model) throws Exception{//异常怎么统一处理？
        return Tools.createCustomMenu();
    }

    @ResponseBody
    @RequestMapping(value = "/users")
    public String users(Model model) throws Exception{//异常怎么统一处理？
        String NEXT_OPENID="";
        String url="https://api.weixin.qq.com/cgi-bin/user/get?access_token="+Tools.getAccessToken();//+"&next_openid="+NEXT_OPENID;
                 //"https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID"
        InputStream inputStream = HttpUtils.connectHttp(url, "GET", null);
        String m = IoUtils.inputStreamToString(inputStream);
        logger.info("userList={}",m);
        return m;
    }

    @ResponseBody
    @RequestMapping(value = "/users1")
    public String userlist1(Model model) throws Exception{//异常怎么统一处理？
        String NEXT_OPENID="otxDk0zYyIF3J4k6FaYdP03szzZw";
        String url="https://api.weixin.qq.com/cgi-bin/user/get?access_token="+Tools.getAccessToken()+"&next_openid="+NEXT_OPENID;
        //"https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID"
        InputStream inputStream = HttpUtils.connectHttp(url, "GET", null);
        String m = IoUtils.inputStreamToString(inputStream);
        logger.info("userList1={}",m);
        return m;
    }
    @ResponseBody
    @RequestMapping(value = "/users2")
    public String userlist2(Model model) throws Exception{//异常怎么统一处理？
        String NEXT_OPENID="otxDk0zA5WURxxdG85zxjja31BKc";
        String url="https://api.weixin.qq.com/cgi-bin/user/get?access_token="+Tools.getAccessToken()+"&next_openid="+NEXT_OPENID;
        //"https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID"
        InputStream inputStream = HttpUtils.connectHttp(url, "GET", null);
        String m = IoUtils.inputStreamToString(inputStream);
        logger.info("userList2={}",m);
        return m;
    }
    @ResponseBody
    @RequestMapping(value = "/users3")
    public String userlist3(Model model) throws Exception{//异常怎么统一处理？
        String NEXT_OPENID="otxDk002XuvoHNEsEHgDIbhbYkSs";
        String url="https://api.weixin.qq.com/cgi-bin/user/get?access_token="+Tools.getAccessToken()+"&next_openid="+NEXT_OPENID;
        //"https://api.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&next_openid=NEXT_OPENID"
        InputStream inputStream = HttpUtils.connectHttp(url, "GET", null);
        String m = IoUtils.inputStreamToString(inputStream);
        logger.info("userList3={}",m);
        return m;
    }


    /**
     * Mar 14, 2016 6:27:19 PM
     *
     * @param validation
     * @return 加密后的字符串
     * @Description: 加密/校验流程如下：
     */
    private static boolean checkSignatureIsTrue(FirstValidation validation) {
        boolean flag = false;
        try {
            // 1. 将token、timestamp、nonce三个参数进行字典序排序
            String[] strs = new String[] { validation.getToken(), validation.getNonce(), validation.getTimestamp() };
            Arrays.sort(strs);

            // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
            StringBuilder appendStr = new StringBuilder();
            for (int i = 0; i < strs.length; i++) {
                appendStr.append(strs[i]);
            }

            StringBuffer signature = new StringBuffer();
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = digest.digest(appendStr.toString().getBytes());

            // 字节数组转换为十六进制数
            for (int i = 0; i < bytes.length; i++) {
                String shaHex = Integer.toHexString(bytes[i] & 0xFF);
                if (shaHex.length() < 2) {
                    signature.append(0);
                }
                signature.append(shaHex);
            }

            if (validation.getSignature().equals(signature.toString())) {
                flag = true;
            }

        } catch (Exception e) {
            logger.error("验证服务器地址的有效性时加密出现错误");

        }
        return flag;
    }


    public String weather(HttpServletRequest req, HttpServletResponse resp) throws  Exception{
//      req.setCharacterEncoding("UTF-8");
//      resp.setCharacterEncoding("UTF-8");
        //PrintWriter out = resp.getWriter();
        String message = null;
        try {
            Map<String,String> map = MessageUtils.xmlToMap(req);
            String toUserName = map.get("ToUserName");
            String fromUserName = map.get("FromUserName");
            String msgType = map.get("MsgType");
            String content = map.get("Content");
            //MessageUtil.MESSAGE_TEXT就是 text
            if(MessageUtils.MESSAGE_TEXT.equals(msgType)){
                if(content.endsWith("天气")){
                    String keyWord = content.replaceAll("天气", "").trim();
                    //message = MessageUtils.initText(toUserName, fromUserName,BaiDuWeatherService.getSend(keyWord));
                    message = MessageUtils.initText(toUserName, fromUserName,WeatherUtil.getTodayWeatherChina(keyWord));
                    //message =WeatherUtil.getTodayWeatherChina(keyWord);
                }
            }
            //out.print(message);
        }catch (Exception e) {
            // TODO: handle exception,
            e.printStackTrace();
            //out.close();
        }
        return message;
    }
}
