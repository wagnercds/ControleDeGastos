package com.blogspot.tudoqueeinteressante.controlegastos;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.Context;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

public class tblDespesas extends ContentProvider {
	public static final String AUTHORITY = "com.blogspot.tudoqueeinteressante.controlegastos";	                                        
	private static  final String DATABASE_NAME = "controlegastos.db";
	private static  final int  DATABASE_VERSION = 2;
	public static final String TAG = "controlegastos";
	
	private static final  String DESPESAS_TABLE = "tblDespesas";
	private  static final int DESPESAS = 1;
	
	private static final  String LANCAMENTOS_TABLE = "tblLancamentos";
	private  static final int LANCAMENTOS = 2;
	
	private static final String LANCAMENTOS_DESPESAS_TABLE ="tblLancamentos,tblDespesas";
	private  static final int LANCAMENTOS_DESPESAS = 3;	

	private DBHelper mHelper;
		
	private static final UriMatcher mMatcher;
	
	private static HashMap<String, String> mProjectionDespesas;
	static {  
		mProjectionDespesas = new HashMap<String, String>();  
		mProjectionDespesas.put(TblCadDespesas.cd_ID, TblCadDespesas.cd_ID);  
		mProjectionDespesas.put(TblCadDespesas.fl_Tipo, TblCadDespesas.fl_Tipo);
		mProjectionDespesas.put(TblCadDespesas.ds_Despesa, TblCadDespesas.ds_Despesa);		
	}
	
	private static HashMap<String, String> mProjectionLancamentos;
	static {  
		mProjectionLancamentos = new HashMap<String, String>();  
		mProjectionLancamentos.put(TblCadLancamento.cd_ID, TblCadLancamento.cd_ID);  
		mProjectionLancamentos.put(TblCadLancamento.dt_Lancamento, TblCadLancamento.dt_Lancamento);
		mProjectionLancamentos.put(TblCadLancamento.cd_Despesa, TblCadLancamento.cd_Despesa);
		mProjectionLancamentos.put(TblCadLancamento.vl_Lancamento, TblCadLancamento.vl_Lancamento);
	}
	
	private static HashMap<String, String> mProjectionLancamentosDespesas;
	static {  
		mProjectionLancamentos = new HashMap<String, String>();  
		mProjectionLancamentos.put(LANCAMENTOS_TABLE + "." + TblCadLancamento.cd_ID, LANCAMENTOS_TABLE + "." +TblCadLancamento.cd_ID);  
		mProjectionLancamentos.put(LANCAMENTOS_TABLE + "." + TblCadLancamento.dt_Lancamento, LANCAMENTOS_TABLE + "." + TblCadLancamento.dt_Lancamento);
		mProjectionLancamentos.put(DESPESAS_TABLE + "." + TblCadDespesas.ds_Despesa, DESPESAS_TABLE + "." + TblCadDespesas.ds_Despesa);
		mProjectionLancamentos.put(LANCAMENTOS_TABLE + "." + TblCadLancamento.vl_Lancamento, LANCAMENTOS_TABLE + "." + TblCadLancamento.vl_Lancamento);
	}
	
	static {  
		mMatcher = new UriMatcher(UriMatcher.NO_MATCH);  
		mMatcher.addURI(AUTHORITY, DESPESAS_TABLE, DESPESAS);
		mMatcher.addURI(AUTHORITY, LANCAMENTOS_TABLE, LANCAMENTOS);
		mMatcher.addURI(AUTHORITY, LANCAMENTOS_DESPESAS_TABLE, LANCAMENTOS_DESPESAS);
	}	
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mHelper.getWritableDatabase();  
		int count;  
		switch (mMatcher.match(uri)) {  
			case DESPESAS:  
				count = db.delete(DESPESAS_TABLE, selection, selectionArgs);  
				break;
			case LANCAMENTOS:
				count = db.delete(LANCAMENTOS_TABLE, selection, selectionArgs);  
				break;
			default:  
				throw new IllegalArgumentException("URI desconhecida " + uri);
		}  
   
		getContext().getContentResolver().notifyChange(uri, null);  
		return count;
	}
	
	@Override
	public String getType(Uri uri) {
		switch (mMatcher.match(uri)) {  
		case DESPESAS:  
			return TblCadDespesas.CONTENT_TYPE;
		case LANCAMENTOS:
			return TblCadLancamento.CONTENT_TYPE;
		default:  
			throw new IllegalArgumentException("URI desconhecida " + uri);  
		}
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		long rowId;
		
		switch (mMatcher.match(uri)) {  
		case DESPESAS:  			  
			rowId = db.insert(DESPESAS_TABLE, TblCadDespesas.fl_Tipo, values);  
			if (rowId > 0) {  
				Uri noteUri = ContentUris.withAppendedId(TblCadDespesas.CONTENT_URI, rowId);  
				getContext().getContentResolver().notifyChange(noteUri, null);  
				return noteUri;  
			}  
		case LANCAMENTOS:			  
			rowId = db.insert(LANCAMENTOS_TABLE, TblCadLancamento.dt_Lancamento, values);  
			if (rowId > 0) {  
				Uri noteUri = ContentUris.withAppendedId(TblCadDespesas.CONTENT_URI, rowId);  
				getContext().getContentResolver().notifyChange(noteUri, null);  
				return noteUri;  
			}  
		default:  
			throw new IllegalArgumentException("URI desconhecida " + uri);  
		}
	}
	
	@Override
	public boolean onCreate() {
		mHelper = new DBHelper(getContext());;  
		return true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();  
		SQLiteDatabase database = mHelper.getReadableDatabase();  
		Cursor cursor;  
		switch (mMatcher.match(uri)) {  
			case DESPESAS:				
				builder.setTables(DESPESAS_TABLE);  
				builder.setProjectionMap(mProjectionDespesas);  
				break; 
			case LANCAMENTOS:
				builder.setTables(LANCAMENTOS_TABLE);  
				builder.setProjectionMap(mProjectionLancamentos);  
				break; 
			case LANCAMENTOS_DESPESAS:				
				builder.setTables(LANCAMENTOS_DESPESAS_TABLE);
				builder.setProjectionMap(mProjectionLancamentosDespesas);
				break;
			default:  
				throw new IllegalArgumentException("URI desconhecida " + uri);  
		}  
			   
		cursor = builder.query(database, projection, selection,selectionArgs, null, null, sortOrder);  
			   
		cursor.setNotificationUri(getContext().getContentResolver(), uri);  
		return cursor;
	}
	
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mHelper.getWritableDatabase();  
		int count;  
		switch (mMatcher.match(uri)) {  
			case DESPESAS:  
				count = db.update(DESPESAS_TABLE, values, selection, selectionArgs);  
				break; 
			case LANCAMENTOS: 
				count = db.update(LANCAMENTOS_TABLE, values, selection, selectionArgs);  
				break; 
			default:  
				throw new IllegalArgumentException("URI desconhecida " + uri);  
		} 
   
		getContext().getContentResolver().notifyChange(uri, null);  
		return count;
	}
	
	public static final class TblCadDespesas implements  BaseColumns {  
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + DESPESAS_TABLE);  
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + AUTHORITY;
		
		public static final String cd_ID = "cd_ID";  
		public static final String fl_Tipo = "fl_Tipo";
		public static final String ds_Despesa = "ds_Despesa";		
	}
	
	public static final class TblCadLancamento implements  BaseColumns {  
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + LANCAMENTOS_TABLE);
		   
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + AUTHORITY;	
		
		public static final String cd_ID = "cd_ID";  
		public static final String dt_Lancamento = "dt_Lancamento";
		public static final String cd_Despesa = "cd_Despesa";
		public static final String vl_Lancamento = "vl_Lancamento";
	};
	
	public static final class vwLancamentoDespesas implements  BaseColumns {  
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + LANCAMENTOS_DESPESAS_TABLE);
		   
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + AUTHORITY;	
		
		public static final String cd_IDLancamento = "cd_ID";  
		public static final String dt_Lancamento = "dt_Lancamento";
		public static final String ds_Despesa = "ds_Despesa";
		public static final String vl_Lancamento = "vl_Lancamento";
		public static final String join_Tables = DESPESAS_TABLE + ".cd_ID = " + LANCAMENTOS_TABLE + ".cd_Despesa";
		
	};
	
	private static class DBHelper extends SQLiteOpenHelper {  
		   
		DBHelper(Context context) {  
			super(context, DATABASE_NAME, null, DATABASE_VERSION);  
		}  
	
		@Override  
		public void onCreate(SQLiteDatabase db) {  
			db.execSQL("CREATE TABLE " + DESPESAS_TABLE + " (" +  
						TblCadDespesas.cd_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
						TblCadDespesas.fl_Tipo + " INTEGER," +						
						TblCadDespesas.ds_Despesa + " TEXT" +
						");");
						
			db.execSQL(		
						"CREATE TABLE " + LANCAMENTOS_TABLE + " (" +  
						TblCadLancamento.cd_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
						TblCadLancamento.dt_Lancamento + " TEXT," +						
						TblCadLancamento.cd_Despesa + " INTEGER," +
						TblCadLancamento.vl_Lancamento + " NUMERIC, " +
						" FOREIGN KEY (" + TblCadLancamento.cd_Despesa + ") REFERENCES "+ DESPESAS_TABLE +" (" + TblCadDespesas.cd_ID + ") ON DELETE CASCADE " + 
						"); " +
						"CREATE INDEX tblLancamento_dtLancamento_idx ON " + LANCAMENTOS_TABLE + "(" + TblCadLancamento.dt_Lancamento + ");" +
						"CREATE INDEX tblLancamento_cdDespesa_idx ON " + LANCAMENTOS_TABLE + "(" + TblCadLancamento.cd_Despesa + ");"						
					);  
		}  
		
		@Override  
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  
			if (newVersion == 2) {
				db.execSQL(						
						"CREATE TABLE " + LANCAMENTOS_TABLE + " (" +  
						TblCadLancamento.cd_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
						TblCadLancamento.dt_Lancamento + " TEXT," +						
						TblCadLancamento.cd_Despesa + " INTEGER," +
						TblCadLancamento.vl_Lancamento + " NUMERIC, " +
						" FOREIGN KEY (" + TblCadLancamento.cd_Despesa + ") REFERENCES "+ DESPESAS_TABLE +" (" + TblCadDespesas.cd_ID + ") ON DELETE CASCADE " + 
						"); " +
						"CREATE INDEX tblLancamento_dtLancamento_idx ON " + LANCAMENTOS_TABLE + "(" + TblCadLancamento.dt_Lancamento + ");" +
						"CREATE INDEX tblLancamento_cdDespesa_idx ON " + LANCAMENTOS_TABLE + "(" + TblCadLancamento.cd_Despesa + ");"						
					);  
			}
		}  
	}
}
