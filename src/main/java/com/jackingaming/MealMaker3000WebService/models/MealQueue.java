package com.jackingaming.MealMaker3000WebService.models;

import java.util.ArrayList;
import java.util.List;

public class MealQueue {
    private static MealQueue singleton;

    List<Meal> meals = new ArrayList<Meal>();

    private MealQueue() {

    }

    public static MealQueue getInstance() {
        if (singleton == null) {
            singleton = new MealQueue();
        }
        return singleton;
    }

    public void addMeal(Meal mealToBeAdded) {
        meals.add(mealToBeAdded);
    }

    public void removeMeal(Meal mealToBeRemoved) {
        meals.remove(mealToBeRemoved);
    }
}
