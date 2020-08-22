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
import java.util.ArrayList;
import java.util.Iterator;

public class WorldPhoneOm extends Handler implements IWorldPhone {
  private static final int[] FDD_STANDBY_TIMER;
  
  private static final String[] MCC_TABLE_DOMESTIC;
  
  private static final String[] PLMN_TABLE_TYPE1;
  
  private static final String[] PLMN_TABLE_TYPE1_EXT;
  
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
  
  private static ArrayList<String> sMccDomestic;
  
  private static ModemSwitchHandler sModemSwitchHandler;
  
  private static String[] sNwPlmnStrings;
  
  private static String sOperatorSpec;
  
  private static String sPlmnSs;
  
  private static ArrayList<String> sPlmnType1;
  
  private static ArrayList<String> sPlmnType1Ext;
  
  private static ArrayList<String> sPlmnType3;
  
  private static Phone[] sProxyPhones;
  
  private static int sRegion;
  
  private static int sRilDataRadioTechnology;
  
  private static int sRilDataRegState;
  
  private static int sRilVoiceRadioTechnology;
  
  private static int sRilVoiceRegState;
  
  private static ServiceState sServiceState;
  
  private static int[] sSuspendId;
  
  private static boolean[] sSuspendWaitImsi;
  
  private static int sTddStandByCounter;
  
  private static UiccController sUiccController;
  
  private static int sUserType;
  
  private static boolean sVoiceCapable;
  
  private static int sVoiceRegState;
  
  private static boolean sWaitInFdd;
  
  private static boolean sWaitInTdd;
  
  private Runnable mFddStandByTimerRunnable = new Runnable() {
      public void run() {
        WorldPhoneOm.access$4508();
        if (WorldPhoneOm.sFddStandByCounter >= WorldPhoneOm.FDD_STANDBY_TIMER.length)
          WorldPhoneOm.access$4502(WorldPhoneOm.FDD_STANDBY_TIMER.length - 1); 
        if (WorldPhoneOm.sBtSapState == 0) {
          WorldPhoneOm.logd("FDD time out!");
          WorldPhoneOm.this.handleSwitchModem(101);
          return;
        } 
        WorldPhoneOm.logd("FDD time out but BT SAP is connected, switch not executed!");
      }
    };
  
  private Runnable mTddStandByTimerRunnable = new Runnable() {
      public void run() {
        WorldPhoneOm.access$4308();
        if (WorldPhoneOm.sTddStandByCounter >= WorldPhoneOm.TDD_STANDBY_TIMER.length)
          WorldPhoneOm.access$4302(WorldPhoneOm.TDD_STANDBY_TIMER.length - 1); 
        if (WorldPhoneOm.sBtSapState == 0) {
          WorldPhoneOm.logd("TDD time out!");
          WorldPhoneOm.this.handleSwitchModem(100);
          return;
        } 
        WorldPhoneOm.logd("TDD time out but BT SAP is connected, switch not executed!");
      }
    };
  
  private final BroadcastReceiver mWorldPhoneReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        WorldPhoneOm.logd("[Receiver]+");
        String str = param1Intent.getAction();
        WorldPhoneOm.logd("Action: " + str);
        if ("android.intent.action.SIM_STATE_CHANGED".equals(str)) {
          str = param1Intent.getStringExtra("ss");
          int i = param1Intent.getIntExtra("slot", 0);
          WorldPhoneOm.access$102(WorldPhoneUtil.getMajorSim());
          WorldPhoneOm.logd("slotId: " + i + " simStatus: " + str + " sMajorSim:" + WorldPhoneOm.sMajorSim);
          if ("IMSI".equals(str)) {
            WorldPhoneOm.access$202(UiccController.getInstance());
            if (WorldPhoneOm.sUiccController != null) {
              WorldPhoneOm.sIccRecordsInstance[i] = WorldPhoneOm.sProxyPhones[i].getIccCard().getIccRecords();
              if (WorldPhoneOm.sIccRecordsInstance[i] != null) {
                WorldPhoneOm.sImsi[i] = WorldPhoneOm.sIccRecordsInstance[i].getIMSI();
                WorldPhoneOm.sIccCardType[i] = WorldPhoneOm.this.getIccCardType(i);
                WorldPhoneOm.logd("sImsi[" + i + "]:" + WorldPhoneOm.sImsi[i]);
                if (WorldPhoneOm.sIsAutoSelectEnable && i == WorldPhoneOm.sMajorSim) {
                  WorldPhoneOm.logd("Major SIM");
                  WorldPhoneOm.access$902(WorldPhoneOm.this.getUserType(WorldPhoneOm.sImsi[i]));
                  if (WorldPhoneOm.sFirstSelect[i]) {
                    WorldPhoneOm.sFirstSelect[i] = false;
                    if (WorldPhoneOm.sUserType == 1) {
                      if (WorldPhoneOm.sRegion == 1) {
                        WorldPhoneOm.this.handleSwitchModem(101);
                      } else if (WorldPhoneOm.sRegion == 2) {
                        WorldPhoneOm.this.handleSwitchModem(100);
                      } else {
                        WorldPhoneOm.logd("Region unknown");
                      } 
                    } else if (WorldPhoneOm.sUserType == 2 || WorldPhoneOm.sUserType == 3) {
                      WorldPhoneOm.this.handleSwitchModem(100);
                    } 
                  } 
                  if (WorldPhoneOm.sSuspendWaitImsi[i]) {
                    WorldPhoneOm.sSuspendWaitImsi[i] = false;
                    if (WorldPhoneOm.sNwPlmnStrings != null) {
                      WorldPhoneOm.logd("IMSI fot slot" + i + " now ready, resuming PLMN:" + WorldPhoneOm.sNwPlmnStrings[0] + " with ID:" + WorldPhoneOm.sSuspendId[i]);
                      WorldPhoneOm.this.resumeCampingProcedure(i);
                    } else {
                      WorldPhoneOm.logd("sNwPlmnStrings is Null");
                    } 
                  } 
                } else {
                  WorldPhoneOm.logd("Not major SIM or in maual selection mode");
                  WorldPhoneOm.this.getUserType(WorldPhoneOm.sImsi[i]);
                  if (WorldPhoneOm.sSuspendWaitImsi[i]) {
                    WorldPhoneOm.sSuspendWaitImsi[i] = false;
                    WorldPhoneOm.logd("IMSI fot slot" + i + " now ready, resuming with ID:" + WorldPhoneOm.sSuspendId[i]);
                    WorldPhoneOm.sCi[i].setResumeRegistration(WorldPhoneOm.sSuspendId[i], null);
                  } 
                } 
              } else {
                WorldPhoneOm.logd("Null sIccRecordsInstance");
                return;
              } 
            } else {
              WorldPhoneOm.logd("Null sUiccController");
              return;
            } 
          } else if (str.equals("ABSENT")) {
            WorldPhoneOm.access$1902((String)null);
            WorldPhoneOm.sImsi[i] = "";
            WorldPhoneOm.sFirstSelect[i] = true;
            WorldPhoneOm.sSuspendWaitImsi[i] = false;
            WorldPhoneOm.sIccCardType[i] = 0;
            if (i == WorldPhoneOm.sMajorSim) {
              WorldPhoneOm.logd("Major SIM removed, no world phone service");
              WorldPhoneOm.this.removeModemStandByTimer();
              WorldPhoneOm.access$902(0);
              WorldPhoneOm.access$2102(1);
              WorldPhoneOm.access$102(-99);
            } else {
              WorldPhoneOm.logd("SIM" + i + " is not major SIM");
            } 
          } else if (str.equals("READY")) {
            WorldPhoneOm.logd("reset sIsInvalidSim by solt:" + i);
            WorldPhoneOm.sIsInvalidSim[i] = false;
          } 
        } else {
          StringBuilder stringBuilder;
          if (str.equals("android.intent.action.SERVICE_STATE")) {
            if (param1Intent.getExtras() != null) {
              WorldPhoneOm.access$2302(ServiceState.newFromBundle(param1Intent.getExtras()));
              if (WorldPhoneOm.sServiceState != null) {
                int i = param1Intent.getIntExtra("slot", 0);
                WorldPhoneOm.access$2402(WorldPhoneOm.sServiceState.getOperatorNumeric());
                WorldPhoneOm.access$2502(WorldPhoneOm.sServiceState.getVoiceRegState());
                WorldPhoneOm.access$2602(WorldPhoneOm.sServiceState.getRilVoiceRegState());
                WorldPhoneOm.access$2702(WorldPhoneOm.sServiceState.getRilVoiceRadioTechnology());
                WorldPhoneOm.access$2802(WorldPhoneOm.sServiceState.getDataRegState());
                WorldPhoneOm.access$2902(WorldPhoneOm.sServiceState.getRilDataRegState());
                WorldPhoneOm.access$3002(WorldPhoneOm.sServiceState.getRilDataRadioTechnology());
                WorldPhoneOm.logd("slotId: " + i + ", " + WorldPhoneUtil.iccCardTypeToString(WorldPhoneOm.sIccCardType[i]));
                WorldPhoneOm.logd("sMajorSim: " + WorldPhoneOm.sMajorSim);
                WorldPhoneOm.logd(ModemSwitchHandler.modemToString(ModemSwitchHandler.getActiveModemType()));
                WorldPhoneOm.logd("sPlmnSs: " + WorldPhoneOm.sPlmnSs);
                WorldPhoneOm.logd("sVoiceRegState: " + WorldPhoneUtil.stateToString(WorldPhoneOm.sVoiceRegState));
                WorldPhoneOm.logd("sRilVoiceRegState: " + WorldPhoneUtil.regStateToString(WorldPhoneOm.sRilVoiceRegState));
                stringBuilder = (new StringBuilder()).append("sRilVoiceRadioTech: ");
                WorldPhoneOm.sServiceState;
                WorldPhoneOm.logd(stringBuilder.append(ServiceState.rilRadioTechnologyToString(WorldPhoneOm.sRilVoiceRadioTechnology)).toString());
                WorldPhoneOm.logd("sDataRegState: " + WorldPhoneUtil.stateToString(WorldPhoneOm.sDataRegState));
                WorldPhoneOm.logd("sRilDataRegState: " + WorldPhoneUtil.regStateToString(WorldPhoneOm.sRilDataRegState));
                stringBuilder = (new StringBuilder()).append("sRilDataRadioTech: ");
                WorldPhoneOm.sServiceState;
                WorldPhoneOm.logd(stringBuilder.append(ServiceState.rilRadioTechnologyToString(WorldPhoneOm.sRilDataRadioTechnology)).toString());
                WorldPhoneOm.logd("sIsAutoSelectEnable: " + WorldPhoneOm.sIsAutoSelectEnable);
                if (i == WorldPhoneOm.sMajorSim)
                  if (WorldPhoneOm.sIsAutoSelectEnable) {
                    if (WorldPhoneOm.this.isNoService()) {
                      WorldPhoneOm.this.handleNoService();
                    } else if (WorldPhoneOm.this.isInService()) {
                      WorldPhoneOm.access$1902(WorldPhoneOm.sPlmnSs);
                      WorldPhoneOm.this.removeModemStandByTimer();
                      WorldPhoneOm.logd("reset sIsInvalidSim");
                      WorldPhoneOm.sIsInvalidSim[i] = false;
                    } 
                  } else if (WorldPhoneOm.this.isInService()) {
                    WorldPhoneOm.logd("reset sIsInvalidSim in manual mode");
                    WorldPhoneOm.access$1902(WorldPhoneOm.sPlmnSs);
                    WorldPhoneOm.sIsInvalidSim[i] = false;
                  }  
              } else {
                WorldPhoneOm.logd("Null sServiceState");
              } 
            } 
          } else if (stringBuilder.equals("android.intent.action.ACTION_SHUTDOWN_IPO")) {
            if (WorldPhoneOm.sDefaultBootuUpModem == 100) {
              if (WorldPhoneUtil.isLteSupport()) {
                ModemSwitchHandler.reloadModem(WorldPhoneOm.sCi[0], 5);
                WorldPhoneOm.logd("Reload to FDD CSFB modem");
              } else {
                ModemSwitchHandler.reloadModem(WorldPhoneOm.sCi[0], 3);
                WorldPhoneOm.logd("Reload to WG modem");
              } 
            } else if (WorldPhoneOm.sDefaultBootuUpModem == 101) {
              if (WorldPhoneUtil.isLteSupport()) {
                ModemSwitchHandler.reloadModem(WorldPhoneOm.sCi[0], 6);
                WorldPhoneOm.logd("Reload to TDD CSFB modem");
              } else {
                ModemSwitchHandler.reloadModem(WorldPhoneOm.sCi[0], 4);
                WorldPhoneOm.logd("Reload to TG modem");
              } 
            } 
          } else if (stringBuilder.equals("android.intent.action.ACTION_ADB_SWITCH_MODEM")) {
            int i = param1Intent.getIntExtra("mdType", 0);
            WorldPhoneOm.logd("toModem: " + i);
            if (i == 3 || i == 4 || i == 5 || i == 6) {
              WorldPhoneOm.this.setModemSelectionMode(0, i);
            } else {
              WorldPhoneOm.this.setModemSelectionMode(1, i);
            } 
          } else {
            if (stringBuilder.equals("android.intent.action.AIRPLANE_MODE")) {
              if (!param1Intent.getBooleanExtra("state", false)) {
                WorldPhoneOm.logd("Leave flight mode");
                WorldPhoneOm.access$1902((String)null);
                int j = 0;
                while (true) {
                  if (j < WorldPhoneOm.PROJECT_SIM_NUM) {
                    WorldPhoneOm.sIsInvalidSim[j] = false;
                    j++;
                    continue;
                  } 
                  WorldPhoneOm.logd("[Receiver]-");
                  return;
                } 
              } 
              WorldPhoneOm.logd("Enter flight mode");
              int i = 0;
              while (true) {
                if (i < WorldPhoneOm.PROJECT_SIM_NUM) {
                  WorldPhoneOm.sFirstSelect[i] = true;
                  i++;
                  continue;
                } 
                WorldPhoneOm.logd("[Receiver]-");
                return;
              } 
            } 
            if (stringBuilder.equals("android.intent.action.ACTION_SET_RADIO_CAPABILITY_DONE")) {
              WorldPhoneOm.access$102(WorldPhoneUtil.getMajorSim());
              WorldPhoneOm.this.handleSimSwitched();
            } else {
              String str1;
              if (stringBuilder.equals("android.intent.action.ACTION_TEST_WORLDPHOE")) {
                int j = param1Intent.getIntExtra("FAKE_USER_TYPE", 0);
                int i = param1Intent.getIntExtra("EXTRA_FAKE_REGION", 0);
                boolean bool2 = false;
                boolean bool1 = false;
                if (j == 0 && i == 0) {
                  WorldPhoneOm.logd("Leave ADB Test mode");
                  WorldPhoneOm.sPlmnType1.clear();
                  WorldPhoneOm.sPlmnType1Ext.clear();
                  WorldPhoneOm.sPlmnType3.clear();
                  WorldPhoneOm.sMccDomestic.clear();
                  WorldPhoneOm.loadDefaultData();
                } else {
                  WorldPhoneOm.access$102(WorldPhoneUtil.getMajorSim());
                  if (WorldPhoneOm.sMajorSim != -99 && WorldPhoneOm.sMajorSim != -1) {
                    str1 = WorldPhoneOm.sImsi[WorldPhoneOm.sMajorSim];
                    if (str1 != null && !str1.equals("")) {
                      str1 = str1.substring(0, 5);
                      switch (j) {
                        default:
                          WorldPhoneOm.logd("Unknown fakeUserType:" + j);
                          str1 = WorldPhoneOm.sNwPlmnStrings[0];
                        case 1:
                          WorldPhoneOm.sPlmnType1.add(str1);
                          bool1 = true;
                          str1 = WorldPhoneOm.sNwPlmnStrings[0];
                        case 3:
                          WorldPhoneOm.sPlmnType3.add(str1);
                          bool1 = true;
                          str1 = WorldPhoneOm.sNwPlmnStrings[0];
                      } 
                    } else {
                      WorldPhoneOm.logd("Imsi of sMajorSim is unknown");
                      str1 = WorldPhoneOm.sNwPlmnStrings[0];
                    } 
                    str1 = str1.substring(0, 3);
                    if (i == 1) {
                      WorldPhoneOm.sMccDomestic.add(str1);
                      bool1 = true;
                    } else if (i == 2) {
                      WorldPhoneOm.sMccDomestic.remove(str1);
                      bool1 = true;
                    } else {
                      WorldPhoneOm.logd("Unknown fakeRegion:" + i);
                    } 
                  } else {
                    WorldPhoneOm.logd("sMajorSim is Unknown or Capability OFF");
                    bool1 = bool2;
                  } 
                  if (bool1) {
                    WorldPhoneOm.logd("sPlmnType1:" + WorldPhoneOm.sPlmnType1);
                    WorldPhoneOm.logd("sPlmnType1Ext:" + WorldPhoneOm.sPlmnType1Ext);
                    WorldPhoneOm.logd("sPlmnType3:" + WorldPhoneOm.sPlmnType3);
                    WorldPhoneOm.logd("sMccDomestic:" + WorldPhoneOm.sMccDomestic);
                    WorldPhoneOm.this.handleRadioTechModeSwitch();
                  } 
                } 
              } else if (str1.equals("android.bluetooth.sap.profile.action.CONNECTION_STATE_CHANGED")) {
                int i = param1Intent.getIntExtra("android.bluetooth.profile.extra.STATE", 0);
                if (i == 2) {
                  WorldPhoneOm.logd("BT_SAP connection state is CONNECTED");
                  WorldPhoneOm.access$4202(1);
                } else if (i == 0) {
                  WorldPhoneOm.logd("BT_SAP connection state is DISCONNECTED");
                  WorldPhoneOm.access$4202(0);
                } else {
                  WorldPhoneOm.logd("BT_SAP connection state is " + i);
                } 
              } 
            } 
          } 
        } 
        WorldPhoneOm.logd("[Receiver]-");
      }
    };
  
  static {
    PROJECT_SIM_NUM = WorldPhoneUtil.getProjectSimNum();
    FDD_STANDBY_TIMER = new int[] { 60 };
    TDD_STANDBY_TIMER = new int[] { 40 };
    PLMN_TABLE_TYPE1 = new String[] { "46000", "46002", "46007", "46008" };
    PLMN_TABLE_TYPE1_EXT = new String[0];
    PLMN_TABLE_TYPE3 = new String[] { "46001", "46006", "46009", "45407", "46003", "46005", "45502", "46011" };
    MCC_TABLE_DOMESTIC = new String[] { "460" };
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
  
  public WorldPhoneOm() {
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
    intentFilter.addAction("android.intent.action.ACTION_TEST_WORLDPHOE");
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
    sPlmnType1 = new ArrayList<String>();
    sPlmnType1Ext = new ArrayList<String>();
    sPlmnType3 = new ArrayList<String>();
    sMccDomestic = new ArrayList<String>();
    loadDefaultData();
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
    if (paramString == null || paramString.equals("") || paramString.length() < 3) {
      logd("[getRegion] Invalid PLMN");
      return 0;
    } 
    paramString = paramString.substring(0, 3);
    Iterator<String> iterator = sMccDomestic.iterator();
    while (iterator.hasNext()) {
      if (paramString.equals(iterator.next())) {
        logd("[getRegion] REGION_DOMESTIC");
        return 1;
      } 
    } 
    logd("[getRegion] REGION_FOREIGN");
    return 2;
  }
  
  private int getUserType(String paramString) {
    if (paramString != null && !paramString.equals("")) {
      paramString = paramString.substring(0, 5);
      Iterator<String> iterator = sPlmnType1.iterator();
      while (iterator.hasNext()) {
        if (paramString.equals(iterator.next())) {
          logd("[getUserType] Type1 user");
          return 1;
        } 
      } 
      iterator = sPlmnType1Ext.iterator();
      while (iterator.hasNext()) {
        if (paramString.equals(iterator.next())) {
          logd("[getUserType] Extended Type1 user");
          return 1;
        } 
      } 
      iterator = sPlmnType3.iterator();
      while (iterator.hasNext()) {
        if (paramString.equals(iterator.next())) {
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
      if (sUserType == 1) {
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
            if (!sWaitInFdd) {
              sWaitInFdd = true;
              logd("Wait " + FDD_STANDBY_TIMER[sFddStandByCounter] + "s. Timer index = " + sFddStandByCounter);
              postDelayed(this.mFddStandByTimerRunnable, (FDD_STANDBY_TIMER[sFddStandByCounter] * 1000));
            } else {
              logd("Timer already set:" + FDD_STANDBY_TIMER[sFddStandByCounter] + "s");
            } 
          } else {
            logd("Standby in FDD modem");
          } 
        } 
      } else if (sUserType == 2 || sUserType == 3) {
        if (i == 5 || i == 3) {
          logd("Standby in FDD modem");
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
        if (sMajorSim == paramInt && sUserType == 1 && sDenyReason != 2)
          searchForDesignateService(arrayOfString[0]); 
        sRegion = getRegion(arrayOfString[0]);
        if (sUserType != 3 && sRegion == 2 && sMajorSim != -1)
          handleSwitchModem(100); 
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
      logd("Not major slot or in maual selection mode, camp on OK");
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
    sUserType = getUserType(sImsi[sMajorSim]);
    if (sUserType == 1) {
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
    if (sUserType == 2 || sUserType == 3) {
      sFirstSelect[sMajorSim] = false;
      handleSwitchModem(100);
      return;
    } 
    logd("Unknown user type");
  }
  
  private void handleSwitchModem(int paramInt) {
    int i = WorldPhoneUtil.getMajorSim();
    if (i >= 0 && sIsInvalidSim[i] && WorldPhoneUtil.getModemSelectionMode() == 1) {
      logd("[handleSwitchModem] Invalid SIM, switch not executed!");
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
      logd("[handleSwitchModem] Auto select disable, storing modem type: " + i);
      sCi[0].storeModemType(i, null);
    } else if (sDefaultBootuUpModem == 0) {
      logd("[handleSwitchModem] Storing modem type: " + i);
      sCi[0].storeModemType(i, null);
    } else if (sDefaultBootuUpModem == 100) {
      if (WorldPhoneUtil.isLteSupport()) {
        logd("[handleSwitchModem] Storing modem type: 3");
        sCi[0].storeModemType(5, null);
      } else {
        logd("[handleSwitchModem] Storing modem type: 5");
        sCi[0].storeModemType(3, null);
      } 
    } else if (sDefaultBootuUpModem == 101) {
      if (WorldPhoneUtil.isLteSupport()) {
        logd("[handleSwitchModem] Storing modem type: 3");
        sCi[0].storeModemType(6, null);
      } else {
        logd("[handleSwitchModem] Storing modem type: 5");
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
    if (sUserType == 1) {
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
    if (sUserType == 2 || sUserType == 3) {
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
  
  private static void loadDefaultData() {
    for (String str : PLMN_TABLE_TYPE1)
      sPlmnType1.add(str); 
    for (String str : PLMN_TABLE_TYPE1_EXT)
      sPlmnType1Ext.add(str); 
    for (String str : PLMN_TABLE_TYPE3)
      sPlmnType3.add(str); 
    for (String str : MCC_TABLE_DOMESTIC)
      sMccDomestic.add(str); 
  }
  
  private static void logd(String paramString) {
    Rlog.d("PHONE", "[WPOM]" + paramString);
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
    sIsResumeCampingFail = false;
    sBtSapState = 0;
    resetSimProperties();
    resetNetworkProperties();
  }
  
  private void resetNetworkProperties() {
    // Byte code:
    //   0: ldc_w '[resetNetworkProperties]'
    //   3: invokestatic logd : (Ljava/lang/String;)V
    //   6: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOm.sLock : Ljava/lang/Object;
    //   9: astore_2
    //   10: aload_2
    //   11: monitorenter
    //   12: iconst_0
    //   13: istore_1
    //   14: iload_1
    //   15: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOm.PROJECT_SIM_NUM : I
    //   18: if_icmpge -> 34
    //   21: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOm.sSuspendWaitImsi : [Z
    //   24: iload_1
    //   25: iconst_0
    //   26: bastore
    //   27: iload_1
    //   28: iconst_1
    //   29: iadd
    //   30: istore_1
    //   31: goto -> 14
    //   34: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOm.sNwPlmnStrings : [Ljava/lang/String;
    //   37: ifnull -> 65
    //   40: iconst_0
    //   41: istore_1
    //   42: iload_1
    //   43: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOm.sNwPlmnStrings : [Ljava/lang/String;
    //   46: arraylength
    //   47: if_icmpge -> 65
    //   50: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOm.sNwPlmnStrings : [Ljava/lang/String;
    //   53: iload_1
    //   54: ldc_w ''
    //   57: aastore
    //   58: iload_1
    //   59: iconst_1
    //   60: iadd
    //   61: istore_1
    //   62: goto -> 42
    //   65: aload_2
    //   66: monitorexit
    //   67: return
    //   68: astore_3
    //   69: aload_2
    //   70: monitorexit
    //   71: aload_3
    //   72: athrow
    // Exception table:
    //   from	to	target	type
    //   14	27	68	finally
    //   34	40	68	finally
    //   42	58	68	finally
    //   65	67	68	finally
    //   69	71	68	finally
  }
  
  private void resetSimProperties() {
    // Byte code:
    //   0: ldc_w '[resetSimProperties]'
    //   3: invokestatic logd : (Ljava/lang/String;)V
    //   6: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOm.sLock : Ljava/lang/Object;
    //   9: astore_2
    //   10: aload_2
    //   11: monitorenter
    //   12: iconst_0
    //   13: istore_1
    //   14: iload_1
    //   15: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOm.PROJECT_SIM_NUM : I
    //   18: if_icmpge -> 42
    //   21: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOm.sImsi : [Ljava/lang/String;
    //   24: iload_1
    //   25: ldc_w ''
    //   28: aastore
    //   29: getstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOm.sIccCardType : [I
    //   32: iload_1
    //   33: iconst_0
    //   34: iastore
    //   35: iload_1
    //   36: iconst_1
    //   37: iadd
    //   38: istore_1
    //   39: goto -> 14
    //   42: iconst_0
    //   43: putstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOm.sUserType : I
    //   46: invokestatic getMajorSim : ()I
    //   49: putstatic com/mediatek/internal/telephony/worldphone/WorldPhoneOm.sMajorSim : I
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
    logd("Resume camping slot " + paramInt);
    if (sNwPlmnStrings != null && sNwPlmnStrings[0] != null) {
      if (isAllowCampOn(sNwPlmnStrings[0], paramInt)) {
        removeModemStandByTimer();
        sCi[paramInt].setResumeRegistration(sSuspendId[paramInt], obtainMessage(70));
        return;
      } 
      logd("Because: " + WorldPhoneUtil.denyReasonToString(sDenyReason));
      if (sDenyReason == 2) {
        handleSwitchModem(100);
        return;
      } 
      if (sDenyReason == 3) {
        handleSwitchModem(101);
        return;
      } 
      return;
    } 
    logd("sNwPlmnStrings[0] is null");
  }
  
  private void searchForDesignateService(String paramString) {
    if (paramString == null || paramString.length() < 5) {
      logd("[searchForDesignateService]- null source");
      return;
    } 
    paramString = paramString.substring(0, 5);
    Iterator<String> iterator = sPlmnType1.iterator();
    while (true) {
      if (iterator.hasNext()) {
        if (paramString.equals(iterator.next())) {
          logd("Find TD service");
          logd("sUserType: " + sUserType + " sRegion: " + sRegion);
          logd(ModemSwitchHandler.modemToString(ModemSwitchHandler.getActiveModemType()));
          handleSwitchModem(101);
          return;
        } 
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
        logd("handleMessage : <EVENT_RADIO_ON_1>");
        handleRadioOn(0);
        return;
      case 10:
        logd("handleMessage : <EVENT_REG_PLMN_CHANGED_1>");
        handlePlmnChange(asyncResult, 0);
        return;
      case 30:
        logd("handleMessage : <EVENT_REG_SUSPENDED_1>");
        handleRegistrationSuspend(asyncResult, 0);
        return;
      case 1:
        logd("handleMessage : <EVENT_RADIO_ON_2>");
        handleRadioOn(1);
        return;
      case 11:
        logd("handleMessage : <EVENT_REG_PLMN_CHANGED_2>");
        handlePlmnChange(asyncResult, 1);
        return;
      case 31:
        logd("handleMessage : <EVENT_REG_SUSPENDED_2>");
        handleRegistrationSuspend(asyncResult, 1);
        return;
      case 60:
        logd("handleMessage : <EVENT_INVALID_SIM_NOTIFY_1>");
        handleInvalidSimNotify(0, asyncResult);
        return;
      case 61:
        logd("handleMessage : <EVENT_INVALID_SIM_NOTIFY_2>");
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
  
  public void handleRadioTechModeSwitch() {
    byte b1;
    byte b2;
    logd("[handleRadioTechModeSwitch]");
    if (!sIsAutoSelectEnable) {
      logd("Auto modem selection disabled");
      removeModemStandByTimer();
      return;
    } 
    logd("Auto modem selection enabled");
    if (sImsi[sMajorSim] == null || sImsi[sMajorSim].equals("")) {
      logd("Capaility slot IMSI not ready");
      sUserType = 0;
      return;
    } 
    sUserType = getUserType(sImsi[sMajorSim]);
    if (sUserType == 1) {
      if (sNwPlmnStrings != null)
        sRegion = getRegion(sNwPlmnStrings[0]); 
      if (sRegion == 1) {
        sFirstSelect[sMajorSim] = false;
        sIccCardType[sMajorSim] = getIccCardType(sMajorSim);
        b2 = 101;
      } else if (sRegion == 2) {
        sFirstSelect[sMajorSim] = false;
        b2 = 100;
      } else {
        logd("Unknown region");
        return;
      } 
    } else if (sUserType == 2 || sUserType == 3) {
      sFirstSelect[sMajorSim] = false;
      b2 = 100;
    } else {
      logd("Unknown user type");
      return;
    } 
    if (b2 == 101) {
      if (WorldPhoneUtil.isLteSupport()) {
        b1 = 6;
      } else {
        b1 = 4;
      } 
    } else {
      b1 = b2;
      if (b2 == 100)
        if (WorldPhoneUtil.isLteSupport()) {
          b1 = 5;
        } else {
          b1 = 3;
        }  
    } 
    logd("[handleRadioTechModeSwitch]: switch type: " + b1);
    handleSwitchModem(b1);
    resetNetworkProperties();
  }
  
  public void notifyRadioCapabilityChange(int paramInt) {
    int i = WorldPhoneUtil.getMajorSim();
    logd("[setRadioCapabilityChange] majorSimId:" + i + " capailitySimId=" + paramInt);
    if (!sIsAutoSelectEnable) {
      logd("[setRadioCapabilityChange] Auto modem selection disabled");
      removeModemStandByTimer();
      return;
    } 
    if (sImsi[paramInt] == null || sImsi[paramInt].equals("")) {
      logd("Capaility slot IMSI not ready");
      sUserType = 0;
      return;
    } 
    sUserType = getUserType(sImsi[paramInt]);
    if (sUserType == 1) {
      if (sNwPlmnStrings != null)
        sRegion = getRegion(sNwPlmnStrings[0]); 
      if (sRegion == 1) {
        sFirstSelect[paramInt] = false;
        sIccCardType[paramInt] = getIccCardType(paramInt);
        i = 101;
      } else if (sRegion == 2) {
        sFirstSelect[paramInt] = false;
        i = 100;
      } else {
        logd("Unknown region");
        return;
      } 
    } else if (sUserType == 2 || sUserType == 3) {
      sFirstSelect[paramInt] = false;
      i = 100;
    } else {
      logd("Unknown user type");
      return;
    } 
    if (i == 101) {
      if (WorldPhoneUtil.isLteSupport()) {
        paramInt = 6;
      } else {
        paramInt = 4;
      } 
    } else {
      paramInt = i;
      if (i == 100)
        if (WorldPhoneUtil.isLteSupport()) {
          paramInt = 5;
        } else {
          paramInt = 3;
        }  
    } 
    logd("notifyRadioCapabilityChange: Storing modem type: " + paramInt);
    sCi[0].storeModemType(paramInt, null);
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
    handleSwitchModem(paramInt2);
    if (paramInt2 == ModemSwitchHandler.getActiveModemType()) {
      removeModemStandByTimer();
      return;
    } 
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/worldphone/WorldPhoneOm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */