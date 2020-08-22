package com.mediatek.internal.telephony.uicc;

import com.android.internal.telephony.uicc.IccServiceTable;

public final class IsimServiceTable extends IccServiceTable {
  public IsimServiceTable(byte[] paramArrayOfbyte) {
    super(paramArrayOfbyte);
  }
  
  protected String getTag() {
    return "IsimServiceTable";
  }
  
  protected Object[] getValues() {
    return (Object[])IsimService.values();
  }
  
  public boolean isAvailable(IsimService paramIsimService) {
    return isAvailable(paramIsimService.ordinal());
  }
  
  public enum IsimService {
    COMMUNICATION_CONTROL_BY_ISIM, GBA, GBA_LOCAL_KEY_ESTABLISHMENT, HTTP_DIGEST, PCSCF_ADDRESS, PCSCF_DISCOVERY, SMS, SMSR, SM_OVER_IP, UICC_ACCESS_IMS;
    
    static {
      GBA_LOCAL_KEY_ESTABLISHMENT = new IsimService("GBA_LOCAL_KEY_ESTABLISHMENT", 3);
      PCSCF_DISCOVERY = new IsimService("PCSCF_DISCOVERY", 4);
      SMS = new IsimService("SMS", 5);
      SMSR = new IsimService("SMSR", 6);
      SM_OVER_IP = new IsimService("SM_OVER_IP", 7);
      COMMUNICATION_CONTROL_BY_ISIM = new IsimService("COMMUNICATION_CONTROL_BY_ISIM", 8);
      UICC_ACCESS_IMS = new IsimService("UICC_ACCESS_IMS", 9);
      $VALUES = new IsimService[] { PCSCF_ADDRESS, GBA, HTTP_DIGEST, GBA_LOCAL_KEY_ESTABLISHMENT, PCSCF_DISCOVERY, SMS, SMSR, SM_OVER_IP, COMMUNICATION_CONTROL_BY_ISIM, UICC_ACCESS_IMS };
    }
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/IsimServiceTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */