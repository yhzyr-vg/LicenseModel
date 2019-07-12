package com.cloud.license.activity;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cloud.license.config.TVConfig;
import com.cloud.license.helper.DatabaseHelper;
import com.cloud.license.helper.LayoutHelper;
import com.cloud.license.model.LicenseInfo;
import com.cloud.license.util.CommonUtil;
import com.cloud.license.util.FileUtil;
import com.cloud.license.util.QRCodeUtil;

public class QRCodeActivity extends Activity {

    private String deviceId;
    private String result;
    private ImageView qrcode;
    private Button btn_confirm;
    private Button btn_reset;
    private EditText editText;
    private Bitmap bitmapQr;
    private String qrPath;
    private Context context;
    private DatabaseHelper databaseHelper;
    private String licensecode;
    protected static final String TAG = "QRCodeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(LayoutHelper.getIdByName(context, LayoutHelper.CLASS_LAYOUT, "activity_qrcode"));
        databaseHelper=new DatabaseHelper(context);
        initview();
    }




    public void initview(){
        qrcode=(ImageView) findViewById(LayoutHelper.getIdByName(context,LayoutHelper.CLASS_ID,"qrcode"));
        btn_confirm=(Button) findViewById(LayoutHelper.getIdByName(context,LayoutHelper.CLASS_ID,"btn_confirm"));
        btn_reset=(Button) findViewById(LayoutHelper.getIdByName(context,LayoutHelper.CLASS_ID,"btn_reset"));
        editText=(EditText) findViewById(LayoutHelper.getIdByName(context,LayoutHelper.CLASS_ID,"editText"));
        
        deviceId=TVConfig.CURRENT_USER+TVConfig.DEVICE_ID;
        String  URL=TVConfig.URL_GET_LICENSE_CODE.replaceAll("MACID",deviceId).replaceAll("USERID",TVConfig.CURRENT_USER+"");
        Log.i(TAG, URL);
        String value=CommonUtil.letterToNum(CommonUtil.calculateLicense(deviceId));
        licensecode=value.substring(value.length()-6,value.length());
        TVConfig.DIR_CACHE=getDir("res",Context.MODE_PRIVATE).getPath();
        FileUtil.createDirtory(TVConfig.DIR_CACHE);
        qrPath=TVConfig.DIR_CACHE+"/qrcode.jgp";
        Log.i(TAG, qrPath);
        boolean success=QRCodeUtil.createQRImage(URL, 280, 280, null, qrPath);
        if (success) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    bitmapQr=BitmapFactory.decodeFile(qrPath);
                    qrcode.setImageBitmap(bitmapQr);
                }
            });
        }

        btn_reset.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                editText.setText("");
            }
        });
        btn_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                String txt=editText.getText().toString();
                if (!txt.equals(licensecode)) {
                    Toast.makeText(context,"验证码错误！", Toast.LENGTH_SHORT).show();
                }else{
                    String key="qaz";
                    String key1=CommonUtil.calculateLicense(TVConfig.CURRENT_USER+"");
                    if(key1!=null&&key1.length()>4){
                        key=key1.substring(0, 4);
                    }
                    long time=CommonUtil.getTimestmap_Expire(10);
                    String str=deviceId+key+time;
                    String value=CommonUtil.calculateLicense(str);

                    LicenseInfo info=new LicenseInfo();
                    info.setLicenseValue(value);
                    info.setLicenseExpire(time);
                    info.setLicenseKey(key);
                    info.setMacId(deviceId);
                    databaseHelper.saveDevice(info);
                    sendBroadcast(new Intent(TVConfig.ACTION_CHECK_LICENSE_SUCCESS));
                    finish();
                }
            }
        });

    }
}