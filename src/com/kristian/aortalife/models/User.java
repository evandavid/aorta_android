package com.kristian.aortalife.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class User {
	private String fullname;
	private String token;
	private boolean exist;
	private Context context;
	private DatabaseHelper dbhelper;
	private SQLiteDatabase db = null;
	
	public User(Context context){
		this.context = context;
		this.dbhelper = new DatabaseHelper(this.context);
	    this.db = this.dbhelper.getWritableDatabase();
	    Cursor cursor = this.dbhelper.getUser(db);
	    this.constructUser(cursor);
	    this.db.close();
	}
	
	private void constructUser(Cursor cursor){
		if (cursor.getCount() == 0){
			this.exist = false;
		}else{
			this.exist = true;
			if (cursor.moveToFirst()){
  			  do{
  				  this.fullname = cursor.getString(cursor.getColumnIndex("fullname"));
  				  this.token = cursor.getString(cursor.getColumnIndex("token"));
  			  	}while(cursor.moveToNext());
  		  	} 
		}
	}
	
	public String getName(){
		if(this.exist)
			return this.fullname;
		else
			return null;
	}
	
	public String getToken(){
		if(this.exist)
			return this.token;
		else
			return null;
	}
	
	public boolean isExist(){
		return this.exist;
	}
	
	public void deleteUser(){
		this.dbhelper = new DatabaseHelper(this.context);
	    this.db = this.dbhelper.getWritableDatabase();
	    this.dbhelper.deleteUser(db);
	    this.db.close();
	}
}
