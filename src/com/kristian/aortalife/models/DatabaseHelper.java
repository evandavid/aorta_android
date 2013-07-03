package com.kristian.aortalife.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "dbaorta";
	public static final String DATABASE_TABLE = "user";
	public static final String USER_FIELD = "ids TEXT, fullname TEXT, token TEXT";
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	public void createTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE if not exists "+DATABASE_TABLE+" ("+USER_FIELD+")");
	}
	
	public void deleteUser(SQLiteDatabase db) {
		db.execSQL("DELETE FROM "+DATABASE_NAME);
	}
	
	public void saveData(SQLiteDatabase db, String fullname, String token) {
		ContentValues values = new ContentValues();
        values.put("token", token);
        values.put("fullname", fullname);
        values.put("ids", "1");
        db.insert("user", null, values);
	}
	
	public void update(SQLiteDatabase db, String token){
		ContentValues value = new ContentValues();
		String where = "ids=?";
		String[] whereArgs = new String[] {"1"};
        value.put("token", token);
        db.update(DATABASE_TABLE, value, where, whereArgs);
	}
	
	
	public Cursor getUser(SQLiteDatabase db){
		Cursor mCount = db.rawQuery("SELECT * FROM "+DATABASE_TABLE, null);
		return mCount;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		createTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
