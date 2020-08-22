package com.mediatek.internal.telephony.dataconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.util.SparseArray;
import com.android.internal.telephony.PhoneBase;
import java.util.ArrayList;

public class FdManager extends Handler {
  private static final int BASE = 0;
  
  protected static final boolean DBG = true;
  
  private static final int EVENT_FD_MODE_SET = 0;
  
  private static final int EVENT_RADIO_AVAILABLE = 1;
  
  protected static final String LOG_TAG = "FdManager";
  
  protected static final String PROPERTY_3G_SWITCH = "persist.radio.simswitch";
  
  protected static final String PROPERTY_FD_ON_CHARGE = "fd.on.charge";
  
  protected static final String PROPERTY_FD_SCREEN_OFF_ONLY = "fd.screen.off.only";
  
  protected static final String PROPERTY_MTK_FD_SUPPORT = "ro.mtk_fd_support";
  
  protected static final String PROPERTY_RIL_FD_MODE = "ril.fd.mode";
  
  private static final String STR_PROPERTY_FD_SCREEN_OFF_R8_TIMER = "persist.radio.fd.off.r8.counter";
  
  private static final String STR_PROPERTY_FD_SCREEN_OFF_TIMER = "persist.radio.fd.off.counter";
  
  private static final String STR_PROPERTY_FD_SCREEN_ON_R8_TIMER = "persist.radio.fd.r8.counter";
  
  private static final String STR_PROPERTY_FD_SCREEN_ON_TIMER = "persist.radio.fd.counter";
  
  private static final String STR_SCREEN_OFF = "SCREEN_OFF";
  
  private static final String STR_SCREEN_ON = "SCREEN_ON";
  
  private static int numberOfSupportedTypes;
  
  private static final SparseArray<FdManager> sFdManagers = new SparseArray();
  
  private static String[] timerValue = new String[] { "50", "150", "50", "150" };
  
  private boolean mChargingMode = false;
  
  private int mEnableFdOnCharing = 0;
  
  protected BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        String str = param1Intent.getAction();
        FdManager.this.logd("onReceive: action=" + str);
        int i = Integer.parseInt(SystemProperties.get("ril.fd.mode", "0"));
        int j = SystemProperties.getInt("persist.radio.simswitch", 1) - 1;
        if (str.equals("android.intent.action.SCREEN_ON")) {
          FdManager.this.onScreenSwitch(true, i, j);
          return;
        } 
        if (str.equals("android.intent.action.SCREEN_OFF")) {
          FdManager.this.onScreenSwitch(false, i, j);
          return;
        } 
        if (str.equals("android.intent.action.BATTERY_CHANGED")) {
          if (FdManager.isFdSupport()) {
            int k = param1Intent.getIntExtra("status", 0);
            int m = param1Intent.getIntExtra("plugged", 0);
            boolean bool = FdManager.this.mChargingMode;
            str = "";
            if (k == 2) {
              FdManager.access$102(FdManager.this, true);
            } else {
              FdManager.access$102(FdManager.this, false);
            } 
            if (m == 1) {
              str = "Plugged in AC";
            } else if (m == 2) {
              str = "Plugged in USB";
            } 
            if (m == 1 || m == 2)
              FdManager.access$102(FdManager.this, true); 
            m = FdManager.this.mEnableFdOnCharing;
            FdManager.access$202(FdManager.this, Integer.parseInt(SystemProperties.get("fd.on.charge", "0")));
            if (bool != FdManager.this.mChargingMode || m != FdManager.this.mEnableFdOnCharing) {
              FdManager.this.logd("fdMdEnableMode=" + i + ", 3gSimID=" + j + ", when charging state is changed");
              FdManager.this.logd("previousEnableFdOnCharging=" + m + ", mEnableFdOnCharing=" + FdManager.this.mEnableFdOnCharing + ", when charging state is changed");
              FdManager.this.logd("previousChargingMode=" + bool + ", mChargingMode=" + FdManager.this.mChargingMode + ", status=" + k + "(" + str + ")");
            } 
            if (i == 1 && FdManager.getPhoneId(FdManager.this.mPhone) == j && (bool != FdManager.this.mChargingMode || m != FdManager.this.mEnableFdOnCharing)) {
              if (FdManager.this.checkNeedTurnOn()) {
                FdManager.this.updateFdMdEnableStatus(true);
                return;
              } 
              FdManager.this.updateFdMdEnableStatus(false);
              return;
            } 
          } 
          return;
        } 
        if (str.equals("android.net.conn.TETHER_STATE_CHANGED") && FdManager.isFdSupport()) {
          boolean bool;
          FdManager.this.logd("Received ConnectivityManager.ACTION_TETHER_STATE_CHANGED");
          ArrayList arrayList = param1Intent.getStringArrayListExtra("activeArray");
          FdManager fdManager = FdManager.this;
          if (arrayList != null && arrayList.size() > 0) {
            bool = true;
          } else {
            bool = false;
          } 
          FdManager.access$702(fdManager, bool);
          FdManager.this.logd("[TETHER_STATE_CHANGED]mIsTetheredMode = " + FdManager.this.mIsTetheredMode + "mChargingMode=" + FdManager.this.mChargingMode);
          if (FdManager.this.checkNeedTurnOn()) {
            FdManager.this.updateFdMdEnableStatus(true);
            return;
          } 
          FdManager.this.updateFdMdEnableStatus(false);
          return;
        } 
      }
    };
  
  private boolean mIsScreenOn = true;
  
  private boolean mIsTetheredMode = false;
  
  private PhoneBase mPhone;
  
  private FdManager(PhoneBase paramPhoneBase) {
    this.mPhone = paramPhoneBase;
    logd("initial FastDormancyManager");
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("android.intent.action.SCREEN_ON");
    intentFilter.addAction("android.intent.action.SCREEN_OFF");
    intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
    intentFilter.addAction("android.net.conn.TETHER_STATE_CHANGED");
    this.mPhone.getContext().registerReceiver(this.mIntentReceiver, intentFilter, null, (Handler)this.mPhone);
    this.mPhone.mCi.registerForAvailable(this, 1, null);
    initFdTimer();
  }
  
  private boolean checkNeedTurnOn() {
    String str = SystemProperties.get("persist.sys.device.country", "");
    return ((!isFdScreenOffOnly() || !this.mIsScreenOn) && (!this.mChargingMode || this.mEnableFdOnCharing != 0) && !this.mIsTetheredMode && str.equals("ES"));
  }
  
  public static String[] getFdTimerValue() {
    return timerValue;
  }
  
  public static FdManager getInstance(PhoneBase paramPhoneBase) {
    FdManager fdManager = null;
    if (isFdSupport() && paramPhoneBase != null) {
      if (getPhoneId(paramPhoneBase) < 0) {
        Rlog.e("FdManager", "phoneId[" + getPhoneId(paramPhoneBase) + "]is invalid!");
        return fdManager;
      } 
      FdManager fdManager1 = (FdManager)sFdManagers.get(getPhoneId(paramPhoneBase));
      fdManager = fdManager1;
      if (fdManager1 == null) {
        Rlog.d("FdManager", "FDMagager for phoneId:" + getPhoneId(paramPhoneBase) + " doesn't exist, create it");
        fdManager = new FdManager(paramPhoneBase);
        sFdManagers.put(getPhoneId(paramPhoneBase), fdManager);
        return fdManager;
      } 
      return fdManager;
    } 
    Rlog.e("FdManager", "FDMagager can't get phone to init!");
    return null;
  }
  
  private static int getPhoneId(PhoneBase paramPhoneBase) {
    return paramPhoneBase.getPhoneId();
  }
  
  private void initFdTimer() {
    String[] arrayOfString = new String[4];
    arrayOfString[0] = SystemProperties.get("persist.radio.fd.off.counter", "5");
    timerValue[FdTimerType.ScreenOffLegacyFd.ordinal()] = Integer.toString((int)(Double.parseDouble(arrayOfString[0]) * 10.0D));
    arrayOfString[1] = SystemProperties.get("persist.radio.fd.counter", "15");
    timerValue[FdTimerType.ScreenOnLegacyFd.ordinal()] = Integer.toString((int)(Double.parseDouble(arrayOfString[1]) * 10.0D));
    arrayOfString[2] = SystemProperties.get("persist.radio.fd.off.r8.counter", "5");
    timerValue[FdTimerType.ScreenOffR8Fd.ordinal()] = Integer.toString((int)(Double.parseDouble(arrayOfString[2]) * 10.0D));
    arrayOfString[3] = SystemProperties.get("persist.radio.fd.r8.counter", "15");
    timerValue[FdTimerType.ScreenOnR8Fd.ordinal()] = Integer.toString((int)(Double.parseDouble(arrayOfString[3]) * 10.0D));
    logd("Default FD timers=" + timerValue[0] + "," + timerValue[1] + "," + timerValue[2] + "," + timerValue[3]);
  }
  
  public static boolean isFdScreenOffOnly() {
    return (SystemProperties.getInt("fd.screen.off.only", 0) == 1);
  }
  
  public static boolean isFdSupport() {
    return (SystemProperties.getInt("ro.mtk_fd_support", 1) == 1);
  }
  
  private void onScreenSwitch(boolean paramBoolean, int paramInt1, int paramInt2) {
    boolean bool;
    String str;
    this.mIsScreenOn = paramBoolean;
    if (this.mIsScreenOn) {
      str = "SCREEN_ON";
    } else {
      str = "SCREEN_OFF";
    } 
    if (paramBoolean) {
      bool = true;
    } else {
      bool = false;
    } 
    if (isFdSupport()) {
      logd("fdMdEnableMode=" + paramInt1 + ", 3gSimID=" + paramInt2 + ", when switching to " + str);
      if (paramInt1 == 1) {
        if (getPhoneId(this.mPhone) == paramInt2) {
          this.mPhone.mCi.setFDMode(FdModeType.INFO_MD_SCREEN_STATUS.ordinal(), bool, -1, obtainMessage(0));
          if (isFdScreenOffOnly()) {
            if (paramBoolean) {
              logd("Because FD_SCREEN_OFF_ONLY, disable fd when screen on.");
              updateFdMdEnableStatus(false);
              return;
            } 
          } else {
            return;
          } 
        } else {
          return;
        } 
        if (!paramBoolean && checkNeedTurnOn()) {
          logd("Because FD_SCREEN_OFF_ONLY, enable fd when screen off.");
          updateFdMdEnableStatus(true);
          return;
        } 
        return;
      } 
    } else {
      return;
    } 
    logd("Not Support AP-trigger FD now");
  }
  
  private void updateFdMdEnableStatus(boolean paramBoolean) {
    int i = Integer.parseInt(SystemProperties.get("ril.fd.mode", "0"));
    int j = SystemProperties.getInt("persist.radio.simswitch", 1) - 1;
    logd("updateFdMdEnableStatus():enabled=" + paramBoolean + ",fdMdEnableMode=" + i + ", 3gSimID=" + j);
    if (i == 1 && getPhoneId(this.mPhone) == j) {
      if (paramBoolean) {
        this.mPhone.mCi.setFDMode(FdModeType.ENABLE_MD_FD.ordinal(), -1, -1, obtainMessage(0));
        return;
      } 
    } else {
      return;
    } 
    this.mPhone.mCi.setFDMode(FdModeType.DISABLE_MD_FD.ordinal(), -1, -1, obtainMessage(0));
  }
  
  public void disableFdWhenTethering() {
    if (isFdSupport()) {
      ConnectivityManager connectivityManager = (ConnectivityManager)this.mPhone.getContext().getSystemService("connectivity");
      if (connectivityManager != null && connectivityManager.getTetheredIfaces() != null) {
        boolean bool;
        if ((connectivityManager.getTetheredIfaces()).length > 0) {
          bool = true;
        } else {
          bool = false;
        } 
        this.mIsTetheredMode = bool;
      } 
      logd("mIsTetheredMode = " + this.mIsTetheredMode + "mChargingMode=" + this.mChargingMode);
      if (checkNeedTurnOn()) {
        updateFdMdEnableStatus(true);
        return;
      } 
    } else {
      return;
    } 
    updateFdMdEnableStatus(false);
  }
  
  public void dispose() {
    logd("FD.dispose");
    if (isFdSupport()) {
      this.mPhone.getContext().unregisterReceiver(this.mIntentReceiver);
      this.mPhone.mCi.unregisterForAvailable(this);
      sFdManagers.remove(getPhoneId(this.mPhone));
    } 
  }
  
  public int getNumberOfSupportedTypes() {
    return FdTimerType.SupportedTimerTypes.ordinal();
  }
  
  public void handleMessage(Message paramMessage) {
    switch (paramMessage.what) {
      default:
        Rlog.e("FdManager", "Unidentified event msg=" + paramMessage);
        return;
      case 0:
        if (((AsyncResult)paramMessage.obj).exception != null) {
          logd("SET_FD_MODE ERROR");
          return;
        } 
        return;
      case 1:
        break;
    } 
    logd("EVENT_RADIO_AVAILABLE check screen on/off again");
    int i = Integer.parseInt(SystemProperties.get("ril.fd.mode", "0"));
    int j = SystemProperties.getInt("persist.radio.simswitch", 1) - 1;
    if (this.mIsScreenOn) {
      onScreenSwitch(true, i, j);
      return;
    } 
    onScreenSwitch(false, i, j);
  }
  
  protected void logd(String paramString) {
    Rlog.d("FdManager", "[GDCT][phoneId" + getPhoneId(this.mPhone) + "]" + paramString);
  }
  
  public int setFdTimerValue(String[] paramArrayOfString, Message paramMessage) {
    int i = Integer.parseInt(SystemProperties.get("ril.fd.mode", "0"));
    int j = SystemProperties.getInt("persist.radio.simswitch", 1);
    if (isFdSupport() && i == 1 && getPhoneId(this.mPhone) == j - 1) {
      for (i = 0; i < paramArrayOfString.length; i++)
        timerValue[i] = paramArrayOfString[i]; 
      this.mPhone.mCi.setFDMode(FdModeType.SET_FD_INACTIVITY_TIMER.ordinal(), FdTimerType.ScreenOffLegacyFd.ordinal(), Integer.parseInt(timerValue[FdTimerType.ScreenOffLegacyFd.ordinal()]), null);
      this.mPhone.mCi.setFDMode(FdModeType.SET_FD_INACTIVITY_TIMER.ordinal(), FdTimerType.ScreenOnLegacyFd.ordinal(), Integer.parseInt(timerValue[FdTimerType.ScreenOnLegacyFd.ordinal()]), null);
      this.mPhone.mCi.setFDMode(FdModeType.SET_FD_INACTIVITY_TIMER.ordinal(), FdTimerType.ScreenOffR8Fd.ordinal(), Integer.parseInt(timerValue[FdTimerType.ScreenOffR8Fd.ordinal()]), null);
      this.mPhone.mCi.setFDMode(FdModeType.SET_FD_INACTIVITY_TIMER.ordinal(), FdTimerType.ScreenOnR8Fd.ordinal(), Integer.parseInt(timerValue[FdTimerType.ScreenOnR8Fd.ordinal()]), paramMessage);
      logd("Set Default FD timers=" + timerValue[0] + "," + timerValue[1] + "," + timerValue[2] + "," + timerValue[3]);
    } 
    return 0;
  }
  
  public int setFdTimerValue(String[] paramArrayOfString, Message paramMessage, PhoneBase paramPhoneBase) {
    FdManager fdManager = getInstance(paramPhoneBase);
    if (fdManager != null) {
      fdManager.setFdTimerValue(paramArrayOfString, paramMessage);
      return 0;
    } 
    logd("setFDTimerValue fail!");
    return 0;
  }
  
  public enum FdModeType {
    DISABLE_MD_FD, ENABLE_MD_FD, INFO_MD_SCREEN_STATUS, SET_FD_INACTIVITY_TIMER;
    
    static {
      $VALUES = new FdModeType[] { DISABLE_MD_FD, ENABLE_MD_FD, SET_FD_INACTIVITY_TIMER, INFO_MD_SCREEN_STATUS };
    }
  }
  
  public enum FdTimerType {
    ScreenOffLegacyFd, ScreenOffR8Fd, ScreenOnLegacyFd, ScreenOnR8Fd, SupportedTimerTypes;
    
    static {
      $VALUES = new FdTimerType[] { ScreenOffLegacyFd, ScreenOnLegacyFd, ScreenOffR8Fd, ScreenOnR8Fd, SupportedTimerTypes };
    }
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/dataconnection/FdManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */