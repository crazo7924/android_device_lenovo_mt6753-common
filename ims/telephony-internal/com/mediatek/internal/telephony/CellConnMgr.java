package com.mediatek.internal.telephony;

import android.content.Context;
import android.content.res.Resources;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import com.android.internal.telephony.ITelephony;
import java.util.ArrayList;

public class CellConnMgr {
  private static final String INTENT_SET_RADIO_POWER = "com.mediatek.internal.telephony.RadioManager.intent.action.FORCE_SET_RADIO_POWER";
  
  public static final int STATE_FLIGHT_MODE = 1;
  
  public static final int STATE_RADIO_OFF = 2;
  
  public static final int STATE_READY = 0;
  
  public static final int STATE_ROAMING = 8;
  
  public static final int STATE_SIM_LOCKED = 4;
  
  private static final String TAG = "CellConnMgr";
  
  private Context mContext;
  
  public CellConnMgr(Context paramContext) {
    this.mContext = paramContext;
    if (this.mContext == null)
      throw new RuntimeException("CellConnMgr must be created by indicated context"); 
  }
  
  private boolean isRadioOffBySimManagement(int paramInt) {
    boolean bool = true;
    try {
      ITelephonyEx iTelephonyEx = ITelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx"));
      if (iTelephonyEx == null) {
        Rlog.d("CellConnMgr", "[isRadioOffBySimManagement] iTelEx is null");
        return false;
      } 
      boolean bool1 = iTelephonyEx.isRadioOffBySimManagement(paramInt);
      bool = bool1;
    } catch (RemoteException remoteException) {
      remoteException.printStackTrace();
    } 
    Rlog.d("CellConnMgr", "[isRadioOffBySimManagement]  subId " + paramInt + ", result = " + bool);
    return bool;
  }
  
  private boolean isRadioOn(int paramInt) {
    Rlog.d("CellConnMgr", "isRadioOff verify subId " + paramInt);
    boolean bool = true;
    try {
      ITelephony iTelephony = ITelephony.Stub.asInterface(ServiceManager.getService("phone"));
      if (iTelephony == null) {
        Rlog.d("CellConnMgr", "isRadioOff iTel is null");
        return false;
      } 
      boolean bool1 = iTelephony.isRadioOnForSubscriber(paramInt, this.mContext.getOpPackageName());
      bool = bool1;
    } catch (RemoteException remoteException) {
      remoteException.printStackTrace();
    } 
    Rlog.d("CellConnMgr", "isRadioOff subId " + paramInt + " radio on? " + bool);
    return bool;
  }
  
  public int getCurrentState(int paramInt1, int paramInt2) {
    boolean bool1;
    boolean bool2;
    int i = Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", -1);
    if (!isRadioOn(paramInt1) && isRadioOffBySimManagement(paramInt1)) {
      bool1 = true;
    } else {
      bool1 = false;
    } 
    int j = SubscriptionManager.getSlotId(paramInt1);
    TelephonyManager telephonyManager = TelephonyManager.getDefault();
    if (2 == telephonyManager.getSimState(j) || 3 == telephonyManager.getSimState(j) || 4 == telephonyManager.getSimState(j)) {
      bool2 = true;
    } else {
      bool2 = false;
    } 
    Rlog.d("CellConnMgr", "[getCurrentState]subId: " + paramInt1 + ", requestType:" + paramInt2 + "; (flight mode, radio off, locked, roaming) = (" + i + "," + bool1 + "," + bool2 + "," + Character.MIN_VALUE + ")");
    switch (paramInt2) {
      default:
        if (i == 1) {
          paramInt1 = 1;
        } else {
          paramInt1 = 0;
        } 
        if (bool1) {
          paramInt2 = 2;
        } else {
          paramInt2 = 0;
        } 
        if (bool2) {
          i = 4;
        } else {
          i = 0;
        } 
        if (false) {
          j = 8;
        } else {
          break;
        } 
        paramInt1 = paramInt1 | paramInt2 | i | j;
        Rlog.d("CellConnMgr", "[getCurrentState] state:" + paramInt1);
        return paramInt1;
      case 1:
        if (i == 1) {
          paramInt1 = 1;
          Rlog.d("CellConnMgr", "[getCurrentState] state:" + paramInt1);
          return paramInt1;
        } 
        paramInt1 = 0;
        Rlog.d("CellConnMgr", "[getCurrentState] state:" + paramInt1);
        return paramInt1;
      case 2:
        if (bool1) {
          paramInt1 = 2;
          Rlog.d("CellConnMgr", "[getCurrentState] state:" + paramInt1);
          return paramInt1;
        } 
        paramInt1 = 0;
        Rlog.d("CellConnMgr", "[getCurrentState] state:" + paramInt1);
        return paramInt1;
      case 4:
        if (i == 1) {
          paramInt1 = 1;
        } else {
          paramInt1 = 0;
        } 
        if (bool1) {
          paramInt2 = 2;
        } else {
          paramInt2 = 0;
        } 
        if (bool2) {
          i = 4;
        } else {
          i = 0;
        } 
        paramInt1 = paramInt1 | paramInt2 | i;
        Rlog.d("CellConnMgr", "[getCurrentState] state:" + paramInt1);
        return paramInt1;
      case 8:
        if (i == 1) {
          paramInt1 = 1;
        } else {
          paramInt1 = 0;
        } 
        if (bool1) {
          paramInt2 = 2;
        } else {
          paramInt2 = 0;
        } 
        if (false) {
          i = 8;
        } else {
          i = 0;
        } 
        paramInt1 = paramInt1 | paramInt2 | i;
        Rlog.d("CellConnMgr", "[getCurrentState] state:" + paramInt1);
        return paramInt1;
    } 
    j = 0;
    paramInt1 = paramInt1 | paramInt2 | i | j;
    Rlog.d("CellConnMgr", "[getCurrentState] state:" + paramInt1);
    return paramInt1;
  }
  
  public ArrayList<String> getStringUsingState(int paramInt1, int paramInt2) {
    ArrayList<String> arrayList = new ArrayList();
    Rlog.d("CellConnMgr", "[getStringUsingState] subId: " + paramInt1 + ", state:" + paramInt2);
    if ((paramInt2 & 0x3) == 3) {
      arrayList.add(Resources.getSystem().getString(134545546));
      arrayList.add(Resources.getSystem().getString(134545547));
      arrayList.add(Resources.getSystem().getString(134545563));
      arrayList.add(Resources.getSystem().getString(134545564));
      Rlog.d("CellConnMgr", "[getStringUsingState] STATE_FLIGHT_MODE + STATE_RADIO_OFF");
      Rlog.d("CellConnMgr", "[getStringUsingState]stringList size: " + arrayList.size());
      return (ArrayList<String>)arrayList.clone();
    } 
    if ((paramInt2 & 0x1) == 1) {
      arrayList.add(Resources.getSystem().getString(134545532));
      arrayList.add(Resources.getSystem().getString(134545533));
      arrayList.add(Resources.getSystem().getString(134545566));
      arrayList.add(Resources.getSystem().getString(134545564));
      Rlog.d("CellConnMgr", "[getStringUsingState] STATE_FLIGHT_MODE");
      Rlog.d("CellConnMgr", "[getStringUsingState]stringList size: " + arrayList.size());
      return (ArrayList<String>)arrayList.clone();
    } 
    if ((paramInt2 & 0x2) == 2) {
      arrayList.add(Resources.getSystem().getString(134545534));
      arrayList.add(Resources.getSystem().getString(134545535));
      arrayList.add(Resources.getSystem().getString(134545567));
      arrayList.add(Resources.getSystem().getString(134545564));
      Rlog.d("CellConnMgr", "[getStringUsingState] STATE_RADIO_OFF");
      Rlog.d("CellConnMgr", "[getStringUsingState]stringList size: " + arrayList.size());
      return (ArrayList<String>)arrayList.clone();
    } 
    if ((paramInt2 & 0x4) == 4) {
      arrayList.add(Resources.getSystem().getString(134545548));
      arrayList.add(Resources.getSystem().getString(134545549));
      arrayList.add(Resources.getSystem().getString(134545565));
      arrayList.add(Resources.getSystem().getString(134545564));
      Rlog.d("CellConnMgr", "[getStringUsingState] STATE_SIM_LOCKED");
    } 
    Rlog.d("CellConnMgr", "[getStringUsingState]stringList size: " + arrayList.size());
    return (ArrayList<String>)arrayList.clone();
  }
  
  public void handleRequest(int paramInt1, int paramInt2) {
    // Byte code:
    //   0: ldc 'CellConnMgr'
    //   2: new java/lang/StringBuilder
    //   5: dup
    //   6: invokespecial <init> : ()V
    //   9: ldc '[handleRequest] subId: '
    //   11: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   14: iload_1
    //   15: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   18: ldc ', state:'
    //   20: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   23: iload_2
    //   24: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   27: invokevirtual toString : ()Ljava/lang/String;
    //   30: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   33: pop
    //   34: iload_2
    //   35: iconst_1
    //   36: iand
    //   37: iconst_1
    //   38: if_icmpne -> 88
    //   41: aload_0
    //   42: getfield mContext : Landroid/content/Context;
    //   45: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   48: ldc 'airplane_mode_on'
    //   50: iconst_0
    //   51: invokestatic putInt : (Landroid/content/ContentResolver;Ljava/lang/String;I)Z
    //   54: pop
    //   55: aload_0
    //   56: getfield mContext : Landroid/content/Context;
    //   59: new android/content/Intent
    //   62: dup
    //   63: ldc 'android.intent.action.AIRPLANE_MODE'
    //   65: invokespecial <init> : (Ljava/lang/String;)V
    //   68: ldc 'state'
    //   70: iconst_0
    //   71: invokevirtual putExtra : (Ljava/lang/String;Z)Landroid/content/Intent;
    //   74: getstatic android/os/UserHandle.ALL : Landroid/os/UserHandle;
    //   77: invokevirtual sendBroadcastAsUser : (Landroid/content/Intent;Landroid/os/UserHandle;)V
    //   80: ldc 'CellConnMgr'
    //   82: ldc '[handleRequest] Turn off flight mode.'
    //   84: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   87: pop
    //   88: iload_2
    //   89: iconst_2
    //   90: iand
    //   91: iconst_2
    //   92: if_icmpne -> 240
    //   95: iconst_0
    //   96: istore #4
    //   98: iconst_0
    //   99: istore_3
    //   100: iload_3
    //   101: invokestatic getDefault : ()Landroid/telephony/TelephonyManager;
    //   104: invokevirtual getSimCount : ()I
    //   107: if_icmpge -> 163
    //   110: iload_3
    //   111: invokestatic getSubId : (I)[I
    //   114: astore #6
    //   116: aload #6
    //   118: ifnull -> 132
    //   121: aload_0
    //   122: aload #6
    //   124: iconst_0
    //   125: iaload
    //   126: invokespecial isRadioOn : (I)Z
    //   129: ifne -> 144
    //   132: iload #4
    //   134: istore #5
    //   136: iload_3
    //   137: iload_1
    //   138: invokestatic getSlotId : (I)I
    //   141: if_icmpne -> 152
    //   144: iload #4
    //   146: iconst_1
    //   147: iload_3
    //   148: ishl
    //   149: ior
    //   150: istore #5
    //   152: iload_3
    //   153: iconst_1
    //   154: iadd
    //   155: istore_3
    //   156: iload #5
    //   158: istore #4
    //   160: goto -> 100
    //   163: aload_0
    //   164: getfield mContext : Landroid/content/Context;
    //   167: invokevirtual getContentResolver : ()Landroid/content/ContentResolver;
    //   170: ldc_w 'msim_mode_setting'
    //   173: iload #4
    //   175: invokestatic putInt : (Landroid/content/ContentResolver;Ljava/lang/String;I)Z
    //   178: pop
    //   179: new android/content/Intent
    //   182: dup
    //   183: ldc 'com.mediatek.internal.telephony.RadioManager.intent.action.FORCE_SET_RADIO_POWER'
    //   185: invokespecial <init> : (Ljava/lang/String;)V
    //   188: astore #6
    //   190: aload #6
    //   192: ldc_w 'mode'
    //   195: iload #4
    //   197: invokevirtual putExtra : (Ljava/lang/String;I)Landroid/content/Intent;
    //   200: pop
    //   201: aload_0
    //   202: getfield mContext : Landroid/content/Context;
    //   205: aload #6
    //   207: getstatic android/os/UserHandle.ALL : Landroid/os/UserHandle;
    //   210: invokevirtual sendBroadcastAsUser : (Landroid/content/Intent;Landroid/os/UserHandle;)V
    //   213: ldc 'CellConnMgr'
    //   215: new java/lang/StringBuilder
    //   218: dup
    //   219: invokespecial <init> : ()V
    //   222: ldc_w '[handleRequest] Turn radio on, MSIM mode:'
    //   225: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   228: iload #4
    //   230: invokevirtual append : (I)Ljava/lang/StringBuilder;
    //   233: invokevirtual toString : ()Ljava/lang/String;
    //   236: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   239: pop
    //   240: iload_2
    //   241: iconst_1
    //   242: iand
    //   243: iconst_1
    //   244: if_icmpeq -> 285
    //   247: iload_2
    //   248: iconst_2
    //   249: iand
    //   250: iconst_2
    //   251: if_icmpeq -> 285
    //   254: iload_2
    //   255: iconst_4
    //   256: iand
    //   257: iconst_4
    //   258: if_icmpne -> 285
    //   261: ldc 'phoneEx'
    //   263: invokestatic getService : (Ljava/lang/String;)Landroid/os/IBinder;
    //   266: invokestatic asInterface : (Landroid/os/IBinder;)Lcom/mediatek/internal/telephony/ITelephonyEx;
    //   269: iload_1
    //   270: invokeinterface broadcastIccUnlockIntent : (I)Z
    //   275: pop
    //   276: ldc 'CellConnMgr'
    //   278: ldc_w '[handleRequest] broadcastIccUnlockIntent'
    //   281: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   284: pop
    //   285: return
    //   286: astore #6
    //   288: aload #6
    //   290: invokevirtual printStackTrace : ()V
    //   293: return
    //   294: astore #6
    //   296: ldc 'CellConnMgr'
    //   298: ldc_w 'ITelephonyEx is null'
    //   301: invokestatic d : (Ljava/lang/String;Ljava/lang/String;)I
    //   304: pop
    //   305: aload #6
    //   307: invokevirtual printStackTrace : ()V
    //   310: return
    // Exception table:
    //   from	to	target	type
    //   261	285	286	android/os/RemoteException
    //   261	285	294	java/lang/NullPointerException
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/CellConnMgr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */