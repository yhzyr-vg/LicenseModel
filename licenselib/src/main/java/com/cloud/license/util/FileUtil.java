package com.cloud.license.util;

import java.io.File;

/**
 * File: FileUtil.java
 * Author: Landy
 * Create: 2019/7/11 11:27
 */
public class FileUtil {
    //创建目录
    public static void createDirtory(String dirName)
    {
        File f = new File(dirName);
        if(!f.exists())
        {
            String[] dirArray = dirName.split("/");
            String temp = "";
            for (int i = 0; i < dirArray.length; i++)
            {
                temp += dirArray[i].trim() + "/";
                f = new File(temp);
                if(!f.exists()){
                    f.mkdir();
                }
            }
        }
    }
}
