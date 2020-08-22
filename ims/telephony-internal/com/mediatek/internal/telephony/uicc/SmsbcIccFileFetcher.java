package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public final class SmsbcIccFileFetcher extends IccFileFetcherBase {
  private static final String BCSMSCFG = "ef_bcsmscfg";
  
  private static final String BCSMSP = "ef_bcsmsp";
  
  private static final String BCSMSTABLE = "ef_bcsmstable";
  
  private static final String TAG = "SmsbcIccFileFetcher";
  
  ArrayList<String> mFileList = new ArrayList<String>();
  
  public SmsbcIccFileFetcher(Context paramContext, Phone paramPhone) {
    super(paramContext, paramPhone);
    this.mFileList.add("ef_bcsmscfg");
    this.mFileList.add("ef_bcsmstable");
    this.mFileList.add("ef_bcsmsp");
  }
  
  public int getBcsmsCfgFromRuim(int paramInt1, int paramInt2) {
    int j = 2;
    byte[] arrayOfByte = (byte[])this.mData.get("ef_bcsmscfg");
    if (arrayOfByte == null || arrayOfByte.length < 1)
      return -1; 
    int i = arrayOfByte[0];
    Rlog.d("SmsbcIccFileFetcher", "getBcsmsCfgFromRuim config = " + i);
    if (i != -1)
      j = i & 0x3; 
    if (j != 1)
      return j; 
    HashMap hashMap1 = (HashMap)this.mData.get("ef_bcsmstable");
    HashMap hashMap2 = (HashMap)this.mData.get("ef_bcsmsp");
    if (hashMap1 == null || hashMap2 == null)
      return -1; 
    Iterator<String> iterator = hashMap1.keySet().iterator();
    while (true) {
      i = j;
      if (iterator.hasNext()) {
        String str = iterator.next();
        byte[] arrayOfByte1 = (byte[])hashMap1.get(str);
        byte[] arrayOfByte2 = (byte[])hashMap2.get(str);
        if (arrayOfByte1 != null && arrayOfByte2 != null) {
          i = arrayOfByte1[0] & 0x1;
          int k = arrayOfByte2[0] & 0x1;
          Rlog.d("SmsbcIccFileFetcher", "getBcsmsCfgFromRuim status=" + i + " select=" + k);
          if (i == 1 && k == 1) {
            i = (arrayOfByte1[1] << 8) + arrayOfByte1[2];
            Rlog.d("SmsbcIccFileFetcher", "getBcsmsCfgFromRuim serviceCategory=" + i);
            Rlog.d("SmsbcIccFileFetcher", "userServiceCategory=" + paramInt1);
            if (i == paramInt1) {
              paramInt1 = arrayOfByte2[1] & 0x3;
              Rlog.d("SmsbcIccFileFetcher", "getBcsmsCfgFromRuim priority=" + paramInt1);
              if (paramInt2 >= paramInt1) {
                i = 2;
                break;
              } 
              i = 0;
              break;
            } 
          } 
        } 
        continue;
      } 
      break;
    } 
    paramInt1 = i;
    if (i == 1)
      paramInt1 = -1; 
    return paramInt1;
  }
  
  public IccFileRequest onGetFilePara(String paramString) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual hashCode : ()I
    //   4: lookupswitch default -> 40, 479328714 -> 70, 822844246 -> 98, 1088943028 -> 84
    //   40: iconst_m1
    //   41: istore_2
    //   42: iload_2
    //   43: tableswitch default -> 68, 0 -> 112, 1 -> 130, 2 -> 148
    //   68: aconst_null
    //   69: areturn
    //   70: aload_1
    //   71: ldc 'ef_bcsmscfg'
    //   73: invokevirtual equals : (Ljava/lang/Object;)Z
    //   76: ifeq -> 40
    //   79: iconst_0
    //   80: istore_2
    //   81: goto -> 42
    //   84: aload_1
    //   85: ldc 'ef_bcsmstable'
    //   87: invokevirtual equals : (Ljava/lang/Object;)Z
    //   90: ifeq -> 40
    //   93: iconst_1
    //   94: istore_2
    //   95: goto -> 42
    //   98: aload_1
    //   99: ldc 'ef_bcsmsp'
    //   101: invokevirtual equals : (Ljava/lang/Object;)Z
    //   104: ifeq -> 40
    //   107: iconst_2
    //   108: istore_2
    //   109: goto -> 42
    //   112: new com/mediatek/internal/telephony/uicc/IccFileRequest
    //   115: dup
    //   116: sipush #28507
    //   119: iconst_1
    //   120: iconst_2
    //   121: ldc '3F007F25'
    //   123: aconst_null
    //   124: iconst_m1
    //   125: aconst_null
    //   126: invokespecial <init> : (IIILjava/lang/String;[BILjava/lang/String;)V
    //   129: areturn
    //   130: new com/mediatek/internal/telephony/uicc/IccFileRequest
    //   133: dup
    //   134: sipush #28509
    //   137: iconst_0
    //   138: iconst_2
    //   139: ldc '3F007F25'
    //   141: aconst_null
    //   142: iconst_m1
    //   143: aconst_null
    //   144: invokespecial <init> : (IIILjava/lang/String;[BILjava/lang/String;)V
    //   147: areturn
    //   148: new com/mediatek/internal/telephony/uicc/IccFileRequest
    //   151: dup
    //   152: sipush #28510
    //   155: iconst_0
    //   156: iconst_2
    //   157: ldc '3F007F25'
    //   159: aconst_null
    //   160: iconst_m1
    //   161: aconst_null
    //   162: invokespecial <init> : (IIILjava/lang/String;[BILjava/lang/String;)V
    //   165: areturn
  }
  
  public ArrayList<String> onGetKeys() {
    return this.mFileList;
  }
  
  public void onParseResult(String paramString, byte[] paramArrayOfbyte, ArrayList<byte[]> paramArrayList) {
    HashMap<Object, Object> hashMap;
    if ("ef_bcsmscfg".equals(paramString)) {
      Rlog.d("SmsbcIccFileFetcher", "BCSMSCFG = " + Arrays.toString(paramArrayOfbyte));
      this.mData.put("ef_bcsmscfg", paramArrayOfbyte);
      return;
    } 
    if ("ef_bcsmstable".equals(paramString)) {
      Rlog.d("SmsbcIccFileFetcher", "BCSMSTABLE = " + paramArrayList);
      if (paramArrayList != null && paramArrayList.size() > 0) {
        hashMap = new HashMap<Object, Object>();
        int i;
        for (i = 0; i < paramArrayList.size(); i++) {
          paramArrayOfbyte = paramArrayList.get(i);
          if (paramArrayOfbyte != null && paramArrayOfbyte.length > 0) {
            Rlog.d("SmsbcIccFileFetcher", "BCSMSTABLE = " + Arrays.toString(paramArrayOfbyte));
            if ((paramArrayOfbyte[0] & 0x1) == 1)
              hashMap.put(String.valueOf(i), paramArrayOfbyte); 
          } 
        } 
        this.mData.put("ef_bcsmstable", hashMap);
        return;
      } 
      return;
    } 
    if ("ef_bcsmsp".equals(hashMap)) {
      Rlog.d("SmsbcIccFileFetcher", "BCSMSP = " + paramArrayList);
      if (paramArrayList != null && paramArrayList.size() > 0) {
        hashMap = new HashMap<Object, Object>();
        int i;
        for (i = 0; i < paramArrayList.size(); i++) {
          paramArrayOfbyte = paramArrayList.get(i);
          if (paramArrayOfbyte != null && paramArrayOfbyte.length > 0) {
            Rlog.d("SmsbcIccFileFetcher", "BCSMSP = " + Arrays.toString(paramArrayOfbyte));
            if ((paramArrayOfbyte[0] & 0x1) == 1)
              hashMap.put(String.valueOf(i), paramArrayOfbyte); 
          } 
        } 
        this.mData.put("ef_bcsmsp", hashMap);
        return;
      } 
    } 
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/SmsbcIccFileFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */