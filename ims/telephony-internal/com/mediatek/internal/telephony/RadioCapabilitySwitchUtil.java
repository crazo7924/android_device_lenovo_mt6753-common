package com.mediatek.internal.telephony;

import android.os.SystemProperties;
import android.telephony.RadioAccessFamily;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.Phone;
import java.util.ArrayList;
import java.util.Arrays;

public class RadioCapabilitySwitchUtil {
  public static final String CN_MCC = "460";
  
  public static final int DO_SWITCH = 0;
  
  public static final int ICCID_ERROR = 3;
  
  public static final String IMSI_NOT_READY = "0";
  
  public static final int IMSI_NOT_READY_OR_SIM_LOCKED = 2;
  
  public static final String IMSI_READY = "1";
  
  private static final String LOG_TAG = "GSM";
  
  public static final String MAIN_SIM_PROP = "persist.radio.simswitch.iccid";
  
  public static final int NOT_SHOW_DIALOG = 1;
  
  public static final int NOT_SWITCH = 1;
  
  public static final int NOT_SWITCH_SIM_INFO_NOT_READY = 2;
  
  private static final String NO_SIM_VALUE = "N/A";
  
  public static final int OP01_6M_PRIORITY_OP01_SIM = 1;
  
  public static final int OP01_6M_PRIORITY_OP01_USIM = 0;
  
  public static final int OP01_6M_PRIORITY_OTHER = 2;
  
  private static final String[] PLMN_TABLE_OP01 = new String[] { 
      "46000", "46002", "46007", "46008", "45412", "45413", "00101", "00211", "00321", "00431", 
      "00541", "00651", "00761", "00871", "00902", "01012", "01122", "01232", "46004", "46602", 
      "50270" };
  
  private static final String[] PLMN_TABLE_OP02 = new String[] { "46001", "46006", "46009", "45407" };
  
  private static final String[] PLMN_TABLE_OP09 = new String[] { "46005", "45502", "46003", "46011" };
  
  private static final String[] PLMN_TABLE_OP18 = new String[] { 
      "405840", "405854", "405855", "405856", "405857", "405858", "405855", "405856", "405857", "405858", 
      "405859", "405860", "405861", "405862", "405863", "405864", "405865", "405866", "405867", "405868", 
      "405869", "405870", "405871", "405872", "405873", "405874" };
  
  private static final String PROPERTY_ICCID = "ril.iccid.sim";
  
  private static final String[] PROPERTY_SIM_ICCID = new String[] { "ril.iccid.sim1", "ril.iccid.sim2", "ril.iccid.sim3", "ril.iccid.sim4" };
  
  private static final String[] PROPERTY_SIM_IMSI_STATUS = new String[] { "ril.imsi.status.sim1", "ril.imsi.status.sim2", "ril.imsi.status.sim3", "ril.imsi.status.sim4" };
  
  public static final int SHOW_DIALOG = 0;
  
  public static final int SIM_OP_INFO_OP01 = 2;
  
  public static final int SIM_OP_INFO_OP02 = 3;
  
  public static final int SIM_OP_INFO_OP09 = 4;
  
  public static final int SIM_OP_INFO_OP18 = 5;
  
  public static final int SIM_OP_INFO_OVERSEA = 1;
  
  public static final int SIM_OP_INFO_UNKNOWN = 0;
  
  public static final int SIM_SWITCH_MODE_DUAL_TALK = 3;
  
  public static final int SIM_SWITCH_MODE_DUAL_TALK_SWAP = 4;
  
  public static final int SIM_SWITCH_MODE_SINGLE_TALK_MDSYS = 1;
  
  public static final int SIM_SWITCH_MODE_SINGLE_TALK_MDSYS_LITE = 2;
  
  public static final int SIM_TYPE_OTHER = 2;
  
  public static final int SIM_TYPE_SIM = 0;
  
  public static final int SIM_TYPE_USIM = 1;
  
  private static boolean checkOp01(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    int m = Integer.valueOf(SystemProperties.get("persist.radio.simswitch", "1")).intValue() - 1;
    int k = 0;
    int i = 0;
    int n = paramArrayOfint1.length;
    String[] arrayOfString = new String[n];
    int j = 0;
    while (j < n) {
      arrayOfString[j] = SystemProperties.get("ril.iccid.sim" + (j + 1));
      int i2 = k;
      int i1 = i;
      if (!"N/A".equals(arrayOfString[j])) {
        i2 = k + 1;
        i1 = i | 1 << j;
      } 
      j++;
      k = i2;
      i = i1;
    } 
    logd("checkOp01 : curPhoneId: " + m + ", insertedSimCount: " + k);
    if (k == 1) {
      logd("checkOp01 : single SIM case, switch!");
      return true;
    } 
    if (isOp01LCProject() && paramArrayOfint2[paramInt] == 2 && paramArrayOfint2[m] != 2) {
      logd("checkOp01 : case L+C; stay in current phone");
      return false;
    } 
    if (paramArrayOfint1[paramInt] == 2) {
      if (paramArrayOfint2[paramInt] == 0) {
        if (paramArrayOfint1[m] == 2 && paramArrayOfint2[m] != 0) {
          logd("checkOp01 : case 1,2; stay in current phone");
          return false;
        } 
        logd("checkOp01 : case 3,4");
        return true;
      } 
      logd("checkOp01 : case 1,2");
      return true;
    } 
    if (paramArrayOfint1[paramInt] == 1) {
      if (paramArrayOfint1[m] == 2) {
        logd("checkOp01 : case 1,2,3,4; stay in current phone");
        return false;
      } 
      if (paramArrayOfint2[paramInt] == 0) {
        if (paramArrayOfint1[m] == 1 && paramArrayOfint2[m] != 0) {
          logd("checkOp01 : case 5,6; stay in current phone");
          return false;
        } 
        logd("checkOp01 : case 7,8");
        return true;
      } 
      logd("checkOp01 : case 5,6");
      return true;
    } 
    if (k == 2 && paramArrayOfint2[m] == 2 && paramArrayOfint2[paramInt] == 2) {
      logd("checkOp01 : case C+C, switch!");
      return true;
    } 
    if (paramArrayOfint1[paramInt] == 0) {
      logd("checkOp01 : case 10, target IMSI not ready");
      if (i <= 2) {
        logd("checkOp01 : case 10, single SIM case, switch!");
        return true;
      } 
    } 
    if (SystemProperties.get("ro.mtk_world_phone_policy").equals("1") && paramArrayOfint1[m] != 2 && paramArrayOfint1[m] != 1) {
      logd("checkOp01 : case 11, op01-B, switch it!");
      return true;
    } 
    logd("checkOp01 : case 9");
    return false;
  }
  
  private static boolean checkOp01LC(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    int m = Integer.valueOf(SystemProperties.get("persist.radio.simswitch", "1")).intValue() - 1;
    int k = 0;
    int i = 0;
    int n = paramArrayOfint1.length;
    String[] arrayOfString = new String[n];
    int[] arrayOfInt = new int[n];
    int j = 0;
    while (j < n) {
      arrayOfString[j] = SystemProperties.get("ril.iccid.sim" + (j + 1));
      int i2 = k;
      int i1 = i;
      if (!"N/A".equals(arrayOfString[j])) {
        i2 = k + 1;
        i1 = i | 1 << j;
      } 
      if (paramArrayOfint1[j] == 2) {
        if (paramArrayOfint2[j] == 1) {
          arrayOfInt[j] = 0;
        } else if (paramArrayOfint2[j] == 0) {
          arrayOfInt[j] = 1;
        } 
      } else {
        arrayOfInt[j] = 2;
      } 
      j++;
      k = i2;
      i = i1;
    } 
    logd("checkOp01LC(curPhoneId): " + m);
    logd("checkOp01LC(insertedSimCount): " + k);
    if (k == 1) {
      logd("checkOp01LC : single SIM case, switch!");
      return true;
    } 
    if (arrayOfInt[paramInt] <= arrayOfInt[m]) {
      logd("checkOp01LC : target priority greater than or equal to current, switch!");
      return true;
    } 
    logd("checkOp01LC : target priority lower than current; stay in current phone");
    return false;
  }
  
  private static boolean checkOp18(int paramInt, int[] paramArrayOfint1, int[] paramArrayOfint2) {
    int i = Integer.valueOf(SystemProperties.get("persist.radio.simswitch", "1")).intValue() - 1;
    logd("checkOp18 : curPhoneId: " + i);
    if (paramArrayOfint1[paramInt] == 5) {
      logd("checkOp18 : case 1");
      return true;
    } 
    if (paramArrayOfint1[i] == 5) {
      logd("checkOp18 : case 2; stay in current phone");
      return false;
    } 
    logd("checkOp18 : case 3; all are not op18");
    return true;
  }
  
  public static void clearAllRilMccMnc(int paramInt) {
    logd("clearAllRilMccMnc");
    for (int i = 0; i < paramInt; i++)
      clearRilMccMnc(i); 
  }
  
  public static void clearAllSimImsiStatus() {
    logd("clearAllSimImsiStatus");
    for (int i = 0; i < PROPERTY_SIM_IMSI_STATUS.length; i++)
      updateSimImsiStatus(i, "0"); 
  }
  
  public static void clearRilMccMnc(int paramInt) {
    String str;
    if (paramInt == 0) {
      str = "gsm.sim.ril.mcc.mnc";
    } else {
      str = "gsm.sim.ril.mcc.mnc." + (paramInt + 1);
    } 
    SystemProperties.set(str, "");
    logd("clear property " + str);
  }
  
  public static int getHigherPrioritySimForOp01(int paramInt, boolean[] paramArrayOfboolean1, boolean[] paramArrayOfboolean2, boolean[] paramArrayOfboolean3, boolean[] paramArrayOfboolean4) {
    int j = -1;
    int k = paramArrayOfboolean1.length;
    if (paramArrayOfboolean1[paramInt] == true)
      return paramInt; 
    int i;
    for (i = 0; i < k; i++) {
      if (paramArrayOfboolean1[i] == true)
        j = i; 
    } 
    if (j != -1 || paramArrayOfboolean2[paramInt] == true)
      return j; 
    for (i = 0; i < k; i++) {
      if (paramArrayOfboolean2[i] == true)
        j = i; 
    } 
    if (j != -1 || paramArrayOfboolean3[paramInt] == true)
      return j; 
    for (i = 0; i < k; i++) {
      if (paramArrayOfboolean3[i] == true)
        j = i; 
    } 
    if (j != -1 || paramArrayOfboolean4[paramInt] == true)
      return j; 
    for (paramInt = 0; paramInt < k; paramInt++) {
      if (paramArrayOfboolean4[paramInt] == true)
        j = paramInt; 
    } 
    return j;
  }
  
  public static int getHighestPriorityPhone(int paramInt, int[] paramArrayOfint) {
    boolean bool = false;
    int m = paramArrayOfint.length;
    int k = 0;
    int j = 0;
    int i = 0;
    while (i < m) {
      int n;
      int i1;
      boolean bool1;
      if (paramArrayOfint[i] < paramArrayOfint[bool]) {
        bool1 = i;
        i1 = 1;
        n = 1 << i;
      } else {
        n = j;
        i1 = k;
        bool1 = bool;
        if (paramArrayOfint[i] == paramArrayOfint[bool]) {
          i1 = k + 1;
          n = j | 1 << i;
          bool1 = bool;
        } 
      } 
      i++;
      j = n;
      k = i1;
      bool = bool1;
    } 
    return (k == 1) ? bool : ((paramInt == -1) ? -1 : (((1 << paramInt & j) != 0) ? paramInt : -1));
  }
  
  public static int getMainCapabilityPhoneId() {
    int i = 0;
    if (SystemProperties.getBoolean("ro.mtk_dt_support", false) == true) {
      int j = SystemProperties.getInt("persist.ril.simswitch.swapmode", 3);
      if (j == 3) {
        i = 0;
        Log.d("GSM", "[RadioCapSwitchUtil] getMainCapabilityPhoneId " + i);
        return i;
      } 
      if (j == 4)
        i = 1; 
      Log.d("GSM", "[RadioCapSwitchUtil] getMainCapabilityPhoneId " + i);
      return i;
    } 
    i = SystemProperties.getInt("persist.radio.simswitch", 1) - 1;
    Log.d("GSM", "[RadioCapSwitchUtil] getMainCapabilityPhoneId " + i);
    return i;
  }
  
  private static String getSimImsiStatus(int paramInt) {
    return SystemProperties.get(PROPERTY_SIM_IMSI_STATUS[paramInt], "0");
  }
  
  public static boolean getSimInfo(int[] paramArrayOfint1, int[] paramArrayOfint2, int paramInt) {
    String[] arrayOfString1 = new String[paramArrayOfint1.length];
    String[] arrayOfString2 = new String[paramArrayOfint1.length];
    int i = 0;
    label100: while (i < paramArrayOfint1.length) {
      String str;
      if (i == 0) {
        str = "gsm.ril.uicctype";
      } else {
        str = "gsm.ril.uicctype." + (i + 1);
      } 
      arrayOfString2[i] = SystemProperties.get(str, "");
      if (arrayOfString2[i].equals("SIM")) {
        paramArrayOfint2[i] = 0;
      } else if (arrayOfString2[i].equals("USIM")) {
        paramArrayOfint2[i] = 1;
      } else {
        paramArrayOfint2[i] = 2;
      } 
      logd("SimType[" + i + "]= " + arrayOfString2[i] + ", simType[" + i + "]=" + paramArrayOfint2[i]);
      if (i == 0) {
        str = "gsm.sim.ril.mcc.mnc";
      } else {
        str = "gsm.sim.ril.mcc.mnc." + (i + 1);
      } 
      arrayOfString1[i] = SystemProperties.get(str, "");
      if (arrayOfString1[i].equals("") || arrayOfString1[i].equals("error") || arrayOfString1[i].equals("sim_absent")) {
        if ("1".equals(getSimImsiStatus(i))) {
          String str1 = str;
          if (isOp01LCProject()) {
            str1 = str;
            if (paramArrayOfint2[i] == 2) {
              str1 = "ril.uim.subscriberid." + (i + 1);
              arrayOfString1[i] = SystemProperties.get(str1, "");
            } 
          } 
          if (arrayOfString1[i].equals("") || arrayOfString1[i].equals("N/A") || arrayOfString1[i].equals("error") || arrayOfString1[i].equals("sim_absent")) {
            str1 = "gsm.sim.operator.imsi";
            arrayOfString1[i] = TelephonyManager.getTelephonyProperty(i, "gsm.sim.operator.imsi", "");
          } 
          if (arrayOfString1[i].length() >= 6) {
            arrayOfString1[i] = arrayOfString1[i].substring(0, 6);
          } else if (arrayOfString1[i].length() >= 5) {
            arrayOfString1[i] = arrayOfString1[i].substring(0, 5);
          } 
          logd("strMnc[" + i + "] from " + str1 + ":" + arrayOfString1[i]);
        } 
      } else {
        logd("strMnc[" + i + "] from ril.mcc.mnc:" + arrayOfString1[i]);
      } 
      logd("insertedStatus:" + paramInt);
      if (paramInt >= 0 && (1 << i & paramInt) > 0) {
        if (arrayOfString1[i].equals("")) {
          logd("SIM is inserted but no imsi");
          return false;
        } 
        if (arrayOfString1[i].equals("sim_lock")) {
          logd("SIM is lock, wait pin unlock");
          return false;
        } 
        if (arrayOfString1[i].equals("error") || arrayOfString1[i].equals("sim_absent")) {
          logd("SIM info is lost");
          return false;
        } 
      } 
      String[] arrayOfString = PLMN_TABLE_OP01;
      int k = arrayOfString.length;
      int j = 0;
      while (true) {
        if (j < k) {
          String str1 = arrayOfString[j];
          if (arrayOfString1[i].startsWith(str1)) {
            paramArrayOfint1[i] = 2;
          } else {
            j++;
            continue;
          } 
        } 
        if (paramArrayOfint1[i] == 0) {
          arrayOfString = PLMN_TABLE_OP02;
          k = arrayOfString.length;
          j = 0;
          while (true) {
            if (j < k) {
              String str1 = arrayOfString[j];
              if (arrayOfString1[i].startsWith(str1)) {
                paramArrayOfint1[i] = 3;
              } else {
                j++;
                continue;
              } 
            } 
            if (paramArrayOfint1[i] == 0) {
              arrayOfString = PLMN_TABLE_OP09;
              k = arrayOfString.length;
              j = 0;
              while (true) {
                if (j < k) {
                  String str1 = arrayOfString[j];
                  if (arrayOfString1[i].startsWith(str1)) {
                    paramArrayOfint1[i] = 4;
                  } else {
                    j++;
                    continue;
                  } 
                } 
                if (SystemProperties.get("ro.operator.optr", "").equals("OP18")) {
                  if (paramArrayOfint1[i] == 0) {
                    arrayOfString = PLMN_TABLE_OP18;
                    k = arrayOfString.length;
                    j = 0;
                    while (true) {
                      if (j < k) {
                        String str1 = arrayOfString[j];
                        if (arrayOfString1[i].startsWith(str1)) {
                          paramArrayOfint1[i] = 5;
                        } else {
                          j++;
                          continue;
                        } 
                      } 
                      if (paramArrayOfint1[i] == 0 && !arrayOfString1[i].equals(""))
                        paramArrayOfint1[i] = 1; 
                      logd("strMnc[" + i + "]= " + arrayOfString1[i] + ", simOpInfo[" + i + "]=" + paramArrayOfint1[i]);
                      i++;
                      continue label100;
                    } 
                    break;
                  } 
                  continue;
                } 
                continue;
              } 
              break;
            } 
            continue;
          } 
          break;
        } 
        continue;
      } 
    } 
    logd("getSimInfo(simOpInfo): " + Arrays.toString(paramArrayOfint1));
    logd("getSimInfo(simType): " + Arrays.toString(paramArrayOfint2));
    return true;
  }
  
  public static boolean isAnySimLocked(int paramInt) {
    if (SystemProperties.get("ro.mtk_svlte_support").equals("1") || SystemProperties.get("ro.mtk_srlte_support").equals("1")) {
      logd("isAnySimLocked always returns false in C2K");
      return false;
    } 
    String[] arrayOfString1 = new String[paramInt];
    String[] arrayOfString2 = new String[paramInt];
    int i = 0;
    while (true) {
      if (i < paramInt) {
        arrayOfString2[i] = SystemProperties.get(PROPERTY_SIM_ICCID[i]);
        if (!arrayOfString2[i].equals("N/A")) {
          arrayOfString1[i] = TelephonyManager.getTelephonyProperty(i, "gsm.sim.operator.imsi", "");
          if (arrayOfString1[i].length() >= 6) {
            arrayOfString1[i] = arrayOfString1[i].substring(0, 6);
          } else if (arrayOfString1[i].length() >= 5) {
            arrayOfString1[i] = arrayOfString1[i].substring(0, 5);
          } 
          if (arrayOfString1[i].equals("")) {
            String str;
            if (i == 0) {
              str = "gsm.sim.ril.mcc.mnc";
            } else {
              str = "gsm.sim.ril.mcc.mnc." + (i + 1);
            } 
            arrayOfString1[i] = SystemProperties.get(str, "");
            logd("mnc[" + i + "] from ril.mcc.mnc:" + arrayOfString1[i] + " ,iccid = " + arrayOfString2[i]);
          } else {
            logd("i = " + i + " from gsm.sim.operator.imsi:" + arrayOfString1[i] + " ,iccid = " + arrayOfString2[i]);
          } 
        } 
        if (!arrayOfString2[i].equals("N/A") && (arrayOfString1[i].equals("") || arrayOfString1[i].equals("sim_lock")))
          return true; 
        if ("1".equals(getSimImsiStatus(i))) {
          String str;
          logd("clear mcc.mnc:" + i);
          if (i == 0) {
            str = "gsm.sim.ril.mcc.mnc";
          } else {
            str = "gsm.sim.ril.mcc.mnc." + (i + 1);
          } 
          SystemProperties.set(str, "");
        } 
        i++;
        continue;
      } 
      return false;
    } 
  }
  
  public static int isNeedShowSimDialog() {
    if (SystemProperties.getBoolean("ro.mtk_disable_cap_switch", false) == true) {
      logd("mtk_disable_cap_switch is true");
      return 0;
    } 
    logd("isNeedShowSimDialog start");
    int m = TelephonyManager.getDefault().getPhoneCount();
    int[] arrayOfInt1 = new int[m];
    int[] arrayOfInt2 = new int[m];
    String[] arrayOfString = new String[m];
    int k = 0;
    int i = 0;
    boolean bool2 = false;
    boolean bool1 = false;
    ArrayList<Integer> arrayList1 = new ArrayList();
    ArrayList<Integer> arrayList2 = new ArrayList();
    ArrayList<Integer> arrayList3 = new ArrayList();
    ArrayList<Integer> arrayList4 = new ArrayList();
    int j = 0;
    while (j < m) {
      arrayOfString[j] = SystemProperties.get(PROPERTY_SIM_ICCID[j]);
      logd("currIccid[" + j + "] : " + arrayOfString[j]);
      if (arrayOfString[j] == null || "".equals(arrayOfString[j])) {
        Log.e("GSM", "iccid not found, wait for next sim state change");
        return 3;
      } 
      int i1 = k;
      int n = i;
      if (!"N/A".equals(arrayOfString[j])) {
        i1 = k + 1;
        n = i | 1 << j;
      } 
      j++;
      k = i1;
      i = n;
    } 
    if (k < 2) {
      logd("isNeedShowSimDialog: insert sim count < 2, do not show dialog");
      return 1;
    } 
    if (!getSimInfo(arrayOfInt1, arrayOfInt2, i)) {
      Log.e("GSM", "isNeedShowSimDialog: Can't get SIM information");
      return 2;
    } 
    for (i = 0; i < m; i++) {
      if (1 == arrayOfInt2[i]) {
        arrayList1.add(Integer.valueOf(i));
      } else if (arrayOfInt2[i] == 0) {
        arrayList2.add(Integer.valueOf(i));
      } 
      if (3 == arrayOfInt1[i]) {
        arrayList3.add(Integer.valueOf(i));
      } else {
        arrayList4.add(Integer.valueOf(i));
      } 
    } 
    logd("usimIndexList size = " + arrayList1.size());
    logd("op02IndexList size = " + arrayList3.size());
    if (arrayList1.size() >= 2) {
      i = 0;
      for (j = bool1; i < arrayList1.size(); j = n) {
        int n = j;
        if (arrayList3.contains(arrayList1.get(i)))
          n = j + 1; 
        i++;
      } 
      if (j == 1) {
        logd("isNeedShowSimDialog: One OP02Usim inserted, not show dialog");
        return 1;
      } 
    } else {
      if (arrayList1.size() == 1) {
        logd("isNeedShowSimDialog: One Usim inserted, not show dialog");
        return 1;
      } 
      i = 0;
      for (j = bool2; i < arrayList2.size(); j = n) {
        int n = j;
        if (arrayList3.contains(arrayList2.get(i)))
          n = j + 1; 
        i++;
      } 
      if (j == 1) {
        logd("isNeedShowSimDialog: One non-OP02 Usim inserted, not show dialog");
        return 1;
      } 
    } 
    logd("isNeedShowSimDialog: Show dialog");
    return 0;
  }
  
  public static int isNeedSwitchInOpPackage(Phone[] paramArrayOfPhone, RadioAccessFamily[] paramArrayOfRadioAccessFamily) {
    String str = SystemProperties.get("ro.operator.optr", "");
    int[] arrayOfInt2 = new int[paramArrayOfPhone.length];
    int[] arrayOfInt1 = new int[paramArrayOfPhone.length];
    String[] arrayOfString = new String[TelephonyManager.getDefault().getPhoneCount()];
    logd("Operator Spec:" + str);
    if (SystemProperties.getBoolean("ro.mtk_disable_cap_switch", false) == true) {
      logd("mtk_disable_cap_switch is true");
      return 1;
    } 
    return 0;
  }
  
  public static boolean isOp01LCProject() {
    if (SystemProperties.get("ro.mtk_c2k_support").equals("1") && SystemProperties.get("ro.operator.optr", "").equals("OP01")) {
      logd("return true for OP01 L+C project");
      return true;
    } 
    return false;
  }
  
  private static void logd(String paramString) {
    Log.d("GSM", "[RadioCapSwitchUtil] " + paramString);
  }
  
  public static void updateIccid(Phone[] paramArrayOfPhone) {
    for (int i = 0;; i++) {
      if (i < paramArrayOfPhone.length) {
        boolean bool = false;
        if (SystemProperties.getInt("ro.mtk_lte_support", 0) == 1) {
          if ((paramArrayOfPhone[i].getRadioAccessFamily() & 0x4000) == 16384)
            bool = true; 
        } else if ((paramArrayOfPhone[i].getRadioAccessFamily() & 0x8) == 8) {
          bool = true;
        } 
        if (bool) {
          String str = SystemProperties.get("ril.iccid.sim" + (i + 1));
          SystemProperties.set("persist.radio.simswitch.iccid", str);
          logd("updateIccid " + str);
          return;
        } 
      } else {
        return;
      } 
    } 
  }
  
  public static void updateSimImsiStatus(int paramInt, String paramString) {
    logd("updateSimImsiStatus slot = " + paramInt + ", value = " + paramString);
    SystemProperties.set(PROPERTY_SIM_IMSI_STATUS[paramInt], paramString);
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/RadioCapabilitySwitchUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */