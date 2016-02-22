package com.tangwh.dboprate.dao.base;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.tangwh.dboprate.dao.annotation.ColumnName;
import com.tangwh.dboprate.dao.annotation.PrimaryKey;
import com.tangwh.dboprate.dao.annotation.Table;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Administrator on 2015/10/22 0022.
 */
public abstract class DaoSupportImpl<T> implements IDaoSupport<T> {
    SQLiteDatabase db;
    T t;
    public DaoSupportImpl(Context context,String dbName){
        t =getInstance();
        String create_table = createTable(t);
        SQLiteOpenHelper dbHelper = new DbHelper(context,dbName,1,create_table);
        this.db = dbHelper.getWritableDatabase();
    }
    @Override
    public boolean save(T t) {
        ContentValues contentValues = new ContentValues();
        fillColumn(t, contentValues);
        db.insertOrThrow(getTableName(), null, contentValues);
        return false;
    }

    private void fillColumn(T t, ContentValues contentValues) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            ColumnName columnName = field.getAnnotation(ColumnName.class);
            if (columnName != null) {
                String key = columnName.value();
                PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
                if (primaryKey != null) {
                    continue;
                }
                field.setAccessible(true);
                try {
                    Class clazz = field.getType();
                    if (clazz == Integer.class) {
                        int value = field.getInt(t);
                        contentValues.put(key, value);
                    } else if (clazz == Float.class) {
                        float value = field.getFloat(t);
                        contentValues.put(key, value);
                    } else if (clazz == Double.class) {
                        double value = field.getDouble(t);
                        contentValues.put(key, value);
                    } else {
                        String value = field.get(t).toString();
                        contentValues.put(key, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public int delete(T t) {
        db.delete(getTableName(),getIdName(t)+"=?",new String[]{getIdValue(t)});
        return 0;
    }

    private String getIdName(T t) {
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                ColumnName columnName = field.getAnnotation(ColumnName.class);
                if (columnName != null)
                    return columnName.value();

            }
        }
        return null;
    }
    private String getIdValue(T t){
        Field[] fields = t.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if(primaryKey!=null){
                try {
                   return field.get(t).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public long update(T t) {
        ContentValues contentValues = new ContentValues();
        fillColumn(t,contentValues);

        return db.replace(getTableName(), null, contentValues);
    }

    @Override
    public List<T> findAll() {
        List<T> result = null;
        Cursor cursor = db.query(getTableName(), null, null, null, null, null, null);
        if(cursor!=null&&cursor.getCount()>0) {
            while (cursor.moveToNext()) {
                T t = getInstance();
                fillField(cursor,t);
                result.add(t);
            }
            cursor.close();
        }
        return result;
    }
    private T getInstance(){
        Class clazz = getClass();
        Type genericSuperclass=clazz.getGenericSuperclass();
        if(genericSuperclass!=null&&genericSuperclass instanceof ParameterizedType)
        {
            Type[] arguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
            try {
                return ((Class<T>)arguments[0]).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
    private void fillField(Cursor cur,T t){
        Field[] fields = t.getClass().getDeclaredFields();
        for(Field field:fields){
            field.setAccessible(true);
            ColumnName columnName = field.getAnnotation(ColumnName.class);
            if(columnName!=null){
                String col = columnName.value();
                Class clazz = field.getType();
                Serializable value=null;
                if(clazz ==Integer.class){
                     value = cur.getInt(cur.getColumnIndex(col));
                }else if(clazz == Long.class){
                    value = cur.getLong(cur.getColumnIndex(col));
                }else if(clazz == Float.class){
                     value = cur.getFloat(cur.getColumnIndex(col));
                }else if(clazz == Double.class){
                    value = cur.getDouble(cur.getColumnIndex(col));
                }else{
                    value = cur.getString(cur.getColumnIndex(col));
                }
                try {
                    field.set(t,value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getTableName() {
        T t = getInstance();
        Table tableName = t.getClass().getAnnotation(Table.class);
        if (tableName != null) {
            return tableName.name();
        } else {

            return dotTdline(t.getClass().getName());
        }

    }

    private String dotTdline(String packagename) {
        if (TextUtils.isEmpty(packagename)) {
            return null;
        } else {
            return packagename.replaceAll(".", "_");
        }
    }

    private String createTable(T t) {
        StringBuilder sb = new StringBuilder();
        Field[] fields = t.getClass().getDeclaredFields();
        if (TextUtils.isEmpty(getTableName()) || fields.length <= 0) {
            throw new Resources.NotFoundException("Table or field is not found!");
        } else {

            sb.append("create table " + getTableName() + " (");
        }
        for (Field field : fields) {
            ColumnName columnName = field.getAnnotation(ColumnName.class);
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            Class clazz = field.getType();
            if (columnName != null) {
                if (primaryKey != null) {
                    if (primaryKey.autoincreament()) {
                        if (clazz == Integer.class) {
                            sb.append(columnName.value() + " integer primarykey autoincreament,");
                        } else if (clazz == Double.class || clazz == Float.class) {
                            sb.append(columnName.value() + " real primarykey autoincreament,");
                        } else {
                            sb.append(columnName.value() + " text primarykey autoincreament,");
                        }

                    } else {
                        if (clazz == Integer.class) {
                            sb.append(columnName.value() + " integer primarykey,");
                        } else if (clazz == Double.class || clazz == Float.class) {
                            sb.append(columnName.value() + " real primarykey,");
                        } else {
                            sb.append(columnName.value() + " text primarykey,");
                        }
                    }
                    continue;
                }
                if (clazz == Integer.class) {
                    sb.append(columnName.value() + " integer,");
                } else if (clazz == Double.class || clazz == Float.class) {
                    sb.append(columnName.value() + " real,");
                } else {
                    sb.append(columnName.value() + " text,");
                }
            }
        }
        sb.setCharAt(sb.lastIndexOf(","), ')');
        return sb.toString();
    }
    class DbHelper extends SQLiteOpenHelper {
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
}
