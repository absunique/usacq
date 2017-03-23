package com.zpayment.cmn.persistent.jdbc.batch;


public interface BatchQueryProcRec<C> {
	public Integer procOneRec(C rec);
}
