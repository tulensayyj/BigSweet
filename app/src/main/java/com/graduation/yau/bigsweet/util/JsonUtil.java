package com.graduation.yau.bigsweet.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.graduation.yau.bigsweet.model.ProvinceModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

/**
 * Created by yyj on 2019/6/12.
 */

public class JsonUtil {

    public static List<ProvinceModel> parseDataToLocation(String data) {

        List<ProvinceModel> list = new ArrayList<>();
        try {
            if (data.isEmpty()) {
                return null;
            }
            Log.d("JsonUtil", "parseDataToLocation: " + data);

            JsonParser jsonParser = new JsonParser();
            JsonArray jsonArray = jsonParser.parse(data.trim()).getAsJsonArray();
            Gson gson = new Gson();

            for (JsonElement bean : jsonArray) {
                ProvinceModel provinceModel = gson.fromJson(bean, ProvinceModel.class);
                list.add(provinceModel);
            }

            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T formJsonToArray(String json, Type t) {
        if (json == null) {
            return null;
        }
        return new Gson().fromJson(json, t);
    }

    public interface Callable {
        void onCall(List<ProvinceModel> list);
    }
}
