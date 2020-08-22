package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public final class SsIccFileFetcher extends IccFileFetcherBase {
  public static final String[] FCNAME = new String[] { 
      "Number", "CD", "dCD", "CFB", "vCFB", "dCFB", "actCFB", "dactCFB", "CFD", "vCFD", 
      "dCFD", "actCFD", "dactCFD", "CFNA", "vCFNA", "dCFNA", "actCFNA", "dactCFNA", "CFU", "vCFU", 
      "dCFU", "actCFU", "dactCFU", "CW", "dCW", "CCW", "CNIR", "dCNIR", "CC", "dCC", 
      "DND", "dDND", "aMWN", "daMWN", "MWN", "dMWN", "CMWN", "PACA", "VMR", "CNAP", 
      "dCNAP", "CNAR", "dCNAR", "AC", "dAC", "AR", "dAR", "USCF", "RUAC", "dRUAC", 
      "AOC", "COT" };
  
  private static final String SSFC = "ef_ssfc";
  
  private static final String TAG = "SsIccFileFetcher";
  
  ArrayList<String> mFileList = new ArrayList<String>();
  
  public SsIccFileFetcher(Context paramContext, Phone paramPhone) {
    super(paramContext, paramPhone);
    this.mFileList.add("ef_ssfc");
  }
  
  private int getFcForApp(int paramInt, HashMap<String, Object> paramHashMap) {
    byte b = -1;
    paramHashMap = (HashMap<String, Object>)paramHashMap.get(FCNAME[paramInt]);
    paramInt = b;
    if (paramHashMap != null)
      paramInt = Integer.parseInt((String)paramHashMap); 
    return paramInt;
  }
  
  private static String getFcFromRuim(byte paramByte) {
    if ((paramByte & 0xFF) != 255) {
      if ((paramByte & 0xF0) == 240)
        return ((paramByte & 0xF) <= 9) ? ("" + (paramByte & 0xF)) : ""; 
      int i = 0;
      if ((paramByte & 0xF) <= 9)
        i = paramByte & 0xF; 
      if ((paramByte & 0xF0) <= 144) {
        int j = paramByte >> 4 & 0xF;
        return (j == 0) ? ("0" + i) : ("" + (i + j * 10));
      } 
    } 
    return "";
  }
  
  private static String getFcFromRuim(byte paramByte1, byte paramByte2) {
    String str1 = getFcFromRuim(paramByte1);
    String str2 = getFcFromRuim(paramByte2);
    return ("".equals(str1) && "".equals(str2)) ? "-1" : (str1 + str2);
  }
  
  public int[] getFcsForApp(int paramInt1, int paramInt2, int paramInt3) {
    int[] arrayOfInt2 = null;
    Rlog.d("SsIccFileFetcher", "getFcsForApp start=" + paramInt1 + ";end=" + paramInt2);
    int[] arrayOfInt1 = arrayOfInt2;
    if (this.mData != null) {
      if (this.mData.size() < 1)
        return arrayOfInt2; 
    } else {
      return arrayOfInt1;
    } 
    arrayOfInt1 = arrayOfInt2;
    if (paramInt2 >= paramInt1) {
      paramInt3 = paramInt2 - paramInt1 + 1;
      arrayOfInt2 = new int[paramInt3];
      paramInt2 = 0;
      while (true) {
        arrayOfInt1 = arrayOfInt2;
        if (paramInt2 < paramInt3) {
          arrayOfInt2[paramInt2] = getFcForApp(paramInt1 + paramInt2, this.mData);
          Rlog.d("SsIccFileFetcher", "getFcsForApp featureCodes=" + arrayOfInt2[paramInt2]);
          paramInt2++;
          continue;
        } 
        return arrayOfInt1;
      } 
    } 
    return arrayOfInt1;
  }
  
  public IccFileRequest onGetFilePara(String paramString) {
    byte b;
    switch (paramString.hashCode()) {
      default:
        b = -1;
        switch (b) {
          default:
            return null;
          case 0:
            break;
        } 
        return new IccFileRequest(28479, 1, 0, "3F007F25", null, -1, null);
      case -1839961093:
        break;
    } 
    if (paramString.equals("ef_ssfc")) {
      b = 0;
    } else {
    
    } 
    switch (b) {
      default:
        return null;
      case 0:
        break;
    } 
    return new IccFileRequest(28479, 1, 0, "3F007F25", null, -1, null);
  }
  
  public ArrayList<String> onGetKeys() {
    return this.mFileList;
  }
  
  public void onParseResult(String paramString, byte[] paramArrayOfbyte, ArrayList<byte[]> paramArrayList) {
    if ("ef_ssfc".equals(paramString)) {
      Rlog.d("SsIccFileFetcher", "featureCode = " + Arrays.toString(paramArrayOfbyte));
      if (paramArrayOfbyte != null && paramArrayOfbyte.length == 103) {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        String str = String.valueOf(paramArrayOfbyte[0]);
        if (!"-1".equals(str)) {
          hashMap.put(FCNAME[0], str);
          int i;
          for (i = 1; i < paramArrayOfbyte.length; i += 2) {
            str = getFcFromRuim(paramArrayOfbyte[i], paramArrayOfbyte[i + 1]);
            if (!"-1".equals(str)) {
              Rlog.d("SsIccFileFetcher", "onParseResult featureCode = " + str);
              hashMap.put(FCNAME[i / 2 + 1], str);
            } 
          } 
          this.mData = (HashMap)hashMap;
          return;
        } 
      } 
    } 
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/SsIccFileFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */