package com.mediatek.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneProxy;
import com.mediatek.internal.telephony.worldphone.WorldPhoneUtil;
import java.util.concurrent.atomic.AtomicBoolean;

public class AirplaneRequestHandler extends Handler {
  private static final int EVENT_GSM_RADIO_CHANGE_FOR_AVALIABLE = 101;
  
  private static final int EVENT_GSM_RADIO_CHANGE_FOR_OFF = 100;
  
  private static final int EVENT_WAIT_RADIO_CHANGE_FOR_AVALIABLE = 102;
  
  private static final String EXTRA_AIRPLANE_MODE = "airplaneMode";
  
  private static final String INTENT_ACTION_AIRPLANE_CHANGE_DONE = "com.mediatek.intent.action.AIRPLANE_CHANGE_DONE";
  
  private static final String LOG_TAG = "AirplaneRequestHandler";
  
  private static AtomicBoolean sInSwitching = new AtomicBoolean(false);
  
  private Context mContext;
  
  private boolean mForceSwitch;
  
  private boolean mHasRegisterWorldModeReceiver = false;
  
  private boolean mNeedIgnoreMessageForChangeDone;
  
  private boolean mNeedIgnoreMessageForWait;
  
  private Boolean mPendingAirplaneModeRequest;
  
  private int mPhoneCount;
  
  private BroadcastReceiver mWorldModeReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        String str = param1Intent.getAction();
        AirplaneRequestHandler.log("mWorldModeReceiver: action = " + str);
        if ("android.intent.action.ACTION_WORLD_MODE_CHANGED".equals(str)) {
          int i = param1Intent.getIntExtra("worldModeState", -1);
          AirplaneRequestHandler.log("mWorldModeReceiver: wmState = " + i);
          if (AirplaneRequestHandler.this.mHasRegisterWorldModeReceiver && i == 1) {
            AirplaneRequestHandler.this.unRegisterWorldModeReceiver();
            AirplaneRequestHandler.sInSwitching.set(false);
            AirplaneRequestHandler.this.checkPendingRequest();
          } 
        } 
      }
    };
  
  public AirplaneRequestHandler(Context paramContext, int paramInt) {
    this.mContext = paramContext;
    this.mPhoneCount = paramInt;
  }
  
  private void checkPendingRequest() {
    log("checkPendingRequest, mPendingAirplaneModeRequest = " + this.mPendingAirplaneModeRequest);
    if (this.mPendingAirplaneModeRequest != null) {
      Boolean bool = this.mPendingAirplaneModeRequest;
      this.mPendingAirplaneModeRequest = null;
      RadioManager.getInstance().notifyAirplaneModeChange(bool.booleanValue());
    } 
  }
  
  private static final boolean isCdmaLteDcSupport() {
    return (SystemProperties.get("ro.mtk_svlte_support").equals("1") || SystemProperties.get("ro.mtk_srlte_support").equals("1"));
  }
  
  private boolean isRadioAvaliable() {
    boolean bool = true;
    for (int i = 0;; i++) {
      boolean bool1 = bool;
      if (i < this.mPhoneCount) {
        if (!isRadioAvaliable(i)) {
          log("isRadioAvaliable=false, phoneId = " + i);
          return false;
        } 
      } else {
        return bool1;
      } 
    } 
  }
  
  private boolean isRadioAvaliable(int paramInt) {
    log("phoneId = " + paramInt);
    return (((PhoneBase)((PhoneProxy)PhoneFactory.getPhone(paramInt)).getActivePhone()).mCi.getRadioState() != CommandsInterface.RadioState.RADIO_UNAVAILABLE);
  }
  
  private boolean isRadioOff(int paramInt) {
    log("phoneId = " + paramInt);
    return (((PhoneBase)((PhoneProxy)PhoneFactory.getPhone(paramInt)).getActivePhone()).mCi.getRadioState() == CommandsInterface.RadioState.RADIO_OFF);
  }
  
  private boolean isWifiOnly() {
    boolean bool = false;
    if (!((ConnectivityManager)this.mContext.getSystemService("connectivity")).isNetworkSupported(0))
      bool = true; 
    return bool;
  }
  
  private static void log(String paramString) {
    Rlog.d("AirplaneRequestHandler", "[RadioManager] " + paramString);
  }
  
  private void registerWorldModeReceiver() {
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("android.intent.action.ACTION_WORLD_MODE_CHANGED");
    this.mContext.registerReceiver(this.mWorldModeReceiver, intentFilter);
    this.mHasRegisterWorldModeReceiver = true;
  }
  
  private void unRegisterWorldModeReceiver() {
    this.mContext.unregisterReceiver(this.mWorldModeReceiver);
    this.mHasRegisterWorldModeReceiver = false;
  }
  
  private void unWaitRadioAvaliable() {
    this.mNeedIgnoreMessageForWait = true;
    for (int i = 0; i < this.mPhoneCount; i++) {
      ((PhoneBase)((PhoneProxy)PhoneFactory.getPhone(i)).getActivePhone()).mCi.unregisterForRadioStateChanged(this);
      log("unWaitRadioAvaliable, for gsm phone,  phoneId = " + i);
    } 
  }
  
  private boolean waitRadioAvaliable(boolean paramBoolean) {
    boolean bool;
    if (isCdmaLteDcSupport() && !isWifiOnly() && !isRadioAvaliable()) {
      bool = true;
    } else {
      bool = false;
    } 
    log("waitRadioAvaliable, enabled=" + paramBoolean + ", wait=" + bool);
    if (bool) {
      pendingAirplaneModeRequest(paramBoolean);
      this.mNeedIgnoreMessageForWait = false;
      sInSwitching.set(true);
      for (int i = 0; i < this.mPhoneCount; i++)
        ((PhoneBase)((PhoneProxy)PhoneFactory.getPhone(i)).getActivePhone()).mCi.registerForRadioStateChanged(this, 102, null); 
    } 
    return bool;
  }
  
  private boolean waitWorlModeSwitching(boolean paramBoolean) {
    boolean bool;
    if (isCdmaLteDcSupport() && !isWifiOnly() && WorldPhoneUtil.isWorldPhoneSwitching()) {
      bool = true;
    } else {
      bool = false;
    } 
    log("waitWorlModeSwitching, enabled=" + paramBoolean + ", wait=" + bool);
    if (bool) {
      pendingAirplaneModeRequest(paramBoolean);
      sInSwitching.set(true);
      if (!this.mHasRegisterWorldModeReceiver)
        registerWorldModeReceiver(); 
    } 
    return bool;
  }
  
  protected boolean allowSwitching() {
    return !(sInSwitching.get() && !this.mForceSwitch);
  }
  
  public void handleMessage(Message paramMessage) {
    boolean bool = isWifiOnly();
    switch (paramMessage.what) {
      default:
        return;
      case 100:
        if (!this.mNeedIgnoreMessageForChangeDone) {
          if (paramMessage.what == 100)
            log("handle EVENT_GSM_RADIO_CHANGE_FOR_OFF"); 
          int i = 0;
          while (true) {
            if (i < this.mPhoneCount)
              if (bool) {
                log("wifi-only, don't judge radio off");
              } else {
                if (!isRadioOff(i)) {
                  log("radio state change, radio not off, phoneId = " + i);
                  return;
                } 
                i++;
                continue;
              }  
            log("All radio off");
            sInSwitching.set(false);
            unMonitorAirplaneChangeDone(true);
            checkPendingRequest();
            return;
          } 
        } 
      case 101:
        if (!this.mNeedIgnoreMessageForChangeDone) {
          if (paramMessage.what == 101)
            log("handle EVENT_GSM_RADIO_CHANGE_FOR_AVALIABLE"); 
          int i = 0;
          while (true) {
            if (i < this.mPhoneCount)
              if (bool) {
                log("wifi-only, don't judge radio avaliable");
              } else {
                if (!isRadioAvaliable(i)) {
                  log("radio state change, radio not avaliable, phoneId = " + i);
                  return;
                } 
                i++;
                continue;
              }  
            log("All radio avaliable");
            sInSwitching.set(false);
            unMonitorAirplaneChangeDone(false);
            checkPendingRequest();
            return;
          } 
        } 
      case 102:
        break;
    } 
    if (!this.mNeedIgnoreMessageForWait) {
      log("handle EVENT_WAIT_RADIO_CHANGE_FOR_AVALIABLE");
      if (isRadioAvaliable()) {
        log("All radio avaliable");
        unWaitRadioAvaliable();
        sInSwitching.set(false);
        checkPendingRequest();
        return;
      } 
    } 
  }
  
  protected void monitorAirplaneChangeDone(boolean paramBoolean) {
    this.mNeedIgnoreMessageForChangeDone = false;
    log("monitorAirplaneChangeDone, power = " + paramBoolean + " mNeedIgnoreMessageForChangeDone = " + this.mNeedIgnoreMessageForChangeDone);
    sInSwitching.set(true);
    for (int i = 0; i < this.mPhoneCount; i++) {
      if (paramBoolean) {
        ((PhoneBase)((PhoneProxy)PhoneFactory.getPhone(i)).getActivePhone()).mCi.registerForRadioStateChanged(this, 101, null);
      } else {
        ((PhoneBase)((PhoneProxy)PhoneFactory.getPhone(i)).getActivePhone()).mCi.registerForRadioStateChanged(this, 100, null);
      } 
    } 
  }
  
  protected void pendingAirplaneModeRequest(boolean paramBoolean) {
    log("pendingAirplaneModeRequest, enabled = " + paramBoolean);
    this.mPendingAirplaneModeRequest = new Boolean(paramBoolean);
  }
  
  public void setForceSwitch(boolean paramBoolean) {
    this.mForceSwitch = paramBoolean;
    log("setForceSwitch, forceSwitch =" + paramBoolean);
  }
  
  protected void unMonitorAirplaneChangeDone(boolean paramBoolean) {
    this.mNeedIgnoreMessageForChangeDone = true;
    Intent intent = new Intent("com.mediatek.intent.action.AIRPLANE_CHANGE_DONE");
    intent.putExtra("airplaneMode", paramBoolean);
    this.mContext.sendBroadcastAsUser(intent, UserHandle.ALL);
    for (int i = 0; i < this.mPhoneCount; i++) {
      ((PhoneBase)((PhoneProxy)PhoneFactory.getPhone(i)).getActivePhone()).mCi.unregisterForRadioStateChanged(this);
      log("unMonitorAirplaneChangeDone, for gsm phone,  phoneId = " + i);
    } 
  }
  
  protected boolean waitForReady(boolean paramBoolean) {
    if (waitRadioAvaliable(paramBoolean)) {
      log("waitForReady, wait radio avaliable");
      return true;
    } 
    if (waitWorlModeSwitching(paramBoolean)) {
      log("waitForReady, wait world mode switching");
      return true;
    } 
    return false;
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/AirplaneRequestHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */