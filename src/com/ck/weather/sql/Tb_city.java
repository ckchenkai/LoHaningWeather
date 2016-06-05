package com.ck.weather.sql;

public class Tb_city {

	private int _id;
	private String city;
	
	public Tb_city() {
		// TODO Auto-generated constructor stub
		super();
	}
	public Tb_city(int id,String city){
		super();
		this._id=id;
		this.city=city;
		}
	
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
}
