/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月24日 - Create By peiwang
 */

package com.zpayment.cmn.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;

import com.zpayment.cmn.log.Logger;

/**
 * IO处理工具类
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class IOUtils {

	/** 用于日志记录的Logger */
	private static final Logger log = Logger.getLogger(IOUtils.class);

	/**
	 * 关闭输入流
	 * 
	 * @since
	 * @param in
	 */
	public static void close(InputStream in) {
		if (in == null) {
			return;
		}

		try {
			in.close();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 关闭输出流
	 * 
	 * @since
	 * @param out
	 */
	public static void close(OutputStream out) {
		if (out == null) {
			return;
		}

		try {
			out.close();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 关闭writer
	 * 
	 * @since
	 * @param writer
	 */
	public static void close(Writer writer) {
		if (writer == null) {
			return;
		}

		try {
			writer.close();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 关闭reader
	 * 
	 * @since
	 * @param reader
	 */
	public static void close(Reader reader) {
		if (reader == null) {
			return;
		}

		try {
			reader.close();
		} catch (Exception e) {
			log.error("", e);
		}
	}

	/**
	 * 关闭channel
	 * 
	 * @since
	 * @param fc
	 */
	public static void close(FileChannel fc) {
		if (fc == null) {
			return;
		}

		try {
			fc.close();
		} catch (Exception e) {
			log.error("", e);
		}
	}

}
