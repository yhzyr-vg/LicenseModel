package com.cloud.license.util;
import java.util.Calendar;

import org.apache.commons.codec.digest.DigestUtils;
/**
 * File: CommonUtil.java
 * Author: Landy
 * Create: 2019/7/11 11:19
 */
public class CommonUtil {
    public static String calculateLicense(String str){
        return DigestUtils.md5Hex(str);
    }
    public static long getTimestmap_Expire(int years){
        long ret=Calendar.getInstance().getTimeInMillis();//默认当前实际
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, years);
        ret=calendar.getTimeInMillis();
        return ret;
    }


    public static String letterToNum(String input) {
        String reg = "[a-zA-Z]";
        StringBuffer strBuf = new StringBuffer();
        input = input.toLowerCase();
        if (null != input && !"".equals(input)) {
            for (char c : input.toCharArray()) {
                if (String.valueOf(c).matches(reg)) {
                    strBuf.append(c - 96);
                } else {
                    strBuf.append(c);
                }
            }
            return strBuf.toString();
        } else {
            return input;
        }
    }
}
