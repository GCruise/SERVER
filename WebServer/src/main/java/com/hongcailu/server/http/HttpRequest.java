package com.hongcailu.server.http;

import java.io.IOException;
import java.io.InputStream;

import com.hongcailu.server.common.HttpContext;

/**
 * ��װHTTP��������
 * @author Java
 *
 */
public class HttpRequest {
	//���󷽷�
	private String method;
	//������Դ
	private String uri;
	//����Э��
	private String protocol;
	/**
	 * ���췽�������ڴ���HttpRequestʵ��
	 * 
	 * @param in ��Ӧ�ͻ��˵���������ͨ��������ȡ�ͻ��˷���
	 *            ������������Ϣ����װ������HttpRequest������
	 */
	public HttpRequest(InputStream in){
		/*
		 * HTTPЭ���е�������Ϣ��ʽ:
		 * ������
		 * GET/index.html  HTTP1.1CRLF
		 * ��Ϣ��ͷ
		 * ��Ϣ����
		 * ����Ϊ��λ����������� ÿ�н�β�ԣ�CR LF��
		 * CR:�س�  LF:����
		 */
		try{
			/*
             *��ȡһ���ַ�������������Ϣ
             *Method Request-URI Http-Version(CRLF)
             *��:
             *GET /index.html HTTP/1.1CRLF
             */	
			String line=readLine(in);
			//�ж϶�ȡ�����ݣ���ֹ�����ͻ��˷��Ϳ���
			if(line.length()>0){
				String[] lines=line.split("\\s");
				this.method=lines[0];
				this.uri=lines[1];
				this.protocol=lines[2];
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
     * �Ӹ������������ж�ȡһ���ַ��������䷵�ء�
     * ������
     * CR LFʱ��Ϊһ�н���
     *       13 10
     * @param is
     * @return
     */
    public String readLine(InputStream is)throws IOException{
    	/*
    	 * ˳������ж�ȡÿ���ֽڲ�ת��Ϊ��Ӧ���ַ�Ȼ��ƴ����һ
    	 * ��ֱ��������ȡ��CR LFʱֹͣ����ƴ���ַ�������
    	 * 
    	 *Ӣ����ĸΪһ���ֽ�
    	 */
    	StringBuilder builder =new StringBuilder();
    	try{
    		int ch1=-1,ch2=-1;
    		while((ch2=is.read())!=-1){
    			if(ch1==HttpContext.CR&&ch2==HttpContext.LF){
    				break;
    			}
    			builder.append((char)ch2);
    			ch1=ch2;
    		}
    		return builder.toString().trim();//ת��Ϊ�ַ���ȥ�����ַ���(���лس���Ϊ��)
    	}catch(IOException e){
    		throw e;
    	}
    }

	public String getMethod() {
		return method;
	}

	public String getUri() {
		return uri;
	}

	public String getProtocol() {
		return protocol;
	}
    
    

}
