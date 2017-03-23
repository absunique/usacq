/*
 * 
 * Copyright 2013, $${COMPANY} Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF $${COMPANY} CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF $${COMPANY} CO., LTD.
 * 
 * $Id: ExlExportUtils.java,v 1.1 2016/08/30 07:28:20 peiwang Exp $
 * 
 * Function:
 * 
 * 导出到excel
 * 
 * Edit History:
 * 
 * 2013-12-17 - Create By CUPPC
 */

package com.zpayment.cmn.util;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import com.zpayment.cmn.log.Logger;

/**
 * 导出到excel
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class ExlExportUtils {

	static Logger logger = Logger.getLogger(ExlExportUtils.class);

	/** 居中 */
	public static String ALIGN_CENTER = "center";
	/** 右对齐 */
	public static String ALIGN_RIGHT = "right";
	/** 左对齐 */
	public static String ALIGN_LEFT = "left";

	/** 数据内容-数据映射名 */
	public static String DATA_CONTENT = "content";
	/** 数据内容-对齐方式映射名 */
	public static String DATA_ALIGN_TYPE = "sortType";

	/**
	 * 直接导出查询的原始记录<br>
	 * 
	 * @since
	 * @param rs
	 * @param response
	 * @throws Exception
	 * @throws PortalException
	 */
	public static void exportReport(String fileName, ResultSet rs,
			HttpServletResponse response) throws Exception {
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ fileName + ".xls");
		response.setHeader("Cache-Control", "private");
		response.setHeader("Pragma", "");
		WritableWorkbook wwb = null;
		try {
			wwb = Workbook.createWorkbook(response.getOutputStream());
			WritableSheet ws = wwb.createSheet("sheet1", 0);

			ResultSetMetaData md = rs.getMetaData();
			int cc = md.getColumnCount();

			// 写标题
			WritableCellFormat wcfHeader = new WritableCellFormat(
					NumberFormats.TEXT);
			// wcfHeader.set;
			for (int i = 1; i <= cc; i++) {
				String colName = md.getColumnName(i);
				Label lable = new Label(i - 1, 0, colName, wcfHeader); // 列号从0开始与数据库列从1开始不同
				ws.addCell(lable);
			}

			// 写数据
			WritableCellFormat wcf = new WritableCellFormat(NumberFormats.TEXT);
			int r = 1;
			while (rs.next()) {
				for (int i = 1; i <= cc; i++) {
					Label lable = buildCell(md, rs, r, i, wcf);
					ws.addCell(lable);
				}
				r++;
			}
			wwb.write();
		} catch (SQLException e) {
			throw new Exception("数据读取异常", e);
		} catch (IOException e) {
			throw new Exception("输出报表文件异常", e);
		} catch (WriteException e) {
			throw new Exception("输出报表文件异常", e);
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (wwb != null) {
				try {
					wwb.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 根据类型构建单元格
	 * 
	 * @since
	 * @param md
	 *            数据集元数据
	 * @param rs
	 *            数据库结果集
	 * @param row
	 *            数据库行
	 * @param col
	 *            数据库列
	 * @param defWcf
	 *            缺省的单元格格式
	 * @return
	 * @throws SQLException
	 */
	private static Label buildCell(ResultSetMetaData md, ResultSet rs, int row,
			int col, WritableCellFormat defWcf) throws SQLException {
		Object obj = rs.getObject(col);
		if (obj == null) {
			return new Label(col, row, "", defWcf);
		}

		String v = obj.toString();
		final int colType = md.getColumnType(col);
		WritableCellFormat wcf = defWcf;
		Label label = null;
		switch (colType) {
		// string
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			break;
		// int
		case Types.BIT:
		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
		case Types.BIGINT:
			wcf = new WritableCellFormat(NumberFormats.INTEGER);
			break;
		// float
		case Types.FLOAT:
		case Types.DOUBLE:
		case Types.NUMERIC:
		case Types.DECIMAL:
			wcf = new WritableCellFormat(NumberFormats.FLOAT);
			break;
		// byte[]
		case Types.BINARY:
		case Types.VARBINARY:
		case Types.LONGVARBINARY:
			byte[] bb = rs.getBytes(col);
			v = new String(bb);
			break;
		// date & time
		case Types.DATE:
		case Types.TIME:
		case Types.TIMESTAMP:
			break;
		// other
		default:
			break;
		}

		label = new Label(col - 1, row, v, wcf); // excel 列号从0开始与数据库列从1开始不同
		return label;
	}
}
