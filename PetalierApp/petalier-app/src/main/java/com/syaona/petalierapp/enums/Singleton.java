package com.syaona.petalierapp.enums;

/**
 * Created by smartwavedev on 4/18/16.
 */
public class Singleton {

    public static int loginFromMain;
    public static int selectedDay;

    public static int getSelectedDay() {
        return selectedDay;
    }

    public static void setSelectedDay(int selectedDay) {
        Singleton.selectedDay = selectedDay;
    }

    public static int getLoginFromMain() {
        return loginFromMain;
    }

    public static void setLoginFromMain(int loginFromMain) {
        Singleton.loginFromMain = loginFromMain;
    }


}
