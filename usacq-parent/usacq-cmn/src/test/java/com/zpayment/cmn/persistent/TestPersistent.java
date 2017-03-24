package com.zpayment.cmn.persistent;

import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zpayment.cmn.persistent.jdbc.builder.DeleteBuilder;
import com.zpayment.cmn.persistent.jdbc.builder.InsertBuilder;
import com.zpayment.cmn.persistent.jdbc.builder.SelectBuilder;
import com.zpayment.cmn.persistent.jdbc.builder.UpdateBuilder;

@Component
public class TestPersistent {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"com/zpayment/cmn/persistent/testPersist.xml");
		TestPersistent tp = ac.getBean(TestPersistent.class);
		// tp.testObject(ac);
		tp.testNative(ac);
	}

	@Transactional(value = "swtTrans")
	public void testNative(ApplicationContext ac) {
		PersistentService ps = ac.getBean("PersistentService_JDBC_swtDb",
				PersistentService.class);

		List<Map<String, Object>> result;
		System.out.println(String.format("-----------%s------",
				"query by preparedSql"));
		SelectBuilder sb = SelectBuilder.build().table("user");
		result = ps.query(sb.toPreparedSQL(), new ColumnMapRowMapper());
		for (Map<String, Object> rec : result) {
			System.out.println(rec);
		}

		System.out.println(String.format("-----------%s------",
				"query by preparedSql using and condition"));
		sb = SelectBuilder.build().table("user");
		sb.condition(true).add("dept = %s", "sjtu");
		result = ps.query(sb.toPreparedSQL(), new ColumnMapRowMapper());
		for (Map<String, Object> rec : result) {
			System.out.println(rec);
		}

		System.out.println(String.format("-----------%s------",
				"update no condition"));
		UpdateBuilder ub = UpdateBuilder.build().table("user");
		ub.stringCol("account", "ssssss");
		int count = ps.excute(ub.toPreparedSQL());
		System.out.println("update count:" + count);

		sb = SelectBuilder.build().table("user");
		result = ps.query(sb.toPreparedSQL(), new ColumnMapRowMapper());
		for (Map<String, Object> rec : result) {
			System.out.println(rec);
		}

		System.out.println(String.format("-----------%s------",
				"update and condition"));
		ub = UpdateBuilder.build().table("user");
		ub.stringCol("account", "ffff");
		ub.condition(true).stringEqual("dept", "sjtu")
				.stringEqual("name", "zhangjj");
		count = ps.excute(ub.toPreparedSQL());
		System.out.println("update count:" + count);

		sb = SelectBuilder.build().table("user");
		result = ps.query(sb.toPreparedSQL(), new ColumnMapRowMapper());
		for (Map<String, Object> rec : result) {
			System.out.println(rec);
		}

		System.out.println(String.format("-----------%s------", "insert"));
		InsertBuilder ib = InsertBuilder.build().table("user");
		ib.intCol("id", 100);
		ib.stringCol("name", "niubility");
		ib.stringCol("dept", "fudan");
		ib.stringCol("account", "idontknow");
		ib.stringCol("password", "333333");
		count = ps.excute(ib.toPreparedSQL());
		System.out.println("insert count:" + count);

		sb = SelectBuilder.build().table("user");
		result = ps.query(sb.toPreparedSQL(), new ColumnMapRowMapper());
		for (Map<String, Object> rec : result) {
			System.out.println(rec);
		}

		System.out.println(String.format("-----------%s------",
				"delete and condition"));
		DeleteBuilder db = DeleteBuilder.build().table("user");
		db.condition(true).stringEqual("name", "niubility")
				.stringEqual("dept", "sjtu");
		count = ps.excute(db.toPreparedSQL());
		System.out.println("delete count:" + count);

		sb = SelectBuilder.build().table("user");
		result = ps.query(sb.toPreparedSQL(), new ColumnMapRowMapper());
		for (Map<String, Object> rec : result) {
			System.out.println(rec);
		}
		System.out.println(String.format("-----------%s------",
				"delete and condition"));
		db = DeleteBuilder.build().table("user");
		db.condition(true).stringEqual("name", "niubility")
				.stringEqual("dept", "fudan");
		count = ps.excute(db.toPreparedSQL());
		System.out.println("delete count:" + count);

		sb = SelectBuilder.build().table("user");
		result = ps.query(sb.toPreparedSQL(), new ColumnMapRowMapper());
		for (Map<String, Object> rec : result) {
			System.out.println(rec);
		}
	}

	@Transactional(value = "swtTrans")
	public void testObject(ApplicationContext ac) {
		PersistentService ps = ac.getBean("PersistentService_JDBC_swtDb",
				PersistentService.class);
		List<DBUser> result;
		DBUser oneResult;
		DBUser inputUser = new DBUser();
		System.out.println(String.format("-----------%s------", "save"));
		inputUser.setId(41);
		inputUser.setDept("sjtu");
		inputUser.setName("test");
		inputUser.setAccount("AAAAA");
		inputUser.setPassword("123456");
		// ps.save(inputUser);
		// JdbcTemplate jt = ac.getBean("jdbcTemplate_swt", JdbcTemplate.class);
		// jt.execute("insert into user(id,name,dept,account,password) values(43,'ddd','ssss','111','123456')");
		// jt.execute("delete from user where id=43");

		System.out.println(String.format("-----------%s------", "queryOne"));
		inputUser.setId(5);
		oneResult = ps.queryOne(inputUser, null);
		System.out.println(oneResult);

		System.out.println(String.format("-----------%s------", "query"));
		result = ps.queryAll(DBUser.class, "user");
		for (DBUser usr : result) {
			System.out.println(usr);
		}

		System.out.println(String.format("-----------%s------",
				"querySingleByColumns"));
		oneResult = ps.querySingleByColumns(DBUser.class, null,
				new String[] { "dept" }, new Object[] { "sjtu" });
		System.out.println(oneResult);

		System.out.println(String.format("-----------%s------",
				"queryByColumns"));
		result = ps.queryByColumns(DBUser.class, null, new String[] { "dept" },
				new Object[] { "sjtu" });
		for (DBUser usr : result) {
			System.out.println(usr);
		}

		Page<DBUser> inputPage = new Page<DBUser>();
		inputPage.setCurrentPage(1);
		inputPage.setPageSize(2);
		Page<DBUser> outputPage;
		System.out.println(String.format("-----------%s------",
				"queryByPageByColumns"));
		outputPage = ps.queryByPageByColumns(inputPage, DBUser.class, null,
				new String[] { "dept" }, new Object[] { "sjtu" });
		System.out.println(outputPage);
		outputPage.forward();
		outputPage = ps.queryByPageByColumns(outputPage, DBUser.class, null,
				new String[] { "dept" }, new Object[] { "sjtu" });
		System.out.println(outputPage);

	}
}
