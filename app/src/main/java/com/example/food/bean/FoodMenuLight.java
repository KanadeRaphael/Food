package com.example.food.bean;

import java.io.Serializable;

public class FoodMenuLight implements Serializable{

    private String name;
    private double calorie;
    private int elements;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public double getElements() {
        return elements;
    }

    public void setElements(int elements) {
        this.elements = elements;
    }

    @Override
    public String toString() {
        return "FoodMenuLight{" +
                "name='" + name + '\'' +
                ", calorie=" + calorie +
                ", elements=" + elements +
                '}';
    }
}
