package com.mediatek.internal.telephony.worldphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneBase;
import com.android.internal.telephony.PhoneConstants;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.PhoneProxy;
import com.android.internal.telephony.uicc.IccRecords;
import com.android.internal.telephony.uicc.UiccController;
import com.mediatek.internal.telephony.ModemSwitchHandler;

public class WorldPhoneOp01 extends Handler implements IWorldPhone {
  private static final int[] FDD_STANDBY_TIMER;
  
  private static final String[] MCC_TABLE_DOMESTIC;
  
  private static final String[] PLMN_TABLE_TYPE1;
  
  private static final String[] PLMN_TABLE_TYPE3;
  
  private static final int PROJECT_SIM_NUM;
  
  private static final int[] TDD_STANDBY_TIMER;
  
  private static Phone[] sActivePhones;
  
  private static int sBtSapState;
  
  private static CommandsInterface[] sCi;
  
  private static Context sContext;
  
  private static int sDataRegState;
  
  private static int sDefaultBootuUpModem;
  
  private static Phone sDefultPhone;
  
  private static int sDenyReason;
  
  private static int sFddStandByCounter;
  
  private static boolean[] sFirstSelect;
  
  private static int[] sIccCardType;
  
  private static IccRecords[] sIccRecordsInstance;
  
  private static String[] sImsi;
  
  private static boolean sIsAutoSelectEnable;
  
  private static boolean[] sIsInvalidSim;
  
  private static boolean sIsResumeCampingFail;
  
  private static String sLastPlmn;
  
  private static Object sLock = new Object();
  
  private static int sMajorSim;
  
  private static ModemSwitchHandler sModemSwitchHandler;
  
  private static String[] sNwPlmnStrings;
  
  private static String sOperatorSpec;
  
  private static String sPlmnSs;
  
  private static Phone[] sProxyPhones;
  
  private static int sRegion;
  
  private static int sRilDataRadioTechnology;
  
  private static int sRilDataRegState;
  
  private static int sRilVoiceRadioTechnology;
  
  private static int sRilVoiceRegState;
  
  private static ServiceState sServiceState;
  
  private static int[] sSuspendId;
  
  private static boolean[] sSuspendWaitImsi;
  
  private static int sSwitchModemCauseType;
  
  private static int sTddStandByCounter;
  
  private static UiccController sUiccController;
  
  private static int sUserType;
  
  private static boolean sVoiceCapable;
  
  private static int sVoiceRegState;
  
  private static boolean sWaitInFdd;
  
  private static boolean sWaitInTdd;
  
  private Runnable mFddStandByTimerRunnable = new Runnable() {
      public void run() {
        WorldPhoneOp01.access$4008();
        if (WorldPhoneOp01.sFddStandByCounter >= WorldPhoneOp01.FDD_STANDBY_TIMER.length)
          WorldPhoneOp01.access$4002(WorldPhoneOp01.FDD_STANDBY_TIMER.length - 1); 
        if (WorldPhoneOp01.sBtSapState == 0) {
          WorldPhoneOp01.logd("FDD time out!");
          WorldPhoneOp01.access$1102(1);
          WorldPhoneOp01.logd("sSwitchModemCauseType = " + WorldPhoneOp01.sSwitchModemCauseType);
          WorldPhoneOp01.this.handleSwitchModem(101);
          return;
        } 
        WorldPhoneOp01.logd("FDD time out but BT SAP is connected, switch not executed!");
      }
    };
  
  private Runnable mTddStandByTimerRunnable = new Runnable() {
      public void run() {
        WorldPhoneOp01.access$3808();
        if (WorldPhoneOp01.sTddStandByCounter >= WorldPhoneOp01.TDD_STANDBY_TIMER.length)
          WorldPhoneOp01.access$3802(WorldPhoneOp01.TDD_STANDBY_TIMER.length - 1); 
        if (WorldPhoneOp01.sBtSapState == 0) {
          WorldPhoneOp01.logd("TDD time out!");
          WorldPhoneOp01.access$1102(1);
          WorldPhoneOp01.logd("sSwitchModemCauseType = " + WorldPhoneOp01.sSwitchModemCauseType);
          WorldPhoneOp01.this.handleSwitchModem(100);
          return;
        } 
        WorldPhoneOp01.logd("TDD time out but BT SAP is connected, switch not executed!");
      }
    };
  
  private final BroadcastReceiver mWorldPhoneReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        WorldPhoneOp01.logd("[Receiver]+");
        String str = param1Intent.getAction();
        WorldPhoneOp01.logd("Action: " + str);
        if ("android.intent.action.SIM_STATE_CHANGED".equals(str)) {
          str = param1Intent.getStringExtra("ss");
          int i = param1Intent.getIntExtra("slot", 0);
          WorldPhoneOp01.access$102(WorldPhoneUtil.getMajorSim());
          WorldPhoneOp01.logd("slotId: " + i + " simStatus: " + str + "sMajorSim:" + WorldPhoneOp01.sMajorSim);
          if ("IMSI".equals(str)) {
            if (WorldPhoneOp01.sMajorSim == -99)
              WorldPhoneOp01.access$102(WorldPhoneUtil.getMajorSim()); 
            WorldPhoneOp01.access$202(UiccController.getInstance());
            if (WorldPhoneOp01.sUiccController != null) {
              WorldPhoneOp01.sIccRecordsInstance[i] = WorldPhoneOp01.sUiccController.getIccRecords(i, 1);
              if (WorldPhoneOp01.sIccRecordsInstance[i] != null) {
                WorldPhoneOp01.sImsi[i] = WorldPhoneOp01.sIccRecordsInstance[i].getIMSI();
                WorldPhoneOp01.sIccCardType[i] = WorldPhoneOp01.this.getIccCardType(i);
                WorldPhoneOp01.logd("sImsi[" + i + "]:" + WorldPhoneOp01.sImsi[i]);
                if (WorldPhoneOp01.sIsAutoSelectEnable && i == WorldPhoneOp01.sMajorSim) {
                  WorldPhoneOp01.logd("Major SIM");
                  WorldPhoneOp01.access$802(WorldPhoneOp01.this.getUserType(WorldPhoneOp01.sImsi[i]));
                  if (WorldPhoneOp01.sFirstSelect[i]) {
                    WorldPhoneOp01.sFirstSelect[i] = false;
                    if (WorldPhoneOp01.sUserType == 1 || WorldPhoneOp01.sUserType == 2) {
                      WorldPhoneOp01.access$1102(0);
                      WorldPhoneOp01.logd("sSwitchModemCauseType = " + WorldPhoneOp01.sSwitchModemCauseType);
                      if (WorldPhoneOp01.sRegion == 1) {
                        WorldPhoneOp01.this.handleSwitchModem(101);
                      } else if (WorldPhoneOp01.sRegion == 2) {
                        WorldPhoneOp01.this.handleSwitchModem(100);
                      } else {
                        WorldPhoneOp01.logd("Region unknown");
                      } 
                    } else if (WorldPhoneOp01.sUserType == 3) {
                      WorldPhoneOp01.access$1102(255);
                      WorldPhoneOp01.logd("sSwitchModemCauseType = " + WorldPhoneOp01.sSwitchModemCauseType);
                      if (SystemProperties.get("ro.mtk_world_phone_policy").equals("1")) {
                        WorldPhoneOp01.this.handleSwitchModem(100);
                      } else {
                        WorldPhoneOp01.this.handleSwitchModem(101);
                      } 
                    } 
                  } 
                  if (WorldPhoneOp01.sSuspendWaitImsi[i]) {
                    WorldPhoneOp01.sSuspendWaitImsi[i] = false;
                    if (WorldPhoneOp01.sNwPlmnStrings != null) {
                      WorldPhoneOp01.logd("IMSI fot slot" + i + " now ready, resuming PLMN:" + WorldPhoneOp01.sNwPlmnStrings[0] + " with ID:" + WorldPhoneOp01.sSuspendId[i]);
                      WorldPhoneOp01.this.resumeCampingProcedure(i);
                    } else {
                      WorldPhoneOp01.logd("sNwPlmnStrings is Null");
                    } 
                  } 
                } else {
                  WorldPhoneOp01.logd("Not major SIM");
                  WorldPhoneOp01.this.getUserType(WorldPhoneOp01.sImsi[i]);
                  if (WorldPhoneOp01.sSuspendWaitImsi[i]) {
                    WorldPhoneOp01.sSuspendWaitImsi[i] = false;
                    WorldPhoneOp01.logd("IMSI fot slot" + i + " now ready, resuming with ID:" + WorldPhoneOp01.sSuspendId[i]);
                    WorldPhoneOp01.sCi[i].setResumeRegistration(WorldPhoneOp01.sSuspendId[i], null);
                  } 
                } 
              } else {
                WorldPhoneOp01.logd("Null sIccRecordsInstance");
                return;
              } 
            } else {
              WorldPhoneOp01.logd("Null sUiccController");
              return;
            } 
          } else if (str.equals("ABSENT")) {
            WorldPhoneOp01.access$1902((String)null);
            WorldPhoneOp01.sImsi[i] = "";
            WorldPhoneOp01.sFirstSelect[i] = true;
            WorldPhoneOp01.sIsInvalidSim[i] = false;
            WorldPhoneOp01.sSuspendWaitImsi[i] = false;
            WorldPhoneOp01.sIccCardType[i] = 0;
            if (i == WorldPhoneOp01.sMajorSim) {
              WorldPhoneOp01.logd("Major SIM removed, no world phone service");
              WorldPhoneOp01.this.removeModemStandByTimer();
              WorldPhoneOp01.access$802(0);
              WorldPhoneOp01.access$2202(1);
              WorldPhoneOp01.access$102(-99);
            } else {
              WorldPhoneOp01.logd("SIM" + i + " is not major SIM");
            } 
          } 
        } else {
          StringBuilder stringBuilder;
          if (str.equals("android.intent.action.SERVICE_STATE")) {
            if (param1Intent.getExtras() != null) {
              WorldPhoneOp01.access$2302(ServiceState.newFromBundle(param1Intent.getExtras()));
              if (WorldPhoneOp01.sServiceState != null) {
                int i = param1Intent.getIntExtra("slot", 0);
                WorldPhoneOp01.access$2402(WorldPhoneOp01.sServiceState.getOperatorNumeric());
                WorldPhoneOp01.access$2502(WorldPhoneOp01.sServiceState.getVoiceRegState());
                WorldPhoneOp01.access$2602(WorldPhoneOp01.sServiceState.getRilVoiceRegState());
                WorldPhoneOp01.access$2702(WorldPhoneOp01.sServiceState.getRilVoiceRadioTechnology());
                WorldPhoneOp01.access$2802(WorldPhoneOp01.sServiceState.getDataRegState());
                WorldPhoneOp01.access$2902(WorldPhoneOp01.sServiceState.getRilDataRegState());
                WorldPhoneOp01.access$3002(WorldPhoneOp01.sServiceState.getRilDataRadioTechnology());
                WorldPhoneOp01.logd("slotId: " + i + ", " + WorldPhoneUtil.iccCardTypeToString(WorldPhoneOp01.sIccCardType[i]));
                WorldPhoneOp01.logd("sMajorSim: " + WorldPhoneOp01.sMajorSim);
                WorldPhoneOp01.logd(ModemSwitchHandler.modemToString(ModemSwitchHandler.getActiveModemType()));
                WorldPhoneOp01.logd("sPlmnSs: " + WorldPhoneOp01.sPlmnSs);
                WorldPhoneOp01.logd("sVoiceRegState: " + WorldPhoneUtil.stateToString(WorldPhoneOp01.sVoiceRegState));
                WorldPhoneOp01.logd("sRilVoiceRegState: " + WorldPhoneUtil.regStateToString(WorldPhoneOp01.sRilVoiceRegState));
                stringBuilder = (new StringBuilder()).append("sRilVoiceRadioTech: ");
                WorldPhoneOp01.sServiceState;
                WorldPhoneOp01.logd(stringBuilder.append(ServiceState.rilRadioTechnologyToString(WorldPhoneOp01.sRilVoiceRadioTechnology)).toString());
                WorldPhoneOp01.logd("sDataRegState: " + WorldPhoneUtil.stateToString(WorldPhoneOp01.sDataRegState));
                WorldPhoneOp01.logd("sRilDataRegState: " + WorldPhoneUtil.regStateToString(WorldPhoneOp01.sRilDataRegState));
                stringBuilder = (new StringBuilder()).append("sRilDataRadioTech: ");
                WorldPhoneOp01.sServiceState;
                WorldPhoneOp01.logd(stringBuilder.append(ServiceState.rilRadioTechnologyToString(WorldPhoneOp01.sRilDataRadioTechnology)).toString());
                WorldPhoneOp01.logd("sIsAutoSelectEnable: " + WorldPhoneOp01.sIsAutoSelectEnable);
                if (WorldPhoneOp01.sIsAutoSelectEnable && i == WorldPhoneOp01.sMajorSim)
                  if (WorldPhoneOp01.this.isNoService()) {
                    WorldPhoneOp01.this.handleNoService();
                  } else if (WorldPhoneOp01.this.isInService()) {
                    WorldPhoneOp01.access$1902(WorldPhoneOp01.sPlmnSs);
                    WorldPhoneOp01.this.removeModemStandByTimer();
                    WorldPhoneOp01.sIsInvalidSim[i] = false;
                  }  
              } else {
                WorldPhoneOp01.logd("Null sServiceState");
              } 
            } 
          } else if (stringBuilder.equals("android.intent.action.ACTION_SHUTDOWN_IPO")) {
            if (WorldPhoneOp01.sDefaultBootuUpModem == 100) {
              if (WorldPhoneUtil.isLteSupport()) {
                ModemSwitchHandler.reloadModem(WorldPhoneOp01.sCi[0], 5);
                WorldPhoneOp01.logd("Reload to FDD CSFB modem");
              } else {
                ModemSwitchHandler.reloadModem(WorldPhoneOp01.sCi[0], 3);
                WorldPhoneOp01.logd("Reload to WG modem");
              } 
            } else if (WorldPhoneOp01.sDefaultBootuUpModem == 101) {
              if (WorldPhoneUtil.isLteSupport()) {
                ModemSwitchHandler.reloadModem(WorldPhoneOp01.sCi[0], 6);
                WorldPhoneOp01.logd("Reload to TDD CSFB modem");
              } else {
                ModemSwitchHandler.reloadModem(WorldPhoneOp01.sCi[0], 4);
                WorldPhoneOp01.logd("Reload to TG modem");
              } 
            } 
          } else if (stringBuilder.equals("android.intent.action.ACTION_ADB_SWITCH_MODEM")) {
            int i = param1Intent.getIntExtra("mdType", 0);
            WorldPhoneOp01.logd("toModem: " + i);
            if (i == 3 || i == 4 || i == 5 || i == 6) {
              WorldPhoneOp01.this.setModemSelectionMode(0, i);
            } else {
              WorldPhoneOp01.this.setModemSelectionMode(1, i);
            } 
          } else if (stringBuilder.equals("android.intent.action.AIRPLANE_MODE")) {
            if (!param1Intent.getBooleanExtra("state", false)) {
              WorldPhoneOp01.logd("Leave flight mode");
              WorldPhoneOp01.access$1902((String)null);
              int j = 0;
              while (true) {
                if (j < WorldPhoneOp01.PROJECT_SIM_NUM) {
                  WorldPhoneOp01.sIsInvalidSim[j] = false;
                  j++;
                  continue;
                } 
                WorldPhoneOp01.logd("[Receiver]-");
                return;
              } 
            } 
            WorldPhoneOp01.logd("Enter flight mode");
            for (int i = 0; i < WorldPhoneOp01.PROJECT_SIM_NUM; i++)
              WorldPhoneOp01.sFirstSelect[i] = true; 
            WorldPhoneOp01.access$1202(0);
          } else if (stringBuilder.equals("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE")) {
            WorldPhoneOp01.access$102(WorldPhoneUtil.getMajorSim());
            WorldPhoneOp01.this.handleSimSwitched();
          } else if (stringBuilder.equals("android.bluetooth.sap.profile.action.CONNECTION_STATE_CHANGED")) {
            int i = param1Intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
            if (i == 2) {
              WorldPhoneOp01.logd("BT_SAP connection state is CONNECTED");
              WorldPhoneOp01.access$3702(1);
            } else if (i == 0) {
              WorldPhoneOp01.logd("BT_SAP connection state is DISCONNECTED");
              WorldPhoneOp01.access$3702(0);
            } else {
              WorldPhoneOp01.logd("BT_SAP connection state is " + i);
            } 
          } 
        } 
        WorldPhoneOp01.logd("[Receiver]-");
      }
    };
  
  static {
    PROJECT_SIM_NUM = WorldPhoneUtil.getProjectSimNum();
    FDD_STANDBY_TIMER = new int[] { 60 };
    TDD_STANDBY_TIMER = new int[] { 40 };
    PLMN_TABLE_TYPE1 = new String[] { 
        "46000", "46002", "46007", "46008", "00101", "00211", "00321", "00431", "00541", "00651", 
        "00761", "00871", "00902", "01012", "01122", "01232", "46004", "46602", "50270", "46003" };
    PLMN_TABLE_TYPE3 = new String[] { "46001", "46006", "46009", "45407", "46005", "45502", "46011" };
    MCC_TABLE_DOMESTIC = new String[] { 
        "460", "001", "002", "003", "004", "005", "006", "007", "008", "009", 
        "010", "011", "012" };
    sContext = null;
    sDefultPhone = null;
    sProxyPhones = null;
    sActivePhones = new Phone[PROJECT_SIM_NUM];
    sCi = new CommandsInterface[PROJECT_SIM_NUM];
    sImsi = new String[PROJECT_SIM_NUM];
    sDefaultBootuUpModem = 0;
    sSuspendId = new int[PROJECT_SIM_NUM];
    sIccCardType = new int[PROJECT_SIM_NUM];
    sIsInvalidSim = new boolean[PROJECT_SIM_NUM];
    sSuspendWaitImsi = new boolean[PROJECT_SIM_NUM];
    sFirstSelect = new boolean[PROJECT_SIM_NUM];
    sUiccController = null;
    sIccRecordsInstance = new IccRecords[PROJECT_SIM_NUM];
    sModemSwitchHandler = null;
  }
  
  public WorldPhoneOp01() {
    logd("Constructor invoked");
    sOperatorSpec = SystemProperties.get("ro.operator.optr", "OM");
    logd("Operator Spec:" + sOperatorSpec);
    sDefultPhone = PhoneFactory.getDefaultPhone();
    sProxyPhones = PhoneFactory.getPhones();
    int i;
    for (i = 0; i < PROJECT_SIM_NUM; i++) {
      sActivePhones[i] = ((PhoneProxy)sProxyPhones[i]).getActivePhone();
      sCi[i] = ((PhoneBase)sActivePhones[i]).mCi;
    } 
    for (i = 0; i < PROJECT_SIM_NUM; i++) {
      sCi[i].setOnPlmnChangeNotification(this, i + 10, null);
      sCi[i].setOnRegistrationSuspended(this, i + 30, null);
      sCi[i].registerForOn(this, i + 0, null);
      sCi[i].setInvalidSimInfo(this, i + 60, null);
    } 
    sModemSwitchHandler = new ModemSwitchHandler();
    logd(ModemSwitchHandler.modemToString(ModemSwitchHandler.getActiveModemType()));
    IntentFilter intentFilter = new IntentFilter("android.intent.action.SIM_STATE_CHANGED");
    intentFilter.addAction("android.intent.action.SERVICE_STATE");
    intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
    intentFilter.addAction("android.intent.action.ACTION_SHUTDOWN_IPO");
    intentFilter.addAction("android.intent.action.ACTION_ADB_SWITCH_MODEM");
    intentFilter.addAction("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE");
    intentFilter.addAction("android.bluetooth.sap.profile.action.CONNECTION_STATE_CHANGED");
    if (sDefultPhone != null) {
      sContext = sDefultPhone.getContext();
    } else {
      logd("DefaultPhone = null");
    } 
    sVoiceCapable = sContext.getResources().getBoolean(17956947);
    sContext.registerReceiver(this.mWorldPhoneReceiver, intentFilter);
    sTddStandByCounter = 0;
    sFddStandByCounter = 0;
    sWaitInTdd = false;
    sWaitInFdd = false;
    sRegion = 0;
    sLastPlmn = null;
    sBtSapState = 0;
    resetAllProperties();
    if (WorldPhoneUtil.getModemSelectionMode() == 0) {
      logd("Auto select disable");
      sIsAutoSelectEnable = false;
      Settings.Global.putInt(sContext.getContentResolver(), "world_phone_auto_select_mode", 0);
    } else {
      logd("Auto select enable");
      sIsAutoSelectEnable = true;
      Settings.Global.putInt(sContext.getContentResolver(), "world_phone_auto_select_mode", 1);
    } 
    FDD_STANDBY_TIMER[sFddStandByCounter] = Settings.Global.getInt(sContext.getContentResolver(), "world_phone_fdd_modem_timer", FDD_STANDBY_TIMER[sFddStandByCounter]);
    Settings.Global.putInt(sContext.getContentResolver(), "world_phone_fdd_modem_timer", FDD_STANDBY_TIMER[sFddStandByCounter]);
    logd("FDD_STANDBY_TIMER = " + FDD_STANDBY_TIMER[sFddStandByCounter] + "s");
    logd("sDefaultBootuUpModem = " + sDefaultBootuUpModem);
  }
  
  private int getIccCardType(int paramInt) {
    String str = ((PhoneProxy)sProxyPhones[paramInt]).getIccCard().getIccCardType();
    if (str.equals("SIM")) {
      logd("IccCard type: SIM");
      return 1;
    } 
    if (str.equals("USIM")) {
      logd("IccCard type: USIM");
      return 2;
    } 
    logd("IccCard type: Unknown");
    return 0;
  }
  
  private int getRegion(String paramString) {
    int j = 1;
    if (paramString == null || paramString.equals("") || paramString.length() < 5) {
      logd("[getRegion] Invalid PLMN");
      return 0;
    } 
    String str = paramString.substring(0, 5);
    int i = j;
    if (!str.equals("46602")) {
      i = j;
      if (!str.equals("50270")) {
        paramString = paramString.substring(0, 3);
        String[] arrayOfString = MCC_TABLE_DOMESTIC;
        j = arrayOfString.length;
        for (i = 0; i < j; i++) {
          if (paramString.equals(arrayOfString[i])) {
            logd("[getRegion] REGION_DOMESTIC");
            return 1;
          } 
        } 
        logd("[getRegion] REGION_FOREIGN");
        return 2;
      } 
    } 
    return i;
  }
  
  private int getUserType(String paramString) {
    if (paramString != null && !paramString.equals("")) {
      paramString = paramString.substring(0, 5);
      String[] arrayOfString = PLMN_TABLE_TYPE1;
      int j = arrayOfString.length;
      int i;
      for (i = 0; i < j; i++) {
        if (paramString.equals(arrayOfString[i])) {
          logd("[getUserType] Type1 user");
          return 1;
        } 
      } 
      arrayOfString = PLMN_TABLE_TYPE3;
      j = arrayOfString.length;
      for (i = 0; i < j; i++) {
        if (paramString.equals(arrayOfString[i])) {
          logd("[getUserType] Type3 user");
          return 3;
        } 
      } 
      logd("[getUserType] Type2 user");
      return 2;
    } 
    logd("[getUserType] null IMSI");
    return 0;
  }
  
  private void handleInvalidSimNotify(int paramInt, AsyncResult paramAsyncResult) {
    String[] arrayOfString;
    logd("Slot" + paramInt);
    if (paramAsyncResult.exception == null && paramAsyncResult.result != null) {
      arrayOfString = (String[])paramAsyncResult.result;
      String str = arrayOfString[0];
      int i = Integer.parseInt(arrayOfString[1]);
      int j = Integer.parseInt(arrayOfString[2]);
      int k = Integer.parseInt(arrayOfString[3]);
      int m = SystemProperties.getInt("gsm.gcf.testmode", 0);
      if (m != 0) {
        logd("Invalid SIM notified during test mode: " + m);
        return;
      } 
      logd("testMode:" + m + ", cause: " + k + ", cs_invalid: " + i + ", ps_invalid: " + j + ", plmn: " + str);
      if (sVoiceCapable && i == 1 && sLastPlmn == null) {
        logd("CS reject, invalid SIM");
        sIsInvalidSim[paramInt] = true;
        return;
      } 
      if (j == 1 && sLastPlmn == null) {
        logd("PS reject, invalid SIM");
        sIsInvalidSim[paramInt] = true;
        return;
      } 
      return;
    } 
    logd("AsyncResult is wrong " + ((AsyncResult)arrayOfString).exception);
  }
  
  private void handleNoService() {
    logd("[handleNoService]+ Can not find service");
    logd("Type" + sUserType + " user");
    logd(WorldPhoneUtil.regionToString(sRegion));
    int i = ModemSwitchHandler.getActiveModemType();
    logd(ModemSwitchHandler.modemToString(i));
    if (((PhoneProxy)sProxyPhones[sMajorSim]).getIccCard().getState() == IccCardConstants.State.READY) {
      if (sUserType == 1 || sUserType == 2) {
        if (i == 6 || i == 4) {
          if (TDD_STANDBY_TIMER[sTddStandByCounter] >= 0) {
            if (!sWaitInTdd) {
              sWaitInTdd = true;
              logd("Wait " + TDD_STANDBY_TIMER[sTddStandByCounter] + "s. Timer index = " + sTddStandByCounter);
              postDelayed(this.mTddStandByTimerRunnable, (TDD_STANDBY_TIMER[sTddStandByCounter] * 1000));
            } else {
              logd("Timer already set:" + TDD_STANDBY_TIMER[sTddStandByCounter] + "s");
            } 
          } else {
            logd("Standby in TDD modem");
          } 
        } else if (i == 5 || i == 3) {
          if (FDD_STANDBY_TIMER[sFddStandByCounter] >= 0) {
            if (sRegion == 2) {
              if (!sWaitInFdd) {
                sWaitInFdd = true;
                logd("Wait " + FDD_STANDBY_TIMER[sFddStandByCounter] + "s. Timer index = " + sFddStandByCounter);
                postDelayed(this.mFddStandByTimerRunnable, (FDD_STANDBY_TIMER[sFddStandByCounter] * 1000));
              } else {
                logd("Timer already set:" + FDD_STANDBY_TIMER[sFddStandByCounter] + "s");
              } 
            } else {
              sSwitchModemCauseType = 1;
              logd("sSwitchModemCauseType = " + sSwitchModemCauseType);
              handleSwitchModem(101);
            } 
          } else {
            logd("Standby in FDD modem");
          } 
        } 
      } else if (sUserType == 3) {
        if (SystemProperties.get("ro.mtk_world_phone_policy").equals("1")) {
          if (i == 5 || i == 3) {
            logd("Standby in FDD modem");
          } else {
            logd("Should not enter this state");
          } 
        } else if (i == 6 || i == 4) {
          logd("Standby in TDD modem");
        } else {
          logd("Should not enter this state");
        } 
      } else {
        logd("Unknow user type");
      } 
    } else {
      logd("IccState not ready");
    } 
    logd("[handleNoService]-");
  }
  
  private void handlePlmnChange(AsyncResult paramAsyncResult, int paramInt) {
    String[] arrayOfString;
    sMajorSim = WorldPhoneUtil.getMajorSim();
    logd("Slot:" + paramInt + " sMajorSim:" + sMajorSim);
    if (paramAsyncResult.exception == null && paramAsyncResult.result != null) {
      arrayOfString = (String[])paramAsyncResult.result;
      if (paramInt == sMajorSim)
        sNwPlmnStrings = arrayOfString; 
      for (int i = 0; i < arrayOfString.length; i++)
        logd("plmnString[" + i + "]=" + arrayOfString[i]); 
      if (sIsAutoSelectEnable) {
        if (sMajorSim == paramInt && (sUserType == 1 || sUserType == 2) && sDenyReason != 2)
          searchForDesignateService(arrayOfString[0]); 
        sRegion = getRegion(arrayOfString[0]);
        if (sUserType != 3 && sRegion == 2 && sMajorSim != -1) {
          sSwitchModemCauseType = 0;
          logd("sSwitchModemCauseType = " + sSwitchModemCauseType);
          handleSwitchModem(100);
        } 
      } 
      return;
    } 
    logd("AsyncResult is wrong " + ((AsyncResult)arrayOfString).exception);
  }
  
  private void handleRadioOn(int paramInt) {
    sMajorSim = WorldPhoneUtil.getMajorSim();
    logd("handleRadioOn Slot:" + paramInt + " sMajorSim:" + sMajorSim);
    sIsInvalidSim[paramInt] = false;
    if (sIsResumeCampingFail) {
      logd("try to resume camping again");
      sCi[paramInt].setResumeRegistration(sSuspendId[paramInt], null);
      sIsResumeCampingFail = false;
    } 
  }
  
  private void handleRegistrationSuspend(AsyncResult paramAsyncResult, int paramInt) {
    logd("Slot" + paramInt);
    if (paramAsyncResult.exception == null && paramAsyncResult.result != null) {
      sSuspendId[paramInt] = ((int[])paramAsyncResult.result)[0];
      logd("Suspending with Id=" + sSuspendId[paramInt]);
      if (sIsAutoSelectEnable && sMajorSim == paramInt) {
        if (sUserType != 0) {
          resumeCampingProcedure(paramInt);
          return;
        } 
        sSuspendWaitImsi[paramInt] = true;
        logd("User type unknown, wait for IMSI");
        return;
      } 
      logd("Not major slot, camp on OK");
      sCi[paramInt].setResumeRegistration(sSuspendId[paramInt], null);
      return;
    } 
    logd("AsyncResult is wrong " + paramAsyncResult.exception);
  }
  
  private void handleSimSwitched() {
    if (sMajorSim == -1) {
      logd("Major capability turned off");
      removeModemStandByTimer();
      sUserType = 0;
      return;
    } 
    if (!sIsAutoSelectEnable) {
      logd("Auto modem selection disabled");
      removeModemStandByTimer();
      return;
    } 
    if (sMajorSim == -99) {
      logd("Major SIM unknown");
      return;
    } 
    logd("Auto modem selection enabled");
    logd("Major capability in slot" + sMajorSim);
    if (sImsi[sMajorSim] == null || sImsi[sMajorSim].equals("")) {
      logd("Major slot IMSI not ready");
      sUserType = 0;
      return;
    } 
    sSwitchModemCauseType = 255;
    logd("sSwitchModemCauseType = " + sSwitchModemCauseType);
    sUserType = getUserType(sImsi[sMajorSim]);
    if (sUserType == 1 || sUserType == 2) {
      if (sNwPlmnStrings != null)
        sRegion = getRegion(sNwPlmnStrings[0]); 
      if (sRegion == 1) {
        sFirstSelect[sMajorSim] = false;
        sIccCardType[sMajorSim] = getIccCardType(sMajorSim);
        handleSwitchModem(101);
        return;
      } 
      if (sRegion == 2) {
        sFirstSelect[sMajorSim] = false;
        handleSwitchModem(100);
        return;
      } 
      logd("Unknown region");
      return;
    } 
    if (sUserType == 3) {
      sFirstSelect[sMajorSim] = false;
      if (SystemProperties.get("ro.mtk_world_phone_policy").equals("1")) {
        handleSwitchModem(100);
        return;
      } 
      handleSwitchModem(101);
      return;
    } 
    logd("Unknown user type");
  }
  
  private void handleSwitchModem(int paramInt) {
    int i = WorldPhoneUtil.getMajorSim();
    if (i >= 0 && sIsInvalidSim[i] && WorldPhoneUtil.getModemSelectionMode() == 1) {
      logd("Invalid SIM, switch not executed!");
      return;
    } 
    if (paramInt == 101) {
      if (WorldPhoneUtil.isLteSupport()) {
        i = 6;
      } else {
        i = 4;
      } 
    } else {
      i = paramInt;
      if (paramInt == 100)
        if (WorldPhoneUtil.isLteSupport()) {
          i = 5;
        } else {
          i = 3;
        }  
    } 
    if (!sIsAutoSelectEnable) {
      logd("Storing modem type: " + i);
      sCi[0].storeModemType(i, null);
    } else if (sDefaultBootuUpModem == 0) {
      logd("Storing modem type: " + i);
      sCi[0].storeModemType(i, null);
    } else if (sDefaultBootuUpModem == 100) {
      if (WorldPhoneUtil.isLteSupport()) {
        logd("Storing modem type: 3");
        sCi[0].storeModemType(5, null);
      } else {
        logd("Storing modem type: 5");
        sCi[0].storeModemType(3, null);
      } 
    } else if (sDefaultBootuUpModem == 101) {
      if (WorldPhoneUtil.isLteSupport()) {
        logd("Storing modem type: 3");
        sCi[0].storeModemType(6, null);
      } else {
        logd("Storing modem type: 5");
        sCi[0].storeModemType(4, null);
      } 
    } 
    if (i == ModemSwitchHandler.getActiveModemType()) {
      if (i == 3) {
        logd("Already in WG modem");
        return;
      } 
      if (i == 4) {
        logd("Already in TG modem");
        return;
      } 
      if (i == 5) {
        logd("Already in FDD CSFB modem");
        return;
      } 
      if (i == 6) {
        logd("Already in TDD CSFB modem");
        return;
      } 
      return;
    } 
    for (paramInt = 0; paramInt < PROJECT_SIM_NUM; paramInt++) {
      if (sActivePhones[paramInt].getState() != PhoneConstants.State.IDLE) {
        logd("Phone" + paramInt + " is not idle, modem switch not allowed");
        return;
      } 
    } 
    removeModemStandByTimer();
    if (i == 3) {
      logd("Switching to WG modem");
    } else if (i == 4) {
      logd("Switching to TG modem");
    } else if (i == 5) {
      logd("Switching to FDD CSFB modem");
    } else if (i == 6) {
      logd("Switching to TDD CSFB modem");
    } 
    SystemProperties.set("ril.switch.modem.cause.type", String.valueOf(sSwitchModemCauseType));
    ModemSwitchHandler.switchModem(i);
    resetNetworkProperties();
  }
  
  private boolean isAllowCampOn(String paramString, int paramInt) {
    logd("[isAllowCampOn]+ " + paramString);
    logd("User type: " + sUserType);
    logd(WorldPhoneUtil.iccCardTypeToString(sIccCardType[paramInt]));
    sRegion = getRegion(paramString);
    paramInt = ModemSwitchHandler.getActiveModemType();
    logd(ModemSwitchHandler.modemToString(paramInt));
    if (sUserType == 1 || sUserType == 2) {
      if (sRegion == 1) {
        if (paramInt == 6 || paramInt == 4) {
          sDenyReason = 0;
          logd("Camp on OK");
          logd("[isAllowCampOn]-");
          return true;
        } 
        if (paramInt == 5 || paramInt == 3) {
          sDenyReason = 3;
          logd("Camp on REJECT");
          logd("[isAllowCampOn]-");
          return false;
        } 
      } else if (sRegion == 2) {
        if (paramInt == 6 || paramInt == 4) {
          sDenyReason = 2;
          logd("Camp on REJECT");
          logd("[isAllowCampOn]-");
          return false;
        } 
        if (paramInt == 5 || paramInt == 3) {
          sDenyReason = 0;
          logd("Camp on OK");
          logd("[isAllowCampOn]-");
          return true;
        } 
      } else {
        logd("Unknow region");
      } 
      sDenyReason = 1;
      logd("Camp on REJECT");
      logd("[isAllowCampOn]-");
      return false;
    } 
    if (sUserType == 3) {
      if (SystemProperties.get("ro.mtk_world_phone_policy").equals("1")) {
        if (paramInt == 6 || paramInt == 4) {
          sDenyReason = 2;
          logd("Camp on REJECT");
          logd("[isAllowCampOn]-");
          return false;
        } 
        if (paramInt == 5 || paramInt == 3) {
          sDenyReason = 0;
          logd("Camp on OK");
          logd("[isAllowCampOn]-");
          return true;
        } 
        sDenyReason = 1;
        logd("Camp on REJECT");
        logd("[isAllowCampOn]-");
        return false;
      } 
      if (paramInt == 6 || paramInt == 4) {
        sDenyReason = 0;
        logd("Camp on OK");
        logd("[isAllowCampOn]-");
        return true;
      } 
      if (paramInt == 5 || paramInt == 3) {
        sDenyReason = 3;
        logd("Camp on REJECT");
        logd("[isAllowCampOn]-");
        return false;
      } 
      sDenyReason = 1;
      logd("Camp on REJECT");
      logd("[isAllowCampOn]-");
      return false;
    } 
    logd("Unknown user type");
    sDenyReason = 1;
    logd("Camp on REJECT");
    logd("[isAllowCampOn]-");
    return false;
  }
  
  private boolean isInService() {
    boolean bool = false;
    if (sVoiceRegState == 0 || sDataRegState == 0)
      bool = true; 
    logd("inService: " + bool);
    return bool;
  }
  
  private boolean isNoService() {
    if (sVoiceRegState == 1 && (sRilVoiceRegState == 0 || sRilVoiceRegState == 10) && sDataRegState == 1 && sRilDataRegState == 0) {
      boolean bool1 = true;
      logd("noService: " + bool1);
      return bool1;
    } 
    boolean bool = false;
    logd("noService: " + bool);
    return bool;
  }
  
  private static void logd(String paramString) {
    Rlog.d("PHONE", "[WPOP01]" + paramString);
  }
  
  private void removeModemStandByTimer() {
    if (sWaitInTdd) {
      logd("Remove TDD wait timer. Set sWaitInTdd = false");
      sWaitInTdd = false;
      removeCallbacks(this.mTddStandByTimerRunnable);
    } 
    if (sWaitInFdd) {
      logd("Remove FDD wait timer. Set sWaitInFdd = false");
      sWaitInFdd = false;
      removeCallbacks(this.mFddStandByTimerRunnable);
    } 
  }
  
  private void resetAllProperties() {
    logd("[resetAllProperties]");
    sNwPlmnStrings = null;
    for (int i = 0; i < PROJECT_SIM_NUM; i++)
      sFirstSelect[i] = true; 
    sDenyReason = 1;
    resetSimProperties();
    resetNetworkProperties();
  }
  
  private void resetNetworkProperties() {
    // Byte code:
    //   0: ldc_w '[resetNetworkProperties]'
    //   3: invokestatic logd : (Ljava/lang/String;)V
    //   6: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.sLock : Ljava/lang/Object;
    //   9: astore_2
    //   10: aload_2
    //   11: monitorenter
    //   12: iconst_0
    //   13: istore_1
    //   14: iload_1
    //   15: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.PROJECT_SIM_NUM : I
    //   18: if_icmpge -> 34
    //   21: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.sSuspendWaitImsi : [Z
    //   24: iload_1
    //   25: iconst_0
    //   26: bastore
    //   27: iload_1
    //   28: iconst_1
    //   29: iadd
    //   30: istore_1
    //   31: goto -> 14
    //   34: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.sNwPlmnStrings : [Ljava/lang/String;
    //   37: ifnull -> 65
    //   40: iconst_0
    //   41: istore_1
    //   42: iload_1
    //   43: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.sNwPlmnStrings : [Ljava/lang/String;
    //   46: arraylength
    //   47: if_icmpge -> 65
    //   50: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.sNwPlmnStrings : [Ljava/lang/String;
    //   53: iload_1
    //   54: ldc_w ''
    //   57: aastore
    //   58: iload_1
    //   59: iconst_1
    //   60: iadd
    //   61: istore_1
    //   62: goto -> 42
    //   65: sipush #255
    //   68: putstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.sSwitchModemCauseType : I
    //   71: new java/lang/StringBuilder
    //   74: dup
    //   75: invokespecial <init> : ()V
    //   78: ldc_w 'sSwitchModemCauseType = '
    //   81: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   84: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.sSwitchModemCauseType : I
    //   87: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   90: invokevirtual toString : ()Ljava/lang/String;
    //   93: invokestatic logd : (Ljava/lang/String;)V
    //   96: aload_2
    //   97: monitorexit
    //   98: return
    //   99: astore_3
    //   100: aload_2
    //   101: monitorexit
    //   102: aload_3
    //   103: athrow
    // Exception table:
    //   from	to	target	type
    //   14	27	99	finally
    //   34	40	99	finally
    //   42	58	99	finally
    //   65	98	99	finally
    //   100	102	99	finally
  }
  
  private void resetSimProperties() {
    // Byte code:
    //   0: ldc_w '[resetSimProperties]'
    //   3: invokestatic logd : (Ljava/lang/String;)V
    //   6: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.sLock : Ljava/lang/Object;
    //   9: astore_2
    //   10: aload_2
    //   11: monitorenter
    //   12: iconst_0
    //   13: istore_1
    //   14: iload_1
    //   15: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.PROJECT_SIM_NUM : I
    //   18: if_icmpge -> 42
    //   21: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.sImsi : [Ljava/lang/String;
    //   24: iload_1
    //   25: ldc_w ''
    //   28: aastore
    //   29: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.sIccCardType : [I
    //   32: iload_1
    //   33: iconst_0
    //   34: iastore
    //   35: iload_1
    //   36: iconst_1
    //   37: iadd
    //   38: istore_1
    //   39: goto -> 14
    //   42: iconst_0
    //   43: putstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.sUserType : I
    //   46: invokestatic getMajorSim : ()I
    //   49: putstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.sMajorSim : I
    //   52: aload_2
    //   53: monitorexit
    //   54: return
    //   55: astore_3
    //   56: aload_2
    //   57: monitorexit
    //   58: aload_3
    //   59: athrow
    // Exception table:
    //   from	to	target	type
    //   14	35	55	finally
    //   42	54	55	finally
    //   56	58	55	finally
  }
  
  private void resumeCampingProcedure(int paramInt) {
    logd("Resume camping slot" + paramInt);
    if (isAllowCampOn(sNwPlmnStrings[0], paramInt)) {
      removeModemStandByTimer();
      sCi[paramInt].setResumeRegistration(sSuspendId[paramInt], obtainMessage(70));
      return;
    } 
    logd("Because: " + WorldPhoneUtil.denyReasonToString(sDenyReason));
    sSwitchModemCauseType = 0;
    logd("sSwitchModemCauseType = " + sSwitchModemCauseType);
    if (sDenyReason == 2) {
      handleSwitchModem(100);
      return;
    } 
    if (sDenyReason == 3) {
      handleSwitchModem(101);
      return;
    } 
  }
  
  private void searchForDesignateService(String paramString) {
    if (paramString == null) {
      logd("[searchForDesignateService]- null source");
      return;
    } 
    paramString = paramString.substring(0, 5);
    String[] arrayOfString = PLMN_TABLE_TYPE1;
    int j = arrayOfString.length;
    int i = 0;
    while (true) {
      if (i < j) {
        if (paramString.equals(arrayOfString[i])) {
          logd("Find TD service");
          logd("sUserType: " + sUserType + " sRegion: " + sRegion);
          logd(ModemSwitchHandler.modemToString(ModemSwitchHandler.getActiveModemType()));
          sSwitchModemCauseType = 0;
          logd("sSwitchModemCauseType = " + sSwitchModemCauseType);
          handleSwitchModem(101);
          return;
        } 
        i++;
        continue;
      } 
      return;
    } 
  }
  
  public void handleMessage(Message paramMessage) {
    AsyncResult asyncResult = (AsyncResult)paramMessage.obj;
    switch (paramMessage.what) {
      default:
        logd("Unknown msg:" + paramMessage.what);
        return;
      case 0:
        logd("handleMessage : <EVENT_RADIO_ON>");
        handleRadioOn(0);
        return;
      case 10:
        logd("handleMessage : <EVENT_REG_PLMN_CHANGED>");
        handlePlmnChange(asyncResult, 0);
        return;
      case 30:
        logd("handleMessage : <EVENT_REG_SUSPENDED>");
        handleRegistrationSuspend(asyncResult, 0);
        return;
      case 1:
        logd("handleMessage : <EVENT_RADIO_ON>");
        handleRadioOn(1);
        return;
      case 11:
        logd("handleMessage : <EVENT_REG_PLMN_CHANGED>");
        handlePlmnChange(asyncResult, 1);
        return;
      case 31:
        logd("handleMessage : <EVENT_REG_SUSPENDED>");
        handleRegistrationSuspend(asyncResult, 1);
        return;
      case 60:
        logd("handleMessage : <EVENT_INVALID_SIM_NOTIFY>");
        handleInvalidSimNotify(0, asyncResult);
        return;
      case 61:
        logd("handleMessage : <EVENT_INVALID_SIM_NOTIFY>");
        handleInvalidSimNotify(1, asyncResult);
        return;
      case 70:
        break;
    } 
    if (asyncResult.exception != null) {
      logd("handleMessage : <EVENT_RESUME_CAMPING> with exception");
      sIsResumeCampingFail = true;
      return;
    } 
  }
  
  public void notifyRadioCapabilityChange(int paramInt) {
    byte b;
    logd("[setRadioCapabilityChange]");
    logd("Major capability will be set to slot:" + paramInt);
    if (!sIsAutoSelectEnable) {
      logd("Auto modem selection disabled");
      removeModemStandByTimer();
      return;
    } 
    logd("Auto modem selection enabled");
    if (sImsi[paramInt] == null || sImsi[paramInt].equals("")) {
      logd("Capaility slot IMSI not ready");
      sUserType = 0;
      return;
    } 
    sUserType = getUserType(sImsi[paramInt]);
    if (sUserType == 1 || sUserType == 2) {
      if (sNwPlmnStrings != null)
        sRegion = getRegion(sNwPlmnStrings[0]); 
      if (sRegion == 1) {
        sFirstSelect[paramInt] = false;
        sIccCardType[paramInt] = getIccCardType(paramInt);
        b = 101;
      } else if (sRegion == 2) {
        sFirstSelect[paramInt] = false;
        b = 100;
      } else {
        logd("Unknown region");
        return;
      } 
    } else if (sUserType == 3) {
      sFirstSelect[paramInt] = false;
      b = 100;
    } else {
      logd("Unknown user type");
      return;
    } 
    if (b == 101) {
      if (WorldPhoneUtil.isLteSupport()) {
        paramInt = 6;
      } else {
        paramInt = 4;
      } 
    } else {
      paramInt = b;
      if (b == 100)
        if (WorldPhoneUtil.isLteSupport()) {
          paramInt = 5;
        } else {
          paramInt = 3;
        }  
    } 
    logd("notifyRadioCapabilityChange: Storing modem type: " + paramInt);
    sCi[0].reloadModemType(paramInt, null);
    resetNetworkProperties();
  }
  
  public void setModemSelectionMode(int paramInt1, int paramInt2) {
    Settings.Global.putInt(sContext.getContentResolver(), "world_phone_auto_select_mode", paramInt1);
    if (paramInt1 == 1) {
      logd("Modem Selection <AUTO>");
      sIsAutoSelectEnable = true;
      sMajorSim = WorldPhoneUtil.getMajorSim();
      handleSimSwitched();
      return;
    } 
    logd("Modem Selection <MANUAL>");
    sIsAutoSelectEnable = false;
    sSwitchModemCauseType = 255;
    logd("sSwitchModemCauseType = " + sSwitchModemCauseType);
    handleSwitchModem(paramInt2);
    if (paramInt2 == ModemSwitchHandler.getActiveModemType()) {
      removeModemStandByTimer();
      return;
    } 
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/worldphone/WorldPhoneOp01.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */