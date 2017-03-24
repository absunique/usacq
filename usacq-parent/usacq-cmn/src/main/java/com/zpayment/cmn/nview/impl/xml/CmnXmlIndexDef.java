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
package com.zpayment.cmn.nview.impl.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
/**
 * 
 * TODO 功能介绍
 *
 * @author peiwang
 * @since  下午2:47:23
 */
public @XStreamAlias("index")
class CmnXmlIndexDef {
	@XStreamAsAttribute
	private int id;
	@XStreamAsAttribute
	private String columns;
	@XStreamAsAttribute
	private String relatedColumns;

	public String getRelatedColumns() {
		return relatedColumns;
	}

	public void setRelatedColumns(String relatedColumns) {
		this.relatedColumns = relatedColumns;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	@Override
	public String toString() {
		return "FlatViewDefIndex [id=" + id + ", columns=" + columns + "]";
	}
}
