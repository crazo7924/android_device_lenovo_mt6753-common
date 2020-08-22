package com.mediatek.internal.telephony;

public class EtwsUtils {
  public static final int ETWS_PDU_LENGTH = 56;
  
  public static int bytesToInt(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null || paramArrayOfbyte.length == 0 || paramArrayOfbyte.length > 4)
      throw new RuntimeException("valid byte array"); 
    int j = 0;
    int k = paramArrayOfbyte.length - 1;
    for (int i = 0; i < k; i++)
      j = (j | paramArrayOfbyte[i] & 0xFF) << 8; 
    return j | paramArrayOfbyte[k] & 0xFF;
  }
  
  public static byte[] intToBytes(int paramInt) {
    byte[] arrayOfByte = new byte[4];
    boolean bool = false;
    int i = paramInt;
    for (paramInt = bool; paramInt < 4; paramInt++) {
      arrayOfByte[3 - paramInt] = (byte)(i & 0xFF);
      i >>= 8;
    } 
    return arrayOfByte;
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/EtwsUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */