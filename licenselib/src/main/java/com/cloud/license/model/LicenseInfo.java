package com.cloud.license.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * File: LicenseInfo.java
 * Author: Landy
 * Create: 2019/7/10 17:17
 */
public class LicenseInfo {

    private String _id;
    private String licenseValue;
    private String licenseKey;
    private String macId;
    private long licenseExpire;

    public static LicenseInfo getLicenseInfo(String str){
        LicenseInfo ret=null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(str);
            int total=jsonObject.getInt("total");
            if(total==1){
                JSONObject obj2=new JSONObject(jsonObject.get("license").toString());
                ret=new LicenseInfo();
                ret.set_id(obj2.getString("id"));
                ret.setLicenseValue(obj2.getString("license_value"));
                ret.setLicenseExpire(obj2.getLong("licenseExpire"));
                ret.setLicenseKey(obj2.getString("licenseKey"));
                ret.setMacId(obj2.getString("macId"));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLicenseValue() {
        return licenseValue;
    }

    public void setLicenseValue(String licenseValue) {
        this.licenseValue = licenseValue;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public long getLicenseExpire() {
        return licenseExpire;
    }

    public void setLicenseExpire(long licenseExpire) {
        this.licenseExpire = licenseExpire;
    }
}
