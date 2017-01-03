package top.moverco.mforecast.top.moverco.MForecast.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liuzongxiang on 2016/12/28.
 */

public class MForecastOpenHelper extends SQLiteOpenHelper {

    /**
     * Create Table String for Province
     */
    public static final String CREATE_PROVINCE = "create table Province("+
            "id integer primary key autoincrement"+
            "province_name text"+
            "province_code text)";
    /**
     * Create Table String for City
     */

    public static final String CREATE_CITY ="create table City("+
            "id integer primary key autoincrement"+
            "city_name text"+
            "city_code text"+
            "province_id integer)";
    /**
     * Create Table String for County
     */
    public static final String CREATE_COUNTY = "create table County("+
            "id integer primary key autoincrement"+
            "county_name text"+
            "county_code text"+
            "city_id integer)";

    /**
     * Constructor Function
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public MForecastOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        /**
         * execSQL to create data table
         */
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
