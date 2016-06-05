package com.ck.weather.sql;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDAO {

	private DBOpenHelper helper;
	private SQLiteDatabase db;
	
	public UserDAO(Context context){
		helper=new DBOpenHelper(context);
	}
	
	public synchronized void add(Tb_user tb_user){
		db=helper.getWritableDatabase();
		db.execSQL("insert into tb_user (user,pwd) values (?,?)", new Object[]{
				tb_user.getUser(),tb_user.getPwd()
		});
	}
	
	
	public synchronized void updatePwd(String user,String pwd){
		db=helper.getWritableDatabase();
		db.execSQL("update tb_user set pwd=? where user=?", new String[]{
				pwd,user
		});
		/*ContentValues values = new ContentValues();
		values.put("pwd", tb_pwd.getPwd());		
		db.delete("tb_pwd", null, null);
		db.insert("tb_pwd", null, values);*/
	}
	
	
	public Tb_user find(String user){
		db=helper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select * from tb_user where user=?", new String[]{user});
		if(cursor.moveToNext()){
			return new Tb_user(cursor.getString(cursor.getColumnIndex("user")),cursor.getString(cursor.getColumnIndex("pwd")));
		}
		return null;
		
	}
	
	
	
	
	public synchronized void delete(String...users){
          if(users.length>0){   //判断是否存在要删除的id
			
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<users.length;i++){

			sb.append('?').append(',');  //将删除条件添加到StringBuffer对象中
		    }
			sb.deleteCharAt(sb.length()-1);  //去掉最后一个“,"字符
			db=helper.getWritableDatabase();
			
			db.execSQL("delete from tb_user where user in ("+sb+")", (Object[]) users);
		}
	}
	
	
	
	
    
     
     
     public long getCount(){
 		
 		db=helper.getWritableDatabase();
 	    Cursor cursor=db.rawQuery("select count(user) from tb_user", null);
 	    if(cursor.moveToNext()){ //判断Cuesor中是否有数据
 	    	return cursor.getLong(0); //返回总记录数
 	    }
 		return 0;  //如果没有数据，则返回0
 		
 	}

 	/*
 	 * 
 	 * 获取收入最大编号
 	 * 
 	 */
 	public int getMaxId(){
 		
 		
 		db=helper.getWritableDatabase();
 		Cursor cursor=db.rawQuery("select max(user) from tb_user", null);
 		while(cursor.moveToNext()){
 			return cursor.getInt(0);
 		}
 		return 0;
 		
 	}
}
