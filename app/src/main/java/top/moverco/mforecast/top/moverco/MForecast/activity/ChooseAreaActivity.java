package top.moverco.mforecast.top.moverco.MForecast.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import top.moverco.mforecast.R;
import top.moverco.mforecast.top.moverco.MForecast.db.MForecastDB;
import top.moverco.mforecast.top.moverco.MForecast.model.City;
import top.moverco.mforecast.top.moverco.MForecast.model.County;
import top.moverco.mforecast.top.moverco.MForecast.model.Province;
import top.moverco.mforecast.top.moverco.MForecast.util.HttpUtil;
import top.moverco.mforecast.top.moverco.MForecast.util.Utility;

/**
 * Created by liuzongxiang on 2016/12/28.
 */

public class ChooseAreaActivity extends Activity {
    /**
     * SET LEVEL for PROVINCE,CITY,COUNTY
     */
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    /**
     * init views
     */
    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    /**
     * init adapter for listview
     */
    private ArrayAdapter<String> arrayAdapter;

    private MForecastDB mForecastDB;
    private List<String> dataList = new ArrayList<String>();

    /**
     * init list for Province,City,County
     */
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;

    /**
     * Selected item[Province,City,County]
     */
    private Province selectedProvince;
    private City selectedCity;
    private County selectedCounty;

    /**
     * selected level
     */
    private int selectedLevel;

    /**
     * init views
     */
    private void initviews(){
        titleText = (TextView) findViewById(R.id.text_title);
        listView = (ListView) findViewById(R.id.list_view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        initviews();
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item_1,dataList);
        listView.setAdapter(arrayAdapter);
        mForecastDB = MForecastDB.getInstance(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (selectedLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(position);

                }
            }
        });

    }
    private void queryProvinces(){
        provinceList = mForecastDB.loadProvince();
        if (provinceList.size() > 0){
            dataList.clear();
            for (Province province : provinceList){
                dataList.add(province.getProvinceName());
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText("China");
            selectedLevel = LEVEL_PROVINCE;
        }
        else {
            queryFromServer(null,"province");
        }
    }
    private void queryCities(){
        cityList = mForecastDB.loadCity(selectedProvince.getId());
        if (cityList.size() > 0){
            dataList.clear();
            for (City c: cityList){
                dataList.add(c.getCityName());
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedProvince.getProvinceName());
            selectedLevel = LEVEL_CITY;
        }else {
            queryFromServer(selectedProvince.getProvinceCode(),"city");
        }

    }
    private void queryCounties(){
        countyList = mForecastDB.loadCounty(selectedCity.getId());
        if (countyList.size() > 0){
            dataList.clear();
            for (County c : countyList){
                dataList.add(c.getCountyName());
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            titleText.setText(selectedCity.getCityName());
            selectedLevel = LEVEL_COUNTY;
        }else {
            queryFromServer(selectedCity.getCityCode(),"county");
        }
    }
    private void queryFromServer(final String code,final String type){
        String address;
        if (!TextUtils.isEmpty(code)){
            address = "http://www.weather.com.cn/data/list3/city"+code+".xml";
        }else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sentHttpRequest(address, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                boolean result = false;
                if ("province".equals(type)){
                    result = Utility.handleProvinceResponse(mForecastDB,response);
                }else if ("city".equals(type)){
                    result = Utility.handleCityResponse(mForecastDB,response,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountyResponse(mForecastDB,response,selectedCity.getId());
                }
                if (result){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }

            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"Failed to load",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void closeProgressDialog() {
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    public void onBackPressed(){
        if (selectedLevel == LEVEL_COUNTY){
            queryCities();
        }else if (selectedLevel == LEVEL_CITY){
            queryProvinces();
        }else {
            finish();
        }
    }
}
