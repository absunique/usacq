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

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.zpayment.cmn.util.XmlUtils;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:47:09
 */
@XStreamAlias("viewdef")
public class CmnXmlDef {
	@XStreamAsAttribute
	private String url;
	@XStreamAsAttribute
	private String name;
	@XStreamAsAttribute
	private boolean refreshable;
	@XStreamAsAttribute
	private String classFullName;
	@XStreamAsAttribute
	private int type;
	@XStreamAsAttribute
	private String extOprClass;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@XStreamAlias("indexs")
	private List<CmnXmlIndexDef> flatViewDefIndexs;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClassFullName() {
		return classFullName;
	}

	public void setClassFullName(String classFullName) {
		this.classFullName = classFullName;
	}

	public List<CmnXmlIndexDef> getFlatViewDefIndexs() {
		return flatViewDefIndexs;
	}

	public void setFlatViewDefIndexs(List<CmnXmlIndexDef> flatViewDefIndexs) {
		this.flatViewDefIndexs = flatViewDefIndexs;
	}

	public String getExtOprClass() {
		return extOprClass;
	}

	public void setExtOprClass(String extOprClass) {
		this.extOprClass = extOprClass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isRefreshable() {
		return refreshable;
	}

	public void setRefreshable(boolean refreshable) {
		this.refreshable = refreshable;
	}

	public static void main(String[] args) {
		XmlUtils.toXml(new CmnXmlDef(), new Class[] { CmnXmlDef.class },
				System.out);
	}

	@Override
	public String toString() {
		return "CmnXmlDef [url=" + url + ", name=" + name + ", refreshable="
				+ refreshable + ", classFullName=" + classFullName + ", type="
				+ type + ", extOprClass=" + extOprClass
				+ ", flatViewDefIndexs=" + flatViewDefIndexs + "]";
	}
}