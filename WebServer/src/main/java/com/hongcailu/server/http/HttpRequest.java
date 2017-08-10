package com.hongcailu.server.http;

import java.io.IOException;
import java.io.InputStream;

import com.hongcailu.server.common.HttpContext;

/**
 * 封装HTTP请求内容
 * @author Java
 *
 */
public class HttpRequest {
	//请求方法
	private String method;
	//请求资源
	private String uri;
	//请求协议
	private String protocol;
	/**
	 * 构造方法，用于创建HttpRequest实例
	 * 
	 * @param in 对应客户端的输入流，通过该流读取客户端发送
	 *            过来的请求信息并封装到当期HttpRequest对象中
	 */
	public HttpRequest(InputStream in){
		/*
		 * HTTP协议中的请求信息格式:
		 * 请求行
		 * GET/index.html  HTTP1.1CRLF
		 * 消息报头
		 * 消息正文
		 * 以行为单位发送至服务端 每行结尾以（CR LF）
		 * CR:回车  LF:换行
		 */
		try{
			/*
             *读取一行字符串，请求行信息
             *Method Request-URI Http-Version(CRLF)
             *如:
             *GET /index.html HTTP/1.1CRLF
             */	
			String line=readLine(in);
			//判断读取行内容，防止解析客户端发送空行
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
     * 从给定的输入流中读取一行字符串并将其返回。
     * 当读到
     * CR LF时认为一行结束
     *       13 10
     * @param is
     * @return
     */
    public String readLine(InputStream is)throws IOException{
    	/*
    	 * 顺序从流中读取每个字节并转换为对应的字符然后拼接在一
    	 * 起，直到连续读取了CR LF时停止并将拼接字符串返回
    	 * 
    	 *英文字母为一个字节
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
    		return builder.toString().trim();//转换为字符并去掉空字符串(换行回车都为空)
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
