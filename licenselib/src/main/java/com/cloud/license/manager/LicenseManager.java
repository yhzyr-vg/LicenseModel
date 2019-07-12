package com.cloud.license.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.cloud.license.activity.QRCodeActivity;
import com.cloud.license.config.TVConfig;
import com.cloud.license.helper.DatabaseHelper;
import com.cloud.license.model.LicenseInfo;
import com.cloud.license.util.HttpUtils;
import com.cloud.license.util.NetworkUtil;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * File: LicenseManager.java
 * Author: Landy
 * Create: 2019/7/10 17:06
 */
public class LicenseManager {
    private static String tag="LicenseManager";
    private Context context;
    private String result;
    private DatabaseHelper databaseHelper;
    private LicenseInfo license;
    private long delayTime=15*10000;

    public LicenseManager(Context context,String deviceId){
        this.context=context;
        TVConfig.DEVICE_ID=deviceId;
    }

    public void checkLicense(){
        databaseHelper=new DatabaseHelper(context);
        license=databaseHelper.getLicenseInfo();
        if(license==null){//数据库中不存在license
            Log.i(tag, "===========license==null");
            new Handler().postDelayed(new Runnable(){

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    if(NetworkUtil.isNetworkAvailable(context)){
                        check();
                    }else{
                        Toast.makeText(context, "未检测到网络,请扫屏幕上方的二维码", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context,QRCodeActivity.class);
                        context.startActivity(intent);
                    }
                }
            }, delayTime);
        } else{//本地存在license ，直接校验
            Log.i(tag, "=============checkLicense:"+license.getMacId());
            checkLicense(license);
        }
    }

    public void check(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String deviceId=TVConfig.CURRENT_USER+TVConfig.DEVICE_ID;
                String url=TVConfig.URL_GET_LICENSE.replaceAll("MACID",deviceId).replaceAll("USERID",TVConfig.CURRENT_USER+"");
                Log.i(tag, "check deviceId:"+deviceId);
                result=HttpUtils.httpGet(url);
                Log.i(tag, "result:"+result);
                handler.sendEmptyMessage(0);
            }
        }).start();
    }

    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            if(msg.what==0){
                LicenseInfo licenseInfo=null;
                if(result!=null&&!"".equals(result)){
                    licenseInfo=LicenseInfo.getLicenseInfo(result);
                }
                if(licenseInfo!=null){
                    databaseHelper.saveDevice(licenseInfo);
                    checkLicense(licenseInfo);
                } else{
                    setResult(false);
                }
            }
        }
    };
    /**
     * 校验license
     * @param licenseInfo
     * @return
     */
    private boolean checkLicense(LicenseInfo licenseInfo){
        if(licenseInfo==null){
            setResult(false);
            return false;
        }
        boolean ret=false;
        String value=calculateLicense(licenseInfo.getMacId()+licenseInfo.getLicenseKey()+licenseInfo.getLicenseExpire());
        if(licenseInfo.getLicenseValue().equals(value)){
            ret=true;
        }
        setResult(ret);
        return ret;
    }

    private void setResult(boolean flag){
        if(flag){
            context.sendBroadcast(new Intent(TVConfig.ACTION_CHECK_LICENSE_SUCCESS));
        }else{
            context.sendBroadcast(new Intent(TVConfig.ACTION_CHECK_LICENSE_FAILED));
        }
    }

    private static String calculateLicense(String str){
        return DigestUtils.md5Hex(str);
    }

    public void setDelayTime(long delayTime){
        this.delayTime=delayTime;
    }

    public void setUserId(int userId){
        TVConfig.CURRENT_USER=userId;
    }

}
