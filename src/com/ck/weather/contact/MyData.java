package com.ck.weather.contact;

public class MyData {

	String content;
	String time;
	 
	int flag;
	 

	public MyData(String content,int flag,String time) {
		// TODO Auto-generated constructor stub
		this.content=content;
		this.flag=flag;
		this.time=time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
