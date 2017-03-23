package com.zpayment.cmn.persistent.jdbc.batch;

import com.zpayment.cmn.persistent.jdbc.util.MappingStruct;

public interface BatchQueryGenSql {
	public String getQuerySql(MappingStruct ms, String tableName);
}
