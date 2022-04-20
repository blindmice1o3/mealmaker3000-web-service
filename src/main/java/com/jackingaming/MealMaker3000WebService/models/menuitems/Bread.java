package com.jackingaming.MealMaker3000WebService.models.menuitems;

import org.json.JSONObject;

public class Bread extends MenuItem {
    public Bread() {
        super();

        name = "bread";
        price = 0.25;
    }

    public Bread(JSONObject response) {
        super(response);
    }
}
