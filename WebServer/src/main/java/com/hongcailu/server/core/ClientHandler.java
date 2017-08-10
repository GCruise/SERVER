package com.hongcailu.server.core;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import com.hongcailu.server.common.HttpContext;
import com.hongcailu.server.common.ServerContext;
import com.hongcailu.server.http.HttpRequest;
import com.hongcailu.server.http.HttpResponse;

/**
 * ����ͻ�������
 * @author Java
 *
 */
public class ClientHandler implements Runnable{
	public Socket socket;
	public ClientHandler(Socket socket){
		this.socket=socket;
	}
	public void run(){
		try{
			System.out.println("һ���ͻ���������");

			//��ȡ������,����ȡ�ͻ��˷��͹���������
			InputStream in=socket.getInputStream();
			//��ȡ����������Ϳͻ�����Ӧ��Ϣ
			OutputStream out=socket.getOutputStream();
			//����HttpRequest��ʾ�ͻ�������
			HttpRequest request=new HttpRequest(in);
			//����HttpResponse��ʾ�ͻ�����Ӧ��Ϣ
			HttpResponse response=new HttpResponse(out);
			//��ֹ�ͻ��˷��Ϳ��ַ���
			if(request.getUri()!=null){

				/*
				 * ��Ӧ�ͻ��˶�Ӧ����Դ
				 * HTTP����Ӧ�ĸ�ʽ:
				 * 1:״̬��
				 * 2:��Ӧͷ
				 * 3:��Ӧ����
				 * 
				 * ״̬�и�ʽ:
				 * HTTP-Version Status_code Reason_Phrase CRLF
				 * HTTPЭ��汾   ״̬����   ״̬����CRLF
				 * 
				 * ����:
				 * HTTP/1.1 200 OK CRLF
				 * 

				 * 
				 * Content-Length:����ָ�����͸�������ʵ�����ĵĳ���
				 *                ��˵���Ƿ��͹�ȥ�����ݵ��ֽ���
				 *                
				 * HTTPЭ��Ҫ��ʵ����Ӧ�ͻ���ʱ�����ݸ�ʽ:
				 * HTTP/1.1 200 OK CRLF           ״̬��
				 * Content-Type:text/html CRLF    ��Ӧͷ��Ϣ
				 * Content-Length:100CRLF         ��Ӧͷ��Ϣ
				 * CRLF     ��������CRLFָ����Ӧͷȫ���������
				 * DATA     ʵ������
				 */

				//���ؿͻ�����Ҫ���ʵ��ļ���Դ
				File file=new File(ServerContext.web_root+request.getUri());
				//����״̬��
				if(file.exists()){
					response.setStatus(HttpContext.STATUS_CODE_OK);
					//������Ӧͷ
					response.setContentType(getContentTypeByFile(file));
					response.setContentLength((int)file.length());
					responseFile(file,response);
					System.out.println("��Ӧ�ͻ������");
				}else{
					//��Ӧ�ͻ���û�и���Դ(��ʾ������ҳ��)
					response.setStatus(HttpContext.STATUS_CODE_NOT_FOUND);
					file=new File(ServerContext.web_root+File.separator+ServerContext.notFoundPage);
					response.setContentType(getContentTypeByFile(file));
					response.setContentLength((int)file.length());
					responseFile(file,response);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				socket.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * ���ݸ������ļ���ȡ��Ӧ��Content-Type
	 * @param file
	 * @return
	 */
	private String getContentTypeByFile(File file){
		/*
		 * 1:���Ȼ�ȡ���ļ�����
		 * 2:��ȡ���ļ������ļ�����(�ļ����ĺ�׺)
		 * 3:������������ServerContext��types����Ϊkey
		 *   ��ȡ��Ӧ��Content-Type
		 */
		String name=file.getName();
		String ext=name.substring(name.indexOf('.'),name.length());
		return ServerContext.types.get(ext);
	}
	
	/**
	 * ��Ӧ�ļ�
	 * @throws IOException 
	 */
	private void responseFile(File file,HttpResponse res) throws IOException {
		BufferedInputStream bis=null;
		/*
		 * �����ļ���ÿ���ֽ�ͨ���ͻ�����������͸��ͻ���
		 */
			try{
				bis=new BufferedInputStream(
					   new FileInputStream(file)
					);
			/*
			 * ͨ��Responseȡ���������������Զ�������״̬�к���Ӧͷ
			 */
			OutputStream out=res.getOutputStream();
			int d=-1;
			while((d=bis.read())!=-1){
				out.write(d);
			}
			}catch(IOException e){
				throw e;
			}finally{
				if(bis!=null){
					bis.close();
				}
				
			}
			
		}
	
	
	
}
