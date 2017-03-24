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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;

/**
 * 命令执行工具
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public abstract class CommandUtils {

	/** 用于日志记录的Logger */
	private static final Logger log = Logger.getLogger(CommandUtils.class);

	/**
	 * 判断是否为Linux操作系统
	 * 
	 * @since
	 * @return
	 */
	public static boolean isLinxOs() {
		String osName = System.getProperty("os.name");
		return osName != null && osName.trim().toLowerCase().contains("linux");
	}

	/**
	 * 获取操作系统名
	 * 
	 * @since
	 * @return
	 */
	public static String getOsName() {
		String osName = System.getProperty("os.name");
		log.debug("osName: " + osName);
		return osName;
	}

	/**
	 * 执行命令
	 * 
	 * @since
	 * @param cmd
	 * @return
	 */
	public static CmdExecRst exec(String cmd) {
		log.debug("exec cmd: " + cmd);

		BufferedReader reader = null;
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			p.waitFor();

			int exitValue = p.exitValue();

			StringBuilder sb = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return new CmdExecRst(exitValue, sb.toString());
		} catch (SecurityException e) {
			log.error("execute failed, cmd: " + cmd, e);
			throw new BaseException(BaseErrorCode.COMN_CMD_NO_PERMISSION);
		} catch (Exception e) {
			log.error("execute failed, cmd: " + cmd, e);
			throw new BaseException(BaseErrorCode.COMN_CMD_EXEC_FAILEd);
		} finally {
			IOUtils.close(reader);
		}
	}

	/**
	 * 执行带环境变量的命令
	 * 
	 * @since
	 * @param cmd
	 * @param envp
	 * @return
	 */
	public static CmdExecRst exec(String cmd, String[] envp) {
		log.debug("exec cmd: " + cmd + ", envp: " + envp);

		BufferedReader reader = null;
		try {
			Process p = Runtime.getRuntime().exec(cmd, envp);
			p.waitFor();

			int exitValue = p.exitValue();

			StringBuilder sb = new StringBuilder();
			reader = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return new CmdExecRst(exitValue, sb.toString());
		} catch (SecurityException e) {
			log.error("execute failed, cmd: " + cmd, e);
			throw new BaseException(BaseErrorCode.COMN_CMD_NO_PERMISSION);
		} catch (Exception e) {
			log.error("execute failed, cmd: " + cmd, e);
			throw new BaseException(BaseErrorCode.COMN_CMD_EXEC_FAILEd);
		} finally {
			IOUtils.close(reader);
		}
	}

	/**
	 * 命令行执行结果
	 * 
	 * @author wangshuzhen
	 * @version
	 * @since
	 * 
	 */
	public static class CmdExecRst {
		public CmdExecRst(int exitValue, String message) {
			this.exitValue = exitValue;
			this.message = message;
		}

		private int exitValue = -1;

		private String message;

		/**
		 * 判断是否成功
		 * 
		 * @since
		 * @return
		 */
		public boolean isSucceed() {
			return exitValue == 0;
		}

		public int getExitValue() {
			return exitValue;
		}

		public String getMessage() {
			return message;
		}

		@Override
		public String toString() {
			return "CmdExecRst[ exitValue: " + exitValue + ", message: "
					+ message + "]";
		}
	}
}
