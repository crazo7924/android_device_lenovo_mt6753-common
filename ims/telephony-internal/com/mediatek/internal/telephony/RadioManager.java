package com.mediatek.internal.telephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.TelephonyManager;
import com.android.ims.ImsManager;
import com.android.internal.telephony.CommandsInterface;
import com.android.internal.telephony.PhoneFactory;
import java.util.concurrent.ConcurrentHashMap;

public class RadioManager extends Handler {
  public static final String ACTION_FORCE_SET_RADIO_POWER = "com.mediatek.internal.telephony.RadioManager.intent.action.FORCE_SET_RADIO_POWER";
  
  private static final String ACTION_WIFI_ONLY_MODE_CHANGED = "android.intent.action.ACTION_WIFI_ONLY_MODE";
  
  protected static final boolean AIRPLANE_MODE_OFF = false;
  
  protected static final boolean AIRPLANE_MODE_ON = true;
  
  private static final int EVENT_RADIO_AVAILABLE = 1;
  
  private static final int EVENT_VIRTUAL_SIM_ON = 2;
  
  private static final boolean ICC_READ_NOT_READY = false;
  
  private static final boolean ICC_READ_READY = true;
  
  protected static final int INITIAL_RETRY_INTERVAL_MSEC = 200;
  
  protected static final int INVALID_PHONE_ID = -1;
  
  private static final String IS_NOT_SILENT_REBOOT = "0";
  
  protected static final String IS_SILENT_REBOOT = "1";
  
  static final String LOG_TAG = "RadioManager";
  
  protected static final boolean MODEM_POWER_OFF = false;
  
  protected static final boolean MODEM_POWER_ON = true;
  
  protected static final int MODE_PHONE1_ONLY = 1;
  
  private static final int MODE_PHONE2_ONLY = 2;
  
  private static final int MODE_PHONE3_ONLY = 4;
  
  private static final int MODE_PHONE4_ONLY = 8;
  
  private static final String MTK_C2K_SUPPORT = "ro.mtk_c2k_support";
  
  protected static final int NO_SIM_INSERTED = 0;
  
  private static final String PREF_CATEGORY_RADIO_STATUS = "RADIO_STATUS";
  
  protected static String[] PROPERTY_ICCID_SIM;
  
  protected static String[] PROPERTY_RADIO_OFF;
  
  private static final String PROPERTY_SILENT_REBOOT_CDMA = "cdma.ril.eboot";
  
  protected static final String PROPERTY_SILENT_REBOOT_MD1 = "gsm.ril.eboot";
  
  protected static final String PROPERTY_SILENT_REBOOT_MD2 = "gsm.ril.eboot.2";
  
  protected static final boolean RADIO_POWER_OFF = false;
  
  protected static final boolean RADIO_POWER_ON = true;
  
  private static final String REGISTRANTS_WITH_NO_NAME = "NO_NAME";
  
  protected static final int SIM_INSERTED = 1;
  
  private static final int SIM_NOT_INITIALIZED = -1;
  
  protected static final String STRING_NO_SIM_INSERTED = "N/A";
  
  private static final int WIFI_ONLY_INIT = -1;
  
  private static final boolean WIFI_ONLY_MODE_OFF = false;
  
  private static final boolean WIFI_ONLY_MODE_ON = true;
  
  protected static ConcurrentHashMap<IRadioPower, String> mNotifyRadioPowerChange = new ConcurrentHashMap<IRadioPower, String>();
  
  protected static SharedPreferences sIccidPreference;
  
  private static RadioManager sRadioManager;
  
  private boolean bIsInIpoShutdown;
  
  private boolean bIsQueueIpoShutdown;
  
  protected boolean mAirplaneMode;
  
  private AirplaneRequestHandler mAirplaneRequestHandler;
  
  protected int mBitmapForPhoneCount;
  
  private CommandsInterface[] mCi;
  
  private Context mContext;
  
  private ImsSwitchController mImsSwitchController;
  
  private int[] mInitializeWaitCounter;
  
  private BroadcastReceiver mIntentReceiver;
  
  protected boolean mIsEccCall;
  
  protected int mPhoneCount;
  
  protected int[] mSimInsertedStatus;
  
  private int mSimModeSetting;
  
  private boolean mWifiOnlyMode;
  
  static {
    PROPERTY_ICCID_SIM = new String[] { "ril.iccid.sim1", "ril.iccid.sim2", "ril.iccid.sim3", "ril.iccid.sim4" };
    PROPERTY_RADIO_OFF = new String[] { "ril.ipo.radiooff", "ril.ipo.radiooff.2" };
  }
  
  protected RadioManager(Context paramContext, int paramInt, CommandsInterface[] paramArrayOfCommandsInterface) {
    boolean bool;
    this.mAirplaneMode = false;
    this.mWifiOnlyMode = false;
    this.mImsSwitchController = null;
    this.mIntentReceiver = new BroadcastReceiver() {
        public void onReceive(Context param1Context, Intent param1Intent) {
          RadioManager.log("BroadcastReceiver: " + param1Intent.getAction());
          if (param1Intent.getAction().equals("android.intent.action.SIM_STATE_CHANGED")) {
            RadioManager.this.onReceiveSimStateChangedIntent(param1Intent);
            return;
          } 
          if (param1Intent.getAction().equals("com.mediatek.internal.telephony.RadioManager.intent.action.FORCE_SET_RADIO_POWER")) {
            RadioManager.this.onReceiveForceSetRadioPowerIntent(param1Intent);
            return;
          } 
          if (param1Intent.getAction().equals("android.intent.action.ACTION_WIFI_ONLY_MODE")) {
            RadioManager.this.onReceiveWifiOnlyModeStateChangedIntent(param1Intent);
            return;
          } 
        }
      };
    int k = Settings.Global.getInt(paramContext.getContentResolver(), "airplane_mode_on", 0);
    int i = -1;
    if (ImsManager.isWfcEnabledByUser(paramContext))
      i = ImsManager.getWfcMode(paramContext); 
    log("Initialize RadioManager under airplane mode:" + k + " wifi only mode:" + i);
    this.mSimInsertedStatus = new int[paramInt];
    int j;
    for (j = 0; j < paramInt; j++)
      this.mSimInsertedStatus[j] = -1; 
    this.mInitializeWaitCounter = new int[paramInt];
    for (j = 0; j < paramInt; j++)
      this.mInitializeWaitCounter[j] = 0; 
    this.mContext = paramContext;
    if (k == 0) {
      bool = false;
    } else {
      bool = true;
    } 
    this.mAirplaneMode = bool;
    if (i == 0) {
      bool = true;
    } else {
      bool = false;
    } 
    this.mWifiOnlyMode = bool;
    this.mCi = paramArrayOfCommandsInterface;
    this.mPhoneCount = paramInt;
    this.mBitmapForPhoneCount = convertPhoneCountIntoBitmap(paramInt);
    sIccidPreference = this.mContext.getSharedPreferences("RADIO_STATUS", 0);
    this.mSimModeSetting = Settings.System.getInt(paramContext.getContentResolver(), "msim_mode_setting", this.mBitmapForPhoneCount);
    this.mImsSwitchController = new ImsSwitchController(this.mContext, this.mPhoneCount, this.mCi);
    if (!SystemProperties.get("ro.mtk_bsp_package").equals("1")) {
      log("Not BSP Package, register intent!!!");
      IntentFilter intentFilter = new IntentFilter();
      intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
      intentFilter.addAction("com.mediatek.internal.telephony.RadioManager.intent.action.FORCE_SET_RADIO_POWER");
      intentFilter.addAction("android.intent.action.ACTION_WIFI_ONLY_MODE");
      this.mContext.registerReceiver(this.mIntentReceiver, intentFilter);
      for (i = 0; i < paramInt; i++) {
        Integer integer = new Integer(i);
        this.mCi[i].registerForVirtualSimOn(this, 2, integer);
        this.mCi[i].registerForAvailable(this, 1, null);
      } 
    } 
    this.mAirplaneRequestHandler = new AirplaneRequestHandler(this.mContext, this.mPhoneCount);
  }
  
  private int convertPhoneCountIntoBitmap(int paramInt) {
    int j = 0;
    for (int i = 0; i < paramInt; i++)
      j += 1 << i; 
    log("Convert phoneCount " + paramInt + " into bitmap " + j);
    return j;
  }
  
  private String eventIdtoString(int paramInt) {
    switch (paramInt) {
      default:
        return null;
      case 1:
        return "EVENT_RADIO_AVAILABLE";
      case 2:
        break;
    } 
    return "EVENT_VIRTUAL_SIM_ON";
  }
  
  private boolean getAirplaneMode() {
    return this.mAirplaneMode;
  }
  
  private int getCiIndex(Message paramMessage) {
    Integer integer2 = new Integer(0);
    Integer integer1 = integer2;
    if (paramMessage != null) {
      if (paramMessage.obj != null && paramMessage.obj instanceof Integer) {
        integer1 = (Integer)paramMessage.obj;
        return integer1.intValue();
      } 
    } else {
      return integer1.intValue();
    } 
    integer1 = integer2;
    if (paramMessage.obj != null) {
      integer1 = integer2;
      if (paramMessage.obj instanceof AsyncResult) {
        AsyncResult asyncResult = (AsyncResult)paramMessage.obj;
        integer1 = integer2;
        if (asyncResult.userObj != null) {
          integer1 = integer2;
          if (asyncResult.userObj instanceof Integer)
            integer1 = (Integer)asyncResult.userObj; 
        } 
      } 
    } 
    return integer1.intValue();
  }
  
  public static RadioManager getInstance() {
    // Byte code:
    //   0: ldc com/mediatek/internal/telephony/RadioManager
    //   2: monitorenter
    //   3: getstatic com/mediatek/internal/telephony/RadioManager.sRadioManager : Lcom/mediatek/internal/telephony/RadioManager;
    //   6: astore_0
    //   7: ldc com/mediatek/internal/telephony/RadioManager
    //   9: monitorexit
    //   10: aload_0
    //   11: areturn
    //   12: astore_0
    //   13: ldc com/mediatek/internal/telephony/RadioManager
    //   15: monitorexit
    //   16: aload_0
    //   17: athrow
    // Exception table:
    //   from	to	target	type
    //   3	10	12	finally
    //   13	16	12	finally
  }
  
  public static RadioManager init(Context paramContext, int paramInt, CommandsInterface[] paramArrayOfCommandsInterface) {
    // Byte code:
    //   0: ldc com/mediatek/internal/telephony/RadioManager
    //   2: monitorenter
    //   3: getstatic com/mediatek/internal/telephony/RadioManager.sRadioManager : Lcom/mediatek/internal/telephony/RadioManager;
    //   6: ifnonnull -> 22
    //   9: new com/mediatek/internal/telephony/RadioManager
    //   12: dup
    //   13: aload_0
    //   14: iload_1
    //   15: aload_2
    //   16: invokespecial <init> : (Landroid/content/Context;I[Lcom/android/internal/telephony/CommandsInterface;)V
    //   19: putstatic com/mediatek/internal/telephony/RadioManager.sRadioManager : Lcom/mediatek/internal/telephony/RadioManager;
    //   22: getstatic com/mediatek/internal/telephony/RadioManager.sRadioManager : Lcom/mediatek/internal/telephony/RadioManager;
    //   25: astore_0
    //   26: ldc com/mediatek/internal/telephony/RadioManager
    //   28: monitorexit
    //   29: aload_0
    //   30: areturn
    //   31: astore_0
    //   32: ldc com/mediatek/internal/telephony/RadioManager
    //   34: monitorexit
    //   35: aload_0
    //   36: athrow
    // Exception table:
    //   from	to	target	type
    //   3	22	31	finally
    //   22	29	31	finally
    //   32	35	31	finally
  }
  
  public static boolean isCardActive(String paramString) {
    return !(sIccidPreference != null && sIccidPreference.contains(paramString));
  }
  
  public static boolean isFlightModePowerOffModemEnabled() {
    // Byte code:
    //   0: iconst_1
    //   1: istore #4
    //   3: ldc_w 'ril.testmode'
    //   6: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   9: ldc '1'
    //   11: invokevirtual equals : (Ljava/lang/Object;)Z
    //   14: ifeq -> 31
    //   17: ldc_w 'ril.test.poweroffmd'
    //   20: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   23: ldc '1'
    //   25: invokevirtual equals : (Ljava/lang/Object;)Z
    //   28: istore_2
    //   29: iload_2
    //   30: ireturn
    //   31: ldc_w 'ro.operator.optr'
    //   34: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   37: astore #5
    //   39: aload #5
    //   41: ifnull -> 194
    //   44: aload #5
    //   46: ldc_w 'op01'
    //   49: invokevirtual equalsIgnoreCase : (Ljava/lang/String;)Z
    //   52: ifeq -> 194
    //   55: iconst_1
    //   56: istore_0
    //   57: ldc_w 'gsm.sim.ril.testsim'
    //   60: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   63: ldc '1'
    //   65: invokevirtual equals : (Ljava/lang/Object;)Z
    //   68: ifne -> 113
    //   71: ldc_w 'gsm.sim.ril.testsim.2'
    //   74: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   77: ldc '1'
    //   79: invokevirtual equals : (Ljava/lang/Object;)Z
    //   82: ifne -> 113
    //   85: ldc_w 'gsm.sim.ril.testsim.3'
    //   88: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   91: ldc '1'
    //   93: invokevirtual equals : (Ljava/lang/Object;)Z
    //   96: ifne -> 113
    //   99: ldc_w 'gsm.sim.ril.testsim.4'
    //   102: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   105: ldc '1'
    //   107: invokevirtual equals : (Ljava/lang/Object;)Z
    //   110: ifeq -> 199
    //   113: iconst_1
    //   114: istore_1
    //   115: iload_0
    //   116: ifeq -> 126
    //   119: iload #4
    //   121: istore_2
    //   122: iload_1
    //   123: ifne -> 29
    //   126: invokestatic getDefault : ()Landroid/telephony/TelephonyManager;
    //   129: iconst_0
    //   130: invokevirtual hasIccCard : (I)Z
    //   133: ifne -> 146
    //   136: invokestatic getDefault : ()Landroid/telephony/TelephonyManager;
    //   139: iconst_1
    //   140: invokevirtual hasIccCard : (I)Z
    //   143: ifeq -> 204
    //   146: iconst_1
    //   147: istore_3
    //   148: new java/lang/StringBuilder
    //   151: dup
    //   152: invokespecial <init> : ()V
    //   155: ldc_w 'isFlightModePowerOffModemEnabled: hasIccCard: '
    //   158: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   161: iload_3
    //   162: invokevirtual append : (Z)Ljava/lang/StringBuilder;
    //   165: invokevirtual toString : ()Ljava/lang/String;
    //   168: invokestatic log : (Ljava/lang/String;)V
    //   171: iload_0
    //   172: ifeq -> 182
    //   175: iload #4
    //   177: istore_2
    //   178: iload_3
    //   179: ifeq -> 29
    //   182: ldc_w 'ro.mtk_flight_mode_power_off_md'
    //   185: invokestatic get : (Ljava/lang/String;)Ljava/lang/String;
    //   188: ldc '1'
    //   190: invokevirtual equals : (Ljava/lang/Object;)Z
    //   193: ireturn
    //   194: iconst_0
    //   195: istore_0
    //   196: goto -> 57
    //   199: iconst_0
    //   200: istore_1
    //   201: goto -> 115
    //   204: iconst_0
    //   205: istore_3
    //   206: goto -> 148
  }
  
  public static boolean isMSimModeSupport() {
    return !SystemProperties.get("ro.mtk_bsp_package").equals("1");
  }
  
  public static boolean isModemPowerOff(int paramInt) {
    return getInstance().isModemOff(paramInt);
  }
  
  public static boolean isPowerOnFeatureAllClosed() {
    boolean bool = true;
    return isFlightModePowerOffModemEnabled() ? false : (isRadioOffPowerOffModemEnabled() ? false : (isMSimModeSupport() ? false : bool));
  }
  
  public static boolean isRadioOffPowerOffModemEnabled() {
    return SystemProperties.get("ro.mtk_radiooff_power_off_md").equals("1");
  }
  
  private static void log(String paramString) {
    Rlog.d("RadioManager", "[RadioManager] " + paramString);
  }
  
  private static void notifyRadioPowerChange(boolean paramBoolean, int paramInt) {
    // Byte code:
    //   0: ldc com/mediatek/internal/telephony/RadioManager
    //   2: monitorenter
    //   3: getstatic com/mediatek/internal/telephony/RadioManager.mNotifyRadioPowerChange : Ljava/util/concurrent/ConcurrentHashMap;
    //   6: invokevirtual entrySet : ()Ljava/util/Set;
    //   9: invokeinterface iterator : ()Ljava/util/Iterator;
    //   14: astore_2
    //   15: aload_2
    //   16: invokeinterface hasNext : ()Z
    //   21: ifeq -> 90
    //   24: aload_2
    //   25: invokeinterface next : ()Ljava/lang/Object;
    //   30: checkcast java/util/Map$Entry
    //   33: astore_3
    //   34: new java/lang/StringBuilder
    //   37: dup
    //   38: invokespecial <init> : ()V
    //   41: ldc_w 'notifyRadioPowerChange: user:'
    //   44: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   47: aload_3
    //   48: invokeinterface getValue : ()Ljava/lang/Object;
    //   53: checkcast java/lang/String
    //   56: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   59: invokevirtual toString : ()Ljava/lang/String;
    //   62: invokestatic log : (Ljava/lang/String;)V
    //   65: aload_3
    //   66: invokeinterface getKey : ()Ljava/lang/Object;
    //   71: checkcast com/mediatek/internal/telephony/IRadioPower
    //   74: iload_0
    //   75: iload_1
    //   76: invokeinterface notifyRadioPowerChange : (ZI)V
    //   81: goto -> 15
    //   84: astore_2
    //   85: ldc com/mediatek/internal/telephony/RadioManager
    //   87: monitorexit
    //   88: aload_2
    //   89: athrow
    //   90: ldc com/mediatek/internal/telephony/RadioManager
    //   92: monitorexit
    //   93: return
    // Exception table:
    //   from	to	target	type
    //   3	15	84	finally
    //   15	81	84	finally
  }
  
  private void onReceiveForceSetRadioPowerIntent(Intent paramIntent) {
    int j = paramIntent.getIntExtra("mode", -1);
    log("force set radio power, mode: " + j);
    if (j == -1) {
      log("Invalid mode, MSIM_MODE intent has no extra value");
      return;
    } 
    int i = 0;
    while (true) {
      if (i < this.mPhoneCount) {
        boolean bool;
        if ((1 << i & j) == 0) {
          bool = false;
        } else {
          bool = true;
        } 
        if (true == bool)
          forceSetRadioPower(true, i); 
        i++;
        continue;
      } 
      return;
    } 
  }
  
  private void putIccIdToPreference(SharedPreferences.Editor paramEditor, String paramString) {
    if (paramString != null) {
      log("Add radio off SIM: " + paramString);
      paramEditor.putInt(paramString, 0);
    } 
  }
  
  public static void registerForRadioPowerChange(String paramString, IRadioPower paramIRadioPower) {
    // Byte code:
    //   0: ldc com/mediatek/internal/telephony/RadioManager
    //   2: monitorenter
    //   3: aload_0
    //   4: astore_2
    //   5: aload_0
    //   6: ifnonnull -> 12
    //   9: ldc 'NO_NAME'
    //   11: astore_2
    //   12: new java/lang/StringBuilder
    //   15: dup
    //   16: invokespecial <init> : ()V
    //   19: aload_2
    //   20: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   23: ldc_w ' registerForRadioPowerChange'
    //   26: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: invokevirtual toString : ()Ljava/lang/String;
    //   32: invokestatic log : (Ljava/lang/String;)V
    //   35: getstatic com/mediatek/internal/telephony/RadioManager.mNotifyRadioPowerChange : Ljava/util/concurrent/ConcurrentHashMap;
    //   38: aload_1
    //   39: aload_2
    //   40: invokevirtual put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
    //   43: pop
    //   44: ldc com/mediatek/internal/telephony/RadioManager
    //   46: monitorexit
    //   47: return
    //   48: astore_0
    //   49: ldc com/mediatek/internal/telephony/RadioManager
    //   51: monitorexit
    //   52: aload_0
    //   53: athrow
    // Exception table:
    //   from	to	target	type
    //   12	44	48	finally
  }
  
  private void removeIccIdFromPreference(SharedPreferences.Editor paramEditor, String paramString) {
    if (paramString != null) {
      log("Remove radio off SIM: " + paramString);
      paramEditor.remove(paramString);
    } 
  }
  
  public static void sendRequestBeforeSetRadioPower(boolean paramBoolean, int paramInt) {
    log("Send request before EFUN, power:" + paramBoolean + " phoneId:" + paramInt);
    notifyRadioPowerChange(paramBoolean, paramInt);
  }
  
  private void setAirplaneMode(boolean paramBoolean) {
    log("set mAirplaneMode as:" + paramBoolean);
    this.mAirplaneMode = paramBoolean;
  }
  
  private void turnOffCTARadioIfNecessary() {
    for (int i = 0;; i++) {
      if (i < this.mPhoneCount) {
        if (this.mSimInsertedStatus[i] == 0) {
          if (isModemPowerOff(i)) {
            log("modem off, not to handle CTA");
            return;
          } 
          log("turn off phone " + i + " radio because we are no longer in CTA mode");
          PhoneFactory.getPhone(i).setRadioPower(false);
        } 
      } else {
        return;
      } 
    } 
  }
  
  public static void unregisterForRadioPowerChange(IRadioPower paramIRadioPower) {
    // Byte code:
    //   0: ldc com/mediatek/internal/telephony/RadioManager
    //   2: monitorenter
    //   3: new java/lang/StringBuilder
    //   6: dup
    //   7: invokespecial <init> : ()V
    //   10: getstatic com/mediatek/internal/telephony/RadioManager.mNotifyRadioPowerChange : Ljava/util/concurrent/ConcurrentHashMap;
    //   13: aload_0
    //   14: invokevirtual get : (Ljava/lang/Object;)Ljava/lang/Object;
    //   17: checkcast java/lang/String
    //   20: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   23: ldc_w ' unregisterForRadioPowerChange'
    //   26: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: invokevirtual toString : ()Ljava/lang/String;
    //   32: invokestatic log : (Ljava/lang/String;)V
    //   35: getstatic com/mediatek/internal/telephony/RadioManager.mNotifyRadioPowerChange : Ljava/util/concurrent/ConcurrentHashMap;
    //   38: aload_0
    //   39: invokevirtual remove : (Ljava/lang/Object;)Ljava/lang/Object;
    //   42: pop
    //   43: ldc com/mediatek/internal/telephony/RadioManager
    //   45: monitorexit
    //   46: return
    //   47: astore_0
    //   48: ldc com/mediatek/internal/telephony/RadioManager
    //   50: monitorexit
    //   51: aload_0
    //   52: athrow
    // Exception table:
    //   from	to	target	type
    //   3	43	47	finally
  }
  
  protected boolean checkForCTACase() {
    boolean bool2;
    boolean bool1 = true;
    log("Check For CTA case!");
    if (!this.mAirplaneMode && this.mWifiOnlyMode != true) {
      int i = 0;
      while (true) {
        bool2 = bool1;
        if (i < this.mPhoneCount) {
          log("Check For CTA case: mSimInsertedStatus[" + i + "]:" + this.mSimInsertedStatus[i]);
          if (this.mSimInsertedStatus[i] == 1 || this.mSimInsertedStatus[i] == -1)
            bool1 = false; 
          i++;
          continue;
        } 
        break;
      } 
    } else {
      bool2 = false;
    } 
    if (!bool2 && !this.mIsEccCall)
      turnOffCTARadioIfNecessary(); 
    log("CTA case: " + bool2);
    return bool2;
  }
  
  protected int findMainCapabilityPhoneId() {
    int i = Integer.valueOf(SystemProperties.get("persist.radio.simswitch", "1")).intValue() - 1;
    if (i >= 0) {
      int j = i;
      return (i >= this.mPhoneCount) ? 0 : j;
    } 
    return 0;
  }
  
  public void forceAllowAirplaneModeChange(boolean paramBoolean) {
    this.mAirplaneRequestHandler.setForceSwitch(paramBoolean);
  }
  
  public void forceSetRadioPower(boolean paramBoolean, int paramInt) {
    log("force set radio power for phone" + paramInt + " ,power: " + paramBoolean);
    if (isFlightModePowerOffModemEnabled() && this.mAirplaneMode == true) {
      log("Force Set Radio Power under airplane mode, ignore");
      return;
    } 
    if (this.bIsInIpoShutdown) {
      log("Force Set Radio Power under ipo shutdown, ignore");
      return;
    } 
    if (isModemPowerOff(paramInt)) {
      refreshIccIdPreference(paramBoolean, readIccIdUsingPhoneId(paramInt));
      log("Modem Power Off for phone " + paramInt + ", Power on modem first");
      setModemPower(true, 1 << paramInt);
      return;
    } 
    if (!isIccIdReady(paramInt)) {
      log("force set radio power, read iccid not ready, wait for200ms");
      postDelayed(new ForceSetRadioPowerRunnable(paramBoolean, paramInt), 200L);
      return;
    } 
    refreshIccIdPreference(paramBoolean, readIccIdUsingPhoneId(paramInt));
    PhoneFactory.getPhone(paramInt).setRadioPower(paramBoolean);
  }
  
  public void forceSetRadioPower(boolean paramBoolean1, int paramInt, boolean paramBoolean2) {
    log("force set radio power isEccOn: " + paramBoolean2);
    this.mIsEccCall = paramBoolean2;
    forceSetRadioPower(paramBoolean1, paramInt);
  }
  
  protected int getSimInsertedStatus(int paramInt) {
    return this.mSimInsertedStatus[paramInt];
  }
  
  public void handleMessage(Message paramMessage) {
    int i = getCiIndex(paramMessage);
    log("handleMessage msg.what: " + eventIdtoString(paramMessage.what));
    switch (paramMessage.what) {
      default:
        super.handleMessage(paramMessage);
        return;
      case 1:
        if (this.bIsQueueIpoShutdown) {
          log("bIsQueueIpoShutdown is true");
          this.bIsQueueIpoShutdown = false;
          log("IPO shut down retry!");
          setModemPower(false, this.mBitmapForPhoneCount);
          return;
        } 
        return;
      case 2:
        break;
    } 
    forceSetRadioPower(true, i);
  }
  
  public boolean isAllowAirplaneModeChange() {
    return this.mAirplaneRequestHandler.allowSwitching();
  }
  
  protected boolean isIccIdReady(int paramInt) {
    String str = readIccIdUsingPhoneId(paramInt);
    if (str == null || "".equals(str)) {
      log("ICC read not ready for phone:" + paramInt);
      return false;
    } 
    log("ICC read ready, iccid[" + paramInt + "]: " + str);
    return true;
  }
  
  protected boolean isModemOff(int paramInt) {
    TelephonyManager.MultiSimVariants multiSimVariants = TelephonyManager.getDefault().getMultiSimConfiguration();
    switch (multiSimVariants) {
      default:
        return !SystemProperties.get("ril.ipo.radiooff").equals("0");
      case DSDS:
        return !SystemProperties.get("ril.ipo.radiooff").equals("0");
      case DSDA:
        switch (paramInt) {
          default:
            return true;
          case 0:
            return !SystemProperties.get("ril.ipo.radiooff").equals("0");
          case 1:
            break;
        } 
        return !SystemProperties.get("ril.ipo.radiooff.2").equals("0");
      case TSTS:
        break;
    } 
    return !SystemProperties.get("ril.ipo.radiooff").equals("0");
  }
  
  protected boolean isUnderCryptKeeper() {
    if (SystemProperties.get("ro.crypto.state").equals("encrypted") && SystemProperties.get("vold.decrypt").equals("trigger_restart_min_framework")) {
      log("[Special Case] Under CryptKeeper, Not to turn on/off modem");
      return true;
    } 
    log("[Special Case] Not Under CryptKeeper");
    return false;
  }
  
  protected boolean isValidPhoneId(int paramInt) {
    return !(paramInt < 0 || paramInt >= TelephonyManager.getDefault().getPhoneCount());
  }
  
  public void notifyAirplaneModeChange(boolean paramBoolean) {
    String str;
    boolean bool = false;
    if (!this.mAirplaneRequestHandler.allowSwitching()) {
      log("airplane mode switching, not allow switch now ");
      this.mAirplaneRequestHandler.pendingAirplaneModeRequest(paramBoolean);
      return;
    } 
    if (this.mAirplaneRequestHandler.waitForReady(paramBoolean)) {
      log("airplane mode switching, wait for ready, not allow switch now");
      return;
    } 
    if (paramBoolean == this.mAirplaneMode) {
      log("enabled = " + paramBoolean + ", mAirplaneMode = " + this.mAirplaneMode + "is not expected (the same)");
      return;
    } 
    this.mAirplaneMode = paramBoolean;
    log("Airplane mode changed:" + paramBoolean);
    if (paramBoolean) {
      str = "true";
    } else {
      str = "false";
    } 
    SystemProperties.set("persist.radio.airplane.mode.on", str);
    if (isFlightModePowerOffModemEnabled() && !isUnderCryptKeeper()) {
      log("Airplane mode changed: turn on/off all modem");
      if (paramBoolean) {
        paramBoolean = bool;
      } else {
        paramBoolean = true;
      } 
      setSilentRebootPropertyForAllModem("1");
      setModemPower(paramBoolean, this.mBitmapForPhoneCount);
      this.mAirplaneRequestHandler.monitorAirplaneChangeDone(paramBoolean);
      return;
    } 
    if (isMSimModeSupport()) {
      log("Airplane mode changed: turn on/off all radio");
      if (paramBoolean) {
        paramBoolean = false;
      } else {
        paramBoolean = true;
      } 
      for (int i = 0; i < this.mPhoneCount; i++)
        setRadioPower(paramBoolean, i); 
      this.mAirplaneRequestHandler.monitorAirplaneChangeDone(paramBoolean);
      return;
    } 
  }
  
  public void notifyIpoPreBoot() {
    log("IPO preboot!");
    this.bIsInIpoShutdown = false;
    this.bIsQueueIpoShutdown = false;
    setSilentRebootPropertyForAllModem("0");
    setModemPower(true, this.mBitmapForPhoneCount);
  }
  
  public void notifyIpoShutDown() {
    log("notify IPO shutdown!");
    this.bIsInIpoShutdown = true;
    for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
      log("mCi[" + i + "].getRadioState().isAvailable(): " + this.mCi[i].getRadioState().isAvailable());
      if (!this.mCi[i].getRadioState().isAvailable())
        this.bIsQueueIpoShutdown = true; 
    } 
    setModemPower(false, this.mBitmapForPhoneCount);
  }
  
  public void notifyMSimModeChange(int paramInt) {
    log("MSIM mode changed, mode: " + paramInt);
    if (paramInt == -1) {
      log("Invalid mode, MSIM_MODE intent has no extra value");
      return;
    } 
    if (!isMSimModeSupport() || this.mAirplaneMode == true) {
      log("Airplane mode on or MSIM Mode option is closed, do nothing!");
      return;
    } 
    boolean bool = true;
    int i = 0;
    while (true) {
      boolean bool1 = bool;
      if (i < this.mPhoneCount)
        if (!isIccIdReady(i)) {
          bool1 = false;
        } else {
          i++;
          continue;
        }  
      if (!bool1) {
        log("msim mode read iccid not ready, wait for 200ms");
        postDelayed(new MSimModeChangeRunnable(paramInt), 200L);
        return;
      } 
      i = 0;
      while (true) {
        if (i < this.mPhoneCount) {
          boolean bool2;
          if ((1 << i & paramInt) == 0) {
            bool2 = false;
          } else {
            bool2 = true;
          } 
          if ("N/A".equals(readIccIdUsingPhoneId(i))) {
            bool2 = false;
            log("phoneId " + i + " sim not insert, set  power  to " + Character.MIN_VALUE);
          } 
          refreshIccIdPreference(bool2, readIccIdUsingPhoneId(i));
          log("Set Radio Power due to MSIM mode change, power: " + bool2 + ", phoneId: " + i);
          setPhoneRadioPower(bool2, i);
          i++;
          continue;
        } 
        return;
      } 
      break;
    } 
  }
  
  public void notifyRadioAvailable(int paramInt) {
    log("Phone " + paramInt + " notifies radio available");
    if (this.mAirplaneMode == true && isFlightModePowerOffModemEnabled() && !isUnderCryptKeeper()) {
      log("Power off modem because boot up under airplane mode");
      setModemPower(false, 1 << paramInt);
    } 
  }
  
  public void notifySimModeChange(boolean paramBoolean, int paramInt) {
    log("SIM mode changed, power: " + paramBoolean + ", phoneId" + paramInt);
    if (!isMSimModeSupport() || this.mAirplaneMode == true) {
      log("Airplane mode on or MSIM Mode option is closed, do nothing!");
      return;
    } 
    if (!isIccIdReady(paramInt)) {
      log("sim mode read iccid not ready, wait for 200ms");
      postDelayed(new SimModeChangeRunnable(paramBoolean, paramInt), 200L);
      return;
    } 
    if ("N/A".equals(readIccIdUsingPhoneId(paramInt))) {
      paramBoolean = false;
      log("phoneId " + paramInt + " sim not insert, set  power  to " + Character.MIN_VALUE);
    } 
    refreshIccIdPreference(paramBoolean, readIccIdUsingPhoneId(paramInt));
    log("Set Radio Power due to SIM mode change, power: " + paramBoolean + ", phoneId: " + paramInt);
    setPhoneRadioPower(paramBoolean, paramInt);
  }
  
  protected void onReceiveSimStateChangedIntent(Intent paramIntent) {
    String str = paramIntent.getStringExtra("ss");
    int i = paramIntent.getIntExtra("phone", -1);
    if (!isValidPhoneId(i)) {
      log("INTENT:Invalid phone id:" + i + ", do nothing!");
      return;
    } 
    log("INTENT:SIM_STATE_CHANGED: " + paramIntent.getAction() + ", sim status: " + str + ", phoneId: " + i);
    if ("READY".equals(str) || "LOCKED".equals(str) || "LOADED".equals(str)) {
      this.mSimInsertedStatus[i] = 1;
      log("Phone[" + i + "]: " + simStatusToString(1));
      if ("N/A".equals(readIccIdUsingPhoneId(i))) {
        log("Phone " + i + ":SIM ready but ICCID not ready, do nothing");
        return;
      } 
      if (!this.mAirplaneMode) {
        log("Set Radio Power due to SIM_STATE_CHANGED, power: " + '\001' + ", phoneId: " + i);
        setRadioPower(true, i);
        return;
      } 
      return;
    } 
    if ("ABSENT".equals(str)) {
      this.mSimInsertedStatus[i] = 0;
      log("Phone[" + i + "]: " + simStatusToString(0));
      if (!this.mAirplaneMode) {
        log("Set Radio Power due to SIM_STATE_CHANGED, power: " + Character.MIN_VALUE + ", phoneId: " + i);
        setRadioPower(false, i);
        return;
      } 
    } 
  }
  
  public void onReceiveWifiOnlyModeStateChangedIntent(Intent paramIntent) {
    boolean bool = false;
    boolean bool1 = paramIntent.getBooleanExtra("state", false);
    log("mReceiver: ACTION_WIFI_ONLY_MODE_CHANGED, enabled = " + bool1);
    if (bool1 == this.mWifiOnlyMode) {
      log("enabled = " + bool1 + ", mWifiOnlyMode = " + this.mWifiOnlyMode + "is not expected (the same)");
      return;
    } 
    this.mWifiOnlyMode = bool1;
    if (!this.mAirplaneMode) {
      if (!bool1)
        bool = true; 
      int i = 0;
      while (true) {
        if (i < this.mPhoneCount) {
          setRadioPower(bool, i);
          i++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  protected String readIccIdUsingPhoneId(int paramInt) {
    String str = SystemProperties.get(PROPERTY_ICCID_SIM[paramInt]);
    log("ICCID for phone " + paramInt + " is " + str);
    return str;
  }
  
  protected void refreshIccIdPreference(boolean paramBoolean, String paramString) {
    log("refresh iccid preference");
    SharedPreferences.Editor editor = sIccidPreference.edit();
    if (!paramBoolean && !"N/A".equals(paramString)) {
      putIccIdToPreference(editor, paramString);
    } else {
      removeIccIdFromPreference(editor, paramString);
    } 
    editor.commit();
  }
  
  protected void refreshSimSetting(boolean paramBoolean, int paramInt) {
    int i = Settings.System.getInt(this.mContext.getContentResolver(), "msim_mode_setting", this.mBitmapForPhoneCount);
    if (!paramBoolean) {
      paramInt = i & (1 << paramInt ^ 0xFFFFFFFF);
    } else {
      paramInt = i | 1 << paramInt;
    } 
    if (paramInt != i) {
      log("Refresh MSIM mode setting to " + paramInt + " from " + i);
      Settings.System.putInt(this.mContext.getContentResolver(), "msim_mode_setting", paramInt);
    } 
  }
  
  protected void resetSimInsertedStatus(int paramInt) {
    log("reset Sim InsertedStatus for Phone:" + paramInt);
    this.mSimInsertedStatus[paramInt] = -1;
  }
  
  public void setModemPower(boolean paramBoolean, int paramInt) {
    int i;
    log("Set Modem Power according to bitmap, Power:" + paramBoolean + ", PhoneBitMap:" + paramInt);
    TelephonyManager.MultiSimVariants multiSimVariants = TelephonyManager.getDefault().getMultiSimConfiguration();
    switch (multiSimVariants) {
      default:
        paramInt = PhoneFactory.getDefaultPhone().getPhoneId();
        log("Set Modem Power under SS mode:" + paramBoolean + ", phoneId:" + paramInt);
        this.mCi[paramInt].setModemPower(paramBoolean, null);
        return;
      case DSDS:
        paramInt = findMainCapabilityPhoneId();
        log("Set Modem Power under DSDS mode, Power:" + paramBoolean + ", phoneId:" + paramInt);
        this.mCi[paramInt].setModemPower(paramBoolean, null);
        if (!paramBoolean) {
          paramInt = 0;
          while (true) {
            if (paramInt < this.mPhoneCount) {
              resetSimInsertedStatus(paramInt);
              paramInt++;
              continue;
            } 
            return;
          } 
        } 
        return;
      case DSDA:
        i = 0;
        while (true) {
          if (i < this.mPhoneCount) {
            if ((1 << i & paramInt) != 0) {
              log("Set Modem Power under DSDA mode, Power:" + paramBoolean + ", phoneId:" + i);
              this.mCi[i].setModemPower(paramBoolean, null);
              if (!paramBoolean)
                resetSimInsertedStatus(i); 
            } 
            i++;
            continue;
          } 
          return;
        } 
      case TSTS:
        break;
    } 
    paramInt = findMainCapabilityPhoneId();
    log("Set Modem Power under TSTS mode, Power:" + paramBoolean + ", phoneId:" + paramInt);
    this.mCi[paramInt].setModemPower(paramBoolean, null);
    if (!paramBoolean) {
      paramInt = 0;
      while (true) {
        if (paramInt < this.mPhoneCount) {
          resetSimInsertedStatus(paramInt);
          paramInt++;
          continue;
        } 
        return;
      } 
    } 
  }
  
  protected void setPhoneRadioPower(boolean paramBoolean, int paramInt) {
    PhoneFactory.getPhone(paramInt).setRadioPower(paramBoolean);
  }
  
  public void setRadioPower(boolean paramBoolean, int paramInt) {
    log("setRadioPower, power=" + paramBoolean + "  phoneId=" + paramInt);
    if (isFlightModePowerOffModemEnabled() && this.mAirplaneMode == true) {
      log("Set Radio Power under airplane mode, ignore");
      return;
    } 
    if (!((ConnectivityManager)this.mContext.getSystemService("connectivity")).isNetworkSupported(0)) {
      log("wifi-only device, so return");
      return;
    } 
    if (isModemPowerOff(paramInt)) {
      log("modem for phone " + paramInt + " off, do not set radio again");
      return;
    } 
    if (!isIccIdReady(paramInt)) {
      log("RILD initialize not completed, wait for 200ms");
      postDelayed(new RadioPowerRunnable(paramBoolean, paramInt), 200L);
      return;
    } 
    setSimInsertedStatus(paramInt);
    String str = readIccIdUsingPhoneId(paramInt);
    if (sIccidPreference.contains(str)) {
      log("Adjust radio to off because once manually turned off, iccid: " + str + " , phone: " + paramInt);
      paramBoolean = false;
    } 
    boolean bool = paramBoolean;
    if (this.mWifiOnlyMode == true) {
      bool = paramBoolean;
      if (!this.mIsEccCall) {
        log("setradiopower but wifi only, turn off");
        bool = false;
      } 
    } 
    paramBoolean = checkForCTACase();
    if (getSimInsertedStatus(paramInt) == 0) {
      if (paramBoolean == true) {
        int i = findMainCapabilityPhoneId();
        log("No SIM inserted, force to turn on 3G/4G phone " + i + " radio if no any sim radio is enabled!");
        PhoneFactory.getPhone(i).setRadioPower(true);
        paramInt = 0;
        while (true) {
          if (paramInt < this.mPhoneCount) {
            if (paramInt != i && this.mIsEccCall != true)
              PhoneFactory.getPhone(paramInt).setRadioPower(false); 
            paramInt++;
            continue;
          } 
          return;
        } 
      } 
      if (true == this.mIsEccCall) {
        log("ECC call Radio Power, power: " + bool + ", phoneId: " + paramInt);
        PhoneFactory.getPhone(paramInt).setRadioPower(bool);
        return;
      } 
      log("No SIM inserted, turn Radio off!");
      PhoneFactory.getPhone(paramInt).setRadioPower(false);
      return;
    } 
    log("Trigger set Radio Power, power: " + bool + ", phoneId: " + paramInt);
    refreshSimSetting(bool, paramInt);
    PhoneFactory.getPhone(paramInt).setRadioPower(bool);
  }
  
  public void setSilentRebootPropertyForAllModem(String paramString) {
    TelephonyManager.MultiSimVariants multiSimVariants = TelephonyManager.getDefault().getMultiSimConfiguration();
    switch (multiSimVariants) {
      default:
        log("set eboot under SS");
        SystemProperties.set("gsm.ril.eboot", paramString);
        if (SystemProperties.get("ro.mtk_c2k_support").equals("1")) {
          log("C2K project, set cdma.ril.eboot to " + paramString);
          SystemProperties.set("cdma.ril.eboot", paramString);
        } 
        return;
      case DSDS:
        log("set eboot under DSDS");
        SystemProperties.set("gsm.ril.eboot", paramString);
        if (SystemProperties.get("ro.mtk_c2k_support").equals("1")) {
          log("C2K project, set cdma.ril.eboot to " + paramString);
          SystemProperties.set("cdma.ril.eboot", paramString);
        } 
        return;
      case DSDA:
        log("set eboot under DSDA");
        SystemProperties.set("gsm.ril.eboot", paramString);
        SystemProperties.set("gsm.ril.eboot.2", paramString);
        if (SystemProperties.get("ro.mtk_c2k_support").equals("1")) {
          log("C2K project, set cdma.ril.eboot to " + paramString);
          SystemProperties.set("cdma.ril.eboot", paramString);
        } 
        return;
      case TSTS:
        break;
    } 
    log("set eboot under TSTS");
    SystemProperties.set("gsm.ril.eboot", paramString);
    if (SystemProperties.get("ro.mtk_c2k_support").equals("1")) {
      log("C2K project, set cdma.ril.eboot to " + paramString);
      SystemProperties.set("cdma.ril.eboot", paramString);
    } 
  }
  
  protected void setSimInsertedStatus(int paramInt) {
    if ("N/A".equals(readIccIdUsingPhoneId(paramInt))) {
      this.mSimInsertedStatus[paramInt] = 0;
      return;
    } 
    this.mSimInsertedStatus[paramInt] = 1;
  }
  
  protected String simStatusToString(int paramInt) {
    switch (paramInt) {
      default:
        return null;
      case -1:
        return "SIM HAVE NOT INITIALIZED";
      case 1:
        return "SIM DETECTED";
      case 0:
        break;
    } 
    return "NO SIM DETECTED";
  }
  
  protected class ForceSetRadioPowerRunnable implements Runnable {
    int mRetryPhoneId;
    
    boolean mRetryPower;
    
    public ForceSetRadioPowerRunnable(boolean param1Boolean, int param1Int) {
      this.mRetryPower = param1Boolean;
      this.mRetryPhoneId = param1Int;
    }
    
    public void run() {
      RadioManager.this.forceSetRadioPower(this.mRetryPower, this.mRetryPhoneId);
    }
  }
  
  protected class MSimModeChangeRunnable implements Runnable {
    int mRetryMode;
    
    public MSimModeChangeRunnable(int param1Int) {
      this.mRetryMode = param1Int;
    }
    
    public void run() {
      RadioManager.this.notifyMSimModeChange(this.mRetryMode);
    }
  }
  
  protected class RadioPowerRunnable implements Runnable {
    int retryPhoneId;
    
    boolean retryPower;
    
    public RadioPowerRunnable(boolean param1Boolean, int param1Int) {
      this.retryPower = param1Boolean;
      this.retryPhoneId = param1Int;
    }
    
    public void run() {
      RadioManager.this.setRadioPower(this.retryPower, this.retryPhoneId);
    }
  }
  
  private class SimModeChangeRunnable implements Runnable {
    int mPhoneId;
    
    boolean mPower;
    
    public SimModeChangeRunnable(boolean param1Boolean, int param1Int) {
      this.mPower = param1Boolean;
      this.mPhoneId = param1Int;
    }
    
    public void run() {
      RadioManager.this.notifySimModeChange(this.mPower, this.mPhoneId);
    }
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/RadioManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */