package persistent;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zpayment.cmn.persistent.PersistentService;

public class TestPersistent {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext(
				"persistent/testPersist.xml");
		PersistentService ps = ac.getBean("PersistentService_JDBC_swtDb",
				PersistentService.class);

		System.out.println(String.format("-----------%s------", "query"));
		List<DBUser> result = ps.query("select * from user", DBUser.class);
		for (DBUser usr : result) {
			System.out.println(usr);
		}

		System.out.println(String.format("-----------%s------", "queryAll"));
		result = ps.queryAll("user", DBUser.class);
		for (DBUser usr : result) {
			System.out.println(usr);
		}

		System.out.println(String.format("-----------%s------",
				"queryByColumns"));
		result = ps.queryByColumns(DBUser.class, null, new String[] { "dept" },
				new Object[] { "sjtu" });
		for (DBUser usr : result) {
			System.out.println(usr);
		}

		System.out.println(String.format("-----------%s------",
				"querySingleByColumns"));
		DBUser singleUser = ps.querySingleByColumns(DBUser.class, null,
				new String[] { "dept" }, new Object[] { "sjtu" });
		System.out.println(singleUser);

	}
}
