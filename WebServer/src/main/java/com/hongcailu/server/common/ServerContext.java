package com.hongcailu.server.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 记录服务端相关信息
 * @author Java
 *
 */
public class ServerContext {
	/*
	 * 服务端口
	 */
	public static int ServerPort;
	/*
	 * 服务端最大并发数
	 */
	public static int max_thread;
	/*
	 * 服务器应用根目录
	 */
	public static String web_root;
	/*
	 * 服务器使用的Http协议版本
	 */
	public static String protocol;
	/*
	 * 根据媒体文件后缀对应Content-Type的类型
	 */
	public static Map<String,String> types=new HashMap<String,String>();
	/*
	 * 404页面
	 */
	public static String notFoundPage;
	//静态成员在静态块里面初始化
	static{
		//初始化ServerContext的静态成员
		init();
	}
	/**
	 * 加载server.xml文件对ServerContext进行初始化
	 */
	private static void init(){
		SAXReader reader=new SAXReader();
		try {
			Document doc=reader.read(new FileInputStream("config"+File.separator+"server.xml"));
			Element root=doc.getRootElement();
			/*
			 * 解析<server>下相关信息
			 */
			Element serviceEle=root.element("service");
			//解析connector
			Element connEle=serviceEle.element("connector");
			protocol=connEle.attributeValue("protocol");
			ServerPort=Integer.parseInt(connEle.attributeValue("port"));
			max_thread=Integer.parseInt(connEle.attributeValue("maxThread"));
			//解析not-found-page
			notFoundPage=serviceEle.elementText("not-found-page");
			//解析webroot
			web_root=serviceEle.elementText("webroot");
			
			//解析<type-mappings>
			Element typemappings=root.element("type-mappings");
			List<Element> typemapping=typemappings.elements();
			for(Element ele:typemapping){
				types.put(ele.attributeValue("ext"),ele.attributeValue("type"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

}
