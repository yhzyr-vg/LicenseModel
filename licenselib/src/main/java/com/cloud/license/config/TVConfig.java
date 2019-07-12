package com.cloud.license.config;

/**
 * File: TVConfig.java
 * Author: Landy
 * Create: 2019/7/9 18:13
 */

public class TVConfig {
    public static String URL_GET_LICENSE_CODE="http://signup.victgroup.com:8088/tvservice/Device!code.action?deviceid=MACID&userId=USERID";
    public static String URL_GET_LICENSE="http://signup.victgroup.com:8088/tvservice/android/DMSDevice!getDeviceLicense.action?deviceid=MACID&userId=USERID";
    public static int CURRENT_USER=131;
    public static String DEVICE_ID;
    public static String DIR_CACHE;
    /**
     * 校验license 成功
     */
    public static String ACTION_CHECK_LICENSE_SUCCESS="com.cloud.license.ACTION_CHECK_LICENSE_SUCCESS";
    /**
     * 校验license 失败
     */
    public static String ACTION_CHECK_LICENSE_FAILED="com.cloud.license.ACTION_CHECK_LICENSE_FAILED";

}
