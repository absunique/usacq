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
package com.zpayment.cmn.nview.impl.db;

import java.io.Serializable;

import com.zpayment.cmn.Const;
import com.zpayment.cmn.persistent.jdbc.annonation.JdbcColumn;
import com.zpayment.cmn.persistent.jdbc.annonation.JdbcView;

/**
 * 
 * @author peiwang
 * @since 2017年3月24日
 */
@JdbcView(name = "tbl_zpayment_cmn_view_def")
public class ViewDef implements Serializable {
	private static final long serialVersionUID = Const.VERSION_ID;
	@JdbcColumn(name = "ID", key = true)
	Integer id;
	@JdbcColumn(name = "CACHE_NM")
	String cacheNm;
	@JdbcColumn(name = "CACHE_INDEX")
	Integer cacheIndex;
	@JdbcColumn(name = "LOAD_TYPE")
	Integer loadType;
	@JdbcColumn(name = "DATA_TYPE")
	Integer dataType;
	@JdbcColumn(name = "INDEX_COLUMNS")
	String indexColumns;
	@JdbcColumn(name = "RELATED_COLUMNS")
	String relatedColumns;
	@JdbcColumn(name = "QUERY_TYPE")
	Integer queryType;
	@JdbcColumn(name = "TABLE_NM")
	String tableNm;
	@JdbcColumn(name = "LOAD_SQL")
	String loadSql;
	@JdbcColumn(name = "COND_SQL")
	String condSql;

	public Integer getId() {
		return id;
	}

	public String getCacheNm() {
		return cacheNm;
	}

	public Integer getCacheIndex() {
		return cacheIndex;
	}

	public Integer getLoadType() {
		return loadType;
	}

	public Integer getDataType() {
		return dataType;
	}

	public String getIndexColumns() {
		return indexColumns;
	}

	public String getRelatedColumns() {
		return relatedColumns;
	}

	public Integer getQueryType() {
		return queryType;
	}

	public String getTableNm() {
		return tableNm;
	}

	public String getLoadSql() {
		return loadSql;
	}

	public String getCondSql() {
		return condSql;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setCacheNm(String cacheNm) {
		this.cacheNm = cacheNm;
	}

	public void setCacheIndex(Integer cacheIndex) {
		this.cacheIndex = cacheIndex;
	}

	public void setLoadType(Integer loadType) {
		this.loadType = loadType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public void setIndexColumns(String indexColumns) {
		this.indexColumns = indexColumns;
	}

	public void setRelatedColumns(String relatedColumns) {
		this.relatedColumns = relatedColumns;
	}

	public void setQueryType(Integer queryType) {
		this.queryType = queryType;
	}

	public void setTableNm(String tableNm) {
		this.tableNm = tableNm;
	}

	public void setLoadSql(String loadSql) {
		this.loadSql = loadSql;
	}

	public void setCondSql(String condSql) {
		this.condSql = condSql;
	}

	@Override
	public String toString() {
		return " IbhcsViewDef [id=" + id + ", cacheNm=" + cacheNm
				+ ", cacheIndex=" + cacheIndex + ", loadType=" + loadType
				+ ", dataType=" + dataType + ", indexColumns=" + indexColumns
				+ ", relatedColumns=" + relatedColumns + ", queryType="
				+ queryType + ", tableNm=" + tableNm + ", loadSql=" + loadSql
				+ ", condSql=" + condSql + ", ]";
	}
}
