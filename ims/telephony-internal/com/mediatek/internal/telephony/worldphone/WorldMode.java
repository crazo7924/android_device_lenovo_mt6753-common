package com.mediatek.internal.telephony.worldphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneProxy;

public class WorldMode extends Handler {
  static final String ACTION_ADB_SWITCH_WORLD_MODE = "android.intent.action.ACTION_ADB_SWITCH_WORLD_MODE";
  
  static final int EVENT_RADIO_ON_1 = 1;
  
  static final int EVENT_RADIO_ON_2 = 2;
  
  static final String EXTRA_WORLDMODE = "worldMode";
  
  private static final String LOG_TAG = "PHONE";
  
  public static final int MD_WM_CHANGED_END = 1;
  
  public static final int MD_WM_CHANGED_START = 0;
  
  public static final int MD_WM_CHANGED_UNKNOWN = -1;
  
  public static final int MD_WORLD_MODE_LCTG = 16;
  
  public static final int MD_WORLD_MODE_LFWCG = 15;
  
  public static final int MD_WORLD_MODE_LFWG = 14;
  
  public static final int MD_WORLD_MODE_LTCTG = 17;
  
  public static final int MD_WORLD_MODE_LTG = 8;
  
  public static final int MD_WORLD_MODE_LTTG = 13;
  
  public static final int MD_WORLD_MODE_LWCG = 11;
  
  public static final int MD_WORLD_MODE_LWCTG = 12;
  
  public static final int MD_WORLD_MODE_LWG = 9;
  
  public static final int MD_WORLD_MODE_LWTG = 10;
  
  public static final int MD_WORLD_MODE_UNKNOWN = 0;
  
  private static final int PROJECT_SIM_NUM = WorldPhoneUtil.getProjectSimNum();
  
  private static Phone[] sActivePhones;
  
  private static int sActiveWorldMode;
  
  private static CommandsInterface[] sCi;
  
  private static Context sContext;
  
  private static int sCurrentWorldMode = updateCurrentWorldMode();
  
  private static WorldMode sInstance;
  
  private static Phone[] sProxyPhones;
  
  private static boolean sSwitchingState;
  
  private final BroadcastReceiver mWorldModeReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        WorldMode.logd("[Receiver]+");
        String str = param1Intent.getAction();
        WorldMode.logd("Action: " + str);
        if ("android.intent.action.ACTION_WORLD_MODE_CHANGED".equals(str)) {
          int i = param1Intent.getIntExtra("worldModeState", -1);
          WorldMode.logd("wmState: " + i);
          if (i == 1)
            WorldMode.access$102(WorldMode.updateCurrentWorldMode()); 
        } else if ("android.intent.action.ACTION_ADB_SWITCH_WORLD_MODE".equals(str)) {
          int i = param1Intent.getIntExtra("worldMode", 0);
          WorldMode.logd("toModem: " + i);
          if (i == 8 || i == 9 || i == 10 || i == 13)
            WorldMode.setWorldMode(i); 
        } 
        WorldMode.logd("[Receiver]-");
      }
    };
  
  static {
    sActiveWorldMode = 0;
    sSwitchingState = false;
    sProxyPhones = null;
    sActivePhones = new Phone[PROJECT_SIM_NUM];
    sContext = null;
    sCi = new CommandsInterface[PROJECT_SIM_NUM];
  }
  
  public WorldMode() {
    logd("Constructor invoked");
    logd("Init world mode: " + sCurrentWorldMode);
    sProxyPhones = PhoneFactory.getPhones();
    for (int i = 0; i < PROJECT_SIM_NUM; i++) {
      sActivePhones[i] = ((PhoneProxy)sProxyPhones[i]).getActivePhone();
      sCi[i] = ((PhoneBase)sActivePhones[i]).mCi;
      sCi[i].registerForOn(this, i + 1, null);
    } 
    IntentFilter intentFilter = new IntentFilter("android.intent.action.ACTION_WORLD_MODE_CHANGED");
    intentFilter.addAction("android.intent.action.ACTION_ADB_SWITCH_WORLD_MODE");
    if (PhoneFactory.getDefaultPhone() != null) {
      sContext = PhoneFactory.getDefaultPhone().getContext();
    } else {
      logd("DefaultPhone = null");
    } 
    sContext.registerReceiver(this.mWorldModeReceiver, intentFilter);
  }
  
  public static String WorldModeToString(int paramInt) {
    return (paramInt == 8) ? "uTLG" : ((paramInt == 9) ? "uLWG" : ((paramInt == 10) ? "uLWTG" : ((paramInt == 11) ? "uLWCG" : ((paramInt == 12) ? "uLWTCG" : ((paramInt == 13) ? "LtTG" : ((paramInt == 14) ? "LfWG" : ((paramInt == 15) ? "uLfWCG" : ((paramInt == 16) ? "uLCTG" : ((paramInt == 17) ? "uLtCTG" : "Invalid world mode")))))))));
  }
  
  public static int getWorldMode() {
    logd("getWorldMode=" + WorldModeToString(sCurrentWorldMode));
    return sCurrentWorldMode;
  }
  
  public static void init() {
    // Byte code:
    //   0: ldc com/mediatek/internal/telephony/worldphone/WorldMode
    //   2: monitorenter
    //   3: getstatic com/mediatek/internal/telephony/worldphone/WorldMode.sInstance : Lcom/mediatek/internal/telephony/worldphone/WorldMode;
    //   6: ifnonnull -> 23
    //   9: new com/mediatek/internal/telephony/worldphone/WorldMode
    //   12: dup
    //   13: invokespecial <init> : ()V
    //   16: putstatic com/mediatek/internal/telephony/worldphone/WorldMode.sInstance : Lcom/mediatek/internal/telephony/worldphone/WorldMode;
    //   19: ldc com/mediatek/internal/telephony/worldphone/WorldMode
    //   21: monitorexit
    //   22: return
    //   23: new java/lang/StringBuilder
    //   26: dup
    //   27: invokespecial <init> : ()V
    //   30: ldc 'init() called multiple times!  sInstance = '
    //   32: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   35: getstatic com/mediatek/internal/telephony/worldphone/WorldMode.sInstance : Lcom/mediatek/internal/telephony/worldphone/WorldMode;
    //   38: invokevirtual append : (Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   41: invokevirtual toString : ()Ljava/lang/String;
    //   44: invokestatic logd : (Ljava/lang/String;)V
    //   47: goto -> 19
    //   50: astore_0
    //   51: ldc com/mediatek/internal/telephony/worldphone/WorldMode
    //   53: monitorexit
    //   54: aload_0
    //   55: athrow
    // Exception table:
    //   from	to	target	type
    //   3	19	50	finally
    //   19	22	50	finally
    //   23	47	50	finally
    //   51	54	50	finally
  }
  
  public static boolean isWorldModeSwitching() {
    return sSwitchingState;
  }
  
  private static void logd(String paramString) {
    Rlog.d("PHONE", "[WorldMode]" + paramString);
  }
  
  public static void setWorldMode(int paramInt) {
    int i = WorldPhoneUtil.getMajorSim();
    logd("[setWorldMode]protocolSim: " + i);
    if (i >= 0 && i <= 3) {
      setWorldMode(sCi[i], paramInt);
      return;
    } 
    setWorldMode(sCi[0], paramInt);
  }
  
  private static void setWorldMode(CommandsInterface paramCommandsInterface, int paramInt) {
    logd("[setWorldMode] worldMode=" + paramInt);
    if (paramInt == sCurrentWorldMode) {
      if (paramInt == 8) {
        logd("Already in uTLG mode");
        return;
      } 
      if (paramInt == 9) {
        logd("Already in uLWG mode");
        return;
      } 
      if (paramInt == 10) {
        logd("Already in uLWTG mode");
        return;
      } 
      if (paramInt == 11) {
        logd("Already in uLWCG mode");
        return;
      } 
      if (paramInt == 12) {
        logd("Already in uLWTCG mode");
        return;
      } 
      if (paramInt == 13) {
        logd("Already in LtTG mode");
        return;
      } 
      if (paramInt == 14) {
        logd("Already in LfWG mode");
        return;
      } 
      if (paramInt == 15) {
        logd("Already in uLfWCG mode");
        return;
      } 
      if (paramInt == 16) {
        logd("Already in uLCTG mode");
        return;
      } 
      if (paramInt == 17) {
        logd("Already in uLtCTG mode");
        return;
      } 
      return;
    } 
    if (paramCommandsInterface.getRadioState() == CommandsInterface.RadioState.RADIO_UNAVAILABLE) {
      logd("Radio unavailable, can not switch world mode");
      return;
    } 
    if (paramInt >= 8 && paramInt <= 17) {
      paramCommandsInterface.reloadModemType(paramInt, null);
      paramCommandsInterface.storeModemType(paramInt, null);
      paramCommandsInterface.setTrm(2, null);
      return;
    } 
    logd("Invalid world mode:" + paramInt);
  }
  
  private static int updateCurrentWorldMode() {
    sCurrentWorldMode = Integer.valueOf(SystemProperties.get("ril.active.md", Integer.toString(0))).intValue();
    logd("updateCurrentWorldMode=" + WorldModeToString(sCurrentWorldMode));
    return sCurrentWorldMode;
  }
  
  public static void updateSwitchingState(boolean paramBoolean) {
    sSwitchingState = paramBoolean;
    logd("updateSwitchingState=" + sSwitchingState);
  }
  
  public void handleMessage(Message paramMessage) {
    AsyncResult asyncResult = (AsyncResult)paramMessage.obj;
    WorldPhoneUtil.getMajorSim();
    switch (paramMessage.what) {
      default:
        logd("Unknown msg:" + paramMessage.what);
        return;
      case 1:
        logd("handleMessage : <EVENT_RADIO_ON_1>");
        if (WorldPhoneUtil.getMajorSim() == 0) {
          sCurrentWorldMode = updateCurrentWorldMode();
          return;
        } 
        return;
      case 2:
        break;
    } 
    logd("handleMessage : <EVENT_RADIO_ON_2>");
    if (WorldPhoneUtil.getMajorSim() == 1) {
      sCurrentWorldMode = updateCurrentWorldMode();
      return;
    } 
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/worldphone/WorldMode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */