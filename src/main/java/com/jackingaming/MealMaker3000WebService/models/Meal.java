package com.jackingaming.MealMaker3000WebService.models;

import com.jackingaming.MealMaker3000WebService.models.menuitems.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    private List<MenuItem> menuItems;

    public Meal() {
        menuItems = new ArrayList<MenuItem>();
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }
}
