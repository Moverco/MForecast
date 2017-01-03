package top.moverco.mforecast.top.moverco.MForecast.util;

import android.text.TextUtils;

import top.moverco.mforecast.top.moverco.MForecast.db.MForecastDB;
import top.moverco.mforecast.top.moverco.MForecast.model.City;
import top.moverco.mforecast.top.moverco.MForecast.model.County;
import top.moverco.mforecast.top.moverco.MForecast.model.Province;

/**
 * Created by liuzongxiang on 2016/12/28.
 */

public class Utility {
    public synchronized static boolean handleProvinceResponse(MForecastDB mForecastDB,String response){
        if(!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0 ){
                for(String p : allProvinces){
                    String[] array = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    mForecastDB.saveProvince(province);

                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCityResponse(MForecastDB mForecastDB,String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length>0){

                for (String c :allCities){
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    mForecastDB.saveCity(city);
                }
                return true;
            }

        }return false;
    }

    public synchronized static boolean handleCountyResponse(MForecastDB mForecastDB,String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0){
                for (String c : allCounties){
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    mForecastDB.saveCounty(county);
                }
                return true;
            }

        }
        return false;
    }
}
