package com.peiwang.usacq.cmn.persistent.jdbc.batch;

import com.peiwang.usacq.cmn.persistent.jdbc.util.MappingStruct;

public interface BatchQueryGenSql {
	public String getQuerySql(MappingStruct ms, String tableName);
}
