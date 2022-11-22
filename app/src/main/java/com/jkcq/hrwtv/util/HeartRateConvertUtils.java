package com.jkcq.hrwtv.util;

import android.util.Log;

import com.jkcq.hrwtv.configure.Constant;

/*
 *
 *
 * @author mhj
 * Create at 2018/4/20 10:26
 */
public class HeartRateConvertUtils {

    //小数点后保留几位
    private static int scale = 2;

    //男士卡路里  time 单位分钟
    public static double hearRate2CaloriForMan(int heartRate, int age, float weight, int time, String util) {
        if (!isValid(heartRate, age)) {
            return 0;
        }
        /**
         * 连上的时间和当前时间段
         *
         */
        double ageD = Arith.mul(age, 0.2017);
        double weightD = Arith.mul(weight, 0.1988);
        double heartRateD = Arith.mul(heartRate, 0.6309);

        double Calori = 0.0000000;

        if (Constant.UNIT_MILLS.equals(util)) {
            Calori = ((age * 0.2017) + (weight * 0.1988) + (heartRate * 0.6309) - 55.0969) / 4.184;

            // Calori = Arith.round(Arith.div(Arith.div(Arith.mul(Arith.sub(Arith.add(ageD, Arith.add(weightD, heartRateD)), 55.0969), time), 60), 4.184), scale);
        } else {
            Calori = ((age * 0.2017) + (weight * 0.1988) + (heartRate * 0.6309) - 55.0969) / 4.184;
            //Calori = ((age * 0.2017) + (weight * 0.1988) + (heartRate * 0.6309) - 55.0969) * time / 4.184;
            // Calori = Arith.round(Arith.div(Arith.mul(Arith.sub(Arith.add(ageD, Arith.add(weightD, heartRateD)), 55.0969), time), 4.184), scale);
        }
        Log.e("hearRate2CaloriFor", "Man Calori UNIT_MILLS=" + Calori);

//        double  one = Arith.add(weightD,heartRateD);
//        double two = Arith.add(ageD,one);
//        double three = Arith.sub(two,55.0969);
//        double four = Arith.mul(three,time);
//        double five = Arith.div(four,60);
//        double six = Arith.div(five,4.184);
//
//        Calori = Arith.round(six,scale);
        if (Calori < 0) {
            Calori = 0;
        }
        return Calori;
    }

    //女士卡路里
    public static double hearRate2CaloriForWoman(int heartRate, int age, float weight, int time, String util) {
        if (!isValid(heartRate, age)) {
            return 0;
        }

        double ageD = Arith.mul(age, 0.074);
        double weightD = Arith.mul(weight, 0.1263);
        double heartRateD = Arith.mul(heartRate, 0.4472);

        double Calori = 0.0;
        //每分钟的cal 单位是kcal
        if (Constant.UNIT_MILLS.equals(util)) {
            Calori = (age * 0.074 - weight * 0.1263 + heartRate * 0.4472 - 20.4022) * time / 4.184;
//            Calori =  Arith.div(Arith.div(Arith.sub(Arith.add(Arith.sub(ageD,weightD),heartRateD),20.4022),60),4.184);
        } else {
            Calori = (age * 0.074 - weight * 0.1263 + heartRate * 0.4472 - 20.4022) * time / 4.184;
            //Calori = (age * 0.074 - weight * 0.1263 + heartRate * 0.4473 - 20.4022) * time / 4.184;
//            Calori =  Arith.div(Arith.sub(Arith.add(Arith.sub(ageD,weightD),heartRateD),20.4022),4.184);
        }
        Log.e("hearRate2CaloriFor", " woman Calori UNIT_MILLS=" + Calori);
        if (Calori < 0) {
            Calori = 0;
        }
        return Calori;
    }


    public static double hearRate2Point(double cal, int mode, float match) {
        if (mode == Constant.MODE_ALL) {
            return cal * 0.06;
        } else {
            return cal * 0.1 * (match / 100.0);
        }
    }

    //点数,单次心率点数
    public static double hearRate2Point(int heartRate, double maxHearRate, String util) {

        double point = 0;
        //心率强度
        double hearStrength = hearRate2Percent(heartRate, maxHearRate);
        if (hearStrength < 50) {
            point = 0;
        } else if (hearStrength >= 50 && hearStrength < 60) {
            point = 1;
        } else if (hearStrength >= 60 && hearStrength < 70) {
            point = 2;
        } else if (hearStrength >= 70 && hearStrength < 80) {
            point = 3;
        } else if (hearStrength >= 80 && hearStrength < 90) {
            point = 4;
        } else if (hearStrength >= 90) {
            point = 5;
        }
        if (Constant.UNIT_MILLS.equals(util)) {
            return Arith.div(point, 60 / Constant.REFRESH_RATE);
        } else {
            return point;
        }
    }

    //心率强度
    public static double hearRate2Percent(int heartRate, double maxHeartRate) {
        double percent = 0;
        //心率强度
//        percent = Arith.div(heartRate,maxHeartRate,0)*100;
        percent = (heartRate / maxHeartRate) * 100;
        percent = Arith.div(percent, 1, 0);
        return percent;
    }


    public static int hearRate2Percent(int heartRate, int age) {
        return (heartRate * 100 / getMaxHeartRate(age));
    }

    //最大心率
    public static int getMaxHeartRate(int age) {
        int maxHeartRate = 220 - age;
        return maxHeartRate;
    }

    /**
     * 判断卡路里是否有效
     *
     * @param heartRate
     * @param age
     * @return
     */
    public static boolean isValid(int heartRate, int age) {
        if (hearRate2Percent(heartRate, getMaxHeartRate(age)) >= 50) {
            return true;
        }
        return false;
    }

    //
    public static int hearRate2MatchRate(int heartRate) {

        int matchRate = 0;

        return matchRate;
    }

    public static String doubleParseStr(double data) {
        int num = (int) data;
        return String.valueOf(num);
    }


    public static int parseRangeStr(String range) {
        if (range.equals("0")) {
            return 0;
        } else if (range.equals("1")) {
            return 1;
        } else if (range.equals("2")) {
            return 2;
        } else if (range.equals("3")) {
            return 3;
        } else if (range.equals("4")) {
            return 4;
        } else if (range.equals("5")) {
            return 5;
        } else {
            return 0;
        }
    }

}
