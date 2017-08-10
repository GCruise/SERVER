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
 * 处理客户端请求
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
			System.out.println("一个客户端连接了");

			//获取输入流,，读取客户端发送过来的数据
			InputStream in=socket.getInputStream();
			//获取输出流，发送客户端响应信息
			OutputStream out=socket.getOutputStream();
			//生成HttpRequest表示客户端请求
			HttpRequest request=new HttpRequest(in);
			//生成HttpResponse表示客户端响应信息
			HttpResponse response=new HttpResponse(out);
			//防止客户端发送空字符串
			if(request.getUri()!=null){

				/*
				 * 响应客户端对应的资源
				 * HTTP中响应的格式:
				 * 1:状态行
				 * 2:响应头
				 * 3:响应正文
				 * 
				 * 状态行格式:
				 * HTTP-Version Status_code Reason_Phrase CRLF
				 * HTTP协议版本   状态代码   状态描述CRLF
				 * 
				 * 例如:
				 * HTTP/1.1 200 OK CRLF
				 * 

				 * 
				 * Content-Length:用来指明发送给接受者实体正文的长度
				 *                简单说就是发送过去的数据的字节量
				 *                
				 * HTTP协议要求实际响应客户端时的数据格式:
				 * HTTP/1.1 200 OK CRLF           状态行
				 * Content-Type:text/html CRLF    响应头信息
				 * Content-Length:100CRLF         响应头信息
				 * CRLF     单独发送CRLF指明响应头全部发送完毕
				 * DATA     实际数据
				 */

				//加载客户端需要访问的文件资源
				File file=new File(ServerContext.web_root+request.getUri());
				//设置状态行
				if(file.exists()){
					response.setStatus(HttpContext.STATUS_CODE_OK);
					//设置响应头
					response.setContentType(getContentTypeByFile(file));
					response.setContentLength((int)file.length());
					responseFile(file,response);
					System.out.println("响应客户端完毕");
				}else{
					//响应客户端没有该资源(显示个错误页面)
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
	 * 根据给定的文件获取对应的Content-Type
	 * @param file
	 * @return
	 */
	private String getContentTypeByFile(File file){
		/*
		 * 1:首先获取该文件名字
		 * 2:获取该文件名的文件类型(文件名的后缀)
		 * 3:根据类型名从ServerContext的types中作为key
		 *   获取对应的Content-Type
		 */
		String name=file.getName();
		String ext=name.substring(name.indexOf('.'),name.length());
		return ServerContext.types.get(ext);
	}
	
	/**
	 * 响应文件
	 * @throws IOException 
	 */
	private void responseFile(File file,HttpResponse res) throws IOException {
		BufferedInputStream bis=null;
		/*
		 * 将该文件中每个字节通过客户端输出流发送给客户端
		 */
			try{
				bis=new BufferedInputStream(
					   new FileInputStream(file)
					);
			/*
			 * 通过Response取出输出流，这里就自动发送了状态行和响应头
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
