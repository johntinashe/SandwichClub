package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {
        try {
            JSONObject object = new JSONObject(json);
            Sandwich sandwich = new Sandwich();
            JSONObject nameObject = object.getJSONObject("name");
            sandwich.setMainName(nameObject.getString("mainName"));
            JSONArray knowAsArray = nameObject.getJSONArray("alsoKnownAs");
            sandwich.setAlsoKnownAs(getStringArray(knowAsArray));
            sandwich.setDescription(object.getString("description"));
            sandwich.setImage(object.getString("image"));
            sandwich.setPlaceOfOrigin(object.getString("placeOfOrigin"));
            JSONArray ingredientsList = object.getJSONArray("ingredients");
            sandwich.setIngredients(getStringArray(ingredientsList));
            return sandwich;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static List<String> getStringArray(JSONArray jsonArray) {
        List<String> stringArray = null;
        if (jsonArray != null) {
            int length = jsonArray.length();
            stringArray = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                stringArray.add(jsonArray.optString(i));
            }
        }
        return stringArray;
    }
}
