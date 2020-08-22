package com.mediatek.internal.telephony;

import com.android.internal.telephony.IccUtils;

public class EtwsNotification {
  public int messageId;
  
  public String plmnId;
  
  public String securityInfo;
  
  public int serialNumber;
  
  public int warningType;
  
  public byte[] getEtwsPdu() {
    byte[] arrayOfByte = new byte[56];
    System.arraycopy(EtwsUtils.intToBytes(this.serialNumber), 2, arrayOfByte, 0, 2);
    System.arraycopy(EtwsUtils.intToBytes(this.messageId), 2, arrayOfByte, 2, 2);
    System.arraycopy(EtwsUtils.intToBytes(this.warningType), 2, arrayOfByte, 4, 2);
    if (this.securityInfo != null)
      System.arraycopy(IccUtils.hexStringToBytes(this.securityInfo), 0, arrayOfByte, 6, 50); 
    return arrayOfByte;
  }
  
  public boolean isDuplicatedEtws(EtwsNotification paramEtwsNotification) {
    return (this.warningType == paramEtwsNotification.warningType && this.messageId == paramEtwsNotification.messageId && this.serialNumber == paramEtwsNotification.serialNumber && this.plmnId.equals(paramEtwsNotification.plmnId));
  }
  
  public String toString() {
    return "EtwsNotification: " + this.warningType + ", " + this.messageId + ", " + this.serialNumber + ", " + this.plmnId + ", " + this.securityInfo;
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/EtwsNotification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */