package com.github.lly835.Utils;

import org.dom4j.Document;
import com.thoughtworks.xstream.XStream;

@SuppressWarnings({"rawtypes","unchecked"})
public class XmlUtils {

    public static <T> T xml2Bean(Class clazz, Document document) {
        XStream xStream = new XStream();
        xStream.alias("xml", clazz);
        xStream.ignoreUnknownElements();//忽略未知的属性
        return (T) xStream.fromXML(document.asXML());
    }

    public static <T> String bean2Xml(T t) {
        XStream xStream = new XStream();
        xStream.alias("xml", t.getClass());
        return xStream.toXML(t);
    }
}
