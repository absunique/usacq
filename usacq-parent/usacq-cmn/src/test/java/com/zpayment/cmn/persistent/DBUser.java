package com.zpayment.cmn.persistent;

import com.zpayment.cmn.persistent.jdbc.annonation.JdbcColumn;
import com.zpayment.cmn.persistent.jdbc.annonation.JdbcView;

@JdbcView(name = "user")
public class DBUser {
	@Override
	public String toString() {
		return "DBUser [id=" + id + ", name=" + name + ", dept=" + dept
				+ ", account=" + account + ", password=" + password + "]";
	}

	@JdbcColumn(name = "id", key = true)
	private int id;
	@JdbcColumn(name = "name")
	private String name;
	@JdbcColumn(name = "dept")
	private String dept;
	@JdbcColumn(name = "account")
	private String account;
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@JdbcColumn(name = "password")
	private String password;

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
