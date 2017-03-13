package com.peiwang.usacq.cmn.persistent.jdbc.batch;

import java.util.Iterator;

public class BatchInesrtPrepareImpl<C> implements BatchInsertPrepare<C> {

	private Iterable<C> list;
	private int count;

	public BatchInesrtPrepareImpl(Iterable<C> list, int count) {
		this.list = list;
		this.count = count;
	}

	@Override
	public int getBatchCount() {
		// TODO Auto-generated method stub
		return count;
	}

	@Override
	public Iterator<C> getIterator() {
		return list.iterator();
	}

}
