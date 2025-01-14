package com.graduation.yau.bigsweet.shop;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.FrameLayout;
import android.widget.NumberPicker;

import com.graduation.yau.bigsweet.R;
import com.graduation.yau.bigsweet.model.CityModel;
import com.graduation.yau.bigsweet.model.ProvinceModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yyj on 2019/6/12.
 */

public class PickLocationDialog {
    Activity mContext;
    NumberPicker mProvicePicker;
    NumberPicker mCityPicker;
    NumberPicker mDistrictPicker;
    List<ProvinceModel> mProvinceList;

    private AlertDialog ad;
    public boolean isGetValue = false;

    /**
     * 所有省集合
     */
    protected String[] mProvinceDatas;
    /**
     * 所有市的集合
     * key - 省 value - 市
     */
    protected Map<String, Object> mCitisDatasMap = new HashMap<>();

    protected Map<String, Object> mDistrictMap = new HashMap<>();
//    protected List<Map<String, Object>> cityList = new ArrayList<>();
    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;

    public PickLocationDialog(Activity mContext) {
        this.mContext = mContext;
    }

    public PickLocationDialog(Activity mContext, List<ProvinceModel> list) {
        this.mContext = mContext;
        initData(list);
    }


    public AlertDialog makeDialog(final AddressPickedListener listener) {
        FrameLayout locatePickLayout = (FrameLayout) mContext.getLayoutInflater().inflate(R.layout.layout_location_picker, null);
        mProvicePicker = (NumberPicker) locatePickLayout.findViewById(R.id.provincePicker);
        mCityPicker = (NumberPicker) locatePickLayout.findViewById(R.id.cityPicker);
        mDistrictPicker = (NumberPicker) locatePickLayout.findViewById(R.id.districtPicker);

        init();

        ad = new AlertDialog.Builder(mContext).setView(locatePickLayout).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProvinceModel provinceModel = mProvinceList.get(mProvicePicker.getValue());
                CityModel cityModel = provinceModel.getCity().get(mCityPicker.getValue());
                String districtModel = null;
                if (cityModel.getArea() != null) {
                    districtModel = cityModel.getArea().get(mDistrictPicker.getValue());
                }
                if (listener != null) {
                    listener.onAddressPicked(provinceModel, cityModel, districtModel);
                }
//                String province = mProvinceDatas[mProvicePicker.getValue()];
//                String city = ((String[]) mCitisDatasMap.get(mProvinceDatas[mProvicePicker.getValue()]))[mCityPicker.getValue()];
//                locate.setText(province + "-"
//                        + city
//                        + "-" + ((String[]) mDistrictMap.get(city))[mDistrictPicker.getValue()]);
                /*locate.setText(provinceModel.getName() + "-"
                        + cityModel.getName()
                        + (districtModel == null ? "" : ("-" + districtModel.getName())));*/
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();

        return ad;
    }

    void init() {
        mProvicePicker.setWrapSelectorWheel(false);
        mProvicePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                String proName = mProvinceDatas[newVal];
                String[] pros = (String[]) mCitisDatasMap.get(proName);
                mCityPicker.setDisplayedValues(null);
                mDistrictPicker.setDisplayedValues(null);
                setCityPickerTextSize((String[]) mCitisDatasMap.get(mProvinceDatas[newVal]));
                setDisTrictPickerTextSize((String[]) (mDistrictMap.get(((String[]) mCitisDatasMap.get(mProvinceDatas[newVal]))[0])));
                mCityPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        mDistrictPicker.setDisplayedValues(null);
                        setDisTrictPickerTextSize((String[]) mDistrictMap.get(mCityPicker.getDisplayedValues()[i1]));
                    }
                });
                mCityPicker.setWrapSelectorWheel(false);
            }
        });
        mCityPicker.setWrapSelectorWheel(false);
        mCityPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        mDistrictPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setCityPickerTextSize((String[]) mCitisDatasMap.get(mProvinceDatas[0]));
        setDisTrictPickerTextSize((String[]) mDistrictMap.get(((String[]) mCitisDatasMap.get(mProvinceDatas[0]))[0]));
    }

    private void setCityPickerTextSize(String[] cities) {
        if (null != mCityPicker) {
            mCityPicker.setMinValue(0);
            mCityPicker.setMaxValue(cities.length - 1);
            mCityPicker.setDisplayedValues(cities);

            mProvicePicker.setMinValue(0);
            mProvicePicker.setMaxValue(mProvinceDatas.length - 1);
            mProvicePicker.setDisplayedValues(mProvinceDatas);
        }
    }

    protected void setDisTrictPickerTextSize(String[] districts) {
        if (mDistrictPicker != null) {
            if (districts != null && districts.length > 0) {
                mDistrictPicker.setMinValue(0);
                mDistrictPicker.setMaxValue(districts.length > 0 ? districts.length - 1 : 0);
                mDistrictPicker.setWrapSelectorWheel(false);
                mDistrictPicker.setDisplayedValues(districts);
            } else {
                mDistrictPicker.setMinValue(0);
                mDistrictPicker.setMaxValue(0);
                mDistrictPicker.setWrapSelectorWheel(false);
                mDistrictPicker.setDisplayedValues(new String[]{"无"});
            }
        }
    }


    protected void initData(List<ProvinceModel> list) {
        mProvinceList = list;
        // 初始化默认选中的省、市
        if (mProvinceList != null && !mProvinceList.isEmpty()) {
            mCurrentProviceName = mProvinceList.get(0).getName();
            List<CityModel> cityList = mProvinceList.get(0).getCity();
            if (cityList != null && !cityList.isEmpty()) {
                mCurrentCityName = cityList.get(0).getName();
                isGetValue = true;
            }
        } else {
            return;
        }
        mProvinceDatas = new String[mProvinceList.size()];
        for (int i = 0; i < mProvinceList.size(); i++) {
            // 遍历所有省的数据
            mProvinceDatas[i] = mProvinceList.get(i).getName();
            List<CityModel> cityList = mProvinceList.get(i).getCity();
            String[] cityNames = new String[cityList.size()];
            for (int j = 0; j < cityList.size(); j++) {
                // 遍历省下面的所有市的数据
                cityNames[j] = cityList.get(j).getName();

                List<String> districtList = cityList.get(j).getArea();
                if (districtList != null && districtList.size() > 0) {
                    String[] districtNames = new String[districtList.size()];

                    for (int d = 0; d < districtList.size(); d++) {
                        districtNames[d] = districtList.get(d);
                    }
                    mDistrictMap.put(cityList.get(j).getName(), districtNames);
                }

            }
            // 省-市的数据，保存到mCitisDatasMap
            mCitisDatasMap.put(mProvinceList.get(i).getName(), cityNames);
        }
    }

    public interface AddressPickedListener {
        abstract void onAddressPicked(ProvinceModel province, CityModel city, String district);
    }
}
