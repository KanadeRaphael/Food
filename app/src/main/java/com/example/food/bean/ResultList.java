package com.example.food.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultList implements Serializable {

    private ArrayList<ClassifyResult> results;

    public ResultList(ArrayList<ClassifyResult> results) {
        super();
        this.results = results;
    }

    public ArrayList<ClassifyResult> getResults() {
        return results;
    }

    public void setResults(ArrayList<ClassifyResult> results) {
        this.results = results;
    }

    /**
     * 用于Intent传递
     */

    @Override
    public String toString() {
        String log = "";
        for (int i = 0; i < results.size(); i++) {
            log += results.get(i).getName() + ";卡路里" +results.get(i).getCalorie() + "\n";
        }
        return log;
    }
}
