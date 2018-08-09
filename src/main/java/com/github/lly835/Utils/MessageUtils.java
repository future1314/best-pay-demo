package com.github.lly835.Utils;

import com.github.lly835.bean.resp.TextMessage;
import com.thoughtworks.xstream.XStream;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class MessageUtils {


        public static final String MESSAGE_TEXT = "text";
        public static final String MESSAGE_IMAGE = "image";
        public static final String MESSAGE_VOICE = "voice";
        public static final String MESSAGE_VIDEO = "video";
        public static final String MESSAGE_LINK = "link";
        public static final String MESSAGE_LOCATION = "location";
        public static final String MESSAGE_EVENT = "event";

        public static final String EVENT_SUB = "subscribe";
        public static final String EVENT_UNSUB = "unsubscribe";
        public static final String EVENT_CLICK = "CLICK";
        public static final String EVENT_VIEW = "VIEW";

        /**
         * xml转为map
         * @param request
         * @return
         * @throws DocumentException
         * @throws IOException
         */
        public static Map<String, String> xmlToMap(HttpServletRequest request ) throws DocumentException, IOException
        {
            Map<String,String> map = new HashMap<String, String>();

            SAXReader reader = new SAXReader();

            InputStream ins = request.getInputStream();
            Document doc = reader.read(ins);

            Element root = doc.getRootElement();
            List<Element> list = root.elements();
            for (Element e : list) {
                map.put(e.getName(), e.getText());
            }
            ins.close();
            return map;
        }

        public static String textMessageToXml(TextMessage textMessage){
            XStream xstream = new XStream();
            xstream.alias("xml", textMessage.getClass());
            return xstream.toXML(textMessage);

        }
        public static String initText(String toUserName, String fromUserName, String content){
            TextMessage text = new TextMessage();///
            text.setFromUserName(toUserName);
            text.setToUserName(fromUserName);
            text.setMsgType(MESSAGE_TEXT);
            text.setCreateTime(new Date().getTime());
            text.setContent(content);
            return textMessageToXml(text);
        }

        public static String menuText(){
            StringBuffer sb = new StringBuffer();
            sb.append("      你关注，\n");
            sb.append("     或者不关注，\n");
            sb.append("      【你我杂志刊】都在这里!\n");
            sb.append("     不离，\n");
            sb.append("      不弃！\n\n");
            sb.append("该公众号已实现以下功能：\n");
            sb.append("回复“天气”、“翻译” 将有该功能的介绍与使用，\n");
            sb.append("如您在使用该公众有任何宝贵意见，欢迎反馈！\n\n");
            sb.append("反馈邮箱：zhenqicai@sohu.com");
            return sb.toString();
        }
}
