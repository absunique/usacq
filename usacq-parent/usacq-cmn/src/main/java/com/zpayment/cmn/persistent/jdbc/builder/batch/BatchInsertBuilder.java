/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月23日 - Create By peiwang
 */
package com.zpayment.cmn.persistent.jdbc.builder.batch;

import java.util.ArrayList;
import java.util.List;

import com.zpayment.cmn.persistent.jdbc.builder.Builder;
import com.zpayment.cmn.persistent.jdbc.builder.ColumnBuilder;
import com.zpayment.cmn.persistent.jdbc.builder.InsertBuilder;
import com.zpayment.cmn.persistent.jdbc.param.BatchPreparedSQL;
import com.zpayment.cmn.persistent.jdbc.param.PreparedSQL;
import com.zpayment.cmn.persistent.jdbc.param.SqlParam;

/**
 * 缺省批量SQL构造器，指定sql语句与各列的值
 * <p>
 * 
 * <pre>
 * DefaultBatchBuilder builder = DefaultBatchBuilder.build();                       <br>
 * builder.setSql("update TBL_IBMGM_XX set c2=?,c3=? where c1=?");                  <br>
 * for (int i = 0; i < x; i++) {                                                    <br>
 *      builder.setString("1").setInt(i + 1).setInt(i);                             <br>
 *      builder.addBatch();                                                         <br>
 * }                                                                                <br>
 * </pre>
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class BatchInsertBuilder extends InsertBuilder implements Builder,
		BatchParamsBuilder {

	// private int paramCount;
	private List<List<SqlParam>> batchParams = new ArrayList<List<SqlParam>>();
	private List<ColParam> flagshipColParam;

	@Override
	public PreparedSQL toPreparedSQL() {
		BatchPreparedSQL psql = new BatchPreparedSQL();
		psql.setSql(getSql());
		for (List<SqlParam> params : getBatchParams()) {
			psql.addBatch(params);
		}

		return psql;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.zpayment.cmn.persistent.jdbc.builder.ColumnBuilder#getFlagshipColParams
	 * ()
	 */
	@Override
	protected List<ColParam> getFlagshipColParams() {
		return flagshipColParam;
	}

	public static BatchInsertBuilder build() {
		return new BatchInsertBuilder();
	}

	private BatchInsertBuilder() {
	}

	/**
	 * 添加一个处理
	 * 
	 * @since
	 */
	public void addBatch() {
		List<SqlParam> params = super.getParams();
		// if (paramCount != params.size()) {
		// throw new BaseException(
		// BaseErrorCode.COMN_DATA_INVALID_FOMAT_ARG_CNT);
		// }
		batchParams.add(params);
		flagshipColParam = getCurrentColParams();
		setColParams(new ArrayList<ColumnBuilder.ColParam>());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cup.ibscmn.dao.builder.batch.BatchBuilder#getParams()
	 */
	@Override
	public List<List<SqlParam>> getBatchParams() {
		// TODO Auto-generated method stub
		return batchParams;
	}

}
