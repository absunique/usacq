/*
 * 
 * Copyright 2013, $${COMPANY} Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF $${COMPANY} CO., LTD.
 * 
 * $Id: XmlUtils.java,v 1.1 2016/08/30 07:28:20 peiwang Exp $
 * 
 * Function:
 * 
 * //TODO 请添加功能描述
 * 
 * Edit History:
 * 
 * 2013-3-12 - Create By szwang
 */

package com.peiwang.usacq.cmn.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import java.util.Set;

import com.peiwang.usacq.cmn.exp.BaseErrorCode;
import com.peiwang.usacq.cmn.exp.BaseException;
import com.peiwang.usacq.cmn.log.Logger;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * XML解析工具类，提供Java对象到XML（JSON）格式的IO流/文件/字符串的转换
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class XmlUtils {

	/** 用于日志记录的log */
	private static final Logger log = Logger.getLogger(XmlUtils.class);

	/**
	 * 从XML格式文件序列化对象
	 * 
	 * @param clazz
	 * @param annotations
	 * @param classpath
	 *            classpath或文件系统路径，优先从classpath中获取XML文件
	 * @return
	 * @throws
	 */
	public static <T> T fromXml(Class<T> clazz, Class<?>[] annotations,
			String classpath) throws BaseException {
		InputStream in = null;
		try {
			in = getInputStream(classpath);
			return fromXml(clazz, annotations, in);
		} finally {
			IOUtils.close(in);
		}
	}

	/**
	 * 从XML格式的字符串中序列化对象
	 * 
	 * @param clazz
	 * @param annotations
	 * @param xml
	 * @return
	 * @throws
	 */
	public static <T> T fromXmlStr(Class<T> clazz, Class<?>[] annotations,
			String xml) throws BaseException {
		try {
			XStream xstream = getXStream(clazz, annotations, false);
			return clazz.cast(xstream.fromXML(xml));
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMN_XML_PARSE_FAILED, e);
		}
	}

	/**
	 * 从XML格式流序列化对象
	 * 
	 * @param clazz
	 * @param annotations
	 * @param in
	 * @return
	 * @throws
	 */
	public static <T> T fromXml(Class<T> clazz, Class<?>[] annotations,
			InputStream in) throws BaseException {
		try {
			XStream xstream = getXStream(clazz, annotations, false);
			return clazz.cast(xstream.fromXML(in));
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMN_XML_PARSE_FAILED, e);
		}
	}

	/**
	 * 将对象序列化为XML格式的文件
	 * 
	 * @param annotations
	 * @param obj
	 * @return
	 */
	public static void toXml(Object object, Class<?>[] annotations,
			String filePath) throws BaseException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			toXml(object, annotations, out);
		} catch (FileNotFoundException e) {
			throw new BaseException(BaseErrorCode.COMN_XML_PARSE_FAILED, e);
		} finally {
			IOUtils.close(out);
		}
	}

	/**
	 * 将对象序列化为XML格式，通过输出流输出
	 * 
	 * @param annotations
	 * @param obj
	 * @return
	 */
	public static void toXml(Object object, Class<?>[] annotations,
			OutputStream out) throws BaseException {
		try {
			XStream xstream = getXStream(object.getClass(), annotations, false);
			xstream.toXML(object, new OutputStreamWriter(out, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new BaseException(BaseErrorCode.COMN_FILE_ENCODE_ERROR, e);
		}
	}

	/**
	 * 将对象序列化为XML格式
	 * 
	 * @param annotations
	 * @param obj
	 * @return
	 * @throws
	 */
	public static String toXmlStr(Class<?>[] annotations, Object obj)
			throws BaseException {
		try {
			XStream xstream = getXStream(obj.getClass(), annotations, false);
			return xstream.toXML(obj);
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMN_XML_PARSE_FAILED, e);
		}
	}

	/**
	 * 从JSON格式的文件序列化对象
	 * 
	 * @param clazz
	 * @param annotations
	 * @param classpath
	 * @return
	 */
	public static <T> T fromJson(Class<T> clazz, Class<?>[] annotations,
			String classpath) throws BaseException {
		InputStream in = null;
		try {
			in = getInputStream(classpath);
			return fromJson(clazz, annotations, in);
		} finally {
			IOUtils.close(in);
		}
	}

	/**
	 * 从JSON格式的字符串中序列化对象
	 * 
	 * @param clazz
	 * @param annotations
	 * @param json
	 * @return
	 * @throws
	 */
	public static <T> T fromJsonStr(Class<T> clazz, Class<?>[] annotations,
			String xml) throws BaseException {
		try {
			XStream xstream = getXStream(clazz, annotations, true);
			return clazz.cast(xstream.fromXML(xml));
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMN_XML_PARSE_FAILED, e);
		}
	}

	/**
	 * 从JSON格式流序列化对象
	 * 
	 * @param clazz
	 * @param annotations
	 * @param in
	 * @return
	 * @throws
	 */
	public static <T> T fromJson(Class<T> clazz, Class<?>[] annotations,
			InputStream in) throws BaseException {
		try {
			XStream xstream = getXStream(clazz, annotations, true);
			return clazz.cast(xstream.fromXML(in));
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMN_XML_PARSE_FAILED, e);
		}
	}

	/**
	 * 将对象序列化为JSON格式的文件
	 * 
	 * @param annotations
	 * @param obj
	 * @return
	 */
	public static void toJson(Object object, Class<?>[] annotations,
			String filePath) throws BaseException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			toJson(object, annotations, out);
		} catch (FileNotFoundException e) {
			throw new BaseException(BaseErrorCode.COMN_XML_PARSE_FAILED, e);
		} finally {
			IOUtils.close(out);
		}
	}

	/**
	 * 将对象序列化为JSON格式，并输出到IO流
	 * 
	 * @param annotations
	 * @param obj
	 * @return
	 */
	public static void toJson(Object object, Class<?>[] annotations,
			OutputStream out) throws BaseException {
		try {
			XStream xstream = getXStream(object.getClass(), annotations, true);
			xstream.toXML(object, new OutputStreamWriter(out, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new BaseException(BaseErrorCode.COMN_FILE_ENCODE_ERROR, e);
		}
	}

	/**
	 * 将对象序列化为JSON格式
	 * 
	 * @param annotations
	 * @param obj
	 * @return
	 * @throws
	 */
	public static String toJsonStr(Class<?>[] annotations, Object obj)
			throws BaseException {
		try {
			XStream xstream = getXStream(obj.getClass(), annotations, true);
			return xstream.toXML(obj);
		} catch (Exception e) {
			throw new BaseException(BaseErrorCode.COMN_XML_PARSE_FAILED, e);
		}
	}

	/**
	 * 从classpath或文件系统获取输入流
	 * 
	 * @since
	 * @param classpath
	 * @return
	 * @throws BaseException
	 */
	private static InputStream getInputStream(String classpath)
			throws BaseException {
		InputStream in = ClassUtils.getDefaultClassLoader()
				.getResourceAsStream(classpath);

		if (in == null) {
			try {
				in = new FileInputStream(classpath);
			} catch (FileNotFoundException e) {
				log.error("xml not exist, filePath: " + classpath, e);
				throw new BaseException(BaseErrorCode.COMN_FILE_NOT_EXIST);
			}
		}
		return in;
	}

	/**
	 * 获取不同转换格式的XStream
	 * 
	 * @param annotations
	 *            注解类
	 * @param json
	 *            JSON格式，<code>true</code>表示JSON格式，<code>false</code>表示XML格式
	 * @return
	 */
	private static XStream getXStream(Class<?> clazz, Class<?>[] annotations,
			boolean json) {
		XStream xstream = null;
		if (json) {
			xstream = new XStream(new JettisonMappedXmlDriver());
		} else {
			xstream = new XStream();
		}

		Set<Class<?>> set = new LinkedHashSet<Class<?>>();
		set.add(clazz);
		if (annotations != null) {
			for (Class<?> cls : annotations) {
				set.add(cls);
			}
		}

		xstream.processAnnotations(set.toArray(new Class<?>[set.size()]));

		return xstream;
	}
}
