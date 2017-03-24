package com.zpayment.cmn.persistent.jdbc.builder;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({ ConditionBuilderTest.class, DeleteBuilderTest.class,
		InsertBuilderTest.class, PSQLBuilderTest.class, PSQLFormatTest.class,
		SelectBuilderTest.class, UpdateBuilderTest.class,
		BatchInsertBuilderTest.class })
public class AllTests {

}
