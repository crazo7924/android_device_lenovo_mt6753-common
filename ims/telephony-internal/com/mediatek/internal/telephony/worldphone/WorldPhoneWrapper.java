package com.mediatek.internal.telephony.worldphone;

import android.os.SystemProperties;
import android.telephony.Rlog;

public class WorldPhoneWrapper implements IWorldPhone {
  private static int sOperatorSpec = -1;
  
  private static IWorldPhone sWorldPhoneInstance = null;
  
  private static WorldPhoneUtil sWorldPhoneUtil = null;
  
  public static IWorldPhone getWorldPhoneInstance() {
    if (sWorldPhoneInstance == null) {
      String str = SystemProperties.get("ro.operator.optr");
      if (str != null && str.equals("OP01")) {
        sOperatorSpec = 1;
      } else {
        sOperatorSpec = 0;
      } 
      sWorldPhoneUtil = new WorldPhoneUtil();
      if (sOperatorSpec == 1) {
        sWorldPhoneInstance = new WorldPhoneOp01();
        logd("sOperatorSpec: " + sOperatorSpec + ", isLteSupport: " + WorldPhoneUtil.isLteSupport());
        return sWorldPhoneInstance;
      } 
    } else {
      logd("sOperatorSpec: " + sOperatorSpec + ", isLteSupport: " + WorldPhoneUtil.isLteSupport());
      return sWorldPhoneInstance;
    } 
    if (sOperatorSpec == 0)
      sWorldPhoneInstance = new WorldPhoneOm(); 
    logd("sOperatorSpec: " + sOperatorSpec + ", isLteSupport: " + WorldPhoneUtil.isLteSupport());
    return sWorldPhoneInstance;
  }
  
  private static void logd(String paramString) {
    Rlog.d("PHONE", "[WPO_WRAPPER]" + paramString);
  }
  
  public void notifyRadioCapabilityChange(int paramInt) {}
  
  public void setModemSelectionMode(int paramInt1, int paramInt2) {
    if (sOperatorSpec == 1 || sOperatorSpec == 0) {
      sWorldPhoneInstance.setModemSelectionMode(paramInt1, paramInt2);
      return;
    } 
    logd("Unknown World Phone Spec");
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/worldphone/WorldPhoneWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */