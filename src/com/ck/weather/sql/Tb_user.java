package com.ck.weather.sql;

public class Tb_user {

	private String user;
	private String pwd;
	public Tb_user(String user,String pwd) {
		// TODO Auto-generated constructor stub
		this.pwd=pwd;
		this.user=user;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
