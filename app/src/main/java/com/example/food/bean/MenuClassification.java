package com.example.food.bean;

import java.io.Serializable;
import java.util.List;

public class MenuClassification implements Serializable{

    private String classification;
    private List<?> cure_occupation;
    private List<String> menu_effect;

    public String getClassification() {
        return classification;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public List<?> getCure_occupation() {
        return cure_occupation;
    }

    public void setCure_occupation(List<?> cure_occupation) {
        this.cure_occupation = cure_occupation;
    }

    public List<String> getMenu_effect() {
        return menu_effect;
    }

    public void setMenu_effect(List<String> menu_effect) {
        this.menu_effect = menu_effect;
    }

    @Override
    public String toString() {
        return "MenuClassification{" +
                "classification='" + classification + '\'' +
                ", cure_occupation=" + cure_occupation +
                ", menu_effect=" + menu_effect +
                '}';
    }
}
