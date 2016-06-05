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
	 * ���������Ϣ
	 */
	public void add(Tb_city tb_city){
		//Tb_inaccount tb_inaccount=new Tb_inaccount();
		db=helper.getWritableDatabase();//��ʼ��SQLiteDatabase����
		//ִ�����������Ϣ����
		db.execSQL("insert into tb_city(_id,city) values (	'"+
		    tb_city.get_id()+ "'," +                    
           "'"+tb_city.getCity()+"')"); 
				
		  
		 
	}
	
/*
 * ����������Ϣ
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
	 * ����������Ϣ
	 * 
	 */
	public Tb_city find(int id){
		
		db=helper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select _id,city"
				+ " from tb_city where _id=?", new String[]{
				
			  String .valueOf(id)   //���ң��洢��Cursor����
		});
		
		if(cursor.moveToNext()){  //�������ҵ�����Ϣ
			//������������Ϣ�洢��Tb_city��
			
			return new Tb_city(cursor.getInt(cursor.getColumnIndex("_id")),
					
					cursor.getString(cursor.getColumnIndex("city"))
					);
			
		}
		return null;   //���û����Ϣ���򷵻�null
	}
	
	
	/**
	 * 
	 * ɾ����Ϣ
	 * 
	 */
	public void delete(integer... ids){
		
		if(ids.length>0){   //�ж��Ƿ����Ҫɾ����id
			
			StringBuffer sb=new StringBuffer();
			for(int i=0;i<ids.length;i++){

			sb.append('?').append(',');  //��ɾ��������ӵ�StringBuffer������
		    }
			sb.deleteCharAt(sb.length()-1);  //ȥ�����һ����,"�ַ�
			db=helper.getWritableDatabase();
			
			db.execSQL("delete from tb_city where _id in ("+sb+")", (Object[]) ids);
		}
		
	}
	
	
	/*
	 * 
	 * ��ȡ��Ϣ
	 * 
	 * 
	 */
	public List<Tb_city> getScrollData(){
		
		List<Tb_city> tb_city =new ArrayList<Tb_city>();//�������϶���
		db=helper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select * from tb_city",null);
		
		while(cursor.moveToNext()){
			tb_city.add(new Tb_city(cursor.getInt(cursor.getColumnIndex("_id")),
			cursor.getString(cursor.getColumnIndex("city"))
			
					
					));//������������Ϣ��ӵ�������
		}
		return tb_city; //���ؼ���
	}
	
	/*
	 * 
	 *��ȡ�ܼ�¼��
	 * 
	 */
	public long getCount(){
		
		db=helper.getWritableDatabase();
	    Cursor cursor=db.rawQuery("select count(_id) from tb_city", null);
	    if(cursor.moveToNext()){ //�ж�Cuesor���Ƿ�������
	    	return cursor.getLong(0); //�����ܼ�¼��
	    }
		return 0;  //���û�����ݣ��򷵻�0
		
	}





	 
	
	/*
	 * 
	 * ��ȡ�����
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
	 * ��ѯ���ݿ��еĳ���
	 */
    public Tb_city find_city(String cityName){
		
		db=helper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select _id,city"
				+ " from tb_city where city=?", new String[]{
				
			  cityName   //���ң��洢��Cursor����
		});
		
		if(cursor.moveToNext()){  //�������ҵ�����Ϣ
			//������������Ϣ�洢��Tb_city��
			
			return new Tb_city(cursor.getInt(cursor.getColumnIndex("_id")),
					
					cursor.getString(cursor.getColumnIndex("city"))
					);
			
		}
		return null;   //���û����Ϣ���򷵻�null
	}
    
    
    
    
    /*
	 * ��ѯcity���ݱ��еĳ���
	 */
    public String cfind(String cityName){
		
		db=helper.getWritableDatabase();
		Cursor cursor=db.rawQuery("select name"
				+ " from city where name=?", new String[]{
				
			  cityName   //���ң��洢��Cursor����
		});
		
		if(cursor.moveToNext()){  //�������ҵ�����Ϣ
			return cursor.getString(cursor.getColumnIndex("name"));	
		}
		return null;   //���û����Ϣ���򷵻�null
	}

}
