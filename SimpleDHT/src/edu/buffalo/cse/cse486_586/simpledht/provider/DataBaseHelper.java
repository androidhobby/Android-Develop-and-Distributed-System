/*
 * In this part, some codes learned from http://developer.android.com 
 */
package edu.buffalo.cse.cse486_586.simpledht.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String TAG = "DataBaseHelper";
	
	private static final int DATABASE_VERSION = 1;
	
	public DataBaseHelper(Context context, String name) {
		super(context, name, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	private static final String CREATE_DATABASE = "Create table "
					+ MyContentProvider.TABLE_NAME
					+ " ("
				    + MyContentProvider.MyContent.ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				    + MyContentProvider.MyContent.KEY + " TEXT, "
					+ MyContentProvider.MyContent.VALUE + " TEXT)";
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try {
			db.execSQL("DROP TABLE IF EXISTS " + MyContentProvider.TABLE_NAME);
		} catch (Exception e){
			e.printStackTrace();
		}
		
		try {
			db.execSQL(CREATE_DATABASE);
			Log.d(TAG, "Table created");
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "Table not exists!");
		}			
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
		
}
