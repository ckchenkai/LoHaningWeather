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
          if(users.length>0){   //�ж��Ƿ����Ҫɾ����id
			
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<users.length;i++){

			sb.append('?').append(',');  //��ɾ��������ӵ�StringBuffer������
		    }
			sb.deleteCharAt(sb.length()-1);  //ȥ�����һ����,"�ַ�
			db=helper.getWritableDatabase();
			
			db.execSQL("delete from tb_user where user in ("+sb+")", (Object[]) users);
		}
	}
	
	
	
	
    
     
     
     public long getCount(){
 		
 		db=helper.getWritableDatabase();
 	    Cursor cursor=db.rawQuery("select count(user) from tb_user", null);
 	    if(cursor.moveToNext()){ //�ж�Cuesor���Ƿ�������
 	    	return cursor.getLong(0); //�����ܼ�¼��
 	    }
 		return 0;  //���û�����ݣ��򷵻�0
 		
 	}

 	/*
 	 * 
 	 * ��ȡ���������
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
