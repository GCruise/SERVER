package com.hongcailu.server.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hongcailu.server.common.HttpContext;
import com.hongcailu.server.common.ServerContext;

/**
 * ����HTTPЭ���Web����˳���
 * @author Java
 *
 */
public class WebServer {
    private ServerSocket server;
    /*
     * �̳߳ء��������ڴ���ͻ���������߳�
     */
    private ExecutorService threadPool;
    /**
     * ���췽�������ڳ�ʼ������˳���
     */
    public WebServer()throws Exception{
    	try {
			server=new ServerSocket(ServerContext.ServerPort);
			threadPool=Executors.newFixedThreadPool(ServerContext.max_thread);
		} catch (Exception e) {
			throw e;
		}
    }
    public void start(){
    	try{
    		while(true){
    			System.out.println("�ȴ��ͻ�������");
    			Socket socket=server.accept();
    			/*
    			 * ������ÿͻ�����������񽻸��̳߳�
    			 */
    			threadPool.execute(new ClientHandler(socket));
    			
    		}
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    public static void main(String[] args){
    	try {
			WebServer ws=new WebServer();
			ws.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("����������ʧ��");
		}
    }
}
