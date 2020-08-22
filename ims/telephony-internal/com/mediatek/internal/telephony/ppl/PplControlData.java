package com.mediatek.internal.telephony.ppl;

import android.util.Log;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class PplControlData {
  private static final int HEADER_SIZE = 48;
  
  public static final int SALT_LIST_LENGTH = 40;
  
  public static final int SALT_SIZE = 20;
  
  public static final int SECRET_LIST_LENGTH = 40;
  
  public static final int SECRET_SIZE = 20;
  
  public static final int SIM_FINGERPRINT_LENGTH = 40;
  
  public static final byte STATUS_ENABLED = 2;
  
  public static final byte STATUS_LOCKED = 4;
  
  public static final byte STATUS_PROVISIONED = 1;
  
  public static final byte STATUS_SIM_LOCKED = 8;
  
  public static final byte STATUS_WIPE_REQUESTED = 16;
  
  private static final String TAG = "PPL/ControlData";
  
  public static final int TRUSTED_NUMBER_LENGTH = 40;
  
  public static final byte VERSION = 1;
  
  private static Comparator<byte[]> mSIMComparator = new Comparator<byte[]>() {
      public int compare(byte[] param1ArrayOfbyte1, byte[] param1ArrayOfbyte2) {
        return PplControlData.compareSIMFingerprints(param1ArrayOfbyte1, param1ArrayOfbyte2);
      }
    };
  
  public List<PplMessageManager.PendingMessage> PendingMessageList = null;
  
  public List<byte[]> SIMFingerprintList = null;
  
  public List<String> TrustedNumberList = null;
  
  public byte[] salt = new byte[20];
  
  public byte[] secret = new byte[20];
  
  public byte status = 0;
  
  public byte version = 1;
  
  public static PplControlData buildControlData(byte[] paramArrayOfbyte) {
    PplControlData pplControlData = new PplControlData();
    if (paramArrayOfbyte != null && paramArrayOfbyte.length != 0) {
      pplControlData.decode(paramArrayOfbyte);
      return pplControlData;
    } 
    Log.w("PPL/ControlData", "buildControlData: data is empty, return empty instance");
    return pplControlData;
  }
  
  public static int compareSIMFingerprints(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2) {
    if (paramArrayOfbyte1.length != paramArrayOfbyte2.length)
      throw new Error("The two fingerprints must have the same length"); 
    for (int i = 0; i < paramArrayOfbyte1.length; i++) {
      int j = paramArrayOfbyte1[i] - paramArrayOfbyte2[i];
      if (j != 0)
        return j; 
    } 
    return 0;
  }
  
  private int getDataSize() {
    int j = 48;
    if (this.SIMFingerprintList != null)
      j = 48 + this.SIMFingerprintList.size() * 40; 
    int i = j;
    if (this.TrustedNumberList != null)
      i = j + this.TrustedNumberList.size() * 40; 
    j = i;
    if (this.PendingMessageList != null)
      j = i + this.PendingMessageList.size() * 49; 
    return j;
  }
  
  public static byte[][] sortSIMFingerprints(byte[][] paramArrayOfbyte) {
    paramArrayOfbyte = (byte[][])paramArrayOfbyte.clone();
    for (int i = 0; i < paramArrayOfbyte.length; i++)
      paramArrayOfbyte[i] = (byte[])paramArrayOfbyte[i].clone(); 
    Arrays.sort(paramArrayOfbyte, (Comparator)mSIMComparator);
    return paramArrayOfbyte;
  }
  
  public void clear() {
    this.version = 1;
    this.status = 0;
    this.secret = new byte[20];
    this.salt = new byte[20];
    this.SIMFingerprintList = null;
    this.TrustedNumberList = null;
    this.PendingMessageList = null;
  }
  
  public PplControlData clone() {
    PplControlData pplControlData = new PplControlData();
    pplControlData.version = this.version;
    pplControlData.status = this.status;
    pplControlData.secret = (byte[])this.secret.clone();
    pplControlData.salt = (byte[])this.salt.clone();
    if (this.SIMFingerprintList != null) {
      pplControlData.SIMFingerprintList = (List)new LinkedList<byte>();
      for (int i = 0; i < this.SIMFingerprintList.size(); i++)
        pplControlData.SIMFingerprintList.add(((byte[])this.SIMFingerprintList.get(i)).clone()); 
    } else {
      pplControlData.SIMFingerprintList = null;
    } 
    if (this.TrustedNumberList != null) {
      pplControlData.TrustedNumberList = new LinkedList<String>();
      for (String str : this.TrustedNumberList)
        pplControlData.TrustedNumberList.add(str); 
    } else {
      pplControlData.TrustedNumberList = null;
    } 
    if (this.PendingMessageList != null) {
      pplControlData.PendingMessageList = new LinkedList<PplMessageManager.PendingMessage>();
      for (PplMessageManager.PendingMessage pendingMessage : this.PendingMessageList)
        pplControlData.PendingMessageList.add(pendingMessage.clone()); 
    } else {
      this.PendingMessageList = null;
    } 
    return pplControlData;
  }
  
  public void decode(byte[] paramArrayOfbyte) {
    int j;
    this.version = paramArrayOfbyte[0];
    this.status = paramArrayOfbyte[1];
    byte b3 = paramArrayOfbyte[2];
    byte b2 = paramArrayOfbyte[3];
    byte b1 = paramArrayOfbyte[4];
    System.arraycopy(paramArrayOfbyte, 8, this.secret, 0, this.secret.length);
    int i = 8 + this.secret.length;
    System.arraycopy(paramArrayOfbyte, i, this.salt, 0, this.salt.length);
    i += this.salt.length;
    if (b3 != 0) {
      this.SIMFingerprintList = (List)new LinkedList<byte>();
      int k = 0;
      j = i;
      while (true) {
        i = j;
        if (k < b3) {
          byte[] arrayOfByte = new byte[40];
          System.arraycopy(paramArrayOfbyte, j, arrayOfByte, 0, 40);
          this.SIMFingerprintList.add(arrayOfByte);
          j += 40;
          k++;
          continue;
        } 
        break;
      } 
    } else {
      this.SIMFingerprintList = null;
    } 
    if (b2 != 0) {
      this.TrustedNumberList = new LinkedList<String>();
      int k = 0;
      label32: while (true) {
        j = i;
        if (k < b2)
          for (j = i;; j++) {
            if (j >= i + 40 || paramArrayOfbyte[j] == 0) {
              this.TrustedNumberList.add(new String(paramArrayOfbyte, i, j - i));
              i += 40;
              k++;
              continue label32;
            } 
          }  
        break;
      } 
    } else {
      this.TrustedNumberList = null;
      j = i;
    } 
    if (b1 != 0) {
      this.PendingMessageList = new LinkedList<PplMessageManager.PendingMessage>();
      for (i = 0; i < b1; i++) {
        this.PendingMessageList.add(new PplMessageManager.PendingMessage(paramArrayOfbyte, j));
        j += 49;
      } 
    } else {
      this.PendingMessageList = null;
    } 
  }
  
  public byte[] encode() {
    byte b;
    byte[] arrayOfByte = new byte[getDataSize()];
    arrayOfByte[0] = this.version;
    arrayOfByte[1] = this.status;
    if (this.SIMFingerprintList == null) {
      b = 0;
    } else {
      b = (byte)this.SIMFingerprintList.size();
    } 
    arrayOfByte[2] = b;
    if (this.TrustedNumberList == null) {
      b = 0;
    } else {
      b = (byte)this.TrustedNumberList.size();
    } 
    arrayOfByte[3] = b;
    if (this.PendingMessageList == null) {
      b = 0;
    } else {
      b = (byte)this.PendingMessageList.size();
    } 
    arrayOfByte[4] = b;
    arrayOfByte[5] = 0;
    arrayOfByte[6] = 0;
    arrayOfByte[7] = 0;
    System.arraycopy(this.secret, 0, arrayOfByte, 8, this.secret.length);
    int i = 8 + this.secret.length;
    System.arraycopy(this.salt, 0, arrayOfByte, i, this.salt.length);
    int j = i + this.salt.length;
    i = j;
    if (this.SIMFingerprintList != null) {
      int k = 0;
      while (true) {
        i = j;
        if (k < this.SIMFingerprintList.size()) {
          System.arraycopy(this.SIMFingerprintList.get(k), 0, arrayOfByte, j, 40);
          j += 40;
          k++;
          continue;
        } 
        break;
      } 
    } 
    j = i;
    if (this.TrustedNumberList != null) {
      int k = 0;
      while (true) {
        j = i;
        if (k < this.TrustedNumberList.size()) {
          byte[] arrayOfByte1 = ((String)this.TrustedNumberList.get(k)).getBytes();
          if (arrayOfByte1.length > 40)
            throw new Error("Trusted number is too long"); 
          System.arraycopy(Arrays.copyOf(arrayOfByte1, 40), 0, arrayOfByte, i, 40);
          i += 40;
          k++;
          continue;
        } 
        break;
      } 
    } 
    if (this.PendingMessageList != null)
      for (i = 0; i < this.PendingMessageList.size(); i++) {
        ((PplMessageManager.PendingMessage)this.PendingMessageList.get(i)).encode(arrayOfByte, j);
        j += 49;
      }  
    return arrayOfByte;
  }
  
  public boolean hasWipeFlag() {
    return ((this.status & 0x10) == 16);
  }
  
  public boolean isEnabled() {
    return ((this.status & 0x2) == 2);
  }
  
  public boolean isLocked() {
    return ((this.status & 0x4) == 4);
  }
  
  public boolean isProvisioned() {
    return ((this.status & 0x1) == 1);
  }
  
  public boolean isSIMLocked() {
    return ((this.status & 0x8) == 8);
  }
  
  public void setEnable(boolean paramBoolean) {
    if (paramBoolean) {
      this.status = (byte)(this.status | 0x2);
      return;
    } 
    this.status = (byte)(this.status & 0xFFFFFFFD);
  }
  
  public void setLock(boolean paramBoolean) {
    if (paramBoolean) {
      this.status = (byte)(this.status | 0x4);
      return;
    } 
    this.status = (byte)(this.status & 0xFFFFFFFB);
  }
  
  public void setProvision(boolean paramBoolean) {
    if (paramBoolean) {
      this.status = (byte)(this.status | 0x1);
      return;
    } 
    this.status = (byte)(this.status & 0xFFFFFFFE);
  }
  
  public void setSIMLock(boolean paramBoolean) {
    if (paramBoolean) {
      this.status = (byte)(this.status | 0x8);
      return;
    } 
    this.status = (byte)(this.status & 0xFFFFFFF7);
  }
  
  public void setWipeFlag(boolean paramBoolean) {
    if (paramBoolean) {
      this.status = (byte)(this.status | 0x10);
      return;
    } 
    this.status = (byte)(this.status & 0xFFFFFFEF);
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("PplControlData ");
    stringBuilder.append(hashCode()).append(" {").append(Integer.toHexString(this.version)).append(", ").append(Integer.toHexString(this.status)).append(", ").append(this.SIMFingerprintList).append(", ").append(this.TrustedNumberList).append(", ").append(this.PendingMessageList).append("}");
    return stringBuilder.toString();
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/ppl/PplControlData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */