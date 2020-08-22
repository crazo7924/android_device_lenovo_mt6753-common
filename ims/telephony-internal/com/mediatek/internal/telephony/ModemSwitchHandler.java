package com.mediatek.internal.telephony;

import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneProxy;
import com.mediatek.internal.telephony.worldphone.WorldPhoneUtil;

public class ModemSwitchHandler {
  private static final String LOG_TAG = "PHONE";
  
  public static final int MD_TYPE_FDD = 100;
  
  public static final int MD_TYPE_LTG = 6;
  
  public static final int MD_TYPE_LWG = 5;
  
  public static final int MD_TYPE_TDD = 101;
  
  public static final int MD_TYPE_TG = 4;
  
  public static final int MD_TYPE_UNKNOWN = 0;
  
  public static final int MD_TYPE_WG = 3;
  
  private static final int PROJECT_SIM_NUM = WorldPhoneUtil.getProjectSimNum();
  
  private static Phone[] sActivePhones;
  
  private static CommandsInterface[] sCi;
  
  private static Context sContext;
  
  private static int sCurrentModemType = getActiveModemType();
  
  private static Phone[] sProxyPhones = null;
  
  static {
    sActivePhones = new Phone[PROJECT_SIM_NUM];
    sContext = null;
    sCi = new CommandsInterface[PROJECT_SIM_NUM];
  }
  
  public ModemSwitchHandler() {
    logd("Constructor invoked");
    logd("Init modem type: " + sCurrentModemType);
    sProxyPhones = PhoneFactory.getPhones();
    for (int i = 0; i < PROJECT_SIM_NUM; i++) {
      sActivePhones[i] = ((PhoneProxy)sProxyPhones[i]).getActivePhone();
      sCi[i] = ((PhoneBase)sActivePhones[i]).mCi;
    } 
    if (PhoneFactory.getDefaultPhone() != null) {
      sContext = PhoneFactory.getDefaultPhone().getContext();
      return;
    } 
    logd("DefaultPhone = null");
  }
  
  public static int getActiveModemType() {
    sCurrentModemType = Integer.valueOf(SystemProperties.get("ril.active.md", Integer.toString(0))).intValue();
    return sCurrentModemType;
  }
  
  private static void logd(String paramString) {
    Rlog.d("PHONE", "[MSH]" + paramString);
  }
  
  public static String modemToString(int paramInt) {
    return (paramInt == 3) ? "WG" : ((paramInt == 4) ? "TG" : ((paramInt == 5) ? "FDD CSFB" : ((paramInt == 6) ? "TDD CSFB" : ((paramInt == 0) ? "UNKNOWN" : "Invalid modem type"))));
  }
  
  public static void reloadModem(int paramInt) {
    int i = WorldPhoneUtil.getMajorSim();
    if (i >= 0 && i <= 3) {
      reloadModem(sCi[i], paramInt);
      return;
    } 
    logd("Invalid MajorSIM id" + i);
  }
  
  public static void reloadModem(CommandsInterface paramCommandsInterface, int paramInt) {
    logd("[reloadModem]");
    if (paramCommandsInterface.getRadioState() == CommandsInterface.RadioState.RADIO_UNAVAILABLE) {
      logd("Radio unavailable, can not reload modem");
      return;
    } 
    if (paramInt == 3) {
      paramCommandsInterface.setTrm(14, null);
      return;
    } 
    if (paramInt == 4) {
      paramCommandsInterface.setTrm(15, null);
      return;
    } 
    if (paramInt == 5) {
      paramCommandsInterface.setTrm(16, null);
      return;
    } 
    if (paramInt == 6) {
      paramCommandsInterface.setTrm(17, null);
      return;
    } 
    logd("Invalid modem type:" + paramInt);
  }
  
  public static void setActiveModemType(int paramInt) {
    SystemProperties.set("ril.active.md", Integer.toString(paramInt));
    sCurrentModemType = paramInt;
    logd("[setActiveModemType] " + modemToString(sCurrentModemType));
  }
  
  public static void switchModem(int paramInt) {
    int i = WorldPhoneUtil.getMajorSim();
    logd("protocolSim: " + i);
    if (i >= 0 && i <= 3) {
      switchModem(sCi[i], paramInt);
      return;
    } 
    switchModem(null, paramInt);
  }
  
  public static void switchModem(CommandsInterface paramCommandsInterface, int paramInt) {
    logd("[switchModem]");
    if (paramInt == sCurrentModemType) {
      if (paramInt == 3) {
        logd("Already in WG modem");
        return;
      } 
      if (paramInt == 4) {
        logd("Already in TG modem");
        return;
      } 
      if (paramInt == 5) {
        logd("Already in FDD CSFB modem");
        return;
      } 
      if (paramInt == 6) {
        logd("Already in TDD CSFB modem");
        return;
      } 
      return;
    } 
    if (paramCommandsInterface.getRadioState() == CommandsInterface.RadioState.RADIO_UNAVAILABLE) {
      logd("Radio unavailable, can not switch modem");
      return;
    } 
    if (paramInt == 3) {
      paramCommandsInterface.setTrm(9, null);
    } else if (paramInt == 4) {
      paramCommandsInterface.setTrm(10, null);
    } else if (paramInt == 5) {
      paramCommandsInterface.setTrm(11, null);
    } else if (paramInt == 6) {
      paramCommandsInterface.setTrm(12, null);
    } else {
      logd("Invalid modem type:" + paramInt);
      return;
    } 
    setActiveModemType(paramInt);
    logd("Broadcast intent ACTION_MD_TYPE_CHANGE");
    Intent intent = new Intent("android.intent.action.ACTION_MD_TYPE_CHANGE");
    intent.putExtra("mdType", paramInt);
    sContext.sendBroadcast(intent);
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/ModemSwitchHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */