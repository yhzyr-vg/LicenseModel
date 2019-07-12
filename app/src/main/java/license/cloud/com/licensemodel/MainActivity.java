package license.cloud.com.licensemodel;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cloud.license.activity.QRCodeActivity;
import com.cloud.license.config.TVConfig;
import com.cloud.license.manager.LicenseManager;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;

public class MainActivity extends Activity {

    private String TAG="MainActivity";
    private Context context;
    private BroadcastReceiver tvBroadcastReceiver;
    private View view;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_main);

        textView=(TextView) findViewById(R.id.textView);
        textView.setText("check license");
        Log.i("test","check license");

        Intent intent = new Intent(context,QRCodeActivity.class);
        context.startActivity(intent);

        IntentFilter filter=new IntentFilter(TVConfig.ACTION_CHECK_LICENSE_SUCCESS);
        filter.addAction(TVConfig.ACTION_CHECK_LICENSE_FAILED);
        tvBroadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                if(action.equals(TVConfig.ACTION_CHECK_LICENSE_SUCCESS)){
                    Toast.makeText(context,"成功",Toast.LENGTH_SHORT).show();
                    Log.i("test","成功");
                    textView.setText("成功");
                } else if(action.equals(TVConfig.ACTION_CHECK_LICENSE_FAILED)){
                    Toast.makeText(context,"失败",Toast.LENGTH_SHORT).show();
                    Log.i("test","失败");
                    textView.setText("失败");
                }
            }
        };
        registerReceiver(tvBroadcastReceiver,filter);

        LicenseManager licenseManager=new LicenseManager(context);
        licenseManager.checkLicense();
    }




    protected void onDestroy() {
        super.onDestroy();
        if(tvBroadcastReceiver!=null){
            unregisterReceiver(tvBroadcastReceiver);
            tvBroadcastReceiver=null;
        }
    }
}
