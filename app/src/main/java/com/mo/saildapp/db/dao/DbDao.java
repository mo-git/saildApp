package com.mo.saildapp.db.dao;

import java.util.ArrayList;
import java.util.List;

import com.mo.saildapp.db.entity.Raokouling;
import com.mo.saildapp.utils.Syscontents;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbDao {
	
	public SQLiteDatabase db;
	
	
	public DbDao(){
		String path = Syscontents.DATABASE_PATH + Syscontents.DATABASE_FILENAME;
		db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
	}
	
	public boolean add(Raokouling data){
		boolean flag = false;
		try{
			ContentValues values = new ContentValues();
			values.put("title", data.getTitle());
			values.put("content", data.getContent());
			long resurt = db.insert(Syscontents.TABLE_NAME, null, values);
			db.close();
			if(resurt == -1){
				flag = false;
			}else{
				flag = true;
			}
		}catch(Exception e){
			flag = false;
			
		}
		return flag;
		
	}
	
	
	public boolean delete(int id){
		boolean flag = false;
		try {
			int resurt = db.delete(Syscontents.TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
			db.close();
			if(resurt == 0){
				flag = false;
			}else{
				flag = true;
			}	
		} catch (Exception e) {
			flag = false;
		}
		return flag;
		
	}
	// id Ä¬ÈÏInteger.MAX_VALUE
	public List<Raokouling> getData(int index){
		List<Raokouling> datas = new ArrayList<Raokouling>();
		try {
			String sql = "select * from raokouling where id < " + index + " order by id desc limit 20 offset 0";
			Cursor cursor = db.rawQuery(sql, null);
//			Cursor cursor = db.query(Syscontents.TABLE_NAME,new String[]{"id","title","content"}, "id<?", new String[]{String.valueOf(14)}, null, null, "id desc", "20,0");
			 while(cursor.moveToNext()){  
				 Raokouling data = new Raokouling();
		            int id = cursor.getInt(cursor.getColumnIndex("id"));  
		            String title = cursor.getString(cursor.getColumnIndex("title"));  
		            String content = cursor.getString(cursor.getColumnIndex("content"));  
		            data.setId(id);
		            data.setTitle(title);
		            data.setContent(content);
		            datas.add(data);
		        }  
		} catch (Exception e) {
		}
		
		return datas;
	}
}
