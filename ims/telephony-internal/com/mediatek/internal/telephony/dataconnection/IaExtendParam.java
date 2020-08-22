package com.mediatek.internal.telephony.dataconnection;

public class IaExtendParam {
  public boolean mCanHandleIms = false;
  
  public String[] mDualApnPlmnList = null;
  
  public String mOperatorNumeric = "";
  
  public IaExtendParam() {}
  
  public IaExtendParam(String paramString) {}
  
  public IaExtendParam(String paramString, boolean paramBoolean, String[] paramArrayOfString) {}
  
  public IaExtendParam(String paramString, String[] paramArrayOfString) {}
  
  public String toString() {
    return "[OperatorNumberic: " + this.mOperatorNumeric + ", CanHandleIms: " + this.mCanHandleIms + ", DualApnPlmnList: " + this.mDualApnPlmnList + "]";
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/dataconnection/IaExtendParam.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */