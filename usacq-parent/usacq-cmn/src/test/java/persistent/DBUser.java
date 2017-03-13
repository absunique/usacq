package persistent;

import com.peiwang.usacq.cmn.persistent.jdbc.annonation.JdbcColumn;
import com.peiwang.usacq.cmn.persistent.jdbc.annonation.JdbcView;

@JdbcView(name = "user")
public class DBUser {
	@Override
	public String toString() {
		return "DBUser [id=" + id + ", name=" + name + ", dept=" + dept + "]";
	}

	@JdbcColumn(name = "id", key = true)
	private int id;
	@JdbcColumn(name = "name")
	private String name;
	@JdbcColumn(name = "dept")
	private String dept;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}
}
