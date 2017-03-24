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
import java.io.IOException;
import java.io.InputStream;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;

import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableWorkbook;

/**
 * 使用模板生成EXL文件
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class GenExlByTemplate {

	/** 用于日志记录的Logger */
	private static final Logger log = Logger.getLogger(GenExlByTemplate.class);
	private static String BASE_TEMPLATE_PATH = "template/";

	/**
	 * 从模板文件中创建表格
	 * 
	 * @param fileInfNode
	 * @param preExchgDt
	 * @param fileRootPath
	 * @return
	 */
	public static WritableWorkbook initXlsFromTemplate(String fileDt,
			String fileRootPath, String tempFileNm) throws BaseException {

		// 获取模板源
		String templateName = BASE_TEMPLATE_PATH + tempFileNm;
		InputStream input = ClassUtils.getDefaultClassLoader()
				.getResourceAsStream(templateName);
		if (input == null) {
			log.error("GenExlByTemplate:initXlsFromTemplate()" + tempFileNm
					+ "文件模板不存在,路径应为：" + tempFileNm);
			throw new BaseException(BaseErrorCode.COMN_FILE_NOT_EXIST);
		}

		// 创建目标文件
		WritableWorkbook wwb = null;
		String filePath = fileRootPath + File.separator;
		File path = new File(filePath);
		if (!path.exists() && !path.isDirectory()) {
			path.mkdirs();
		}
		String dstFile = fileRootPath + File.separator
				+ tempFileNm.replaceAll("-Template", fileDt);
		File fDst = new File(dstFile);

		Workbook wb = null;
		try {
			wb = Workbook.getWorkbook(input);
		} catch (BiffException e) {
			log.error("GenExlByTemplate:initXlsFromTemplate():Workbook.getWorkbook 失败！");
			throw new BaseException(BaseErrorCode.COMN_FILE_CREATE_ERROR);
		} catch (IOException e) {
			log.error("GenExlByTemplate:initXlsFromTemplate():Workbook.getWorkbook 失败！");
			throw new BaseException(BaseErrorCode.COMN_FILE_CREATE_ERROR);
		}

		try {
			wwb = Workbook.createWorkbook(fDst, wb);
		} catch (IOException e) {
			log.error("GenExlByTemplate:initXlsFromTemplate():Workbook.createWorkbook（） 失败！");
			throw new BaseException(BaseErrorCode.COMN_FILE_CREATE_ERROR);
		}

		log.debug("GenExlByTemplate.initXlsFromTemplate() 结束");
		return wwb;

	}
}
