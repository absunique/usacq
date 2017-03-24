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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;

/**
 * 文件工具类
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class FileUtils {

	private static Logger log = Logger.getLogger(FileUtils.class);

	/** 文件路径分隔符 */
	public static final String SEPERATOR = "/";

	/** 文件拷贝单次处理最大字节数 1MB */
	private static final int COPY_MAX_COUNT = 1 * 1024 * 1024;

	/** 拷贝或移动中的临时文件后缀 */
	private static final String TEMP_SUFFIX = ".tmp";

	/**
	 * 判断是否为临时文件
	 * 
	 * @since
	 * @param fileName
	 * @return
	 */
	public static boolean isTempFile(String fileName) {
		return fileName != null && fileName.endsWith(TEMP_SUFFIX);
	}

	/**
	 * 获取目录名
	 * 
	 * @since
	 * @param rootPath
	 * @param subPaths
	 * @return
	 */
	public static String getDirPath(String filePath) {
		if (filePath == null) {
			return null;
		}
		int lastSep = filePath.lastIndexOf(SEPERATOR);
		if (lastSep == -1) {
			return "";
		}

		return filePath.substring(0, lastSep);
	}

	/**
	 * 构建绝对路径
	 * 
	 * @since
	 * @param rootPath
	 * @param subPaths
	 * @return
	 */
	public static String getAbsolutePath(String rootPath, String... subPaths) {
		if (rootPath == null) {
			return null;
		}

		String path = rootPath;
		while (path.endsWith(SEPERATOR)) {
			path = rootPath
					.substring(0, rootPath.length() - SEPERATOR.length());
		}

		if (subPaths == null || subPaths.length == 0) {
			return path;
		}

		StringBuilder sb = new StringBuilder(path);
		for (String subPath : subPaths) {
			if (subPath != null) {
				sb.append(SEPERATOR).append(subPath);
			}
		}

		return sb.toString();
	}

	/**
	 * 获取相对路径
	 * 
	 * @since
	 * @param rootPath
	 * @param absolutePath
	 * @return
	 */
	public static String getRelativePath(String rootPath, String absolutePath)
			throws BaseException {
		log.debug("getRelativePath rootPath: " + rootPath + ", absolutePath: "
				+ absolutePath);

		if (rootPath == null || absolutePath == null) {
			log.error("getRelativePath failed, rootPath: " + rootPath
					+ ", absolutePath: " + absolutePath);
			throw new BaseException(BaseErrorCode.COMN_FILE_PATH_INVALID);
		}

		if (!rootPath.endsWith(SEPERATOR)) {
			rootPath = rootPath + SEPERATOR;
		}
		if (absolutePath.startsWith(rootPath)) {
			return absolutePath.substring(rootPath.length());
		}

		// 类似盘符大小写问题，导致不能直接按字符串匹配路径，做如下处理:
		LinkedList<String> subPaths = new LinkedList<String>();
		File rootFile = new File(rootPath);
		File tmpFile = new File(absolutePath);
		while (tmpFile != null && !tmpFile.equals(rootFile)) {
			subPaths.addFirst(tmpFile.getName());
			tmpFile = tmpFile.getParentFile();
		}

		if (tmpFile == null) {
			log.error("getRelativePath failed, rootPath: " + rootPath
					+ ", absolutePath: " + absolutePath);
			throw new BaseException(BaseErrorCode.COMN_FILE_PATH_INVALID);
		}

		return StringUtils.toString(subPaths, SEPERATOR);
	}

	/**
	 * 获取文件名称
	 * 
	 * @since
	 * @param filePath
	 * @return
	 */
	public static String getFileNm(String filePath) {
		File file = new File(filePath);
		return file.getName();
	}

	/**
	 * 获取文件大小(byte)
	 * 
	 * @since
	 * @param filePath
	 * @return
	 * @throws BaseException
	 */
	public static long getFileLength(String filePath) throws BaseException {
		try {
			File file = new File(filePath);
			return file.length();
		} catch (SecurityException e) {
			log.error("getFileLen failed, filePath: " + filePath, e);
			throw new BaseException(BaseErrorCode.COMN_FILE_NO_PERMISSION);
		}
	}

	/**
	 * 为文件创建不存在的祖先目录
	 * 
	 * @since
	 * @param filePath
	 * @throws BaseException
	 */
	public static void mkParentDir(String filePath) throws BaseException {
		File file = new File(filePath);

		String parent = file.getParent();
		mkDir(parent);
	}

	/**
	 * 创建目录
	 * 
	 * @since
	 * @param dirPath
	 * @throws BaseException
	 */
	public static void mkDir(String dirPath) throws BaseException {
		try {
			File dir = new File(dirPath);
			if (!dir.exists() || !dir.isDirectory()) {
				mkParentDir(dirPath);

				if (!dir.mkdir()) {
					log.error("mkDir failed, dirPath: " + dirPath);
					throw new BaseException(
							BaseErrorCode.COMN_FILE_CREATE_ERROR);
				}
			}
		} catch (SecurityException e) {
			log.error("mkDir failed, dirPath: " + dirPath, e);
			throw new BaseException(BaseErrorCode.COMN_FILE_NO_PERMISSION);
		}
	}

	/**
	 * move file
	 * 
	 * @since
	 * @param srcFilePath
	 * @param destFilePath
	 * @throws BaseException
	 */
	public static void move(String srcFilePath, String destFilePath)
			throws BaseException {
		log.error("move, srcFilePath: " + srcFilePath + ", destFilePath: "
				+ destFilePath);

		copy(srcFilePath, destFilePath);
		delete(srcFilePath);
	}

	/**
	 * copy file
	 * 
	 * @since
	 * @param srcFilePath
	 * @param destFilePath
	 * @param append
	 * @throws BaseException
	 */
	@SuppressWarnings("resource")
	public static void copy(String srcFilePath, String destFilePath)
			throws BaseException {
		log.debug("copy, srcFilePath: " + srcFilePath + ", destFilePath: "
				+ destFilePath);

		mkParentDir(destFilePath);

		String tmpFilePath = destFilePath + TEMP_SUFFIX;
		FileChannel in = null;
		FileChannel out = null;
		try {
			delete(destFilePath);
			in = new FileInputStream(srcFilePath).getChannel();
			out = new FileOutputStream(tmpFilePath).getChannel();

			long size = in.size();
			long position = 0;
			while (position < size) {
				position += in.transferTo(position, COPY_MAX_COUNT, out);
			}
			out.force(true);
		} catch (FileNotFoundException e) {
			log.error("copy failed, srcFilePath: " + srcFilePath
					+ ", destFilePath: " + destFilePath, e);
			throw new BaseException(BaseErrorCode.COMN_FILE_NOT_EXIST);
		} catch (IOException e) {
			log.error("copy failed, srcFilePath: " + srcFilePath
					+ ", destFilePath: " + destFilePath, e);
			throw new BaseException(BaseErrorCode.COMN_FILE_IO_ERROR);
		} catch (SecurityException e) {
			log.error("copy failed, srcFilePath: " + srcFilePath
					+ ", destFilePath: " + destFilePath, e);
			throw new BaseException(BaseErrorCode.COMN_FILE_NO_PERMISSION);
		} finally {
			IOUtils.close(in);
			IOUtils.close(out);
		}
		new File(tmpFilePath).renameTo(new File(destFilePath));
	}

	/**
	 * 删除目录或文件
	 * 
	 * @since
	 * @param path
	 * @throws 无文件操作权限异常或删除异常
	 */
	public static void delete(String path) throws BaseException {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return;
			}

			if (f.isDirectory()) {
				deleteDirectory(f);
			} else if (f.isFile()) {
				deleteFile(f);
			} else {
				// do nothing
			}
		} catch (SecurityException e) {
			log.error("delete file or directory failed, path: " + path, e);
			throw new BaseException(BaseErrorCode.COMN_FILE_NO_PERMISSION);
		}
	}

	/**
	 * 删除目录及其下的所有文件
	 * 
	 * @since
	 * @param dir
	 * @throws BaseException
	 */
	public static void deleteDirectory(File dir) throws BaseException {
		try {
			if (!dir.exists() || !dir.isDirectory()) {
				return;
			}

			File[] files = dir.listFiles();
			if (files != null) {
				for (File f : files) {
					if (f.isDirectory()) {
						deleteDirectory(f);
					} else if (f.isFile()) {
						deleteFile(f);
					} else {
						// do nothing
					}
				}
			}
			dir.delete();
		} catch (SecurityException e) {
			log.error(
					"delete directory failed, dirPath: "
							+ dir.getAbsolutePath(), e);
			throw new BaseException(BaseErrorCode.COMN_FILE_NO_PERMISSION);
		}
	}

	/**
	 * 删除文件，如果删除失败，则抛出异常
	 * 
	 * @since
	 * @param file
	 * @throws
	 */
	public static void deleteFile(File file) throws BaseException {
		log.debug("delete file: " + file);
		if (!file.exists() || !file.isFile()) {
			return;
		}

		try {
			if (!file.delete()) {
				log.error("delete file failed, filePath: "
						+ file.getAbsolutePath());
				throw new BaseException(BaseErrorCode.COMN_FILE_DELETE_ERROR);
			}
		} catch (SecurityException e) {
			log.error(
					"delete file failed, filePath: " + file.getAbsolutePath(),
					e);
			throw new BaseException(BaseErrorCode.COMN_FILE_NO_PERMISSION);
		}
	}
}
