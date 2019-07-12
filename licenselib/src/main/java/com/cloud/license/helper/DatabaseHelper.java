package com.cloud.license.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cloud.license.model.LicenseInfo;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {

	private static final String TAG="DatabaseHelper";
	private static String DB_NAME = "cloud_license.db";
	private static int DB_VERSION = 1;

	private static final String QHID="qhid";
	private static final String LICENSE_VALUE="license_value";
	private static final String KEY="licenseKey";
	private static final String MACID="macId";
	private static final String EXPIRE="licenseExpire";

	private SQLiteDatabase db;
	private SQLiteHelper dbHelper;
	private Cursor cursor;

	Context context;
	public DatabaseHelper(Context context){
		this.context=context;
		dbHelper = new SQLiteHelper(context, DB_NAME, null, DB_VERSION);
		db = dbHelper.getWritableDatabase();
	}


	public LicenseInfo getLicenseInfo(){
		LicenseInfo device=null;
		try{
			List<LicenseInfo> list=new ArrayList<LicenseInfo>();
			Cursor cursor = db.query(SQLiteHelper.TB_NAME, null, null, null, null, null, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					LicenseInfo licenseInfo = new LicenseInfo();
					licenseInfo.setLicenseValue(cursor.getString(cursor.getColumnIndex(DatabaseHelper.LICENSE_VALUE)));
					licenseInfo.setLicenseKey(cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY)));
					licenseInfo.setMacId(cursor.getString(cursor.getColumnIndex(DatabaseHelper.MACID)));
					licenseInfo.setLicenseExpire(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.EXPIRE)));
					list.add(licenseInfo);
				}
			}
			cursor.close();

			if(list!=null&&list.size()>0){
				return list.get(0);
			}

		}catch(Exception ex){
			Log.e(TAG, "error "+ex.toString());
		}
		return device;
	}

	public boolean saveDevice(LicenseInfo bean){
		ContentValues values = new ContentValues();
		values.put(QHID, bean.get_id());
		values.put(LICENSE_VALUE, bean.getLicenseValue());
		values.put(KEY, bean.getLicenseKey());
		values.put(MACID, bean.getMacId());
		values.put(EXPIRE, bean.getLicenseExpire());
		//插入数据 用ContentValues对象也即HashMap操作,并返回ID号
		Long myID = db.insert(SQLiteHelper.TB_NAME, null, values);
		if(myID>0){
			return true;
		}
		else{
			return false;
		}
	}

	public boolean updateDevice(LicenseInfo bean){
		boolean ret=false;
		ContentValues values = new ContentValues();
		values.put(QHID, bean.get_id());
		values.put(LICENSE_VALUE, bean.getLicenseValue());
		values.put(KEY, bean.getLicenseKey());
		//values.put(MACID, bean.getMacId());
		values.put(EXPIRE, bean.getLicenseExpire());
		//插入数据 用ContentValues对象也即HashMap操作,并返回ID号
		int flag = db.update(SQLiteHelper.TB_NAME, values, " macId = '" + bean.getMacId()+"'", null);
		if(flag>0){
			ret=true;
		}
		return ret;
	}


}
