package com.ck.weather.sql;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CityDAO {

	private DBOpenHelper helper;
	private SQLiteDatabase db;
	
	public CityDAO(Context context){
		helper=new DBOpenHelper(context);
	}
	
	/*
	 * 添加收入信息
	 */
	public void add(Tb_city tb_city){
		//Tb_inaccount tb_inaccount=new Tb_inaccount();
		db=helper.getWritableDatabase();//初始化SQLiteDatabase对象
		//执行添加收入信息操作
		db.execSQL("insert into tb_city(_id,city) values (	'"+
		    tb_city.get_id()+ "'," +                    
           "'"+tb_city.getCity()+"')"); 
				
		  
		 
	}
	
/*
 * 更新收入信息
 * 	
 */
	public void upDate(Tb_city tb_city){
		
		db=helper.getWritableDatabase();
		
		db.execSQL("update tb_city set city=? where _id=?", new Object[]{	 
			 tb_city.getCity(),
		});
	}
	
	
	/*
	 * 
	 * 查找收入信息
	 * 
	 */
	public Tb_city find(int id){
		
		db=helper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select _id,city"
				+ " from tb_city where _id=?", new String[]{
				
			  String .valueOf(id)   //查找，存储到Cursor类中
		});
		
		if(cursor.moveToNext()){  //遍历查找到的信息
			//将遍历到的信息存储到Tb_city中
			
			return new Tb_city(cursor.getInt(cursor.getColumnIndex("_id")),
					
					cursor.getString(cursor.getColumnIndex("city"))
					);
			
		}
		return null;   //如果没有信息，则返回null
	}
	
	
	/**
	 * 
	 * 删除信息
	 * 
	 */
	public void delete(integer... ids){
		
		if(ids.length>0){   //判断是否存在要删除的id
			
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<ids.length;i++){

			sb.append('?').append(',');  //将删除条件添加到StringBuffer对象中
		    }
			sb.deleteCharAt(sb.length()-1);  //去掉最后一个“,"字符
			db=helper.getWritableDatabase();
			
			db.execSQL("delete from tb_city where _id in ("+sb+")", (Object[]) ids);
		}
		
	}
	
	
	/*
	 * 
	 * 获取信息
	 * 
	 * 
	 */
	public List<Tb_city> getScrollData(){
		
		List<Tb_city> tb_city =new ArrayList<Tb_city>();//创建集合对象
		db=helper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select * from tb_city",null);
		
		while(cursor.moveToNext()){
			tb_city.add(new Tb_city(cursor.getInt(cursor.getColumnIndex("_id")),
			cursor.getString(cursor.getColumnIndex("city"))
			
					
					));//将遍历到的信息添加到集合中
		}
		return tb_city; //返回集合
	}
	
	/*
	 * 
	 *获取总记录数
	 * 
	 */
	public long getCount(){
		
		db=helper.getWritableDatabase();
	    Cursor cursor=db.rawQuery("select count(_id) from tb_city", null);
	    if(cursor.moveToNext()){ //判断Cuesor中是否有数据
	    	return cursor.getLong(0); //返回总记录数
	    }
		return 0;  //如果没有数据，则返回0
		
	}





	 
	
	/*
	 * 
	 * 获取最大编号
	 * 
	 */
	public int getMaxId(){
		
		
		db=helper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select max(_id) from tb_city", null);
		while(cursor.moveToNext()){
			return cursor.getInt(0);
		}
		return 0;
		
	}

	public void delete(String cityName) {
		// TODO Auto-generated method stub
		db=helper.getWritableDatabase();
		
		db.execSQL("delete from tb_city where city=?",new String[]{
				
				  String .valueOf(cityName)} );
	}
	
	public void update(String cityName,int id) {
		// TODO Auto-generated method stub
		db=helper.getWritableDatabase();
		
		db.execSQL("update  tb_city set city=? where id=?",new String[]{
				
				   cityName,String.valueOf(id)} );
	}
	
	/*
	 * 查询数据库中的城市
	 */
    public Tb_city find_city(String cityName){
		
		db=helper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select _id,city"
				+ " from tb_city where city=?", new String[]{
				
			  cityName   //查找，存储到Cursor类中
		});
		
		if(cursor.moveToNext()){  //遍历查找到的信息
			//将遍历到的信息存储到Tb_city中
			
			return new Tb_city(cursor.getInt(cursor.getColumnIndex("_id")),
					
					cursor.getString(cursor.getColumnIndex("city"))
					);
			
		}
		return null;   //如果没有信息，则返回null
	}
    
    
    
    
    /*
	 * 查询city数据表中的城市
	 */
    public String cfind(String cityName){
		
		db=helper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select name"
				+ " from city where name=?", new String[]{
				
			  cityName   //查找，存储到Cursor类中
		});
		
		if(cursor.moveToNext()){  //遍历查找到的信息
			return cursor.getString(cursor.getColumnIndex("name"));	
		}
		return null;   //如果没有信息，则返回null
	}

}
