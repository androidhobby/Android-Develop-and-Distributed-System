/**
 * In this part, some codes learned from http://developer.android.com
 */
package edu.buffalo.cse.cse486_586.simpledynamo.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Fang Wu
 * April 20, 2012
 *
 */
public class MyContentProvider extends ContentProvider {

	@SuppressWarnings("unused")
	private static final String TAG = "MyContentProvider";
	
	public static final String DATABASE_NAME = "Context.db";
	public static final String TABLE_NAME = "Dynamo_Table";
	private SQLiteDatabase db;
	private DataBaseHelper dbHelper;
	private long rowId = 0;
	
	public static final class MyContent implements BaseColumns {
		
		public static final String ID = "id";
		public static final String KEY = "provider_key";   
		public static final String VALUE = "provider_value";
		public static final String ATTRIBUTE = "attribute";
		public static final Uri CONTENT_URI = Uri.parse("content://edu.buffalo.cse.cse486_586.simpledynamo.provider" + "/myProvider");
	}
	
	private static final HashMap<String, String> dynamoMap;
	static {
		dynamoMap = new HashMap<String, String>();
		dynamoMap.put(MyContent.ID, MyContent.ID);
		dynamoMap.put(MyContent.KEY, MyContent.KEY);
		dynamoMap.put(MyContent.VALUE, MyContent.VALUE);
		dynamoMap.put(MyContent.ATTRIBUTE, MyContent.ATTRIBUTE);
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		rowId = db.delete(TABLE_NAME, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(uri, null);
		return (int) rowId;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		rowId = db.insert(TABLE_NAME, null, values);
		if (rowId > 0) {
			Uri newUri = ContentUris.withAppendedId(MyContent.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(newUri, null);
			return newUri;
		}
		throw new SQLException("Failed to insert row into" + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		dbHelper = new DataBaseHelper(getContext(), DATABASE_NAME);
		db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder sqlQB = new SQLiteQueryBuilder();	
		sqlQB.setTables(TABLE_NAME);
		sqlQB.setProjectionMap(dynamoMap);
		Cursor cur = sqlQB.query(dbHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, null); 
		cur.setNotificationUri(getContext().getContentResolver(), uri);
		
		if (!cur.moveToFirst()) {
			cur.close();
			return null;
		}
		else
			return cur;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
