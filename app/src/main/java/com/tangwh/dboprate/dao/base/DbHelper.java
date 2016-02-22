package com.tangwh.dboprate.dao.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015/10/23 0023.
 */
public class DbHelper extends SQLiteOpenHelper {
    String create_sql;
    public DbHelper(Context context, String dbname, int version,String create_sql) {
        super(context, dbname, null, version);
        this.create_sql = create_sql;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
