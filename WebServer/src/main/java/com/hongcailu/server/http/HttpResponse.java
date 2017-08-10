package com.hongcailu.server.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.hongcailu.server.common.HttpContext;
import com.hongcailu.server.common.ServerContext;

/**
 * ��װHttp��Ӧ
 * HTTP��Ӧ��ʽ
 * 1:״̬��
 * 2:��Ӧͷ
 * 3:��Ӧ����
 * 
 * ״̬��
 * ״̬���������������:Э��汾��������ʽ��״̬���룬״̬����
 * Http_Version Status-code Readon -Phrase CRLF
 * ����:HTTP/1.1 200 OK CRLF
 * 
 * ״̬�����һ������������
 * 1xx:ָʾ����Ϣ����ʾ�����ѽ��գ���������
 * 2xx:�ɹ�����ʾ�����ѽ��գ���⣬����
 * 3xx:�ض���Ҫ���������Ҫ����һ���Ĳ���
 * 4xx:�ͻ��˴���Ҫ���﷨����������޷�ʵ��
 * 5xx:����˴��󣬷����δ��ʵ�ָ�����
 * 
 * ������״̬������:
 * 200 OK �ͻ�������ɹ�
 * 400 Bad Request        �ͻ����������﷨���󣬷���˲������
 * 401 Unauthonzed        ����δ��Ȩ 
 * 403 Forbidden          ����˽��յ����󣬵��Ǿܾ��ṩ����
 * 404 Not Found          ������Դ������
 * 500 Internal Server Error   �����������˲���Ԥ��Ĵ���
 * 503Service Unavailable      ��������ǰ���ܴ���ͻ�����          
 * 
 * 
 * 2:��Ӧͷ
 * ��Ӧͷע���ܶ෵�ص���Ϣ�������������:
 * Content-Type:����ָ�����͸������ߵ�ý������
 * ������Content-Type:
 * text/html: HTML��ʽ�ļ�(��ҳ)
 * text/xml:  XML��ʽ�ļ�
 * image/gif: gifͼƬ
 * image/jpeg:jpeg��ʽͼƬ
 * image/png: png��ʽͼƬ
 * @author Java
 *
 */
public class HttpResponse {
	private OutputStream out;
	private int status;//״̬���е�״̬����
	private String contentType;//2:��Ӧͷ�е���������
	private long contentLength;//��Ӧͷ�е����ĳ���
	
	//��ʾ��Ӧ״̬���Լ���Ӧͷ��Ϣ�Ƿ��Ѿ����͹�
	private boolean hasPrintHeader;
	/*
	 * ��¼���п���״̬�뼰����
	 */
	private Map<Integer,String> statusMap; 
	/**
	 * ���췽��
	 * @param out  ��Ӧ�ͻ��˵��������ͨ����������Ϣ��Ӧ���ͻ���
	 */
	public HttpResponse(OutputStream out){
		this.out=out;
		statusMap=new HashMap<Integer, String>();
		statusMap.put(HttpContext.STATUS_CODE_OK, HttpContext.STATUS_VALUE_OK);
		statusMap.put(HttpContext.STATUS_CODE_ERROR, HttpContext.STATUS_VALUE_ERROR);
		statusMap.put(HttpContext.STATUS_CODE_NOT_FOUND, HttpContext.STATUS_VALUE_NOT_FOUND);
	}
	public OutputStream getOutputStream() throws IOException {
		if(!hasPrintHeader){
			/*
			 * ��ȡ������ݣ���״̬�У���Ӧͷ��Ϣ�Զ�����
			 */
			//����״̬��
			println(ServerContext.protocol+" "+status+" "+statusMap.get(status));
			
			//������Ӧͷ
			println("ContentType:" +contentType);
			println("ContentLength:" +contentLength);
			println("");//�������Ϳ��У���ʾ��Ӧͷ�������
			hasPrintHeader=true;
		}
		
		return out;
	}
	public void setOut(OutputStream out) {
		this.out = out;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public long getContentLength() {
		return contentLength;
	}
	public void setContentLength(long contentLength) {
		this.contentLength = contentLength;
	}
	public Map<Integer, String> getStatusMap() {
		return statusMap;
	}
	
	/**
	 * ��������һ���ַ�����HTTPЭ���ʽҪ����д��
	 * @param str
	 * @param out
	 * @throws IOException 
	 */
	private void println(String str) throws IOException{
		try {
			out.write(str.getBytes("ISO8859-1"));
			out.write(HttpContext.CR);
			out.write(HttpContext.LF);
		} catch (UnsupportedEncodingException e) {
			System.out.println("����֧�ֵı���");
			throw e;
		} catch (IOException e) {
			throw e;
		}
	}

}
