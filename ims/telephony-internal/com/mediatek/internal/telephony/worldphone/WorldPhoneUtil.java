package com.mediatek.internal.telephony.worldphone;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.ProxyController;
import com.mediatek.internal.telephony.ModemSwitchHandler;

public class WorldPhoneUtil implements IWorldPhone {
  private static final int ACTIVE_MD_TYPE_LTG = 4;
  
  private static final int ACTIVE_MD_TYPE_LWCG = 5;
  
  private static final int ACTIVE_MD_TYPE_LWG = 3;
  
  private static final int ACTIVE_MD_TYPE_LfWG = 7;
  
  private static final int ACTIVE_MD_TYPE_LtTG = 6;
  
  private static final int ACTIVE_MD_TYPE_TG = 2;
  
  private static final int ACTIVE_MD_TYPE_UNKNOWN = 0;
  
  private static final int ACTIVE_MD_TYPE_WG = 1;
  
  private static final boolean IS_LTE_SUPPORT;
  
  private static final boolean IS_WORLD_MODE_SUPPORT;
  
  private static final boolean IS_WORLD_PHONE_SUPPORT;
  
  private static final int PROJECT_SIM_NUM = TelephonyManager.getDefault().getSimCount();
  
  private static final String PROPERTY_MAJOR_SIM = "persist.radio.simswitch";
  
  public static final int UTRAN_DIVISION_DUPLEX_MODE_FDD = 1;
  
  public static final int UTRAN_DIVISION_DUPLEX_MODE_TDD = 2;
  
  public static final int UTRAN_DIVISION_DUPLEX_MODE_UNKNOWN = 0;
  
  private static Phone[] sActivePhones;
  
  private static Context sContext;
  
  private static Phone sDefultPhone;
  
  private static Phone[] sProxyPhones;
  
  static {
    if (SystemProperties.getInt("ro.mtk_world_phone", 0) == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    IS_WORLD_PHONE_SUPPORT = bool1;
    if (SystemProperties.getInt("ro.mtk_lte_support", 0) == 1) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    IS_LTE_SUPPORT = bool1;
    if (SystemProperties.getInt("ro.mtk_md_world_mode_support", 0) == 1) {
      bool1 = bool2;
    } else {
      bool1 = false;
    } 
    IS_WORLD_MODE_SUPPORT = bool1;
    sContext = null;
    sDefultPhone = null;
    sProxyPhones = null;
    sActivePhones = new Phone[PROJECT_SIM_NUM];
  }
  
  public WorldPhoneUtil() {
    logd("Constructor invoked");
    sDefultPhone = PhoneFactory.getDefaultPhone();
    sProxyPhones = PhoneFactory.getPhones();
    for (int i = 0; i < PROJECT_SIM_NUM; i++)
      sActivePhones[i] = ((PhoneProxy)sProxyPhones[i]).getActivePhone(); 
    if (sDefultPhone != null) {
      sContext = sDefultPhone.getContext();
      return;
    } 
    logd("DefaultPhone = null");
  }
  
  public static String denyReasonToString(int paramInt) {
    switch (paramInt) {
      default:
        return "Invalid Reason";
      case 0:
        return "CAMP_ON_NOT_DENIED";
      case 1:
        return "CAMP_ON_DENY_REASON_UNKNOWN";
      case 2:
        return "CAMP_ON_DENY_REASON_NEED_SWITCH_TO_FDD";
      case 3:
        return "CAMP_ON_DENY_REASON_NEED_SWITCH_TO_TDD";
      case 4:
        break;
    } 
    return "CAMP_ON_DENY_REASON_DOMESTIC_FDD_MD";
  }
  
  public static int get3GDivisionDuplexMode() {
    switch (getActiveModemType()) {
      default:
        b = 0;
        logd("get3GDivisionDuplexMode=" + b);
        return b;
      case 1:
      case 3:
      case 5:
      case 7:
        b = 1;
        logd("get3GDivisionDuplexMode=" + b);
        return b;
      case 2:
      case 4:
      case 6:
        break;
    } 
    byte b = 2;
    logd("get3GDivisionDuplexMode=" + b);
    return b;
  }
  
  private static int getActiveModemType() {
    boolean bool = false;
    if (isWorldPhoneSupport() && !isWorldModeSupport()) {
      switch (ModemSwitchHandler.getActiveModemType()) {
        default:
          b = 0;
          logd("getActiveModemType=" + b);
          return b;
        case 3:
          b = 1;
          logd("getActiveModemType=" + b);
          return b;
        case 4:
          b = 2;
          logd("getActiveModemType=" + b);
          return b;
        case 5:
          b = 3;
          logd("getActiveModemType=" + b);
          return b;
        case 6:
          break;
      } 
      byte b = 4;
      logd("getActiveModemType=" + b);
      return b;
    } 
    int i = WorldMode.getWorldMode();
    int j = Integer.valueOf(SystemProperties.get("ril.nw.worldmode.activemode", Integer.toString(0))).intValue();
    logd("[getActiveModemType]: activeMode" + j);
    switch (i) {
      default:
        i = 0;
        logd("getActiveModemType=" + i);
        return i;
      case 8:
      case 16:
        i = 4;
        logd("getActiveModemType=" + i);
        return i;
      case 9:
        i = 3;
        logd("getActiveModemType=" + i);
        return i;
      case 10:
      case 12:
        i = bool;
        if (j > 0) {
          if (j == 1) {
            i = 3;
            logd("getActiveModemType=" + i);
            return i;
          } 
          i = bool;
          if (j == 2)
            i = 4; 
        } 
        logd("getActiveModemType=" + i);
        return i;
      case 11:
      case 15:
        i = 5;
        logd("getActiveModemType=" + i);
        return i;
      case 13:
      case 17:
        i = 6;
        logd("getActiveModemType=" + i);
        return i;
      case 14:
        break;
    } 
    i = 7;
    logd("getActiveModemType=" + i);
    return i;
  }
  
  public static int getMajorSim() {
    if (!ProxyController.getInstance().isCapabilitySwitching()) {
      String str = SystemProperties.get("persist.radio.simswitch", "");
      if (str != null && !str.equals("")) {
        logd("[getMajorSim]: " + (Integer.parseInt(str) - 1));
        return Integer.parseInt(str) - 1;
      } 
      logd("[getMajorSim]: fail to get major SIM");
      return -99;
    } 
    logd("[getMajorSim]: radio capability is switching");
    return -99;
  }
  
  public static int getModemSelectionMode() {
    if (sContext == null) {
      logd("sContext = null");
      return 1;
    } 
    return Settings.Global.getInt(sContext.getContentResolver(), "world_phone_auto_select_mode", 1);
  }
  
  public static int getProjectSimNum() {
    return PROJECT_SIM_NUM;
  }
  
  public static String iccCardTypeToString(int paramInt) {
    switch (paramInt) {
      default:
        return "Invalid Icc Card Type";
      case 1:
        return "SIM";
      case 2:
        return "USIM";
      case 0:
        break;
    } 
    return "Icc Card Type Unknown";
  }
  
  public static boolean isLteSupport() {
    return IS_LTE_SUPPORT;
  }
  
  public static boolean isWorldModeSupport() {
    return IS_WORLD_MODE_SUPPORT;
  }
  
  public static boolean isWorldPhoneSupport() {
    return IS_WORLD_PHONE_SUPPORT;
  }
  
  public static boolean isWorldPhoneSwitching() {
    return isWorldModeSupport() ? WorldMode.isWorldModeSwitching() : false;
  }
  
  private static void logd(String paramString) {
    Rlog.d("PHONE", "[WPP_UTIL]" + paramString);
  }
  
  public static String regStateToString(int paramInt) {
    switch (paramInt) {
      default:
        return "Invalid RegState";
      case 0:
        return "REGISTRATION_STATE_NOT_REGISTERED_AND_NOT_SEARCHING";
      case 1:
        return "REGISTRATION_STATE_HOME_NETWORK";
      case 2:
        return "REGISTRATION_STATE_NOT_REGISTERED_AND_SEARCHING";
      case 3:
        return "REGISTRATION_STATE_REGISTRATION_DENIED";
      case 4:
        return "REGISTRATION_STATE_UNKNOWN";
      case 5:
        return "REGISTRATION_STATE_ROAMING";
      case 10:
        return "REGISTRATION_STATE_NOT_REGISTERED_AND_NOT_SEARCHING_EMERGENCY_CALL_ENABLED";
      case 12:
        return "REGISTRATION_STATE_NOT_REGISTERED_AND_SEARCHING_EMERGENCY_CALL_ENABLED";
      case 13:
        return "REGISTRATION_STATE_REGISTRATION_DENIED_EMERGENCY_CALL_ENABLED";
      case 14:
        break;
    } 
    return "REGISTRATION_STATE_UNKNOWN_EMERGENCY_CALL_ENABLED";
  }
  
  public static String regionToString(int paramInt) {
    switch (paramInt) {
      default:
        return "Invalid Region";
      case 0:
        return "REGION_UNKNOWN";
      case 1:
        return "REGION_DOMESTIC";
      case 2:
        break;
    } 
    return "REGION_FOREIGN";
  }
  
  public static String stateToString(int paramInt) {
    switch (paramInt) {
      default:
        return "Invalid State";
      case 3:
        return "STATE_POWER_OFF";
      case 0:
        return "STATE_IN_SERVICE";
      case 1:
        return "STATE_OUT_OF_SERVICE";
      case 2:
        break;
    } 
    return "STATE_EMERGENCY_ONLY";
  }
  
  public void notifyRadioCapabilityChange(int paramInt) {}
  
  public void setModemSelectionMode(int paramInt1, int paramInt2) {}
  
  static {
    boolean bool1;
    boolean bool2 = true;
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/worldphone/WorldPhoneUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */