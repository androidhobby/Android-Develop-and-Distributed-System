package com.cse586.fwu8.contentprovider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MyContentProvider extends ContentProvider { 
	
	private static final String TAG = "MyContentProvider";
	
	private static final String TABLE_NAME = "Contents";
	private static final String DATABASE_NAME = "msg_record.db";
	private SQLiteDatabase db = null;
	private DataBaseHelper dbHelper = null;
	private long rowId = 0;
	
	private static HashMap<String, String> userMsgProjectionMap = new HashMap<String, String>();
	private static final String ID = "provider_key";
	private static final String VALUE = "provider_value";
	
	private static final String[] READ_RECORD_PROJECTION = new String[] {
		ID,        //provider_key
		VALUE,     //provider_value
	};
	
	private static final Uri CONTENT_URI = Uri.parse("content://edu.buffalo.cse.cse486_586.provider");

	static {
		userMsgProjectionMap.put(ID, VALUE);
	}

	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		int rowId = db.delete(TABLE_NAME, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return rowId;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		ContentValues keyValueToInsert;
		
		if (values != null) {
			keyValueToInsert = new ContentValues(values);
		} else {
			keyValueToInsert = new ContentValues();
		}
		//TODO
		keyValueToInsert.put(ID, ID);
		keyValueToInsert.put(VALUE, VALUE);
		
		db = dbHelper.getWritableDatabase();
		rowId = db.insert(TABLE_NAME,"", values);                    // learned from Android Developer
		if (rowId > 0) {												  // learned from Android Developer
			Uri newUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}
		throw new SQLException("Failed to insert row into" + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbHelper = new DataBaseHelper(getContext(), DATABASE_NAME, null, 1);  //TODO
		return (dbHelper == null) ? false:true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder sqlQB = new SQLiteQueryBuilder();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		sqlQB.setTables(TABLE_NAME);
		Cursor cur = sqlQB.query(db, READ_RECORD_PROJECTION, selection, null, null, null, null); //TODO
		cur.setNotificationUri(getContext().getContentResolver(), uri);
		return cur;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		int updateId = db.update(TABLE_NAME, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return updateId;
	}
	
}
