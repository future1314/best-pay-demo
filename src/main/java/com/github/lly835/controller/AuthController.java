package com.github.lly835.controller;

import com.github.lly835.Utils.Tools;
import com.github.lly835.bean.FirstValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.Arrays;

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
    @RequestMapping(value = "/core", method = RequestMethod.POST)
    public String coreByPost(Model model) {//异常怎么统一处理？
        return "";
    }

    @ResponseBody
    @RequestMapping(value = "/createMenu")
    public String menu(Model model) throws Exception{//异常怎么统一处理？
        return Tools.createCustomMenu();
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


}
