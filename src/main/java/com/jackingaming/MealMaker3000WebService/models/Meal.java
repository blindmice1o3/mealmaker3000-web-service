package com.jackingaming.MealMaker3000WebService.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jackingaming.MealMaker3000WebService.models.menuitems.Bread;
import com.jackingaming.MealMaker3000WebService.models.menuitems.MenuItem;
import com.jackingaming.MealMaker3000WebService.models.menuitems.Water;
import com.sun.xml.internal.bind.v2.model.core.TypeRef;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Meal {
    public static final String JSON_ID = "id";
    public static final String JSON_MENU_ITEMS = "menu_items";

    private Long id;
    private List<MenuItem> menuItems;

    public Meal() {
        // TODO: assign an ID
        menuItems = new ArrayList<MenuItem>();
    }

    public Meal(JSONObject json) {
        id = json.getLong(JSON_ID);

        JSONArray jsonArray = json.getJSONArray(JSON_MENU_ITEMS);
        menuItems = new ArrayList<MenuItem>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject menuItemAsJSON = jsonArray.getJSONObject(0);
            String nameOfMenuItem = menuItemAsJSON.getString(MenuItem.JSON_NAME);
            if (nameOfMenuItem.equals("bread")) {
                Bread bread = new Bread(menuItemAsJSON);
                menuItems.add(bread);
            } else if (nameOfMenuItem.equals("water")) {
                Water water = new Water(menuItemAsJSON);
                menuItems.add(water);
            } else {
                System.out.println("nameOfMenuItem does not equals() \"bread\" nor \"water\".");
            }
        }
    }

    public Meal(String menuItemsAsJSONString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        //Set pretty printing of json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        TypeReference<List<MenuItem>> mapType = new TypeReference<List<MenuItem>>() {};
        menuItems = objectMapper.readValue(menuItemsAsJSONString, mapType);
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public String toJSONString() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        //Set pretty printing of json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        String menuItemsAsJSONString = objectMapper.writeValueAsString(menuItems);

        return menuItemsAsJSONString;
    }
}
