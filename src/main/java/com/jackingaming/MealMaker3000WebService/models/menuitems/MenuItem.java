package com.jackingaming.MealMaker3000WebService.models.menuitems;

import org.json.JSONException;
import org.json.JSONObject;

public class MenuItem {
    public static final String JSON_PRICE = "price";

    protected double price;

    public MenuItem() {
    }

    public MenuItem(JSONObject json) throws JSONException {
        price = json.getDouble(JSON_PRICE);
    }

    protected JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_PRICE, price);
        return json;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}