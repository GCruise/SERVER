package com.hongcailu.server.common;
/**
 * ����HTTPЭ���е������Ϣ
 * @author Java
 *
 */
public class HttpContext {
	/**
	 * �س�
	 */
	public static final int CR=13;
	/**
	 * ����
	 */
	public static final int LF=10;
	
	/**
	 * ״̬��:�ɹ�
	 */
	public static final int STATUS_CODE_OK=200;
	
	/**
	 * ״̬����:�ɹ�
	 */
	public static final String STATUS_VALUE_OK="OK";
	
	/**
	 * ״̬��:404
	 */
	public static final int STATUS_CODE_NOT_FOUND=404;
	
	/**
	 * ״̬����:δ�ҵ�
	 */
	public static final String STATUS_VALUE_NOT_FOUND="Not Found";
	
	/**
	 * ״̬��:500
	 */
	public static final int STATUS_CODE_ERROR=500;
	
	/**
	 * ״̬����:δ�ҵ�
	 */
	public static final String STATUS_VALUE_ERROR="Service Error";


}
