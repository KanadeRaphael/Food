package com.example.food.utils;

import com.example.food.application.Food;
import com.example.food.bean.ClassifyResult;
import com.example.food.bean.Element;
import com.example.food.bean.FoodMenu;
import com.example.food.bean.Illness;
import com.example.food.bean.MyUser;
import com.example.food.bean.Occupation;
import com.example.food.bean.Physique;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CalculateUtils {
    /**
     * 通过身高获得健康的体重
     */
    public static float[] standardH2W(float height) {
        if (height > 10) {
            height = height / 100;
        }
        float min;
        float max;
        min = (float) 18.5 * height * height;
        max = (float) 14 * height * height;
        return new float[]{min, max};
    }

    /**
     * 计算BMI值
     * 計算公式     BMI = 體重(kg) / 身高^2(m^2)
     */
    public static float BMI(float height, float weight) {
        if (height > 10) {
            height = height / 100.0f;
        }
        return weight / (height * height);
    }

    /**
     * 根据BMI得到体质情况
     */
    public static String bodyStatus(float BMI) {
        if (BMI < 18.5) {
            return "轻体重";
        } else if (BMI < 24) {
            return "健康体重";
        } else if (BMI < 27) {
            return "轻度肥胖";
        } else if (BMI < 30) {
            return "中度肥胖";
        } else {
            return "重度肥胖";
        }
    }

    /**
     * 计算每个食物吃多少
     */
    public static ArrayList<ClassifyResult> getDishQuantity(
            ArrayList<ClassifyResult> classifyResultArrayList, MyUser user) {
        double calorieSum = 0;
        double[] calories = new double[classifyResultArrayList.size()];
        float baseQuantity = 600f;
        float factor = 1;

        if (user.getBmi() == -1) {
            MessageUtils.MakeToast("检测到未填写个人信息，按成人标准进行计算");
        } else {
            if (user.getAge() <= 60) {
                factor = 1 - (Math.abs(user.getAge() - 30)) / 30.0f;
            } else {
                factor = 1 - (Math.abs(user.getAge() - 30)) / 70.0f;
            }
            factor = factor * (user.getBmi() / 21.0f) - (factor - 1.0f) / 5;
            Logger.d(factor);
        }

        for (int i = 0; i < classifyResultArrayList.size(); i++) {
            calorieSum += classifyResultArrayList.get(i).getCalorie();
            calories[i] = classifyResultArrayList.get(i).getCalorie();
        }
        for (int i = 0; i < classifyResultArrayList.size(); i++) {
            classifyResultArrayList.get(i).setQuantity(calories[i] / calorieSum * baseQuantity * factor
                    + Food.flavourCount * 5);
        }
        return classifyResultArrayList;
    }

    /**
     * 元素比例
     */
    public static HashMap<String, Integer> elementsProportion(FoodMenu.ElementsBean elementsBean) {
        HashMap<String, Integer> map = new HashMap<>();
        Double suger = elementsBean.getCarbohydrate();
        Double fat = elementsBean.getFat();
        Double protein = elementsBean.getProtein();
        Double sum = fat + suger + protein;
        suger = suger / sum * 100;
        fat = fat / sum * 100;
        protein = protein / sum * 100;
        map.put("suger", suger.intValue());
        map.put("fat", fat.intValue());
        map.put("protein", protein.intValue());
        return map;
    }

    /**
     * 计算元素需求
     */
    public static Element getElementsByOccupation(MyUser user, Occupation occupation) {
        Element userElement = new Element(user);
        Element occupationElement = new Element(occupation.getElements());
        userElement.add(occupationElement, -1);
        return userElement;
    }

    public static Element getElementsByPhysique(MyUser user, Physique physique) {
        Element userElement = new Element(user);
        Element physiqueElement = new Element(physique.getElements());
        userElement.add(physiqueElement, -1);
        return userElement;
    }

    public static Element getElementsByOccupationAndPhysique(MyUser user, Occupation occupation, Physique physique) {
        Element userElement = new Element(user);
        Element occupationElement = new Element(occupation.getElements());
        Element physiqueElement = new Element(physique.getElements());
        physiqueElement.add(occupationElement, 2);
        userElement.add(physiqueElement, -1);
        return userElement;
    }

    public static Element getElementsAddIllness(Illness illness, MyUser user, Occupation occupation, Physique physique) {
        Element userElement = new Element(user);
        Element occupationElement = new Element(occupation.getElements());
        Element illnessElement = new Element(occupation.getElements());
        Element physiqueElement = new Element(physique.getElements());
        physiqueElement.add(occupationElement, 2);
        physiqueElement.add(illnessElement, 1);
        userElement.add(physiqueElement, -1);
        return userElement;
    }

    /**
     * 获得星期数
     */
    public static int getWeek() {
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) {
            return 7;
        }
        return week;
    }

    /**
     * 获得秒
     */
    public static int getSecond() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.SECOND);
    }

    public static int title2Int(String text) {
        String temp = text.substring(1, 2);
        Logger.d(temp);
        switch (temp) {
            case "一":
                return 1;
            case "二":
                return 2;
            case "三":
                return 3;
            case "四":
                return 4;
            case "五":
                return 5;
            case "六":
                return 6;
            default:
                return 7;
        }
    }

    /**
     * 性别转数字
     */
    public static int sex2int(String sex) {
        if (sex.equals("男")) {
            return 1;
        } else if (sex.equals("女")) {
            return 0;
        } else {
            Logger.e("不男不女");
            return 1;
        }
    }

    /**
     * 将步骤字符串分解
     */
    public static ArrayList<String> getStepArray(String whole) {
        ArrayList<String> list = new ArrayList<>();
        String[] array = whole.split("'");
        for (String s : array) {
            if (s.equals("[") || s.equals("]") || s.equals(", ") ||
                    s.equals("，") || s.equals(" ")) {

            } else {
                list.add(s);
            }
        }
        return list;
    }
}
