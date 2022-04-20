package com.jackingaming.MealMaker3000WebService.models.menuitems;

import org.json.JSONObject;

public class Water extends MenuItem {
    public Water() {
        super();

        name = "water";
        price = 0.05;
    }

    public Water(JSONObject response) {
        super(response);
    }
}
