package com.jkcq.hrwtv.wu.util;

import com.jkcq.hrwtv.util.Arith;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin
 * Date 2022/1/7
 */
public class MatchUtils {

    private static final HashMap<Integer,Integer> hashMap = new HashMap<>();
     private static void getHashMap(){

         for(int i = 0;i<6;i++){
             hashMap.put(i,0);
         }
     }


    /**
     * 计算经验值
     * @param sex 性别
     * @param age 年龄
     * @param heartList 心率集合
     * @return 经验值
     */
    public static double matchHeartPoint(int sex, int age, List<Integer> heartList){
         try {
             getHashMap();
             //计算最大心率
             int maxHeart = matchMaxHeart(sex,age);

             //每个心率点对应的心率强度
             int[] strengthRand = new int[heartList.size()];
             //集合
             for(int i = 0;i<heartList.size();i++){
                 int currStrength = currentHeartStrength(heartList.get(i),maxHeart);
                 strengthRand[i] = strengthValue(currStrength);
             }

             for(int k = 0;k<strengthRand.length;k++){
                 if(hashMap.containsKey(strengthRand[k]) ){
                     int mapValue = hashMap.get(strengthRand[k]);
                     mapValue = mapValue+1;
                     hashMap.put(strengthRand[k],mapValue);

                 }
             }

             double totalPoint = 0;
             for(Map.Entry<Integer,Integer> mp : hashMap.entrySet()){
                 int tmpCount = mp.getKey() * mp.getValue();
                 double tmpTotal = Arith.div(tmpCount,60,3);

                 totalPoint = Arith.add(tmpTotal,totalPoint);

             }
             return totalPoint;

         }catch (Exception e){
             e.printStackTrace();
             return 0.0;
         }

    }

    //计算最大心率值 性别数-年龄
    private static int matchMaxHeart(int sex,int age){
        return sex == 0 ? 220 : 226 - age;
    }

    //计算当前心率强度 = 当前心率/最大心率 * 100%
    private static int currentHeartStrength(int currentHeart,int maxHeart){
        DecimalFormat decimalFormat = new DecimalFormat("##");
        double currH = Arith.div(currentHeart,maxHeart,2);
        return (int) (currH * 100);
    }


    //根据心率强度计算在哪个区间内
    private static int strengthValue(int heartStrength){
        if(heartStrength <= 49)
            return 1;
        if(heartStrength <= 59)
            return 2;
        if(heartStrength <= 69)
            return 3;
        if(heartStrength <= 79)
            return 4;
        if(heartStrength <= 89)
            return 5;
        return 1;
    }

}
