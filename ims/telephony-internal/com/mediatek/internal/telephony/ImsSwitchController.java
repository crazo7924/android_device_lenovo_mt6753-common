package com.mediatek.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.Rlog;
import com.android.ims.internal.IImsService;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.dataconnection.DctController;

public class ImsSwitchController extends Handler {
  static final int DEFAULT_IMS_DEREGISTER_TIMEOUT = 30000;
  
  static final int DEFAULT_IMS_STATE = 0;
  
  static final int DEFAULT_INVALID_PHONE_ID = -1;
  
  static final int DEFAULT_MAJOR_CAPABILITY_PHONE_ID = 0;
  
  static final int DISABLE_WIFI_FLIGHTMODE = 1;
  
  protected static final int EVENT_CONNECTIVITY_CHANGE = 6;
  
  protected static final int EVENT_DC_SWITCH_STATE_CHANGE = 5;
  
  protected static final int EVENT_IMS_DEREGISTER_TIMEOUT = 7;
  
  protected static final int EVENT_RADIO_AVAILABLE_PHONE1 = 2;
  
  protected static final int EVENT_RADIO_AVAILABLE_PHONE2 = 4;
  
  protected static final int EVENT_RADIO_NOT_AVAILABLE_PHONE1 = 1;
  
  protected static final int EVENT_RADIO_NOT_AVAILABLE_PHONE2 = 3;
  
  public static final String IMS_SERVICE = "ims";
  
  static final String LOG_TAG = "ImsSwitchController";
  
  public static final String NW_SUB_TYPE_IMS = "IMS";
  
  public static final String NW_TYPE_WIFI = "MOBILE_IMS";
  
  private static final String PROPERTY_IMS_VIDEO_ENALBE = "persist.mtk.ims.video.enable";
  
  private static final String PROPERTY_VOLTE_ENALBE = "persist.mtk.volte.enable";
  
  private static final String PROPERTY_WFC_ENALBE = "persist.mtk.wfc.enable";
  
  private static IImsService mImsService = null;
  
  private int REASON_INVALID = -1;
  
  private CommandsInterface[] mCi;
  
  private Context mContext;
  
  private DctController.DcStateParam mDcStateParam = null;
  
  private ImsServiceDeathRecipient mDeathRecipient = new ImsServiceDeathRecipient();
  
  private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        if (param1Intent != null) {
          String str = param1Intent.getAction();
          ImsSwitchController.log("mIntentReceiver Receive action " + str);
          if (str.equals("com.android.ims.IMS_SERVICE_DOWN") || str.equals("com.android.ims.IMS_SERVICE_DEREGISTERED")) {
            ImsSwitchController.this.removeMessages(7);
            ImsSwitchController.this.confirmPreCheckDetachIfNeed();
            ImsSwitchController.access$502(ImsSwitchController.this, false);
            return;
          } 
          if (str.equals("android.intent.action.PHONE_STATE") || str.equals("android.intent.action.SUBSCRIPTION_PHONE_STATE")) {
            str = param1Intent.getStringExtra("state");
            ImsSwitchController.this.onReceivePhoneStateChange(param1Intent.getIntExtra("phone", -1), param1Intent.getIntExtra("phoneType", 0), Enum.<PhoneConstants.State>valueOf(PhoneConstants.State.class, str));
            return;
          } 
        } 
      }
    };
  
  private boolean mIsInVoLteCall = false;
  
  protected final Object mLock = new Object();
  
  private boolean mNeedTurnOffWifi = false;
  
  private int mPhoneCount;
  
  private RadioPowerInterface mRadioPowerIf;
  
  private int mReason = this.REASON_INVALID;
  
  ImsSwitchController(Context paramContext, int paramInt, CommandsInterface[] paramArrayOfCommandsInterface) {
    log("Initialize ImsSwitchController");
    this.mContext = paramContext;
    this.mCi = paramArrayOfCommandsInterface;
    this.mPhoneCount = paramInt;
    if (SystemProperties.get("ro.mtk_ims_support").equals("1") && !SystemProperties.get("ro.mtk_tc1_feature").equals("1")) {
      IntentFilter intentFilter = new IntentFilter("com.android.ims.IMS_SERVICE_DOWN");
      intentFilter.addAction("com.android.ims.IMS_SERVICE_DEREGISTERED");
      intentFilter.addAction("android.intent.action.PHONE_STATE");
      intentFilter.addAction("android.intent.action.SUBSCRIPTION_PHONE_STATE");
      intentFilter.addAction("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE");
      if (SystemProperties.get("ro.mtk_wfc_support").equals("1")) {
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
      } 
      this.mContext.registerReceiver(this.mIntentReceiver, intentFilter);
      this.mRadioPowerIf = new RadioPowerInterface();
      RadioManager.registerForRadioPowerChange("ImsSwitchController", this.mRadioPowerIf);
      this.mCi[0].registerForNotAvailable(this, 1, null);
      this.mCi[0].registerForAvailable(this, 2, null);
      if (this.mPhoneCount > 1) {
        this.mCi[1].registerForNotAvailable(this, 3, null);
        this.mCi[1].registerForAvailable(this, 4, null);
      } 
    } 
  }
  
  private void checkAndBindImsService(int paramInt) {
    IBinder iBinder = ServiceManager.getService("ims");
    if (iBinder != null)
      try {
        iBinder.linkToDeath(this.mDeathRecipient, 0);
      } catch (RemoteException remoteException) {} 
    mImsService = IImsService.Stub.asInterface(iBinder);
    log("checkAndBindImsService: mImsService = " + mImsService);
  }
  
  private void confirmPreCheckDetachIfNeed() {
    synchronized (this.mLock) {
      if (this.mDcStateParam != null) {
        log("confirmPreCheckDetachIfNeed, phoneId:" + this.mDcStateParam.getPhoneId());
        this.mDcStateParam.confirmPreCheckDetach();
        this.mDcStateParam = null;
      } 
      return;
    } 
  }
  
  private void deregisterIms(int paramInt) {
    try {
      mImsService.deregisterIms(paramInt);
      removeMessages(7);
      sendMessageDelayed(obtainMessage(7), 30000L);
      return;
    } catch (RemoteException remoteException) {
      log("RemoteException can't deregister ims");
      return;
    } 
  }
  
  private String eventIdtoString(int paramInt) {
    switch (paramInt) {
      default:
        return null;
      case 1:
        return "RADIO_NOT_AVAILABLE_PHONE1";
      case 3:
        return "RADIO_NOT_AVAILABLE_PHONE2";
      case 2:
        return "RADIO_AVAILABLE_PHONE1";
      case 4:
        return "RADIO_AVAILABLE_PHONE2";
      case 5:
        return "DC_SWITCH_STATE_CHANGE";
      case 7:
        break;
    } 
    return "EVENT_IMS_DEREGISTER_TIMEOUT";
  }
  
  private void handleDcStateAttaching(DctController.DcStateParam paramDcStateParam) {
    synchronized (this.mLock) {
      log("handleDcStateAttaching param.getPhoneId():" + paramDcStateParam.getPhoneId());
      return;
    } 
  }
  
  private void handleDcStatePreCheckDisconnect(DctController.DcStateParam paramDcStateParam) {
    synchronized (this.mLock) {
      int i;
      if (this.mIsInVoLteCall == true) {
        log("handleDcStatePreCheckDisconnect, in volte call, suspend DcState preCheck");
        this.mDcStateParam = paramDcStateParam;
        return;
      } 
      log("handleDcStatePreCheckDisconnect, param.getPhoneId():" + paramDcStateParam.getPhoneId());
      if (paramDcStateParam.getPhoneId() == RadioCapabilitySwitchUtil.getMainCapabilityPhoneId()) {
        if (mImsService == null)
          checkAndBindImsService(paramDcStateParam.getPhoneId()); 
        IImsService iImsService = mImsService;
        if (iImsService != null) {
          i = 0;
          try {
            int j = mImsService.getImsState();
            i = j;
          } catch (RemoteException remoteException) {}
        } else {
          log("handleDcStatePreCheckDisconnect: ImsService not ready !!!");
          this.mDcStateParam = paramDcStateParam;
        } 
      } else {
        paramDcStateParam.confirmPreCheckDetach();
      } 
      log("ims state=" + i);
      if (i != 0) {
        if (i != 3)
          deregisterIms(paramDcStateParam.getPhoneId()); 
        this.mDcStateParam = paramDcStateParam;
      } else {
        log("handleDcStatePreCheckDisconnect: ims is disable and confirm directly");
        paramDcStateParam.confirmPreCheckDetach();
      } 
    } 
  }
  
  private static void log(String paramString) {
    Rlog.d("ImsSwitchController", paramString);
  }
  
  private void onReceiveDcSwitchStateChange(DctController.DcStateParam paramDcStateParam) {
    log("handleMessage param.getState: " + paramDcStateParam.getState() + " param.getReason(): " + paramDcStateParam.getReason());
    String str = paramDcStateParam.getState();
    byte b = -1;
    switch (str.hashCode()) {
      default:
        switch (b) {
          default:
            return;
          case 0:
            handleDcStatePreCheckDisconnect(paramDcStateParam);
            return;
          case 1:
            break;
        } 
        break;
      case 1379920594:
        if (str.equals("predetachcheck"))
          b = 0; 
      case -478984323:
        if (str.equals("attaching"))
          b = 1; 
    } 
    if (!paramDcStateParam.getReason().equals("Lost Connection")) {
      handleDcStateAttaching(paramDcStateParam);
      return;
    } 
  }
  
  private void onReceivePhoneStateChange(int paramInt1, int paramInt2, PhoneConstants.State paramState) {
    synchronized (this.mLock) {
      log("onReceivePhoneStateChange phoneId:" + paramInt1 + " phoneType: " + paramInt2 + " phoneState: " + paramState);
      log("mIsInVoLteCall: " + this.mIsInVoLteCall);
      if (this.mIsInVoLteCall == true) {
        if (paramInt1 == RadioCapabilitySwitchUtil.getMainCapabilityPhoneId() && paramState == PhoneConstants.State.IDLE) {
          this.mIsInVoLteCall = false;
          if (this.mDcStateParam != null) {
            if (mImsService == null)
              checkAndBindImsService(paramInt1); 
            if (mImsService != null) {
              deregisterIms(this.mDcStateParam.getPhoneId());
            } else {
              log("onReceivePhoneStateChange: ImsService not ready !!!");
            } 
          } 
        } 
      } else if (paramInt2 == 5 && paramState != PhoneConstants.State.IDLE) {
        this.mIsInVoLteCall = true;
      } 
      return;
    } 
  }
  
  private void registerEvent() {
    log("registerEvent, major phoneid:" + RadioCapabilitySwitchUtil.getMainCapabilityPhoneId());
    DctController dctController1 = DctController.getInstance();
    DctController dctController2 = DctController.getInstance();
    dctController2.getClass();
    dctController1.registerForDcSwitchStateChange(this, 5, null, new DctController.DcStateParam(dctController2, "ImsSwitchController", true));
  }
  
  private void unregisterEvent() {
    log("unregisterEvent, major phoneid:" + RadioCapabilitySwitchUtil.getMainCapabilityPhoneId());
    DctController.getInstance().unregisterForDcSwitchStateChange(this);
  }
  
  public void handleMessage(Message paramMessage) {
    AsyncResult asyncResult = (AsyncResult)paramMessage.obj;
    log("handleMessage msg.what: " + eventIdtoString(paramMessage.what));
    switch (paramMessage.what) {
      default:
        super.handleMessage(paramMessage);
        return;
      case 1:
        if (RadioCapabilitySwitchUtil.getMainCapabilityPhoneId() == 0) {
          unregisterEvent();
          return;
        } 
        return;
      case 3:
        if (RadioCapabilitySwitchUtil.getMainCapabilityPhoneId() == 1) {
          unregisterEvent();
          return;
        } 
        return;
      case 2:
        if (RadioCapabilitySwitchUtil.getMainCapabilityPhoneId() == 0) {
          registerEvent();
          return;
        } 
        return;
      case 4:
        if (RadioCapabilitySwitchUtil.getMainCapabilityPhoneId() == 1) {
          registerEvent();
          return;
        } 
        return;
      case 5:
        onReceiveDcSwitchStateChange((DctController.DcStateParam)asyncResult.result);
        return;
      case 7:
        break;
    } 
    confirmPreCheckDetachIfNeed();
  }
  
  private class ImsServiceDeathRecipient implements IBinder.DeathRecipient {
    private ImsServiceDeathRecipient() {}
    
    public void binderDied() {
      ImsSwitchController.access$202((IImsService)null);
    }
  }
  
  class RadioPowerInterface implements IRadioPower {
    public void notifyRadioPowerChange(boolean param1Boolean, int param1Int) {
      int i;
      ImsSwitchController.log("notifyRadioPowerChange, power:" + param1Boolean + " phoneId:" + param1Int);
      if (RadioCapabilitySwitchUtil.getMainCapabilityPhoneId() == param1Int) {
        if (ImsSwitchController.mImsService == null)
          ImsSwitchController.this.checkAndBindImsService(param1Int); 
        if (ImsSwitchController.mImsService != null) {
          if (param1Boolean)
            try {
              int j = CommandsInterface.RadioState.RADIO_ON.ordinal();
              ImsSwitchController.mImsService.updateRadioState(j, param1Int);
              return;
            } catch (RemoteException remoteException) {
              ImsSwitchController.log("RemoteException can't notify power state change");
              return;
            }  
          i = CommandsInterface.RadioState.RADIO_OFF.ordinal();
        } else {
          ImsSwitchController.log("notifyRadioPowerChange: ImsService not ready !!!");
          return;
        } 
      } else {
        return;
      } 
      ImsSwitchController.mImsService.updateRadioState(i, param1Int);
    }
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/ImsSwitchController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */