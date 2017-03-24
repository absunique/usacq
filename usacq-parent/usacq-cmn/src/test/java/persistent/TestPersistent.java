package persistent;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.zpayment.cmn.persistent.Page;
import com.zpayment.cmn.persistent.PersistentService;

@Component
public class TestPersistent {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"persistent/testPersist.xml");
		TestPersistent tp = ac.getBean(TestPersistent.class);
		System.out.println(tp.getClass());
		tp.test(ac);
	}

	@Transactional(value = "swtTrans")
	public void test(ApplicationContext ac) {
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
//		ps.save(inputUser);
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
