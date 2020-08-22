package com.mediatek.internal.telephony.worldphone;

public interface IWorldPhone {
  public static final String ACTION_ADB_SWITCH_MODEM = "android.intent.action.ACTION_ADB_SWITCH_MODEM";
  
  public static final String ACTION_SAP_CONNECTION_STATE_CHANGED = "android.bluetooth.sap.profile.action.CONNECTION_STATE_CHANGED";
  
  public static final String ACTION_SHUTDOWN_IPO = "android.intent.action.ACTION_SHUTDOWN_IPO";
  
  public static final String ACTION_TEST_WORLDPHONE = "android.intent.action.ACTION_TEST_WORLDPHOE";
  
  public static final int AUTO_SWITCH_OFF = -98;
  
  public static final int CAMP_ON_DENY_REASON_DOMESTIC_FDD_MD = 4;
  
  public static final int CAMP_ON_DENY_REASON_NEED_SWITCH_TO_FDD = 2;
  
  public static final int CAMP_ON_DENY_REASON_NEED_SWITCH_TO_TDD = 3;
  
  public static final int CAMP_ON_DENY_REASON_UNKNOWN = 1;
  
  public static final int CAMP_ON_NOT_DENIED = 0;
  
  public static final int CAUSE_TYPE_OOS = 1;
  
  public static final int CAUSE_TYPE_OTHERS = 255;
  
  public static final int CAUSE_TYPE_PLMN_CHANGE = 0;
  
  public static final int DEFAULT_MAJOR_SIM = 0;
  
  public static final int EVENT_INVALID_SIM_NOTIFY_1 = 60;
  
  public static final int EVENT_INVALID_SIM_NOTIFY_2 = 61;
  
  public static final int EVENT_QUERY_MODEM_TYPE = 50;
  
  public static final int EVENT_RADIO_ON_1 = 0;
  
  public static final int EVENT_RADIO_ON_2 = 1;
  
  public static final int EVENT_REG_PLMN_CHANGED_1 = 10;
  
  public static final int EVENT_REG_PLMN_CHANGED_2 = 11;
  
  public static final int EVENT_REG_SUSPENDED_1 = 30;
  
  public static final int EVENT_REG_SUSPENDED_2 = 31;
  
  public static final int EVENT_RESUME_CAMPING = 70;
  
  public static final int EVENT_STORE_MODEM_TYPE = 40;
  
  public static final String EXTRA_FAKE_REGION = "EXTRA_FAKE_REGION";
  
  public static final String EXTRA_FAKE_USER_TYPE = "FAKE_USER_TYPE";
  
  public static final int ICC_CARD_TYPE_SIM = 1;
  
  public static final int ICC_CARD_TYPE_UNKNOWN = 0;
  
  public static final int ICC_CARD_TYPE_USIM = 2;
  
  public static final String LOG_TAG = "PHONE";
  
  public static final int MAJOR_CAPABILITY_OFF = -1;
  
  public static final int MAJOR_SIM_UNKNOWN = -99;
  
  public static final String NO_OP = "OM";
  
  public static final int POLICY_OM = 0;
  
  public static final int POLICY_OP01 = 1;
  
  public static final String PROPERTY_SWITCH_MODEM_CAUSE_TYPE = "ril.switch.modem.cause.type";
  
  public static final int REGION_DOMESTIC = 1;
  
  public static final int REGION_FOREIGN = 2;
  
  public static final int REGION_UNKNOWN = 0;
  
  public static final int SELECTION_MODE_AUTO = 1;
  
  public static final int SELECTION_MODE_MANUAL = 0;
  
  public static final int TYPE1_USER = 1;
  
  public static final int TYPE2_USER = 2;
  
  public static final int TYPE3_USER = 3;
  
  public static final int UNKNOWN_USER = 0;
  
  void notifyRadioCapabilityChange(int paramInt);
  
  void setModemSelectionMode(int paramInt1, int paramInt2);
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/worldphone/IWorldPhone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */