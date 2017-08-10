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
 * ��¼����������Ϣ
 * @author Java
 *
 */
public class ServerContext {
	/*
	 * ����˿�
	 */
	public static int ServerPort;
	/*
	 * �������󲢷���
	 */
	public static int max_thread;
	/*
	 * ������Ӧ�ø�Ŀ¼
	 */
	public static String web_root;
	/*
	 * ������ʹ�õ�HttpЭ��汾
	 */
	public static String protocol;
	/*
	 * ����ý���ļ���׺��ӦContent-Type������
	 */
	public static Map<String,String> types=new HashMap<String,String>();
	/*
	 * 404ҳ��
	 */
	public static String notFoundPage;
	//��̬��Ա�ھ�̬�������ʼ��
	static{
		//��ʼ��ServerContext�ľ�̬��Ա
		init();
	}
	/**
	 * ����server.xml�ļ���ServerContext���г�ʼ��
	 */
	private static void init(){
		SAXReader reader=new SAXReader();
		try {
			Document doc=reader.read(new FileInputStream("config"+File.separator+"server.xml"));
			Element root=doc.getRootElement();
			/*
			 * ����<server>�������Ϣ
			 */
			Element serviceEle=root.element("service");
			//����connector
			Element connEle=serviceEle.element("connector");
			protocol=connEle.attributeValue("protocol");
			ServerPort=Integer.parseInt(connEle.attributeValue("port"));
			max_thread=Integer.parseInt(connEle.attributeValue("maxThread"));
			//����not-found-page
			notFoundPage=serviceEle.elementText("not-found-page");
			//����webroot
			web_root=serviceEle.elementText("webroot");
			
			//����<type-mappings>
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
