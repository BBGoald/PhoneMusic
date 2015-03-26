package phone.wobo.music.bll;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqliteHelper extends SQLiteOpenHelper {

	public static final String favoritesTableName = "favorites";
	public static final String downloadTableName = "download";
	public static final String singersTableName = "singers";
	public static final String historyTableName = "history";
	public static final String localmusicTableName = "local";
	private static final String TAG = "liangbang";
	private int referencedCount;

	public SqliteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		Log.d(TAG, "******instance SqliteHelper");
		referencedCount = 0;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "	--->SqliteHelper--->onCreate");
		createFavoritesTable(db);
		createDownloadTable(db);
		createFavoritesSingersTable(db);
		createHistoryTable(db);
		createLocalMusic(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);		
		/*if (oldVersion <= 10 && newVersion > 10) {
			addpluginPath(db);
			addSingerImg(db);
			createDownloadTable(db);
			createFavoritesSingersTable(db);
			createHistoryTable(db);
		}*/
		
	}
	
	
	private boolean fromgGetReadableDatabase = false;

	public synchronized SQLiteDatabase getWritableDatabase() {
		Log.d(TAG, "	--->SqliteHelper--->getWritableDatabase");
		if (!fromgGetReadableDatabase)
			++referencedCount;
		return super.getWritableDatabase();
	}

	public synchronized SQLiteDatabase getReadableDatabase() {
		Log.d(TAG, "	--->SqliteHelper--->getReadableDatabase");
		++referencedCount;
		fromgGetReadableDatabase = true;
		SQLiteDatabase database = super.getReadableDatabase();
		fromgGetReadableDatabase = false;
		return database;
	}

	public synchronized void close() {
		--referencedCount;
		if (referencedCount <= 0) {
			referencedCount = 0;
			super.close();
		}
	}

	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL("PRAGMA user_version = " + String.valueOf(newVersion));
		} catch (Exception e) {
			e.printStackTrace();
		}
		onCreate(db);
	}
	//创建歌曲收藏表 HMM
	private void createFavoritesTable(SQLiteDatabase db) {
		Log.d(TAG, "	--->SqliteHelper--->createFavoritesTable");
		String favoritesSql = "CREATE TABLE IF NOT EXISTS " 
			+ favoritesTableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ "name VARCHAR,artist VARCHAR,"
			+ "album VARCHAR,path VARCHAR,duration INT,pluginPath VARCHAR,singerImg VARCHAR,fileType INT)";
		db.execSQL(favoritesSql);
	}
	
	// 创建下载表 
	private void createDownloadTable(SQLiteDatabase db) {
		Log.d(TAG, "	--->SqliteHelper--->createDownloadTable");
		String downloadSql = "CREATE TABLE IF NOT EXISTS "
				+ downloadTableName
				+ " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "name VARCHAR,artist VARCHAR,"
				+ "localpath VARCHAR,pluginPath VARCHAR,state VARCHAR,fileType VARCHAR)";
		db.execSQL(downloadSql);
	}

	//创建歌手收藏表
	private void createFavoritesSingersTable(SQLiteDatabase db) {
		Log.d(TAG, "	--->SqliteHelper--->createFavoritesSingersTable");
		String favoritesSongerSql = "CREATE TABLE IF NOT EXISTS "
				+ singersTableName
				+ " (id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "keySinger VARCHAR,artist VARCHAR,"
				+ "img_url VARCHAR,keySingerClass VARCHAR)";
		db.execSQL(favoritesSongerSql);
	}
	
	//创建历史播放表 HMM
	private void createHistoryTable(SQLiteDatabase db) {
		Log.d(TAG, "	--->SqliteHelper--->createHistoryTable");
		String favoritesSql = "CREATE TABLE IF NOT EXISTS " 
			+ historyTableName + " (id INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ "name VARCHAR,artist VARCHAR,"
			+ "album VARCHAR,path VARCHAR,duration INT,pluginPath VARCHAR,singerImg VARCHAR,fileType INT)";
		db.execSQL(favoritesSql);
	}
	
	//创建本地音乐表
	private void createLocalMusic(SQLiteDatabase db)
	{
		Log.d(TAG, "	--->SqliteHelper--->createLocalMusic");
		String localMusiSql = "CREATE TABLE IF NOT EXISTS "+localmusicTableName
				+" (id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+" name VARCHAR,artist VARCHAR,path VARCHAR UNIQUE,stat VERCHAR)";
		db.execSQL(localMusiSql);
	}
	
	
	
	private boolean isExistColumn(SQLiteDatabase db, String table, String column) {
		try {
			String sql = "select * from sqlite_master where tbl_name=?";
			Cursor c = db.rawQuery(sql, new String[] { table });

			if (c.getCount() > 0) {
				while (c.moveToNext()) {
					String s = c.getString(c.getColumnIndex("sql")).toLowerCase();

					if (s.indexOf(column + " ") >= 0) {
						return true;
					}
				}
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
