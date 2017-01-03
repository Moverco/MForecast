package top.moverco.mforecast.top.moverco.MForecast.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import top.moverco.mforecast.top.moverco.MForecast.model.City;
import top.moverco.mforecast.top.moverco.MForecast.model.County;
import top.moverco.mforecast.top.moverco.MForecast.model.Province;

/**
 * Created by liuzongxiang on 2016/12/28.
 */

public class MForecastDB {
    /**
     * DATABASE NAME
     */
    public static final String DB_NAME = "MForecast";
    /**
     *
     */
    public static final int VERSION = 1;
    /**
     *
     */
    private static MForecastDB mForecastDB;
    private SQLiteDatabase db;

    /**
     * A private constructor
     * @param context
     */
    private MForecastDB(Context context){
        MForecastOpenHelper mForecastOpenHelper = new MForecastOpenHelper(context,DB_NAME,null,VERSION);
        db = mForecastOpenHelper.getWritableDatabase();
    }
    /**
     * Get instance of MForecastDB
     */
    public synchronized static MForecastDB getInstance(Context context){
        if(mForecastDB == null){
            mForecastDB = new MForecastDB(context);
        }
        return mForecastDB;
    }
    /**
     * Save data of Province
     */
    public void saveProvince(Province province){
        if(province != null){
            ContentValues values = new ContentValues();
            values.put("province_name",province.getProvinceName());
            values.put("province_code",province.getProvinceCode());
            db.insert("Province",null,values);
        }
    }
    /**
     * Get date of Province from database
     */
    public List<Province> loadProvince(){
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("Province",null,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                list.add(province);
            }while (cursor.moveToNext());
        }

        return list;
    }
    /**
     * Save data of City
     */
    public void saveCity(City city){
        if(city != null){
            ContentValues values = new ContentValues();
            values.put("city_name",city.getCityName());
            values.put("city_code",city.getCityCode());
            values.put("province_id",city.getProvinceId());
            db.insert("CITY",null,values);
        }
    }

    /**
     * Get data of City from database
     * @return
     */
    public List<City> loadCity(){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City",null,null,null,null,null,null,null);
        if (cursor.moveToFirst())
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
            }while (cursor.moveToNext());
        return list;
    }
    public List<City> loadCity(int provinceId){
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("City",null,null,null,null,null,null,null);
        if (cursor.moveToFirst())
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setProvinceId(provinceId);
            }while (cursor.moveToNext());
        return list;
    }
    /**
     * Save data of County
     */
    public void saveCounty(County county){
        if(county != null){
            ContentValues values = new ContentValues();
            values.put("county_name",county.getCountyName());
            values.put("county_code",county.getCountyCode());
            values.put("city_id",county.getCityId());
            db.insert("County",null,values);
        }
    }
    /**
     * Get data of County from database
     */
    public List<County> loadCounty(){
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County",null,null,null,null,null,null,null);
        if (cursor.moveToFirst())
            do{
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getColumnName(cursor.getColumnIndex("county_code")));
                county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
            }while (cursor.moveToNext());
        return list;
    }
    public List<County> loadCounty(int cityId){
        List<County> list = new ArrayList<County>();
        Cursor cursor = db.query("County",null,null,null,null,null,null,null);
        if (cursor.moveToFirst())
            do{
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                county.setCountyCode(cursor.getColumnName(cursor.getColumnIndex("county_code")));
                county.setCityId(cityId);
            }while (cursor.moveToNext());
        return list;
    }
}
