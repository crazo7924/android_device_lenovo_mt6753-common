package com.mediatek.internal.telephony.dataconnection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SubscriptionController;
import com.mediatek.internal.telephony.ITelephonyEx;
import com.mediatek.internal.telephony.RadioCapabilitySwitchUtil;
import com.mediatek.internal.telephony.RadioManager;
import java.util.ArrayList;
import java.util.Arrays;

public class DataSubSelector {
  public static final String ACTION_MOBILE_DATA_ENABLE = "android.intent.action.ACTION_MOBILE_DATA_ENABLE";
  
  static final String ACTION_SHUTDOWN_IPO = "android.intent.action.ACTION_SHUTDOWN_IPO";
  
  private static final String ACTION_SUBINFO_READY = "cn.nubia.intent.action.ACTION_SUBINFO_READY";
  
  private static final boolean BSP_PACKAGE = SystemProperties.getBoolean("ro.mtk_bsp_package", false);
  
  private static final int CARD_TYPE_DATA = 1;
  
  private static final int CARD_TYPE_NEW_CARD = 3;
  
  private static final int CARD_TYPE_NOT_DATA = 2;
  
  private static final int CARD_TYPE_UNKNOWN = 0;
  
  private static final boolean DBG = true;
  
  private static final String DEACTIVATED_SIM_VALUE = "DEACTVATED";
  
  private static final int EVENT_SELECT_DATA_SUB = 0;
  
  public static final String EXTRA_MOBILE_DATA_ENABLE_REASON = "reason";
  
  private static final String FIRST_TIME_ROAMING = "first_time_roaming";
  
  private static final int HOME_POLICY = 0;
  
  private static final String MTK_C2K_SUPPORT = "ro.mtk_c2k_support";
  
  private static final String NEED_TO_EXECUTE_ROAMING = "need_to_execute_roaming";
  
  private static final String NEED_TO_WAIT_UNLOCKED = "persist.radio.unlock";
  
  private static final String NEED_TO_WAIT_UNLOCKED_ROAMING = "persist.radio.unlock.roaming";
  
  private static final String NEW_SIM_SLOT = "persist.radio.new.sim.slot";
  
  private static final String NO_SIM_VALUE = "N/A";
  
  private static final String OLD_ICCID = "old_iccid";
  
  private static final String OPERATOR_OM = "OM";
  
  private static final String OPERATOR_OP01 = "OP01";
  
  private static final String OPERATOR_OP02 = "OP02";
  
  private static final String OPERATOR_OP09 = "OP09";
  
  private static final String OPERATOR_OP18 = "OP18";
  
  private static final int POLICY_DEFAULT = 1;
  
  private static final int POLICY_NO_AUTO = 0;
  
  private static final int POLICY_POLICY1 = 2;
  
  private static final String PROPERTY_3G_SIM = "persist.radio.simswitch";
  
  private static final String PROPERTY_CAPABILITY_SWITCH_POLICY = "ro.mtk_sim_switch_policy";
  
  private static final String PROPERTY_DEFAULT_DATA_ICCID = "persist.radio.data.iccid";
  
  private static final String PROPERTY_DEFAULT_SIMSWITCH_ICCID = "persist.radio.simswitch.iccid";
  
  private static final String PROPERTY_MOBILE_DATA_ENABLE = "persist.radio.mobile.data";
  
  public static final String REASON_MOBILE_DATA_ENABLE_SYSTEM = "system";
  
  public static final String REASON_MOBILE_DATA_ENABLE_USER = "user";
  
  private static final int ROAMING_POLICY = 1;
  
  private static final String SEGC = "SEGC";
  
  private static final String SEGDEFAULT = "SEGDEFAULT";
  
  private static final String SIM_STATUS = "persist.radio.sim.status";
  
  private static final int TIME_RETRY_EXCEED = 2000;
  
  private static final int TIME_RETRY_NOMAL = 3000;
  
  private static final int capability_switch_policy = SystemProperties.getInt("ro.mtk_sim_switch_policy", 1);
  
  private static boolean m6mProject;
  
  private static String mOperatorSpec;
  
  private String[] PROPERTY_ICCID = new String[] { "ril.iccid.sim1", "ril.iccid.sim2", "ril.iccid.sim3", "ril.iccid.sim4" };
  
  private String[] PROPERTY_ICCID_PRE = new String[] { "persist.radio.iccid.sim1_pre", "persist.radio.iccid.sim2_pre", "persist.radio.iccid.sim3_pre", "persist.radio.iccid.sim4_pre" };
  
  private String[] PROPERTY_ICCID_SIM = new String[] { "ril.iccid.sim1", "ril.iccid.sim2", "ril.iccid.sim3", "ril.iccid.sim4" };
  
  private boolean mAirplaneModeOn = false;
  
  protected BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        String str = param1Intent.getAction();
        if (str != null) {
          String str1;
          SharedPreferences.Editor editor;
          DataSubSelector.this.log("onReceive: action=" + str);
          if (str.equals("android.intent.action.SIM_STATE_CHANGED")) {
            String str2 = param1Intent.getStringExtra("ss");
            int i = param1Intent.getIntExtra("slot", 0);
            DataSubSelector.this.log("slotId: " + i + " simStatus: " + str2 + " mIsNeedWaitImsi: " + DataSubSelector.this.mIsNeedWaitImsi + " mIsNeedWaitUnlock: " + DataSubSelector.this.isNeedWaitUnlock("persist.radio.unlock"));
            if (str2.equals("IMSI")) {
              RadioCapabilitySwitchUtil.updateSimImsiStatus(i, "1");
              RadioCapabilitySwitchUtil.clearRilMccMnc(i);
              if (DataSubSelector.this.isOP01OMSupport() == true) {
                DataSubSelector.this.log("sub for OP01 open market");
                DataSubSelector.this.subSelectorForOp01OM();
                return;
              } 
              if (DataSubSelector.this.mIsNeedWaitImsi == true || DataSubSelector.this.mIsNeedWaitImsiRoaming == true) {
                if (DataSubSelector.this.mIsNeedWaitImsi == true) {
                  DataSubSelector.access$102(DataSubSelector.this, false);
                  if ("OP02".equals(DataSubSelector.mOperatorSpec)) {
                    DataSubSelector.this.log("get imsi and need to check op02 again");
                    if (!DataSubSelector.this.checkOp02CapSwitch(0))
                      DataSubSelector.access$102(DataSubSelector.this, true); 
                  } else if ("OP18".equals(DataSubSelector.mOperatorSpec)) {
                    DataSubSelector.this.log("get imsi and need to check op18 again");
                    if (!DataSubSelector.this.checkOp18CapSwitch())
                      DataSubSelector.access$102(DataSubSelector.this, true); 
                  } 
                } 
                if (DataSubSelector.this.mIsNeedWaitImsiRoaming == true) {
                  DataSubSelector.access$502(DataSubSelector.this, false);
                  DataSubSelector.this.log("get imsi and need to check op02Roaming again");
                  if (!DataSubSelector.this.checkOp02CapSwitch(1))
                    DataSubSelector.access$502(DataSubSelector.this, true); 
                } 
                if ("OP01".equals(DataSubSelector.mOperatorSpec)) {
                  DataSubSelector.this.log("get imsi so check op01 again, do not set mIntent");
                  DataSubSelector.this.subSelectorForOp01(DataSubSelector.this.mIntent);
                  return;
                } 
                return;
              } 
              if (DataSubSelector.this.isNeedWaitUnlock("persist.radio.unlock") || DataSubSelector.this.isNeedWaitUnlock("persist.radio.unlock.roaming")) {
                DataSubSelector.this.log("get imsi because unlock");
                ITelephonyEx iTelephonyEx = ITelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
                if (iTelephonyEx != null)
                  try {
                    if (!iTelephonyEx.isCapabilitySwitching()) {
                      if (DataSubSelector.this.isNeedWaitUnlock("persist.radio.unlock")) {
                        DataSubSelector.this.setNeedWaitUnlock("persist.radio.unlock", "false");
                        if ("OP02".equals(DataSubSelector.mOperatorSpec)) {
                          if (SystemProperties.getBoolean("ro.mtk_disable_cap_switch", false) == true) {
                            DataSubSelector.this.subSelectorForOp02(DataSubSelector.this.mIntent);
                          } else {
                            DataSubSelector.this.subSelectorForOp02();
                          } 
                        } else if ("OM".equals(DataSubSelector.mOperatorSpec)) {
                          DataSubSelector.this.subSelectorForOm(DataSubSelector.this.mIntent);
                        } else if (DataSubSelector.this.isOP09ASupport()) {
                          DataSubSelector.this.subSelectorForOp09(DataSubSelector.this.mIntent);
                        } else if ("OP18".equals(DataSubSelector.mOperatorSpec)) {
                          DataSubSelector.this.subSelectorForOp18(DataSubSelector.this.mIntent);
                        } 
                      } 
                      if (DataSubSelector.this.isNeedWaitUnlock("persist.radio.unlock.roaming")) {
                        DataSubSelector.this.setNeedWaitUnlock("persist.radio.unlock.roaming", "false");
                        DataSubSelector.this.checkOp02CapSwitch(1);
                      } 
                      if ("OP01".equals(DataSubSelector.mOperatorSpec)) {
                        DataSubSelector.this.log("get imsi so check op01 again, do not set mIntent");
                        DataSubSelector.this.subSelectorForOp01(DataSubSelector.this.mIntent);
                        return;
                      } 
                    } 
                    return;
                  } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                    return;
                  }  
              } 
              return;
            } 
            if (remoteException.equals("ABSENT")) {
              RadioCapabilitySwitchUtil.updateSimImsiStatus(i, "0");
              RadioCapabilitySwitchUtil.clearRilMccMnc(i);
              return;
            } 
            if (remoteException.equals("NOT_READY")) {
              RadioCapabilitySwitchUtil.updateSimImsiStatus(i, "0");
              return;
            } 
            return;
          } 
          if (str.equals("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE") || str.equals("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED")) {
            DataSubSelector.this.log("mIsNeedWaitUnlock = " + DataSubSelector.this.isNeedWaitUnlock("persist.radio.unlock") + ", mIsNeedWaitUnlockRoaming = " + DataSubSelector.this.isNeedWaitUnlock("persist.radio.unlock.roaming"));
            if (DataSubSelector.this.isNeedWaitUnlock("persist.radio.unlock") || DataSubSelector.this.isNeedWaitUnlock("persist.radio.unlock.roaming")) {
              if (DataSubSelector.this.isNeedWaitUnlock("persist.radio.unlock")) {
                DataSubSelector.this.setNeedWaitUnlock("persist.radio.unlock", "false");
                if ("OP01".equals(DataSubSelector.mOperatorSpec)) {
                  DataSubSelector.this.subSelectorForOp01(DataSubSelector.this.mIntent);
                } else if ("OP02".equals(DataSubSelector.mOperatorSpec)) {
                  if (SystemProperties.getBoolean("ro.mtk_disable_cap_switch", false) == true) {
                    DataSubSelector.this.subSelectorForOp02(DataSubSelector.this.mIntent);
                  } else {
                    DataSubSelector.this.subSelectorForOp02();
                  } 
                } else if ("OM".equals(DataSubSelector.mOperatorSpec)) {
                  DataSubSelector.this.subSelectorForOm(DataSubSelector.this.mIntent);
                } else if (DataSubSelector.this.isOP09ASupport()) {
                  DataSubSelector.this.subSelectorForOp09(DataSubSelector.this.mIntent);
                } else if ("OP18".equals(DataSubSelector.mOperatorSpec)) {
                  DataSubSelector.this.subSelectorForOp18(DataSubSelector.this.mIntent);
                } 
              } 
              if (DataSubSelector.this.isNeedWaitUnlock("persist.radio.unlock.roaming")) {
                DataSubSelector.this.setNeedWaitUnlock("persist.radio.unlock.roaming", "false");
                DataSubSelector.this.checkOp02CapSwitch(1);
                return;
              } 
            } 
            return;
          } 
          if (str.equals("mediatek.intent.action.LOCATED_PLMN_CHANGED")) {
            DataSubSelector.this.log("ACTION_LOCATED_PLMN_CHANGED");
            if (!SystemProperties.getBoolean("ro.mtk_disable_cap_switch", false) && "OP02".equals(DataSubSelector.mOperatorSpec)) {
              str1 = param1Intent.getStringExtra("plmn");
              if (str1 != null && !"".equals(str1)) {
                DataSubSelector.this.log("plmn = " + str1);
                SharedPreferences sharedPreferences = remoteException.getSharedPreferences("first_time_roaming", 0);
                editor = sharedPreferences.edit();
                boolean bool = sharedPreferences.getBoolean("need_to_execute_roaming", true);
                if (!str1.startsWith("460")) {
                  if (bool == true) {
                    if (!DataSubSelector.this.mIsNeedWaitImsi) {
                      DataSubSelector.this.checkOp02CapSwitch(1);
                      return;
                    } 
                    DataSubSelector.access$502(DataSubSelector.this, true);
                    return;
                  } 
                  return;
                } 
                if (!bool) {
                  DataSubSelector.this.log("reset roaming flag");
                  editor.clear();
                  editor.commit();
                  return;
                } 
              } 
            } 
            return;
          } 
          if ("android.intent.action.AIRPLANE_MODE".equals(editor)) {
            boolean bool;
            DataSubSelector dataSubSelector = DataSubSelector.this;
            if (str1.getBooleanExtra("state", false)) {
              bool = true;
            } else {
              bool = false;
            } 
            DataSubSelector.access$1802(dataSubSelector, bool);
            DataSubSelector.this.log("ACTION_AIRPLANE_MODE_CHANGED, enabled = " + DataSubSelector.this.mAirplaneModeOn);
            if (!DataSubSelector.this.mAirplaneModeOn) {
              if (DataSubSelector.this.mIsNeedWaitAirplaneModeOff) {
                DataSubSelector.access$1902(DataSubSelector.this, false);
                if ("OP01".equals(DataSubSelector.mOperatorSpec)) {
                  DataSubSelector.this.subSelectorForOp01(DataSubSelector.this.mIntent);
                } else if ("OP02".equals(DataSubSelector.mOperatorSpec)) {
                  if (SystemProperties.getBoolean("ro.mtk_disable_cap_switch", false) == true) {
                    DataSubSelector.this.subSelectorForOp02(DataSubSelector.this.mIntent);
                  } else {
                    DataSubSelector.this.subSelectorForOp02();
                  } 
                } else if ("OM".equals(DataSubSelector.mOperatorSpec)) {
                  DataSubSelector.this.subSelectorForOm(DataSubSelector.this.mIntent);
                } else if (DataSubSelector.this.isOP09ASupport()) {
                  DataSubSelector.this.subSelectorForOp09(DataSubSelector.this.mIntent);
                } 
              } 
              if (DataSubSelector.this.mIsNeedWaitAirplaneModeOffRoaming) {
                DataSubSelector.access$2002(DataSubSelector.this, false);
                DataSubSelector.this.checkOp02CapSwitch(1);
                return;
              } 
            } 
            return;
          } 
          if ("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED".equals(editor)) {
            int i = str1.getIntExtra("subscription", -1);
            DataSubSelector.this.log("mIsUserConfirmDefaultData/nDefaultDataSubId:" + DataSubSelector.this.mIsUserConfirmDefaultData + "/" + i);
            if (DataSubSelector.this.mIsUserConfirmDefaultData && SubscriptionManager.isValidSubscriptionId(i)) {
              DataSubSelector.this.handleDataEnableForOp02(2);
              DataSubSelector.access$2102(DataSubSelector.this, false);
            } 
            DataSubSelector.this.setLastValidDefaultDataSub(i);
            return;
          } 
          if ("android.intent.action.ACTION_SUBINFO_RECORD_UPDATED".equals(editor)) {
            if (SystemProperties.get("ro.mtk_c2k_support").equals("1")) {
              int i = str1.getIntExtra("simDetectStatus", 4);
              DataSubSelector.this.log("DataSubSelector detectedType:" + i);
              if ((DataSubSelector.mOperatorSpec.equals("OM") || DataSubSelector.this.isOP09CSupport()) && DataSubSelector.this.isCanSwitch())
                if (DataSubSelector.this.isOP09CSupport()) {
                  if (i == 4)
                    DataSubSelector.this.subSelectorForC2k6m(DataSubSelector.this.mIntent); 
                } else {
                  DataSubSelector.this.subSelectorForC2k6m(DataSubSelector.this.mIntent);
                }  
            } 
            if (DataSubSelector.this.isOP01OMSupport() == true) {
              DataSubSelector.this.log("sub for OP01 open market");
              DataSubSelector.this.subSelectorForOp01OM();
              return;
            } 
            return;
          } 
          if (editor.equals("android.intent.action.ACTION_SHUTDOWN_IPO")) {
            DataSubSelector.this.log("DataSubSelector receive ACTION_SHUTDOWN_IPO, clear properties");
            RadioCapabilitySwitchUtil.clearAllSimImsiStatus();
            RadioCapabilitySwitchUtil.clearAllRilMccMnc(DataSubSelector.this.mPhoneNum);
            return;
          } 
        } 
      }
    };
  
  private Context mContext = null;
  
  private Handler mHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        if (param1Message == null)
          return; 
        switch (param1Message.what) {
          default:
            return;
          case 0:
            break;
        } 
        DataSubSelector.this.log("setDefaultDataForNubia after " + param1Message.arg1 + "'s waiting");
        String[] arrayOfString = new String[DataSubSelector.this.mPhoneNum];
        int j = 0;
        int i = 0;
        while (i < DataSubSelector.this.mPhoneNum) {
          arrayOfString[i] = DataSubSelector.this.getCurrentIccid(i);
          DataSubSelector.this.log("subSelectorForNubia phoneId = " + i + ", currIccId = " + arrayOfString[i]);
          int k = j;
          if ("N/A".equals(arrayOfString[i]))
            k = j + 1; 
          i++;
          j = k;
        } 
        DataSubSelector.this.log("subSelectorForNubia noCardCount = " + j);
        if (j == DataSubSelector.this.mPhoneNum) {
          DataSubSelector.this.log("should not be here, return!");
          return;
        } 
        if (j > 0 && param1Message.arg1 == 3000) {
          param1Message = DataSubSelector.this.mHandler.obtainMessage(0, 2000, -1);
          DataSubSelector.this.mHandler.sendMessageDelayed(param1Message, 2000L);
          DataSubSelector.this.log("setDefaultDataForNubia after 2000");
          return;
        } 
        DataSubSelector.this.setDefaultDataForNubia(arrayOfString);
      }
    };
  
  private boolean mHasRegisterWorldModeReceiver = false;
  
  private Intent mIntent = null;
  
  private boolean mIsNeedWaitAirplaneModeOff = false;
  
  private boolean mIsNeedWaitAirplaneModeOffRoaming = false;
  
  private boolean mIsNeedWaitImsi = false;
  
  private boolean mIsNeedWaitImsiRoaming = false;
  
  private boolean mIsRetryForDefaultData = false;
  
  private boolean mIsUserConfirmDefaultData = false;
  
  private int mLastValidDefaultDataSubId = -1;
  
  private int mPhoneId = -1;
  
  private int mPhoneNum;
  
  private int mPrevDefaultDataSubId = -1;
  
  private BroadcastReceiver mWorldModeReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        String str = param1Intent.getAction();
        DataSubSelector.this.log("mWorldModeReceiver: action = " + str);
        if ("android.intent.action.ACTION_WORLD_MODE_CHANGED".equals(str)) {
          int i = param1Intent.getIntExtra("worldModeState", -1);
          DataSubSelector.this.log("wmState: " + i);
          if (i == 1)
            DataSubSelector.this.setCapability(DataSubSelector.this.mPhoneId); 
        } 
      }
    };
  
  public DataSubSelector(Context paramContext, int paramInt) {
    log("DataSubSelector is created");
    this.mPhoneNum = paramInt;
    mOperatorSpec = SystemProperties.get("ro.operator.optr", "OM");
    m6mProject = SystemProperties.get("ro.mtk_c2k_support").equals("1");
    log("Operator Spec:" + mOperatorSpec + ", c2k_support:" + m6mProject);
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
    intentFilter.addAction("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE");
    intentFilter.addAction("android.intent.action.ACTION_SET_RADIO_CAPABILITY_FAILED");
    intentFilter.addAction("mediatek.intent.action.LOCATED_PLMN_CHANGED");
    intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
    intentFilter.addAction("android.intent.action.ACTION_DEFAULT_DATA_SUBSCRIPTION_CHANGED");
    intentFilter.addAction("android.intent.action.ACTION_SUBINFO_RECORD_UPDATED");
    intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN_IPO");
    paramContext.registerReceiver(this.mBroadcastReceiver, intentFilter);
    this.mContext = paramContext;
    if (Settings.Global.getInt(paramContext.getContentResolver(), "airplane_mode_on", 0) != 1)
      bool = false; 
    this.mAirplaneModeOn = bool;
    paramInt = SubscriptionManager.getDefaultDataSubId();
    log("defaultDataSub:" + paramInt);
    setLastValidDefaultDataSub(paramInt);
  }
  
  private boolean checkIfNeedRetry(String[] paramArrayOfString) {
    for (int i = 0; i < this.mPhoneNum; i++) {
      if (paramArrayOfString[i].equals("N/A"))
        return true; 
    } 
    return false;
  }
  
  private boolean checkOp01CapSwitch() {
    int[] arrayOfInt1 = new int[this.mPhoneNum];
    int[] arrayOfInt2 = new int[this.mPhoneNum];
    int j = 0;
    byte b = 0;
    byte b1 = -1;
    int i = 0;
    boolean[] arrayOfBoolean1 = new boolean[this.mPhoneNum];
    boolean[] arrayOfBoolean2 = new boolean[this.mPhoneNum];
    boolean[] arrayOfBoolean3 = new boolean[this.mPhoneNum];
    boolean[] arrayOfBoolean4 = new boolean[this.mPhoneNum];
    String str = SystemProperties.get("persist.radio.simswitch.iccid");
    String[] arrayOfString = new String[this.mPhoneNum];
    log("checkOp01CapSwitch start");
    int k = 0;
    while (k < this.mPhoneNum) {
      arrayOfString[k] = SystemProperties.get(this.PROPERTY_ICCID[k]);
      if (arrayOfString[k] == null || "".equals(arrayOfString[k])) {
        log("error: iccid not found, wait for next sub ready");
        return false;
      } 
      int i2 = j;
      int i1 = i;
      if (!"N/A".equals(arrayOfString[k])) {
        i2 = j + 1;
        i1 = i | 1 << k;
      } 
      k++;
      j = i2;
      i = i1;
    } 
    log("checkOp01CapSwitch : Inserted SIM count: " + j + ", insertedStatus: " + i);
    if (RadioCapabilitySwitchUtil.isAnySimLocked(this.mPhoneNum)) {
      log("checkOp01CapSwitch: sim locked");
      setNeedWaitUnlock("persist.radio.unlock", "true");
    } else {
      log("checkOp01CapSwitch: no sim locked");
      setNeedWaitUnlock("persist.radio.unlock", "false");
    } 
    if (!RadioCapabilitySwitchUtil.getSimInfo(arrayOfInt1, arrayOfInt2, i))
      return false; 
    int n = Integer.valueOf(SystemProperties.get("persist.radio.simswitch", "1")).intValue() - 1;
    log("op01: capabilitySimIccid:" + str + "capabilitySimId:" + n);
    i = 0;
    k = b1;
    int m = b;
    while (i < this.mPhoneNum) {
      if (arrayOfInt1[i] == 2) {
        if (arrayOfInt2[i] != 0) {
          arrayOfBoolean1[i] = true;
        } else {
          arrayOfBoolean2[i] = true;
        } 
      } else if (arrayOfInt1[i] == 1) {
        if (arrayOfInt2[i] != 0) {
          arrayOfBoolean3[i] = true;
        } else {
          arrayOfBoolean4[i] = true;
        } 
      } 
      if (arrayOfInt2[i] == 0 || arrayOfInt2[i] == 1) {
        k = i;
      } else {
        m++;
      } 
      i++;
    } 
    log("op01Usim: " + Arrays.toString(arrayOfBoolean1));
    log("op01Sim: " + Arrays.toString(arrayOfBoolean2));
    log("overseaUsim: " + Arrays.toString(arrayOfBoolean3));
    log("overseaSim: " + Arrays.toString(arrayOfBoolean4));
    log("csimRuimCount: " + m);
    log("nonCsimRuimPhoneId: " + k);
    if (RadioCapabilitySwitchUtil.isOp01LCProject() && j == 2 && m == 1) {
      if (k != n) {
        log("op01-L+C: current capability SIM is on CSIM/RUIM, change!");
        setCapability(k);
      } 
      return true;
    } 
    for (i = 0; i < this.mPhoneNum; i++) {
      if (str.equals(arrayOfString[i])) {
        j = RadioCapabilitySwitchUtil.getHigherPrioritySimForOp01(i, arrayOfBoolean1, arrayOfBoolean2, arrayOfBoolean3, arrayOfBoolean4);
        log("op01: i = " + i + ", currIccId : " + arrayOfString[i] + ", targetSim : " + j);
        if (arrayOfBoolean1[i] == true) {
          log("op01-C1: cur is old op01 USIM, no change");
          if (n != i) {
            log("op01-C1a: old op01 USIM change slot, change!");
            setCapability(i);
          } 
          return true;
        } 
        if (arrayOfBoolean2[i] == true) {
          if (j != -1) {
            log("op01-C2: cur is old op01 SIM but find op01 USIM, change!");
            setCapability(j);
            return true;
          } 
          if (n != i) {
            log("op01-C2a: old op01 SIM change slot, change!");
            setCapability(i);
          } 
          return true;
        } 
        if (arrayOfBoolean3[i] == true) {
          if (j != -1) {
            log("op01-C3: cur is old OS USIM but find op01 SIMs, change!");
            setCapability(j);
            return true;
          } 
          if (n != i) {
            log("op01-C3a: old OS USIM change slot, change!");
            setCapability(i);
          } 
          return true;
        } 
        if (arrayOfBoolean4[i] == true) {
          if (j != -1) {
            log("op01-C4: cur is old OS SIM but find op01 SIMs/OS USIM, change!");
            setCapability(j);
            return true;
          } 
          if (n != i) {
            log("op01-C4a: old OS SIM change slot, change!");
            setCapability(i);
          } 
          return true;
        } 
        if (j != -1) {
          log("op01-C5: cur is old non-op01 SIM/USIM but find higher SIM, change!");
          setCapability(j);
          return true;
        } 
        log("op01-C6: no higher priority SIM, no cahnge");
        return true;
      } 
    } 
    i = RadioCapabilitySwitchUtil.getHigherPrioritySimForOp01(n, arrayOfBoolean1, arrayOfBoolean2, arrayOfBoolean3, arrayOfBoolean4);
    log("op01: target SIM :" + i);
    if (arrayOfBoolean1[n] == true) {
      log("op01-C7: cur is new op01 USIM, no change");
      return true;
    } 
    if (arrayOfBoolean2[n] == true) {
      if (i != -1) {
        log("op01-C8: cur is new op01 SIM but find op01 USIM, change!");
        setCapability(i);
      } 
      return true;
    } 
    if (arrayOfBoolean3[n] == true) {
      if (i != -1) {
        log("op01-C9: cur is new OS USIM but find op01 SIMs, change!");
        setCapability(i);
      } 
      return true;
    } 
    if (arrayOfBoolean4[n] == true) {
      if (i != -1) {
        log("op01-C10: cur is new OS SIM but find op01 SIMs/OS USIM, change!");
        setCapability(i);
      } 
      return true;
    } 
    if (i != -1) {
      log("op01-C11: cur is non-op01 but find higher priority SIM, change!");
      setCapability(i);
      return true;
    } 
    log("op01-C12: no higher priority SIM, no cahnge");
    return true;
  }
  
  private boolean checkOp01CapSwitch6m() {
    int[] arrayOfInt1 = new int[this.mPhoneNum];
    int[] arrayOfInt2 = new int[this.mPhoneNum];
    int j = 0;
    int k = 0;
    String[] arrayOfString = new String[this.mPhoneNum];
    int[] arrayOfInt3 = new int[this.mPhoneNum];
    int i = SubscriptionManager.getPhoneId(SubscriptionController.getInstance().getDefaultDataSubId());
    if (i >= 0 && i < this.mPhoneNum) {
      log("default data phoneId from sub = " + i);
    } else {
      log("phoneId out of boundary :" + i);
      i = -1;
    } 
    log("checkOp01CapSwitch6m start");
    int m = 0;
    while (m < this.mPhoneNum) {
      arrayOfString[m] = SystemProperties.get(this.PROPERTY_ICCID[m]);
      if (arrayOfString[m] == null || "".equals(arrayOfString[m])) {
        log("error: iccid not found, wait for next sub ready");
        return false;
      } 
      int i1 = j;
      int n = k;
      if (!"N/A".equals(arrayOfString[m])) {
        i1 = j + 1;
        n = k | 1 << m;
      } 
      m++;
      j = i1;
      k = n;
    } 
    log("checkOp01CapSwitch6m : Inserted SIM count: " + j + ", insertedStatus: " + k);
    if (RadioCapabilitySwitchUtil.isAnySimLocked(this.mPhoneNum)) {
      log("checkOp01CapSwitch6m: sim locked");
      setNeedWaitUnlock("persist.radio.unlock", "true");
    } else {
      log("checkOp01CapSwitch6m: no sim locked");
      setNeedWaitUnlock("persist.radio.unlock", "false");
    } 
    if (RadioCapabilitySwitchUtil.getSimInfo(arrayOfInt1, arrayOfInt2, k)) {
      for (k = 0; k < this.mPhoneNum; k++) {
        if (arrayOfInt1[k] == 2) {
          if (arrayOfInt2[k] == 1) {
            arrayOfInt3[k] = 0;
          } else if (arrayOfInt2[k] == 0) {
            arrayOfInt3[k] = 1;
          } 
        } else {
          arrayOfInt3[k] = 2;
        } 
      } 
      log("priority: " + Arrays.toString(arrayOfInt3));
      i = RadioCapabilitySwitchUtil.getHighestPriorityPhone(i, arrayOfInt3);
      log("op01-6m: target phone: " + i);
      if (i != -1) {
        log("op01-6m: highest priority SIM determined, change!");
        setCapability(i);
      } else {
        log("op01-6m: can't determine highest priority SIM, no change");
      } 
      if (j >= 2)
        handleDefaultDataOp01MultiSims(); 
      return true;
    } 
    return false;
  }
  
  private boolean checkOp02CapSwitch(int paramInt) {
    int[] arrayOfInt1 = new int[this.mPhoneNum];
    int[] arrayOfInt2 = new int[this.mPhoneNum];
    int i = 0;
    int k = 0;
    String[] arrayOfString = new String[this.mPhoneNum];
    ArrayList<Integer> arrayList1 = new ArrayList();
    ArrayList<Integer> arrayList2 = new ArrayList();
    ArrayList<Integer> arrayList3 = new ArrayList();
    ArrayList<Integer> arrayList4 = new ArrayList();
    int j = 0;
    while (j < this.mPhoneNum) {
      arrayOfString[j] = SystemProperties.get(this.PROPERTY_ICCID[j]);
      if (arrayOfString[j] == null || "".equals(arrayOfString[j])) {
        log("error: iccid not found, wait for next sub ready");
        return false;
      } 
      int n = k;
      int m = i;
      if (!"N/A".equals(arrayOfString[j])) {
        n = k + 1;
        m = i | 1 << j;
      } 
      j++;
      k = n;
      i = m;
    } 
    log("checkOp02CapSwitch : Inserted SIM count: " + k + ", insertedStatus: " + i);
    if (RadioCapabilitySwitchUtil.isAnySimLocked(this.mPhoneNum)) {
      log("checkOp02CapSwitch: sim locked");
      if (paramInt == 0) {
        setNeedWaitUnlock("persist.radio.unlock", "true");
      } else {
        setNeedWaitUnlock("persist.radio.unlock.roaming", "true");
      } 
    } else {
      log("checkOp02CapSwitch: no sim locked");
      if (paramInt == 0) {
        setNeedWaitUnlock("persist.radio.unlock", "false");
      } else {
        setNeedWaitUnlock("persist.radio.unlock.roaming", "false");
      } 
    } 
    if (RadioCapabilitySwitchUtil.getSimInfo(arrayOfInt1, arrayOfInt2, i)) {
      if (this.mAirplaneModeOn) {
        log("DataSubSelector for OP02: do not switch because of mAirplaneModeOn");
        if (paramInt == 0) {
          this.mIsNeedWaitAirplaneModeOff = true;
        } else if (1 == paramInt) {
          this.mIsNeedWaitAirplaneModeOffRoaming = true;
        } 
      } 
      for (i = 0; i < this.mPhoneNum; i++) {
        if (3 == arrayOfInt1[i]) {
          arrayList3.add(Integer.valueOf(i));
        } else {
          arrayList4.add(Integer.valueOf(i));
        } 
        if (1 == arrayOfInt2[i]) {
          arrayList1.add(Integer.valueOf(i));
        } else {
          arrayList2.add(Integer.valueOf(i));
        } 
      } 
      log("usimIndexList size = " + arrayList1.size());
      log("op02IndexList size = " + arrayList3.size());
      log("policy = " + paramInt);
      this.mIsUserConfirmDefaultData = false;
      switch (paramInt) {
        default:
          loge("Should NOT be here");
          return true;
        case 0:
          executeOp02HomePolicy(arrayList1, arrayList3, arrayList2);
          return true;
        case 1:
          break;
      } 
      executeOp02RoamingPolocy(arrayList1, arrayList3, arrayList4);
      return true;
    } 
    return false;
  }
  
  private boolean checkOp18CapSwitch() {
    // Byte code:
    //   0: getstatic com/mediatek/internal/telephony/dataconnection/DataSubSelector.capability_switch_policy : I
    //   3: iconst_2
    //   4: if_icmpeq -> 16
    //   7: aload_0
    //   8: ldc_w 'checkOp18CapSwitch: config is not policy1, do nothing'
    //   11: invokespecial log : (Ljava/lang/String;)V
    //   14: iconst_1
    //   15: ireturn
    //   16: aload_0
    //   17: getfield mPhoneNum : I
    //   20: newarray int
    //   22: astore #8
    //   24: aload_0
    //   25: getfield mPhoneNum : I
    //   28: newarray int
    //   30: astore #7
    //   32: iconst_m1
    //   33: istore_2
    //   34: iconst_0
    //   35: istore #5
    //   37: iconst_0
    //   38: istore_1
    //   39: aload_0
    //   40: getfield mPhoneNum : I
    //   43: newarray boolean
    //   45: astore #9
    //   47: ldc 'persist.radio.simswitch.iccid'
    //   49: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   52: astore #10
    //   54: aload_0
    //   55: getfield mPhoneNum : I
    //   58: anewarray java/lang/String
    //   61: astore #11
    //   63: aload_0
    //   64: ldc_w 'checkOp18CapSwitch start'
    //   67: invokespecial log : (Ljava/lang/String;)V
    //   70: iconst_0
    //   71: istore_3
    //   72: iload_3
    //   73: aload_0
    //   74: getfield mPhoneNum : I
    //   77: if_icmpge -> 168
    //   80: aload #11
    //   82: iload_3
    //   83: aload_0
    //   84: getfield PROPERTY_ICCID : [Ljava/lang/String;
    //   87: iload_3
    //   88: aaload
    //   89: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   92: aastore
    //   93: aload #11
    //   95: iload_3
    //   96: aaload
    //   97: ifnull -> 113
    //   100: ldc_w ''
    //   103: aload #11
    //   105: iload_3
    //   106: aaload
    //   107: invokevirtual equals : (Ljava/lang/Object;)Z
    //   110: ifeq -> 122
    //   113: aload_0
    //   114: ldc_w 'error: iccid not found, wait for next sub ready'
    //   117: invokespecial log : (Ljava/lang/String;)V
    //   120: iconst_0
    //   121: ireturn
    //   122: iload #5
    //   124: istore #6
    //   126: iload_1
    //   127: istore #4
    //   129: ldc 'N/A'
    //   131: aload #11
    //   133: iload_3
    //   134: aaload
    //   135: invokevirtual equals : (Ljava/lang/Object;)Z
    //   138: ifne -> 154
    //   141: iload #5
    //   143: iconst_1
    //   144: iadd
    //   145: istore #6
    //   147: iload_1
    //   148: iconst_1
    //   149: iload_3
    //   150: ishl
    //   151: ior
    //   152: istore #4
    //   154: iload_3
    //   155: iconst_1
    //   156: iadd
    //   157: istore_3
    //   158: iload #6
    //   160: istore #5
    //   162: iload #4
    //   164: istore_1
    //   165: goto -> 72
    //   168: aload_0
    //   169: new java/lang/StringBuilder
    //   172: dup
    //   173: invokespecial <init> : ()V
    //   176: ldc_w 'checkOp18CapSwitch : Inserted SIM count: '
    //   179: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   182: iload #5
    //   184: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   187: ldc_w ', insertedStatus: '
    //   190: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   193: iload_1
    //   194: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   197: invokevirtual toString : ()Ljava/lang/String;
    //   200: invokespecial log : (Ljava/lang/String;)V
    //   203: aload #8
    //   205: aload #7
    //   207: iload_1
    //   208: invokestatic getSimInfo : ([I[II)Z
    //   211: ifne -> 216
    //   214: iconst_0
    //   215: ireturn
    //   216: iconst_0
    //   217: istore_1
    //   218: iload_1
    //   219: aload_0
    //   220: getfield mPhoneNum : I
    //   223: if_icmpge -> 325
    //   226: iload_1
    //   227: ifne -> 298
    //   230: ldc_w 'gsm.sim.ril.mcc.mnc'
    //   233: astore #7
    //   235: aload #7
    //   237: ldc_w ''
    //   240: invokestatic get : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   243: ldc_w 'sim_lock'
    //   246: invokevirtual equals : (Ljava/lang/Object;)Z
    //   249: ifeq -> 291
    //   252: aload_0
    //   253: new java/lang/StringBuilder
    //   256: dup
    //   257: invokespecial <init> : ()V
    //   260: ldc_w 'checkOp18CapSwitch : phone '
    //   263: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   266: iload_1
    //   267: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   270: ldc_w ' is sim lock'
    //   273: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   276: invokevirtual toString : ()Ljava/lang/String;
    //   279: invokespecial log : (Ljava/lang/String;)V
    //   282: aload_0
    //   283: ldc 'persist.radio.unlock'
    //   285: ldc_w 'true'
    //   288: invokespecial setNeedWaitUnlock : (Ljava/lang/String;Ljava/lang/String;)V
    //   291: iload_1
    //   292: iconst_1
    //   293: iadd
    //   294: istore_1
    //   295: goto -> 218
    //   298: new java/lang/StringBuilder
    //   301: dup
    //   302: invokespecial <init> : ()V
    //   305: ldc_w 'gsm.sim.ril.mcc.mnc.'
    //   308: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   311: iload_1
    //   312: iconst_1
    //   313: iadd
    //   314: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   317: invokevirtual toString : ()Ljava/lang/String;
    //   320: astore #7
    //   322: goto -> 235
    //   325: ldc 'persist.radio.simswitch'
    //   327: ldc '1'
    //   329: invokestatic get : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   332: invokestatic valueOf : (Ljava/lang/String;)Ljava/lang/Integer;
    //   335: invokevirtual intValue : ()I
    //   338: iconst_1
    //   339: isub
    //   340: istore #5
    //   342: aload_0
    //   343: new java/lang/StringBuilder
    //   346: dup
    //   347: invokespecial <init> : ()V
    //   350: ldc_w 'op18: capabilitySimIccid:'
    //   353: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   356: aload #10
    //   358: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   361: ldc_w 'capabilitySimId:'
    //   364: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   367: iload #5
    //   369: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   372: invokevirtual toString : ()Ljava/lang/String;
    //   375: invokespecial log : (Ljava/lang/String;)V
    //   378: iconst_0
    //   379: istore_1
    //   380: iload_1
    //   381: aload_0
    //   382: getfield mPhoneNum : I
    //   385: if_icmpge -> 408
    //   388: aload #8
    //   390: iload_1
    //   391: iaload
    //   392: iconst_5
    //   393: if_icmpne -> 401
    //   396: aload #9
    //   398: iload_1
    //   399: iconst_1
    //   400: bastore
    //   401: iload_1
    //   402: iconst_1
    //   403: iadd
    //   404: istore_1
    //   405: goto -> 380
    //   408: aload_0
    //   409: new java/lang/StringBuilder
    //   412: dup
    //   413: invokespecial <init> : ()V
    //   416: ldc_w 'op18Usim: '
    //   419: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   422: aload #9
    //   424: invokestatic toString : ([Z)Ljava/lang/String;
    //   427: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   430: invokevirtual toString : ()Ljava/lang/String;
    //   433: invokespecial log : (Ljava/lang/String;)V
    //   436: iconst_0
    //   437: istore_1
    //   438: iload_1
    //   439: aload_0
    //   440: getfield mPhoneNum : I
    //   443: if_icmpge -> 664
    //   446: aload #10
    //   448: aload #11
    //   450: iload_1
    //   451: aaload
    //   452: invokevirtual equals : (Ljava/lang/Object;)Z
    //   455: ifeq -> 657
    //   458: aload #9
    //   460: iload_1
    //   461: baload
    //   462: iconst_1
    //   463: if_icmpne -> 564
    //   466: iload_1
    //   467: istore #4
    //   469: aload_0
    //   470: new java/lang/StringBuilder
    //   473: dup
    //   474: invokespecial <init> : ()V
    //   477: ldc_w 'op18: i = '
    //   480: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   483: iload_1
    //   484: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   487: ldc_w ', currIccId : '
    //   490: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   493: aload #11
    //   495: iload_1
    //   496: aaload
    //   497: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   500: ldc_w ', targetSim : '
    //   503: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   506: iload #4
    //   508: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   511: invokevirtual toString : ()Ljava/lang/String;
    //   514: invokespecial log : (Ljava/lang/String;)V
    //   517: aload #9
    //   519: iload_1
    //   520: baload
    //   521: iconst_1
    //   522: if_icmpne -> 600
    //   525: aload_0
    //   526: ldc_w 'op18-C1: cur is old op18 USIM, no change'
    //   529: invokespecial log : (Ljava/lang/String;)V
    //   532: iload #5
    //   534: iload_1
    //   535: if_icmpeq -> 551
    //   538: aload_0
    //   539: ldc_w 'op18-C1a: old op18 SIM change slot, change!'
    //   542: invokespecial log : (Ljava/lang/String;)V
    //   545: aload_0
    //   546: iload_1
    //   547: invokespecial setCapability : (I)Z
    //   550: pop
    //   551: aload_0
    //   552: iload_1
    //   553: invokespecial setDefaultData : (I)V
    //   556: aload_0
    //   557: iload_1
    //   558: iconst_1
    //   559: invokespecial setDataEnabled : (IZ)V
    //   562: iconst_1
    //   563: ireturn
    //   564: iconst_0
    //   565: istore #4
    //   567: iload_2
    //   568: istore_3
    //   569: iload #4
    //   571: istore_2
    //   572: iload_3
    //   573: istore #4
    //   575: iload_2
    //   576: aload_0
    //   577: getfield mPhoneNum : I
    //   580: if_icmpge -> 469
    //   583: aload #9
    //   585: iload_2
    //   586: baload
    //   587: iconst_1
    //   588: if_icmpne -> 593
    //   591: iload_2
    //   592: istore_3
    //   593: iload_2
    //   594: iconst_1
    //   595: iadd
    //   596: istore_2
    //   597: goto -> 572
    //   600: iload #4
    //   602: iconst_m1
    //   603: if_icmpeq -> 635
    //   606: aload_0
    //   607: ldc_w 'op18-C2: cur is not op18 SIM but find op18 SIM, change!'
    //   610: invokespecial log : (Ljava/lang/String;)V
    //   613: aload_0
    //   614: iload #4
    //   616: invokespecial setCapability : (I)Z
    //   619: pop
    //   620: aload_0
    //   621: iload #4
    //   623: invokespecial setDefaultData : (I)V
    //   626: aload_0
    //   627: iload #4
    //   629: iconst_1
    //   630: invokespecial setDataEnabled : (IZ)V
    //   633: iconst_1
    //   634: ireturn
    //   635: aload_0
    //   636: iload #5
    //   638: invokespecial setDefaultData : (I)V
    //   641: aload_0
    //   642: iload #5
    //   644: iconst_1
    //   645: invokespecial setDataEnabled : (IZ)V
    //   648: aload_0
    //   649: ldc_w 'op18-C6: no higher priority SIM, no cahnge'
    //   652: invokespecial log : (Ljava/lang/String;)V
    //   655: iconst_1
    //   656: ireturn
    //   657: iload_1
    //   658: iconst_1
    //   659: iadd
    //   660: istore_1
    //   661: goto -> 438
    //   664: aload #9
    //   666: iload #5
    //   668: baload
    //   669: iconst_1
    //   670: if_icmpne -> 731
    //   673: iload #5
    //   675: istore_3
    //   676: aload_0
    //   677: new java/lang/StringBuilder
    //   680: dup
    //   681: invokespecial <init> : ()V
    //   684: ldc_w 'op18: target SIM :'
    //   687: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   690: iload_3
    //   691: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   694: invokevirtual toString : ()Ljava/lang/String;
    //   697: invokespecial log : (Ljava/lang/String;)V
    //   700: aload #9
    //   702: iload #5
    //   704: baload
    //   705: iconst_1
    //   706: if_icmpne -> 760
    //   709: aload_0
    //   710: ldc_w 'op18-C7: cur is new op18 USIM, no change'
    //   713: invokespecial log : (Ljava/lang/String;)V
    //   716: aload_0
    //   717: iload #5
    //   719: invokespecial setDefaultData : (I)V
    //   722: aload_0
    //   723: iload #5
    //   725: iconst_1
    //   726: invokespecial setDataEnabled : (IZ)V
    //   729: iconst_1
    //   730: ireturn
    //   731: iconst_0
    //   732: istore_1
    //   733: iload_2
    //   734: istore_3
    //   735: iload_1
    //   736: aload_0
    //   737: getfield mPhoneNum : I
    //   740: if_icmpge -> 676
    //   743: aload #9
    //   745: iload_1
    //   746: baload
    //   747: iconst_1
    //   748: if_icmpne -> 753
    //   751: iload_1
    //   752: istore_2
    //   753: iload_1
    //   754: iconst_1
    //   755: iadd
    //   756: istore_1
    //   757: goto -> 733
    //   760: iload_3
    //   761: iconst_m1
    //   762: if_icmpeq -> 791
    //   765: aload_0
    //   766: ldc_w 'op18-C8: find op18 USIM, change!'
    //   769: invokespecial log : (Ljava/lang/String;)V
    //   772: aload_0
    //   773: iload_3
    //   774: invokespecial setCapability : (I)Z
    //   777: pop
    //   778: aload_0
    //   779: iload_3
    //   780: invokespecial setDefaultData : (I)V
    //   783: aload_0
    //   784: iload_3
    //   785: iconst_1
    //   786: invokespecial setDataEnabled : (IZ)V
    //   789: iconst_1
    //   790: ireturn
    //   791: aload_0
    //   792: iload #5
    //   794: invokespecial setDefaultData : (I)V
    //   797: aload_0
    //   798: iload #5
    //   800: iconst_1
    //   801: invokespecial setDataEnabled : (IZ)V
    //   804: aload_0
    //   805: ldc_w 'op18-C12: no higher priority SIM, no cahnge'
    //   808: invokespecial log : (Ljava/lang/String;)V
    //   811: iconst_1
    //   812: ireturn
  }
  
  private void executeOp02HomePolicy(ArrayList<Integer> paramArrayList1, ArrayList<Integer> paramArrayList2, ArrayList<Integer> paramArrayList3) {
    int k = -1;
    int m = 0;
    int j = 0;
    log("Enter op02HomePolicy");
    if (paramArrayList1.size() >= 2) {
      int n = 0;
      while (n < paramArrayList1.size()) {
        m = j;
        if (paramArrayList2.contains(paramArrayList1.get(n))) {
          m = j + 1;
          k = n;
        } 
        n++;
        j = m;
      } 
      if (j == 1) {
        log("C2: Only one OP02 USIM inserted, set default data to phone: " + k);
        if (setCapability(k)) {
          setDefaultData(k);
          handleDataEnableForOp02(2);
        } 
        return;
      } 
      log("C3: More than two OP02 cards or other operator cards inserted,Display dialog");
      this.mPrevDefaultDataSubId = SubscriptionManager.getDefaultDataSubId();
      log("mPrevDefaultDataSubId:" + this.mPrevDefaultDataSubId);
      this.mIsUserConfirmDefaultData = true;
      return;
    } 
    if (paramArrayList1.size() == 1) {
      int n = ((Integer)paramArrayList1.get(0)).intValue();
      log("C4: Only one USIM inserted, set default data to phone: " + n);
      if (setCapability(n)) {
        setDefaultData(n);
        handleDataEnableForOp02(2);
        return;
      } 
      return;
    } 
    int i = 0;
    for (j = m; i < paramArrayList3.size(); j = m) {
      m = j;
      if (paramArrayList2.contains(paramArrayList3.get(i))) {
        m = j + 1;
        k = i;
      } 
      i++;
    } 
    if (j == 1) {
      log("C5: OP02 card + otehr op cards inserted, set default data to phone: " + k);
      if (setCapability(k)) {
        setDefaultData(k);
        handleDataEnableForOp02(2);
        return;
      } 
      return;
    } 
    log("C6: More than two OP02 cards or other operator cards inserted,display dialog");
    this.mPrevDefaultDataSubId = SubscriptionManager.getDefaultDataSubId();
    this.mIsUserConfirmDefaultData = true;
  }
  
  private void executeOp02RoamingPolocy(ArrayList<Integer> paramArrayList1, ArrayList<Integer> paramArrayList2, ArrayList<Integer> paramArrayList3) {
    int i = -1;
    int k = 0;
    int j = 0;
    log("Enter op02RoamingPolocy");
    if (this.mContext == null)
      loge("mContext is null, return"); 
    if (paramArrayList2.size() >= 2) {
      int m = 0;
      k = i;
      i = m;
      while (i < paramArrayList2.size()) {
        m = j;
        if (paramArrayList1.contains(paramArrayList2.get(i))) {
          m = j + 1;
          k = i;
        } 
        i++;
        j = m;
      } 
      if (j == 1) {
        log("C2: Only one OP02 USIM inserted, set default data to phone: " + k);
        if (setCapability(k)) {
          setDefaultData(k);
          handleDataEnableForOp02(2);
        } 
      } else {
        log("C3: More than two USIM cards or other SIM cards inserted, show dialog");
        this.mPrevDefaultDataSubId = SubscriptionManager.getDefaultDataSubId();
        this.mIsUserConfirmDefaultData = true;
      } 
    } else if (paramArrayList2.size() == 1) {
      i = ((Integer)paramArrayList2.get(0)).intValue();
      log("C4: OP02 card + other cards inserted, set default data to phone: " + i);
      if (setCapability(i)) {
        setDefaultData(i);
        handleDataEnableForOp02(2);
      } 
    } else {
      int m = 0;
      j = k;
      k = i;
      i = m;
      while (i < paramArrayList3.size()) {
        m = j;
        if (paramArrayList1.contains(paramArrayList3.get(i))) {
          m = j + 1;
          k = i;
        } 
        i++;
        j = m;
      } 
      if (j == 1) {
        log("C5: Other USIM + other SIM cards inserted, set default data to phone: " + k);
        if (setCapability(k)) {
          setDefaultData(k);
          handleDataEnableForOp02(2);
        } 
      } else {
        log("C6: More than two USIM cards or all SIM cards inserted, diaplay dialog");
        this.mPrevDefaultDataSubId = SubscriptionManager.getDefaultDataSubId();
        this.mIsUserConfirmDefaultData = true;
      } 
    } 
    SharedPreferences.Editor editor = this.mContext.getSharedPreferences("first_time_roaming", 0).edit();
    editor.putBoolean("need_to_execute_roaming", false);
    if (!editor.commit())
      loge("write sharedPreference ERROR"); 
  }
  
  private int getCurrentCardType(String paramString1, String paramString2) {
    byte b = 0;
    if (paramString1 == null || paramString1.isEmpty() || paramString1.equals("N/A") || paramString1.equals("DEACTVATED"))
      return 0; 
    if (!paramString2.equals("") && !paramString2.equals("N/A") && paramString1.equals(paramString2))
      return 1; 
    int i = 0;
    while (true) {
      if (i < this.mPhoneNum) {
        paramString2 = getPreIccid(i);
        log("getCurrentCardType slotId = " + i + ", preIccId =" + paramString2);
        if (paramString2.equals(paramString1))
          return 2; 
        b = 3;
        i++;
        continue;
      } 
      return b;
    } 
  }
  
  private String getCurrentIccid(int paramInt) {
    int i = SubscriptionController.getInstance().getSubIdUsingPhoneId(paramInt);
    String str = SystemProperties.get(this.PROPERTY_ICCID[paramInt]);
    if (str == null || "".equals(str) || "N/A".equals(str))
      return "N/A"; 
    if (SubscriptionManager.isValidSubscriptionId(i)) {
      String str1 = str;
      return !RadioManager.isCardActive(str) ? "DEACTVATED" : str1;
    } 
    return "N/A";
  }
  
  private boolean getDataEnabledFromSetting(int paramInt) {
    boolean bool;
    log("getDataEnabledFromSetting, nSubId = " + paramInt);
    if (this.mContext == null || this.mContext.getContentResolver() == null) {
      log("getDataEnabledFromSetting, context or resolver is null, return");
      return false;
    } 
    try {
      paramInt = Settings.Global.getInt(this.mContext.getContentResolver(), "mobile_data" + paramInt);
      if (paramInt != 0) {
        boolean bool1 = true;
        log("getDataEnabledFromSetting, retVal = " + bool1);
        return bool1;
      } 
      bool = false;
    } catch (android.provider.Settings.SettingNotFoundException settingNotFoundException) {
      bool = false;
    } 
    log("getDataEnabledFromSetting, retVal = " + bool);
    return bool;
  }
  
  private int getNewDefaultDataSubIdByNubia(int[] paramArrayOfint, int paramInt) {
    int i = 0;
    paramInt = 0;
    if (isHaveMultiCard(paramArrayOfint)) {
      paramInt = getPreCardCount(paramArrayOfint);
      log("phoneId getNewDefaultDataSubIdByNubia count:" + paramInt);
      if (paramInt == 0 || paramInt == this.mPhoneNum) {
        paramInt = 0;
        log("phoneId getNewDefaultDataSubId newDefaultDataPhoneId:" + paramInt);
        return SubscriptionManager.getSubIdUsingPhoneId(paramInt);
      } 
      if (getPrePhoneId(paramArrayOfint) == 0) {
        paramInt = 1;
        log("phoneId getNewDefaultDataSubId newDefaultDataPhoneId:" + paramInt);
        return SubscriptionManager.getSubIdUsingPhoneId(paramInt);
      } 
      paramInt = 0;
      log("phoneId getNewDefaultDataSubId newDefaultDataPhoneId:" + paramInt);
      return SubscriptionManager.getSubIdUsingPhoneId(paramInt);
    } 
    if (isHaveCard(paramArrayOfint)) {
      int j = 0;
      while (true) {
        paramInt = i;
        if (j < this.mPhoneNum) {
          if (paramArrayOfint[j] != 0)
            i = j; 
          j++;
          continue;
        } 
        log("phoneId getNewDefaultDataSubId newDefaultDataPhoneId:" + paramInt);
        return SubscriptionManager.getSubIdUsingPhoneId(paramInt);
      } 
    } 
    log("phoneId getNewDefaultDataSubId newDefaultDataPhoneId:" + paramInt);
    return SubscriptionManager.getSubIdUsingPhoneId(paramInt);
  }
  
  private int getNewSimSlot() {
    log("getNewSimSlot");
    return SystemProperties.getInt("persist.radio.new.sim.slot", 0);
  }
  
  private int getPhoneIdIfHaveOnlyOneValidCard(String[] paramArrayOfString) {
    int j = 0;
    int k = -1;
    int i = 0;
    while (i < this.mPhoneNum) {
      int m = j;
      if (!TextUtils.equals(paramArrayOfString[i], "N/A")) {
        m = j + 1;
        k = i;
      } 
      i++;
      j = m;
    } 
    return (1 == j) ? k : -1;
  }
  
  private int getPreCardCount(int[] paramArrayOfint) {
    int j = 0;
    int i = 0;
    while (i < this.mPhoneNum) {
      int k = j;
      if (paramArrayOfint[i] == 2)
        k = j + 1; 
      i++;
      j = k;
    } 
    return j;
  }
  
  private String getPreIccid(int paramInt) {
    return SystemProperties.get(this.PROPERTY_ICCID_PRE[paramInt]);
  }
  
  private int getPrePhoneId(int[] paramArrayOfint) {
    int j = 0;
    for (int i = 0; i < this.mPhoneNum; i++) {
      log("getPrePhoneId cardTypes[" + i + "]:" + paramArrayOfint[i]);
      if (paramArrayOfint[i] == 2)
        j = i; 
    } 
    log("getPrePhoneId prePhoneId:" + j);
    return j;
  }
  
  private int getSimStatus() {
    log("getSimStatus");
    return SystemProperties.getInt("persist.radio.sim.status", 0);
  }
  
  private void handleDataEnableForOp02(int paramInt) {
    log("handleDataEnableForOp02: insertedSimCount = " + paramInt);
    if (TelephonyManager.getDefault() == null) {
      loge("TelephonyManager.getDefault() return null");
      return;
    } 
    int i = SubscriptionManager.getDefaultDataSubId();
    if (!SubscriptionManager.isValidSubscriptionId(this.mPrevDefaultDataSubId)) {
      if (SubscriptionManager.isValidSubscriptionId(this.mLastValidDefaultDataSubId)) {
        boolean bool = getDataEnabledFromSetting(this.mLastValidDefaultDataSubId);
        log("setEnable by lastValidDataSub's setting = " + bool);
        setDataEnabled(SubscriptionManager.getPhoneId(i), bool);
        return;
      } 
      setDataEnabled(SubscriptionManager.getPhoneId(i), true);
      return;
    } 
    if (SubscriptionManager.isValidSubscriptionId(this.mPrevDefaultDataSubId) && SubscriptionManager.isValidSubscriptionId(i)) {
      if (this.mPrevDefaultDataSubId != i) {
        if (getDataEnabledFromSetting(this.mPrevDefaultDataSubId)) {
          setDataEnabled(SubscriptionManager.getPhoneId(i), true);
          return;
        } 
        setDataEnabled(SubscriptionManager.getPhoneId(i), false);
        return;
      } 
      if (paramInt == 2) {
        if (SubscriptionManager.getPhoneId(i) == 0) {
          paramInt = 1;
        } else {
          paramInt = 0;
        } 
        if (getDataEnabledFromSetting(i)) {
          setDataEnabled(paramInt, false);
          return;
        } 
      } 
    } 
  }
  
  private void handleDefaultDataOp01MultiSims() {
    int i = -1;
    int[] arrayOfInt1 = new int[this.mPhoneNum];
    int[] arrayOfInt2 = new int[this.mPhoneNum];
    log("OP01 C2: Multi SIM: E");
    if (RadioCapabilitySwitchUtil.getSimInfo(arrayOfInt1, arrayOfInt2, 0)) {
      boolean bool2 = false;
      boolean bool1 = false;
      int k = -1;
      int m = SubscriptionController.getInstance().getDefaultDataSubId();
      log("OP01 C2: Multi SIM: preDataSub=" + m);
      int j = 0;
      while (j < this.mPhoneNum) {
        boolean bool3;
        boolean bool4;
        int n;
        int i1;
        if (arrayOfInt1[j] == 2) {
          bool3 = bool2;
          bool4 = bool1;
          n = k;
          i1 = i;
          if (!bool2) {
            bool3 = true;
            i1 = j;
            n = k;
            bool4 = bool1;
          } 
        } else {
          bool3 = bool2;
          bool4 = bool1;
          n = k;
          i1 = i;
          if (!bool1) {
            bool4 = true;
            n = j;
            bool3 = bool2;
            i1 = i;
          } 
        } 
        i = SubscriptionManager.getSubIdUsingPhoneId(j);
        log("OP01 C2: sub=" + i);
        if (m != i && i > -1)
          setDataEnabled(j, false); 
        j++;
        bool2 = bool3;
        bool1 = bool4;
        k = n;
        i = i1;
      } 
      if (bool2 && bool1) {
        j = SubscriptionManager.getSubIdUsingPhoneId(i);
        if (j > -1 && j != m) {
          log("OP01 C2: Multi SIM: CMCC + Other, set default data to CMCC");
          setDefaultData(i);
          setDataEnabled(k, false);
        } 
      } 
      log("OP01 C2: Multi SIM: Turn off non default data");
      if (this.mIntent != null)
        turnOffNewSimData(this.mIntent); 
      updateDataEnableProperty();
    } 
  }
  
  private boolean isCanSwitch() {
    if (this.mAirplaneModeOn) {
      log("DataSubselector,isCanSwitch mAirplaneModeOn = " + this.mAirplaneModeOn);
      return false;
    } 
    int j = TelephonyManager.getDefault().getPhoneCount();
    for (int i = 0; i < j; i++) {
      int k = TelephonyManager.from(this.mContext).getSimState(i);
      if (k == 2 || k == 3 || k == 4 || k == 6) {
        log("DataSubselector,sim locked ,isCanSwitch simState = " + k);
        return false;
      } 
    } 
    return true;
  }
  
  private boolean isCardPresentAndSubReady() {
    return (SubscriptionController.getInstance().getActiveSubInfoCount("com.android.phone") > 0);
  }
  
  private boolean isHaveCard(int[] paramArrayOfint) {
    for (int i = 0; i < this.mPhoneNum; i++) {
      if (paramArrayOfint[i] != 0)
        return true; 
    } 
    return false;
  }
  
  private boolean isHaveDataCard(int[] paramArrayOfint) {
    for (int i = 0; i < this.mPhoneNum; i++) {
      if (paramArrayOfint[i] == 1)
        return true; 
    } 
    return false;
  }
  
  private boolean isHaveMultiCard(int[] paramArrayOfint) {
    for (int i = 0; i < this.mPhoneNum; i++) {
      if (paramArrayOfint[i] == 0)
        return false; 
    } 
    return true;
  }
  
  private boolean isNeedWaitUnlock(String paramString) {
    return SystemProperties.getBoolean(paramString, false);
  }
  
  private boolean isNotHaveCard(String paramString) {
    return (paramString == null || paramString.isEmpty());
  }
  
  private boolean isNotHaveCard(int[] paramArrayOfint) {
    for (int i = 0; i < this.mPhoneNum; i++) {
      if (paramArrayOfint[i] != 0)
        return false; 
    } 
    return true;
  }
  
  private boolean isOP01OMSupport() {
    return SystemProperties.get("ro.cmcc_light_cust_support").equals("1");
  }
  
  private boolean isOP09ASupport() {
    return ("OP09".equals(SystemProperties.get("ro.operator.optr", "")) && "SEGDEFAULT".equals(SystemProperties.get("ro.operator.seg", "")));
  }
  
  private boolean isOP09CSupport() {
    return ("OP09".equals(SystemProperties.get("ro.operator.optr", "")) && "SEGC".equals(SystemProperties.get("ro.operator.seg", "")));
  }
  
  private boolean isOP09Support() {
    return "OP09".equals(SystemProperties.get("ro.operator.optr", ""));
  }
  
  private void log(String paramString) {
    Rlog.d("DataSubSelector", paramString);
  }
  
  private void loge(String paramString) {
    Rlog.e("DataSubSelector", paramString);
  }
  
  private void registerWorldModeReceiver() {
    if (this.mContext == null) {
      log("registerWorldModeReceiver, context is null => return");
      return;
    } 
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("android.intent.action.ACTION_WORLD_MODE_CHANGED");
    this.mContext.registerReceiver(this.mWorldModeReceiver, intentFilter);
    this.mHasRegisterWorldModeReceiver = true;
  }
  
  private void resetNewSimSlot() {
    log("resetNewSimSlot");
    SystemProperties.set("persist.radio.new.sim.slot", "");
  }
  
  private void resetSimStatus() {
    log("resetSimStatus");
    SystemProperties.set("persist.radio.sim.status", "");
  }
  
  private boolean setCapability(int paramInt) {
    // Byte code:
    //   0: aload_0
    //   1: invokespecial isOP09CSupport : ()Z
    //   4: ifeq -> 27
    //   7: aload_0
    //   8: invokespecial isCanSwitch : ()Z
    //   11: ifne -> 27
    //   14: aload_0
    //   15: ldc_w 'setCapability: isCanSwitch = false'
    //   18: invokespecial log : (Ljava/lang/String;)V
    //   21: iconst_1
    //   22: istore #4
    //   24: iload #4
    //   26: ireturn
    //   27: aload_0
    //   28: getfield mPhoneNum : I
    //   31: newarray int
    //   33: astore #5
    //   35: iconst_1
    //   36: istore_3
    //   37: aload_0
    //   38: new java/lang/StringBuilder
    //   41: dup
    //   42: invokespecial <init> : ()V
    //   45: ldc_w 'setCapability: '
    //   48: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   51: iload_1
    //   52: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   55: invokevirtual toString : ()Ljava/lang/String;
    //   58: invokespecial log : (Ljava/lang/String;)V
    //   61: ldc 'persist.radio.simswitch'
    //   63: ldc_w ''
    //   66: invokestatic get : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
    //   69: astore #7
    //   71: aload_0
    //   72: new java/lang/StringBuilder
    //   75: dup
    //   76: invokespecial <init> : ()V
    //   79: ldc_w 'current 3G Sim = '
    //   82: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   85: aload #7
    //   87: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   90: invokevirtual toString : ()Ljava/lang/String;
    //   93: invokespecial log : (Ljava/lang/String;)V
    //   96: ldc_w 'phone'
    //   99: invokestatic getService : (Ljava/lang/String;)Landroid/os/IBinder;
    //   102: invokestatic asInterface : (Landroid/os/IBinder;)Lcom/android/internal/telephony/ITelephony;
    //   105: astore #8
    //   107: ldc_w 'phoneEx'
    //   110: invokestatic getService : (Ljava/lang/String;)Landroid/os/IBinder;
    //   113: invokestatic asInterface : (Landroid/os/IBinder;)Lcom/mediatek/internal/telephony/ITelephonyEx;
    //   116: astore #6
    //   118: aload #8
    //   120: ifnull -> 128
    //   123: aload #6
    //   125: ifnonnull -> 137
    //   128: aload_0
    //   129: ldc_w 'Can not get phone service'
    //   132: invokespecial loge : (Ljava/lang/String;)V
    //   135: iconst_0
    //   136: ireturn
    //   137: aload #6
    //   139: invokeinterface isCapabilitySwitching : ()Z
    //   144: istore #4
    //   146: aload_0
    //   147: new java/lang/StringBuilder
    //   150: dup
    //   151: invokespecial <init> : ()V
    //   154: ldc_w 'current capability switch status = '
    //   157: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   160: iload #4
    //   162: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   165: invokevirtual toString : ()Ljava/lang/String;
    //   168: invokespecial log : (Ljava/lang/String;)V
    //   171: iload #4
    //   173: ifne -> 210
    //   176: aload #7
    //   178: ifnull -> 210
    //   181: aload #7
    //   183: ldc_w ''
    //   186: invokevirtual equals : (Ljava/lang/Object;)Z
    //   189: ifne -> 210
    //   192: aload #7
    //   194: invokestatic parseInt : (Ljava/lang/String;)I
    //   197: iload_1
    //   198: iconst_1
    //   199: iadd
    //   200: if_icmpne -> 210
    //   203: aload_0
    //   204: ldc_w 'Current 3G phone equals target phone, don't trigger switch'
    //   207: invokespecial log : (Ljava/lang/String;)V
    //   210: aload #8
    //   212: iload_1
    //   213: ldc_w 'phone'
    //   216: invokeinterface getRadioAccessFamily : (ILjava/lang/String;)I
    //   221: istore_2
    //   222: aload_0
    //   223: new java/lang/StringBuilder
    //   226: dup
    //   227: invokespecial <init> : ()V
    //   230: ldc_w 'Current phoneRat:'
    //   233: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   236: iload_2
    //   237: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   240: invokevirtual toString : ()Ljava/lang/String;
    //   243: invokespecial log : (Ljava/lang/String;)V
    //   246: aload_0
    //   247: getfield mPhoneNum : I
    //   250: anewarray android/telephony/RadioAccessFamily
    //   253: astore #7
    //   255: iconst_0
    //   256: istore_2
    //   257: iload_2
    //   258: aload_0
    //   259: getfield mPhoneNum : I
    //   262: if_icmpge -> 407
    //   265: iload_1
    //   266: iload_2
    //   267: if_icmpne -> 327
    //   270: aload_0
    //   271: new java/lang/StringBuilder
    //   274: dup
    //   275: invokespecial <init> : ()V
    //   278: ldc_w 'SIM switch to Phone'
    //   281: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   284: iload_2
    //   285: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   288: invokevirtual toString : ()Ljava/lang/String;
    //   291: invokespecial log : (Ljava/lang/String;)V
    //   294: aload #5
    //   296: iload_2
    //   297: invokestatic getInstance : ()Lcom/android/internal/telephony/ProxyController;
    //   300: invokevirtual getMaxRafSupported : ()I
    //   303: iastore
    //   304: aload #7
    //   306: iload_2
    //   307: new android/telephony/RadioAccessFamily
    //   310: dup
    //   311: iload_2
    //   312: aload #5
    //   314: iload_2
    //   315: iaload
    //   316: invokespecial <init> : (II)V
    //   319: aastore
    //   320: iload_2
    //   321: iconst_1
    //   322: iadd
    //   323: istore_2
    //   324: goto -> 257
    //   327: aload #5
    //   329: iload_2
    //   330: invokestatic getInstance : ()Lcom/android/internal/telephony/ProxyController;
    //   333: invokevirtual getMinRafSupported : ()I
    //   336: iastore
    //   337: goto -> 304
    //   340: astore #6
    //   342: aload_0
    //   343: new java/lang/StringBuilder
    //   346: dup
    //   347: invokespecial <init> : ()V
    //   350: ldc_w '[Exception]Set phone rat fail!!! MaxPhoneRat='
    //   353: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   356: aload #5
    //   358: iload_1
    //   359: iaload
    //   360: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   363: invokevirtual toString : ()Ljava/lang/String;
    //   366: invokespecial log : (Ljava/lang/String;)V
    //   369: aload #6
    //   371: invokevirtual printStackTrace : ()V
    //   374: iconst_0
    //   375: istore_3
    //   376: iload_3
    //   377: ifne -> 469
    //   380: iload_3
    //   381: istore #4
    //   383: invokestatic isWorldPhoneSwitching : ()Z
    //   386: ifeq -> 24
    //   389: aload_0
    //   390: ldc_w 'world mode switching!'
    //   393: invokespecial log : (Ljava/lang/String;)V
    //   396: aload_0
    //   397: invokespecial registerWorldModeReceiver : ()V
    //   400: aload_0
    //   401: iload_1
    //   402: putfield mPhoneId : I
    //   405: iload_3
    //   406: ireturn
    //   407: invokestatic isWorldPhoneSwitching : ()Z
    //   410: ifeq -> 425
    //   413: aload_0
    //   414: ldc_w 'world mode switching, don't trigger sim switch.'
    //   417: invokespecial log : (Ljava/lang/String;)V
    //   420: iconst_0
    //   421: istore_3
    //   422: goto -> 376
    //   425: aload #6
    //   427: aload #7
    //   429: invokeinterface setRadioCapability : ([Landroid/telephony/RadioAccessFamily;)Z
    //   434: ifne -> 376
    //   437: aload_0
    //   438: new java/lang/StringBuilder
    //   441: dup
    //   442: invokespecial <init> : ()V
    //   445: ldc_w 'Set phone rat fail!!! MaxPhoneRat='
    //   448: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   451: aload #5
    //   453: iload_1
    //   454: iaload
    //   455: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   458: invokevirtual toString : ()Ljava/lang/String;
    //   461: invokespecial log : (Ljava/lang/String;)V
    //   464: iconst_0
    //   465: istore_3
    //   466: goto -> 376
    //   469: iload_3
    //   470: istore #4
    //   472: aload_0
    //   473: getfield mHasRegisterWorldModeReceiver : Z
    //   476: ifeq -> 24
    //   479: aload_0
    //   480: invokespecial unRegisterWorldModeReceiver : ()V
    //   483: aload_0
    //   484: iconst_m1
    //   485: putfield mPhoneId : I
    //   488: iload_3
    //   489: ireturn
    // Exception table:
    //   from	to	target	type
    //   96	118	340	android/os/RemoteException
    //   128	135	340	android/os/RemoteException
    //   137	171	340	android/os/RemoteException
    //   181	210	340	android/os/RemoteException
    //   210	255	340	android/os/RemoteException
    //   257	265	340	android/os/RemoteException
    //   270	304	340	android/os/RemoteException
    //   304	320	340	android/os/RemoteException
    //   327	337	340	android/os/RemoteException
    //   407	420	340	android/os/RemoteException
    //   425	464	340	android/os/RemoteException
  }
  
  private boolean setCapabilityIfNeeded(int paramInt) {
    return isOP09ASupport() ? true : setCapability(paramInt);
  }
  
  private void setDataEnabled(int paramInt, boolean paramBoolean) {
    log("setDataEnabled: phoneId=" + paramInt + ", enable=" + paramBoolean);
    TelephonyManager telephonyManager = TelephonyManager.getDefault();
    if (telephonyManager != null) {
      if (paramInt == -1) {
        telephonyManager.setDataEnabled(paramBoolean);
        return;
      } 
    } else {
      return;
    } 
    if (!paramBoolean) {
      paramInt = PhoneFactory.getPhone(paramInt).getSubId();
      log("Set Sub" + paramInt + " to disable");
      telephonyManager.setDataEnabled(paramInt, paramBoolean);
      return;
    } 
    int i = 0;
    while (true) {
      if (i < this.mPhoneNum) {
        int j = PhoneFactory.getPhone(i).getSubId();
        if (i != paramInt) {
          log("Set Sub" + j + " to disable");
          telephonyManager.setDataEnabled(j, false);
        } else {
          log("Set Sub" + j + " to enable");
          telephonyManager.setDataEnabled(j, true);
        } 
        i++;
        continue;
      } 
      return;
    } 
  }
  
  private void setDefaultData(int paramInt) {
    SubscriptionController subscriptionController = SubscriptionController.getInstance();
    paramInt = SubscriptionManager.getSubIdUsingPhoneId(paramInt);
    int i = SubscriptionManager.getDefaultDataSubId();
    this.mPrevDefaultDataSubId = i;
    setLastValidDefaultDataSub(i);
    log("setDefaultData: " + paramInt + ", current default sub:" + i + "last valid default sub:" + this.mLastValidDefaultDataSubId);
    if (paramInt != i && paramInt >= -1) {
      subscriptionController.setDefaultDataSubIdWithoutCapabilitySwitch(paramInt);
      return;
    } 
    log("setDefaultData: default data unchanged");
  }
  
  private void setDefaultData(String[] paramArrayOfString, int paramInt) {
    log("setDefaultData defDataPhoneId :" + paramInt);
    int[] arrayOfInt = new int[4];
    arrayOfInt[0] = 0;
    arrayOfInt[1] = 0;
    arrayOfInt[2] = 0;
    arrayOfInt[3] = 0;
    String str2 = "";
    String str1 = str2;
    if (paramInt >= 0)
      if (paramInt >= this.PROPERTY_ICCID_SIM.length) {
        log("phoneId out of boundary :" + paramInt);
        str1 = str2;
      } else {
        str1 = SystemProperties.get("persist.radio.data.iccid");
      }  
    for (int i = 0; i < this.mPhoneNum; i++) {
      log("setDefaultData currIccId[i] :" + paramArrayOfString[i]);
      log("setDefaultData defaultIccid :" + str1);
      arrayOfInt[i] = getCurrentCardType(paramArrayOfString[i], str1);
      log("setDefaultData cardTypes :" + arrayOfInt[i]);
    } 
    if (isHaveDataCard(arrayOfInt)) {
      log("setDefaultData have data card");
      updateIccId();
      return;
    } 
    if (isNotHaveCard(arrayOfInt)) {
      log("all card is deactivated or no card, return!");
      return;
    } 
    setDefaultDataToSubId(getNewDefaultDataSubIdByNubia(arrayOfInt, paramInt));
  }
  
  private void setDefaultDataForNubia(String[] paramArrayOfString) {
    int i = getPhoneIdIfHaveOnlyOneValidCard(paramArrayOfString);
    if (-1 != i) {
      log("have only one valid card , phoneId = " + i + ", set as default data slot!");
      if (!RadioManager.isCardActive(SystemProperties.get(this.PROPERTY_ICCID[i]))) {
        log("Phone[" + i + "] is only card and deactive, setRadioPower on");
        RadioManager.getInstance().notifySimModeChange(true, i);
      } 
      setDefaultDataToSubId(SubscriptionController.getInstance().getSubIdUsingPhoneId(i));
      return;
    } 
    i = SubscriptionManager.getPhoneId(SubscriptionController.getInstance().getDefaultDataSubId());
    log("subSelectorForNubia current defDataPhoneId = " + i);
    setDefaultData(paramArrayOfString, i);
  }
  
  private void setDefaultDataToOnlyCard(String[] paramArrayOfString) {
    if (paramArrayOfString != null && this.mPhoneNum == 2) {
      if (isNotHaveCard(paramArrayOfString[0]) && !isNotHaveCard(paramArrayOfString[1])) {
        setCapability(1);
        return;
      } 
    } else {
      return;
    } 
    if (!isNotHaveCard(paramArrayOfString[0]) && isNotHaveCard(paramArrayOfString[1])) {
      setCapability(0);
      return;
    } 
  }
  
  private void setDefaultDataToSubId(int paramInt) {
    log("setDefaultData newDataSubId = " + paramInt);
    if (SubscriptionManager.isUsableSubIdValue(paramInt)) {
      SubscriptionController.getInstance().setDefaultDataSubId(paramInt);
      updateIccId();
      int i = SubscriptionController.getInstance().getPhoneId(paramInt);
      TelephonyManager telephonyManager = TelephonyManager.getDefault();
      boolean bool = true;
      if (telephonyManager != null)
        bool = telephonyManager.getDataEnabled(paramInt); 
      setDataEnabled(i, bool);
    } 
  }
  
  private void setLastValidDefaultDataSub(int paramInt) {
    if (SubscriptionManager.isValidSubscriptionId(paramInt)) {
      log("setLastValidDefaultDataSub = " + paramInt);
      this.mLastValidDefaultDataSubId = paramInt;
      return;
    } 
    log("because invalid id to set, keep LastValidDefaultDataSub = " + this.mLastValidDefaultDataSubId);
  }
  
  private void setNeedWaitUnlock(String paramString1, String paramString2) {
    SystemProperties.set(paramString1, paramString2);
  }
  
  private void setNewSimSlot(Intent paramIntent) {
    if (paramIntent == null) {
      log("setNewSimSlot, intent is null => return");
      return;
    } 
    log("setNewSimSlot");
    SystemProperties.set("persist.radio.new.sim.slot", Integer.toString(paramIntent.getIntExtra("newSIMSlot", 0)));
  }
  
  private void setSimStatus(Intent paramIntent) {
    if (paramIntent == null) {
      log("setSimStatus, intent is null => return");
      return;
    } 
    log("setSimStatus");
    SystemProperties.set("persist.radio.sim.status", Integer.toString(paramIntent.getIntExtra("simDetectStatus", 0)));
  }
  
  private void subSelectorForC2k6m(Intent paramIntent) {
    log("DataSubSelector for C2K6M: only for capability switch;");
    byte b = -1;
    String[] arrayOfString = new String[this.mPhoneNum];
    String str2 = "";
    int i = SubscriptionManager.getPhoneId(SubscriptionController.getInstance().getDefaultDataSubId());
    String str1 = str2;
    if (i >= 0)
      if (i >= this.PROPERTY_ICCID_SIM.length) {
        log("phoneId out of boundary :" + i);
        str1 = str2;
      } else {
        log("default data phoneId from sub = " + i);
        str1 = SystemProperties.get(this.PROPERTY_ICCID_SIM[i]);
      }  
    log("Default data Iccid = " + str1);
    if ("".equals(str1) || "N/A".equals(str1)) {
      log("Default data Iccid N/A or null,subSelectorForNubia");
      subSelectorForNubia();
      updateDataEnableProperty();
      resetSimStatus();
      resetNewSimSlot();
      return;
    } 
    i = 0;
    while (true) {
      int j = b;
      if (i < this.mPhoneNum) {
        arrayOfString[i] = SystemProperties.get(this.PROPERTY_ICCID[i]);
        if (arrayOfString[i] == null || "".equals(arrayOfString[i])) {
          log("error: iccid not found, wait for next sub ready");
          return;
        } 
        if (str1.equals(arrayOfString[i])) {
          j = i;
        } else {
          i++;
          continue;
        } 
      } 
      if (!RadioManager.isCardActive(str1)) {
        log("Default data Iccid = " + str1 + " is unactive");
        j = -1;
      } 
      log("Default data phoneid = " + j);
      if (j != -1) {
        setCapability(j);
        str2 = SystemProperties.get("persist.radio.simswitch.iccid");
        if (str1.equals("") || str1.equals("N/A") || str2.equals("N/A") || str2.equals("") || !str1.equals(str2)) {
          subSelectorForNubia();
        } else {
          updateIccId();
        } 
      } else {
        subSelectorForNubia();
      } 
      updateDataEnableProperty();
      resetSimStatus();
      resetNewSimSlot();
      return;
    } 
  }
  
  private void subSelectorForNubia() {
    Message message;
    if (!isCardPresentAndSubReady()) {
      log("subInfo not ready, return!");
      return;
    } 
    String[] arrayOfString = new String[this.mPhoneNum];
    int j = 0;
    int i = 0;
    while (i < this.mPhoneNum) {
      arrayOfString[i] = getCurrentIccid(i);
      log("subSelectorForNubia phoneId = " + i + ", currIccId = " + arrayOfString[i]);
      int k = j;
      if ("N/A".equals(arrayOfString[i]))
        k = j + 1; 
      i++;
      j = k;
    } 
    log("subSelectorForNubia noCardCount = " + j);
    if (j == this.mPhoneNum) {
      log("should not be here, return!");
      return;
    } 
    if (j > 0) {
      message = this.mHandler.obtainMessage(0, 3000, -1);
      this.mHandler.sendMessageDelayed(message, 3000L);
      log("setDefaultDataForNubia after 3000");
      return;
    } 
    setDefaultDataForNubia((String[])message);
  }
  
  private void subSelectorForOm(Intent paramIntent) {
    if (SystemProperties.get("ro.mtk_c2k_support").equals("1")) {
      if (!isOP09CSupport())
        turnOffNewSimData(paramIntent); 
      updateDataEnableProperty();
      return;
    } 
    log("DataSubSelector for OM: only for capability switch; for default data, use google");
    byte b = -1;
    String[] arrayOfString = new String[this.mPhoneNum];
    turnOffNewSimData(paramIntent);
    String str2 = "";
    int i = SubscriptionManager.getPhoneId(SubscriptionController.getInstance().getDefaultDataSubId());
    String str1 = str2;
    if (i >= 0)
      if (i >= this.PROPERTY_ICCID_SIM.length) {
        log("phoneId out of boundary :" + i);
        str1 = str2;
      } else {
        log("defDataPhoneId =" + i);
        str1 = SystemProperties.get(this.PROPERTY_ICCID_SIM[i]);
      }  
    log("Default data Iccid = " + str1);
    if ("N/A".equals(str1) || "".equals(str1)) {
      log("Default data Iccid N/A or null,subSelectorForNubia");
      subSelectorForNubia();
      updateDataEnableProperty();
      resetSimStatus();
      resetNewSimSlot();
      return;
    } 
    i = 0;
    while (true) {
      int j = b;
      if (i < this.mPhoneNum) {
        arrayOfString[i] = SystemProperties.get(this.PROPERTY_ICCID[i]);
        if (arrayOfString[i] == null || "".equals(arrayOfString[i])) {
          log("error: iccid not found, wait for next sub ready");
          return;
        } 
        if (str1.equals(arrayOfString[i])) {
          j = i;
        } else {
          if ("N/A".equals(arrayOfString[i])) {
            log("clear mcc.mnc:" + i);
            if (i == 0) {
              str2 = "gsm.sim.ril.mcc.mnc";
            } else {
              str2 = "gsm.sim.ril.mcc.mnc." + (i + 1);
            } 
            SystemProperties.set(str2, "");
          } 
          i++;
          continue;
        } 
      } 
      if (RadioCapabilitySwitchUtil.isAnySimLocked(this.mPhoneNum)) {
        log("DataSubSelector for OM: do not switch because of sim locking");
        setNeedWaitUnlock("persist.radio.unlock", "true");
        this.mIntent = paramIntent;
        setSimStatus(paramIntent);
        setNewSimSlot(paramIntent);
        return;
      } 
      log("DataSubSelector for OM: no pin lock");
      setNeedWaitUnlock("persist.radio.unlock", "false");
      if (this.mAirplaneModeOn) {
        log("DataSubSelector for OM: do not switch because of mAirplaneModeOn");
        this.mIsNeedWaitAirplaneModeOff = true;
        this.mIntent = paramIntent;
        setSimStatus(paramIntent);
        setNewSimSlot(paramIntent);
        return;
      } 
      if (!RadioManager.isCardActive(str1)) {
        log("Default data Iccid = " + str1 + " is unactive");
        j = -1;
      } 
      log("Default data phoneId = " + j);
      if (j != -1) {
        setCapability(j);
        String str = SystemProperties.get("persist.radio.simswitch.iccid");
        if (str1.equals("") || str1.equals("N/A") || str.equals("N/A") || str.equals("") || !str1.equals(str)) {
          subSelectorForNubia();
        } else {
          updateIccId();
        } 
      } else {
        subSelectorForNubia();
      } 
      updateDataEnableProperty();
      resetSimStatus();
      resetNewSimSlot();
      return;
    } 
  }
  
  private void subSelectorForOp01(Intent paramIntent) {
    int k;
    int i = -1;
    int n = 0;
    int m = 0;
    if (paramIntent == null) {
      k = getSimStatus();
    } else {
      k = paramIntent.getIntExtra("simDetectStatus", 0);
    } 
    String[] arrayOfString = new String[this.mPhoneNum];
    log("DataSubSelector for op01");
    String str2 = "";
    int j = SubscriptionManager.getPhoneId(SubscriptionController.getInstance().getDefaultDataSubId());
    String str1 = str2;
    if (j >= 0)
      if (j >= this.PROPERTY_ICCID_SIM.length) {
        log("phoneId out of boundary :" + j);
        str1 = str2;
      } else {
        str1 = SystemProperties.get(this.PROPERTY_ICCID_SIM[j]);
      }  
    log("Default data Iccid = " + str1);
    for (j = 0; j < this.mPhoneNum; j++) {
      arrayOfString[j] = SystemProperties.get(this.PROPERTY_ICCID[j]);
      if (arrayOfString[j] == null || "".equals(arrayOfString[j])) {
        log("error: iccid not found, wait for next sub ready");
        return;
      } 
      if (str1.equals(arrayOfString[j]))
        i = j; 
      log("currIccId[" + j + "] : " + arrayOfString[j]);
      if (!"N/A".equals(arrayOfString[j])) {
        n++;
        m |= 1 << j;
      } else {
        log("clear mcc.mnc:" + j);
        if (j == 0) {
          str2 = "gsm.sim.ril.mcc.mnc";
        } else {
          str2 = "gsm.sim.ril.mcc.mnc." + (j + 1);
        } 
        SystemProperties.set(str2, "");
      } 
    } 
    if (RadioCapabilitySwitchUtil.isAnySimLocked(this.mPhoneNum)) {
      log("DataSubSelector for OP01: do not switch because of sim locking");
      setNeedWaitUnlock("persist.radio.unlock", "true");
      this.mIntent = paramIntent;
      setSimStatus(paramIntent);
      setNewSimSlot(paramIntent);
      return;
    } 
    log("DataSubSelector for OP01: no pin lock");
    setNeedWaitUnlock("persist.radio.unlock", "false");
    if (this.mAirplaneModeOn) {
      log("DataSubSelector for OP01: do not switch because of mAirplaneModeOn");
      this.mIsNeedWaitAirplaneModeOff = true;
      this.mIntent = paramIntent;
      setSimStatus(paramIntent);
      setNewSimSlot(paramIntent);
      return;
    } 
    log("Inserted SIM count: " + n + ", insertedStatus: " + m);
    if (n == 0) {
      log("OP01 C0: No SIM inserted, do nothing");
    } else {
      if (n == 1) {
        j = 0;
        while (true) {
          n = i;
          if (j < this.mPhoneNum)
            if ((1 << j & m) != 0) {
              n = j;
            } else {
              j++;
              continue;
            }  
          log("OP01 C1: Single SIM: Set Default data to phone:" + n);
          if (setCapability(n) && k != 4)
            setDefaultData(n); 
          turnOffNewSimData(paramIntent);
          resetSimStatus();
          resetNewSimSlot();
          return;
        } 
      } 
      if (n >= 2) {
        if (paramIntent != null && "android.intent.action.ACTION_SUBINFO_RECORD_UPDATED".equals(paramIntent.getAction()))
          this.mIntent = paramIntent; 
        if (!checkOp01CapSwitch6m()) {
          this.mIsNeedWaitImsi = true;
          this.mIntent = paramIntent;
          setSimStatus(paramIntent);
          setNewSimSlot(paramIntent);
          return;
        } 
      } 
    } 
    resetSimStatus();
    resetNewSimSlot();
  }
  
  private void subSelectorForOp01OM() {
    byte b = -1;
    int k = 0;
    int j = 0;
    String[] arrayOfString = new String[this.mPhoneNum];
    log("DataSubSelector for op01OM");
    int i = 0;
    while (i < this.mPhoneNum) {
      arrayOfString[i] = SystemProperties.get(this.PROPERTY_ICCID[i]);
      if (arrayOfString[i] == null || "".equals(arrayOfString[i])) {
        log("error: iccid not found, wait for next sub ready");
        return;
      } 
      log("currIccId[" + i + "] : " + arrayOfString[i]);
      int n = k;
      int m = j;
      if (!"N/A".equals(arrayOfString[i])) {
        n = k + 1;
        m = j | 1 << i;
      } 
      i++;
      k = n;
      j = m;
    } 
    log("Inserted SIM count: " + k + ", insertedStatus: " + j);
    if (k != 1) {
      log("DataSubSelector for OP01OM: do not switch because of SimCount != 1");
      return;
    } 
    if (RadioCapabilitySwitchUtil.isAnySimLocked(this.mPhoneNum)) {
      log("DataSubSelector for OP01OM: do not switch because of sim locking");
      return;
    } 
    if (this.mAirplaneModeOn) {
      log("DataSubSelector for OP01OM: do not switch because of mAirplaneModeOn");
      return;
    } 
    i = 0;
    while (true) {
      int m = b;
      if (i < this.mPhoneNum)
        if ((1 << i & j) != 0) {
          m = i;
        } else {
          i++;
          continue;
        }  
      log("OP01OM: Single SIM: Set Default data to phone:" + m);
      if (setCapability(m)) {
        setDefaultData(m);
        return;
      } 
      return;
    } 
  }
  
  private void subSelectorForOp02() {
    byte b = -1;
    int k = 0;
    int j = 0;
    String[] arrayOfString = new String[this.mPhoneNum];
    log("DataSubSelector for op02 (subSelectorForOp02)");
    int i;
    for (i = 0; i < this.mPhoneNum; i++) {
      arrayOfString[i] = SystemProperties.get(this.PROPERTY_ICCID[i]);
      log("currIccid[" + i + "] : " + arrayOfString[i]);
      if (arrayOfString[i] == null || "".equals(arrayOfString[i])) {
        log("error: iccid not found, wait for next sub ready");
        return;
      } 
      if (!"N/A".equals(arrayOfString[i])) {
        k++;
        j |= 1 << i;
      } else {
        String str;
        log("clear mcc.mnc:" + i);
        if (i == 0) {
          str = "gsm.sim.ril.mcc.mnc";
        } else {
          str = "gsm.sim.ril.mcc.mnc." + (i + 1);
        } 
        SystemProperties.set(str, "");
        log("sim index: " + i + " not inserted");
      } 
    } 
    if (RadioCapabilitySwitchUtil.isAnySimLocked(this.mPhoneNum)) {
      log("DataSubSelector for OP02: do not switch because of sim locking");
      setNeedWaitUnlock("persist.radio.unlock", "true");
      return;
    } 
    log("DataSubSelector for OP02: no pin lock");
    setNeedWaitUnlock("persist.radio.unlock", "false");
    if (this.mAirplaneModeOn) {
      log("DataSubSelector for OP02: do not switch because of mAirplaneModeOn");
      this.mIsNeedWaitAirplaneModeOff = true;
      return;
    } 
    log("Inserted SIM count: " + k + ", insertedStatus: " + j);
    if (k == 0) {
      log("C0: No SIM inserted: set default data unset");
      setDefaultData(-1);
    } else {
      if (k == 1) {
        i = 0;
        while (true) {
          int m = b;
          if (i < this.mPhoneNum)
            if ((1 << i & j) != 0) {
              m = i;
            } else {
              i++;
              continue;
            }  
          log("C1: Single SIM inserted: set default data to phone: " + m);
          if (setCapability(m)) {
            setDefaultData(m);
            handleDataEnableForOp02(k);
          } 
          updateDataEnableProperty();
          return;
        } 
      } 
      if (k >= 2 && !checkOp02CapSwitch(0))
        this.mIsNeedWaitImsi = true; 
    } 
    updateDataEnableProperty();
  }
  
  private void subSelectorForOp02(Intent paramIntent) {
    int i;
    byte b = -1;
    int k = 0;
    int j = 0;
    if (paramIntent == null) {
      i = getSimStatus();
    } else {
      i = paramIntent.getIntExtra("simDetectStatus", 0);
    } 
    String[] arrayOfString = new String[this.mPhoneNum];
    log("DataSubSelector for OP02");
    int m;
    for (m = 0; m < this.mPhoneNum; m++) {
      arrayOfString[m] = SystemProperties.get(this.PROPERTY_ICCID[m]);
      if (arrayOfString[m] == null || "".equals(arrayOfString[m])) {
        log("error: iccid not found, wait for next sub ready");
        return;
      } 
      if (!"N/A".equals(arrayOfString[m])) {
        k++;
        j |= 1 << m;
      } else {
        String str;
        log("clear mcc.mnc:" + m);
        if (m == 0) {
          str = "gsm.sim.ril.mcc.mnc";
        } else {
          str = "gsm.sim.ril.mcc.mnc." + (m + 1);
        } 
        SystemProperties.set(str, "");
      } 
    } 
    if (RadioCapabilitySwitchUtil.isAnySimLocked(this.mPhoneNum)) {
      log("DataSubSelector for OP02: do not switch because of sim locking");
      setNeedWaitUnlock("persist.radio.unlock", "true");
      setSimStatus(paramIntent);
      setNewSimSlot(paramIntent);
      return;
    } 
    log("DataSubSelector for OP02: no pin lock");
    setNeedWaitUnlock("persist.radio.unlock", "false");
    if (this.mAirplaneModeOn) {
      log("DataSubSelector for OP02: do not switch because of mAirplaneModeOn");
      this.mIsNeedWaitAirplaneModeOff = true;
      setSimStatus(paramIntent);
      setNewSimSlot(paramIntent);
      return;
    } 
    log("Inserted SIM count: " + k + ", insertedStatus: " + j);
    if (i == 4) {
      log("OP02 C0: Inserted status no change, do nothing");
    } else if (k == 0) {
      log("OP02 C1: No SIM inserted, set data unset");
      setDefaultData(-1);
    } else {
      if (k == 1) {
        i = 0;
        while (true) {
          m = b;
          if (i < this.mPhoneNum)
            if ((1 << i & j) != 0) {
              m = i;
            } else {
              i++;
              continue;
            }  
          log("OP02 C2: Single SIM: Set Default data to phone:" + m);
          setDefaultData(m);
          handleDataEnableForOp02(k);
          updateDataEnableProperty();
          resetSimStatus();
          resetNewSimSlot();
          return;
        } 
      } 
      if (k >= 2) {
        log("OP02 C3: Multi SIM: Set Default data to phone1");
        setDefaultData(0);
        handleDataEnableForOp02(k);
      } 
    } 
    updateDataEnableProperty();
    resetSimStatus();
    resetNewSimSlot();
  }
  
  private void subSelectorForOp09(Intent paramIntent) {
    if (paramIntent == null) {
      log("OP09: intent is null, ignore!");
      return;
    } 
    int j = paramIntent.getIntExtra("simDetectStatus", -1);
    int k = paramIntent.getIntExtra("simCount", -1);
    SubscriptionController subscriptionController = SubscriptionController.getInstance();
    int[] arrayOfInt = subscriptionController.getActiveSubIdList();
    int i = subscriptionController.getDefaultDataSubId();
    int m = arrayOfInt.length;
    log("OP09: Inserted SIM count: " + m + " Intent count: " + k + " detectedType = " + j + " defaultSub = " + i);
    if (k > -1 && k != m) {
      log("OP09: Intent count and latest sub count not match, ignore and wait next.");
      return;
    } 
    if (m == 0) {
      log("OP09 C0: No SIM inserted, do nothing.");
    } else if (m == 1) {
      switch (j) {
        default:
          log("OP09 C5: ignore unknown detectedType: " + j);
          updateDataEnableProperty();
          return;
        case 1:
          i = subscriptionController.getPhoneId(arrayOfInt[0]);
          log("OP09 C1: a new sim detected, Set Default slot: " + i);
          setDefaultData(i);
          setDataEnabled(i, true);
          setCapabilityIfNeeded(i);
          updateDataEnableProperty();
          return;
        case 2:
          if (arrayOfInt[0] != i) {
            j = subscriptionController.getPhoneId(arrayOfInt[0]);
            log("OP09 C2.1: left a sim not default, Set Default: " + j);
            setDataEnabled(j, getDataEnabledFromSetting(i));
            setDefaultData(j);
            setCapabilityIfNeeded(j);
          } else {
            log("OP09 C2.2: a sim left and it's default sub, do nothing.");
          } 
          updateDataEnableProperty();
          return;
        case 3:
          if (arrayOfInt[0] != i) {
            log("OP09 C3.1: a sim left and is not default data sim, set it as default data sim.");
            j = subscriptionController.getPhoneId(arrayOfInt[0]);
            setDataEnabled(j, getDataEnabledFromSetting(i));
            setDefaultData(j);
          } else {
            log("OP09 C3.2: a sim left with default data on it, do nothing.");
          } 
          updateDataEnableProperty();
          return;
        case 4:
          break;
      } 
      log("OP09 C4: a sim exist and is old, do nothing.");
    } else if (m == 2) {
      switch (j) {
        default:
          log("OP09 C10: ignore unknown detectedType: " + j);
          updateDataEnableProperty();
          return;
        case 1:
          j = paramIntent.getIntExtra("newSIMSlot", 0);
          log("OP09 C6.0: newSimStatus = " + j + " subList[0] = " + arrayOfInt[0] + " subList[1] = " + arrayOfInt[1]);
          if (i == arrayOfInt[0]) {
            log("OP09 C6.1: data on old sim1, turn off SIM2, set capability to SIM1.");
            if (j == 3)
              setDataEnabled(0, true); 
            setDataEnabled(1, false);
            setCapabilityIfNeeded(0);
          } else if (i == arrayOfInt[1]) {
            log("OP09 C6.2: data on old sim2, turn off SIM1, set capability to SIM2.");
            if (j == 3)
              setDataEnabled(1, true); 
            setDataEnabled(0, false);
            setCapabilityIfNeeded(1);
          } else {
            log("OP09 C6.3: new + new or new + old, no default, set sim1 as default.");
            if (j == 1 || j == 2) {
              setDataEnabled(0, getDataEnabledFromSetting(i));
            } else {
              setDataEnabled(0, true);
            } 
            setDataEnabled(1, false);
            setDefaultData(0);
            setCapabilityIfNeeded(0);
          } 
          updateDataEnableProperty();
          return;
        case 2:
          log("OP09 C7: a sim removed and two sim left, not support yet!");
          updateDataEnableProperty();
          return;
        case 3:
          log("OP09 C8: two sims swap slot location, do nothing.");
          updateDataEnableProperty();
          return;
        case 4:
          break;
      } 
      log("OP09 C9: two sims exist and are old, do nothing.");
    } else {
      log("OP09 C11: sim count bigger than 2, not support yet!");
    } 
    updateDataEnableProperty();
  }
  
  private void subSelectorForOp18(Intent paramIntent) {
    int j;
    switch (capability_switch_policy) {
      default:
        log("subSelectorForOp18: Unknow policy, skip");
        return;
      case 0:
        log("subSelectorForOp18: no auto policy, skip");
        return;
      case 1:
        subSelectorForOm(paramIntent);
        return;
      case 2:
        break;
    } 
    byte b = -1;
    int m = 0;
    int k = 0;
    if (paramIntent == null) {
      j = getSimStatus();
    } else {
      j = paramIntent.getIntExtra("simDetectStatus", 0);
    } 
    String[] arrayOfString = new String[this.mPhoneNum];
    log("DataSubSelector for op18");
    int i;
    for (i = 0; i < this.mPhoneNum; i++) {
      arrayOfString[i] = SystemProperties.get(this.PROPERTY_ICCID[i]);
      if (arrayOfString[i] == null || "".equals(arrayOfString[i])) {
        log("error: iccid not found, wait for next sub ready");
        return;
      } 
      log("currIccId[" + i + "] : " + arrayOfString[i]);
      if (!"N/A".equals(arrayOfString[i])) {
        m++;
        k |= 1 << i;
      } else {
        String str1;
        log("clear mcc.mnc:" + i);
        if (i == 0) {
          str1 = "gsm.sim.ril.mcc.mnc";
        } else {
          str1 = "gsm.sim.ril.mcc.mnc." + (i + 1);
        } 
        SystemProperties.set(str1, "");
      } 
    } 
    if (RadioCapabilitySwitchUtil.isAnySimLocked(this.mPhoneNum)) {
      log("DataSubSelector for OP18: do not switch because of sim locking");
      setNeedWaitUnlock("persist.radio.unlock", "true");
      this.mIntent = paramIntent;
      return;
    } 
    log("DataSubSelector for OP18: no pin lock");
    setNeedWaitUnlock("persist.radio.unlock", "false");
    log("Inserted SIM count: " + m + ", insertedStatus: " + k);
    String str = SystemProperties.get("persist.radio.data.iccid");
    log("Default data Iccid = " + str);
    if (m == 0) {
      log("C0: No SIM inserted, set data unset");
      setDefaultData(-1);
      return;
    } 
    if (m == 1) {
      i = 0;
      while (true) {
        m = b;
        if (i < this.mPhoneNum)
          if ((1 << i & k) != 0) {
            m = i;
          } else {
            i++;
            continue;
          }  
        if (j == 1) {
          log("C1: Single SIM + New SIM: Set Default data to phone:" + m);
          if (setCapability(m))
            setDefaultData(m); 
          setDataEnabled(m, true);
          return;
        } 
        if (str == null || "".equals(str)) {
          log("C3: Single SIM + Non Data SIM: Set Default data to phone:" + m);
          if (setCapability(m))
            setDefaultData(m); 
          setDataEnabled(m, true);
          return;
        } 
        if (str.equals(arrayOfString[m])) {
          log("C2: Single SIM + Data SIM: Set Default data to phone:" + m);
          if (setCapability(m)) {
            setDefaultData(m);
            return;
          } 
          return;
        } 
        log("C3: Single SIM + Non Data SIM: Set Default data to phone:" + m);
        if (setCapability(m))
          setDefaultData(m); 
        setDataEnabled(m, true);
        return;
      } 
    } 
    if (m >= 2 && !checkOp18CapSwitch()) {
      this.mIsNeedWaitImsi = true;
      this.mIntent = paramIntent;
      return;
    } 
  }
  
  private void subSelectorForSolution15(Intent paramIntent) {
    log("DataSubSelector for C2K om solution 1.5: capability maybe diff with default data");
    byte b = -1;
    String[] arrayOfString = new String[this.mPhoneNum];
    turnOffNewSimData(paramIntent);
    String str = SystemProperties.get("persist.radio.simswitch.iccid");
    log("capability Iccid = " + str);
    int i = 0;
    while (true) {
      int j = b;
      if (i < this.mPhoneNum) {
        arrayOfString[i] = SystemProperties.get(this.PROPERTY_ICCID[i]);
        if (arrayOfString[i] == null || "".equals(arrayOfString[i]) || "N/A".equals(arrayOfString[i])) {
          log("error: iccid not found, wait for next sub ready");
          return;
        } 
        if (str.equals(arrayOfString[i])) {
          j = i;
        } else {
          i++;
          continue;
        } 
      } 
      log("capability  phoneid = " + j);
      if (j != -1) {
        setCapability(j);
        return;
      } 
      return;
    } 
  }
  
  private void turnOffNewSimData(Intent paramIntent) {
    int i;
    if (TelephonyManager.getDefault().getSimCount() == 1 && !mOperatorSpec.equals("OP09")) {
      log("[turnOffNewSimData] Single SIM project, don't change data enable setting");
      return;
    } 
    if (paramIntent == null) {
      i = getSimStatus();
    } else {
      i = paramIntent.getIntExtra("simDetectStatus", 0);
    } 
    log("turnOffNewSimData detectedType = " + i);
    if (i == 1) {
      if (paramIntent == null) {
        i = getNewSimSlot();
      } else {
        i = paramIntent.getIntExtra("newSIMSlot", 0);
      } 
      log("newSimSlot = " + i);
      log("default iccid = " + SystemProperties.get("persist.radio.data.iccid"));
      int j = 0;
      while (true) {
        if (j < this.mPhoneNum) {
          if ((1 << j & i) != 0) {
            String str1 = SystemProperties.get("persist.radio.data.iccid");
            String str2 = SystemProperties.get(this.PROPERTY_ICCID[j]);
            if (!str2.equals("N/A") && !str2.equals(str1))
              log("Detect NEW SIM, turn off phone " + j + " data."); 
          } 
          j++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  private void unRegisterWorldModeReceiver() {
    if (this.mContext == null) {
      log("unRegisterWorldModeReceiver, context is null => return");
      return;
    } 
    this.mContext.unregisterReceiver(this.mWorldModeReceiver);
    this.mHasRegisterWorldModeReceiver = false;
  }
  
  private void updateDataEnableProperty() {
    TelephonyManager telephonyManager = TelephonyManager.getDefault();
    boolean bool = false;
    for (int i = 0; i < this.mPhoneNum; i++) {
      String str;
      int j = PhoneFactory.getPhone(i).getSubId();
      if (telephonyManager != null)
        bool = telephonyManager.getDataEnabled(j); 
      if (bool) {
        str = SystemProperties.get(this.PROPERTY_ICCID[i], "0");
      } else {
        str = "0";
      } 
      log("setUserDataProperty:" + str);
      TelephonyManager.getDefault();
      TelephonyManager.setTelephonyProperty(i, "persist.radio.mobile.data", str);
    } 
  }
  
  private void updateIccId() {
    for (int i = 0; i < this.mPhoneNum; i++) {
      String str = SystemProperties.get(this.PROPERTY_ICCID[i]);
      SystemProperties.set(this.PROPERTY_ICCID_PRE[i], str);
    } 
  }
  
  public void onSubInfoReady(Intent paramIntent) {
    this.mIsNeedWaitImsi = false;
    if (BSP_PACKAGE) {
      log("Don't support BSP Package.");
      return;
    } 
    if (mOperatorSpec.equals("OP01")) {
      subSelectorForOp01(paramIntent);
      return;
    } 
    if (mOperatorSpec.equals("OP02")) {
      if (SystemProperties.getBoolean("ro.mtk_disable_cap_switch", false) == true) {
        subSelectorForOp02(paramIntent);
        return;
      } 
      subSelectorForOp02();
      return;
    } 
    if (isOP09ASupport()) {
      subSelectorForOp09(paramIntent);
      return;
    } 
    if ("OP18".equals(mOperatorSpec)) {
      int i = paramIntent.getIntExtra("simDetectStatus", 4);
      log("detectedType:" + i);
      if (i != 4) {
        subSelectorForOp18(paramIntent);
        return;
      } 
      log("skip auto switch when detectedType is NOCHANGE for OP18 when user may set");
      return;
    } 
    subSelectorForOm(paramIntent);
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/dataconnection/DataSubSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */