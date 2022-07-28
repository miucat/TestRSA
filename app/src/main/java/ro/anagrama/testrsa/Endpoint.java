package ro.anagrama.testrsa;

import com.google.gson.annotations.SerializedName;

public class Endpoint {

    @SerializedName("RSA")
    private String rsa;

    @SerializedName("SMS")
    private String sms;

    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("version")
    private String version;
    @SerializedName("phone")
    private String phone;

    public String getRsa() {
        return rsa;
    }

    public void setRsa(String rsa) {
        this.rsa = rsa;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
