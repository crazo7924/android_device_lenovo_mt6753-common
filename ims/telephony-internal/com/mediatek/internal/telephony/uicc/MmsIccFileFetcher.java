package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import com.android.internal.telephony.Phone;
import com.mediatek.internal.telephony.MmsConfigInfo;
import com.mediatek.internal.telephony.MmsIcpInfo;
import java.util.ArrayList;

public final class MmsIccFileFetcher extends IccFileFetcherBase {
  private static final String MMS_CONFIG_INFO = "ef_mms_config_info";
  
  private static final int MMS_ICP_AI_TAG = 133;
  
  private static final int MMS_ICP_AM_TAG = 132;
  
  private static final int MMS_ICP_CP_TAG = 171;
  
  private static final int MMS_ICP_G_TAG = 131;
  
  private static final int MMS_ICP_ICBI_TAG = 130;
  
  private static final String MMS_ICP_INFO = "ef_mms_icp_info";
  
  private static final int MMS_ICP_INVALID_TAG = 255;
  
  private static final int MMS_ICP_I_TAG = 128;
  
  private static final int MMS_ICP_RS_TAG = 129;
  
  private static final String TAG = "MmsIccFileFetcher";
  
  ArrayList<String> mFileList = new ArrayList<String>();
  
  public MmsIccFileFetcher(Context paramContext, Phone paramPhone) {
    super(paramContext, paramPhone);
    this.mFileList.add("ef_mms_icp_info");
    this.mFileList.add("ef_mms_config_info");
  }
  
  void decodeGateWay(MmsIcpInfo paramMmsIcpInfo, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramMmsIcpInfo == null)
      return; 
    int i;
    for (i = paramInt1;; i++) {
      if (i >= paramInt1 + paramInt2 || (paramArrayOfbyte[i] & 0xFF) == 0) {
        String str = new String(paramArrayOfbyte, paramInt1, i - paramInt1);
        log("parseMmsIcpInfo decodeGateWay gateWay = " + str.trim());
        paramMmsIcpInfo.mDomainName = str.trim();
        return;
      } 
    } 
  }
  
  void decodeMmsImplementation(MmsIcpInfo paramMmsIcpInfo, byte[] paramArrayOfbyte, int paramInt1, int paramInt2) {
    if (paramMmsIcpInfo == null)
      return; 
    paramInt1 = paramArrayOfbyte[paramInt1] & 0xFF;
    if ((paramInt1 & 0x1) == 1) {
      paramMmsIcpInfo.mImplementation = "WAP";
    } else if ((paramInt1 & 0x2) == 2) {
      paramMmsIcpInfo.mImplementation = "M-IMAP";
    } else if ((paramInt1 & 0x4) == 4) {
      paramMmsIcpInfo.mImplementation = "SIP";
    } else {
      paramMmsIcpInfo.mImplementation = "UNKNOWN";
    } 
    log("parseMmsIcpInfo decodeMmsImplementation imp = " + paramMmsIcpInfo.mImplementation);
  }
  
  String dumpBytes(byte[] paramArrayOfbyte) {
    String str = "";
    for (int i = 0; i < paramArrayOfbyte.length; i++) {
      str = str + Integer.toHexString((paramArrayOfbyte[i] & 0xF0) >> 4);
      str = str + Integer.toHexString(paramArrayOfbyte[i] & 0xF);
    } 
    return str;
  }
  
  protected MmsConfigInfo getMmsConfigInfo() {
    return this.mData.containsKey("ef_mms_config_info") ? (MmsConfigInfo)this.mData.get("ef_mms_config_info") : null;
  }
  
  protected MmsIcpInfo getMmsIcpInfo() {
    return this.mData.containsKey("ef_mms_icp_info") ? (MmsIcpInfo)this.mData.get("ef_mms_icp_info") : null;
  }
  
  boolean isValideIcpInfo(MmsIcpInfo paramMmsIcpInfo) {
    if (paramMmsIcpInfo.mImplementation == null || paramMmsIcpInfo.mImplementation.isEmpty()) {
      log("parseMmsIcpInfo isValide = false");
      return false;
    } 
    log("parseMmsIcpInfo isValide = true");
    return true;
  }
  
  public IccFileRequest onGetFilePara(String paramString) {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual hashCode : ()I
    //   4: lookupswitch default -> 32, -720915327 -> 72, 937715585 -> 58
    //   32: iconst_m1
    //   33: istore_2
    //   34: iload_2
    //   35: tableswitch default -> 56, 0 -> 86, 1 -> 104
    //   56: aconst_null
    //   57: areturn
    //   58: aload_1
    //   59: ldc 'ef_mms_icp_info'
    //   61: invokevirtual equals : (Ljava/lang/Object;)Z
    //   64: ifeq -> 32
    //   67: iconst_0
    //   68: istore_2
    //   69: goto -> 34
    //   72: aload_1
    //   73: ldc 'ef_mms_config_info'
    //   75: invokevirtual equals : (Ljava/lang/Object;)Z
    //   78: ifeq -> 32
    //   81: iconst_1
    //   82: istore_2
    //   83: goto -> 34
    //   86: new com/mediatek/internal/telephony/uicc/IccFileRequest
    //   89: dup
    //   90: sipush #28519
    //   93: iconst_1
    //   94: iconst_0
    //   95: ldc '3F007F25'
    //   97: aconst_null
    //   98: iconst_m1
    //   99: aconst_null
    //   100: invokespecial <init> : (IIILjava/lang/String;[BILjava/lang/String;)V
    //   103: areturn
    //   104: new com/mediatek/internal/telephony/uicc/IccFileRequest
    //   107: dup
    //   108: sipush #28542
    //   111: iconst_1
    //   112: iconst_0
    //   113: ldc '3F007F25'
    //   115: aconst_null
    //   116: iconst_m1
    //   117: aconst_null
    //   118: invokespecial <init> : (IIILjava/lang/String;[BILjava/lang/String;)V
    //   121: areturn
  }
  
  public ArrayList<String> onGetKeys() {
    return this.mFileList;
  }
  
  public void onParseResult(String paramString, byte[] paramArrayOfbyte, ArrayList<byte[]> paramArrayList) {
    log("KEY = " + paramString + " transparent = " + paramArrayOfbyte + " linearfixed = " + paramArrayList);
    byte b = -1;
    switch (paramString.hashCode()) {
      default:
        switch (b) {
          default:
            loge("unknown key type.");
            return;
          case 0:
            this.mData.put("ef_mms_icp_info", parseMmsIcpInfo(paramArrayOfbyte));
            return;
          case 1:
            break;
        } 
        break;
      case 937715585:
        if (paramString.equals("ef_mms_icp_info"))
          b = 0; 
      case -720915327:
        if (paramString.equals("ef_mms_config_info"))
          b = 1; 
    } 
    this.mData.put("ef_mms_config_info", parseMmsConfigInfo(paramArrayOfbyte));
  }
  
  MmsConfigInfo parseMmsConfigInfo(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null || paramArrayOfbyte.length < 8)
      return null; 
    log("parseMmsConfigInfo data = " + dumpBytes(paramArrayOfbyte));
    MmsConfigInfo mmsConfigInfo = new MmsConfigInfo();
    mmsConfigInfo.mMessageMaxSize = (paramArrayOfbyte[0] & 0xFF) << 24 | (paramArrayOfbyte[1] & 0xFF) << 16 | (paramArrayOfbyte[2] & 0xFF) << 8 | paramArrayOfbyte[3] & 0xFF;
    mmsConfigInfo.mRetryTimes = paramArrayOfbyte[4] & 0xFF;
    mmsConfigInfo.mRetryInterval = (paramArrayOfbyte[5] & 0xFF);
    mmsConfigInfo.mCenterTimeout = (paramArrayOfbyte[6] & 0xFF) << 8 | paramArrayOfbyte[7] & 0xFF;
    log("parseMmsConfigInfo: mMessageMaxSize = " + mmsConfigInfo.mMessageMaxSize + " mRetryTimes = " + mmsConfigInfo.mRetryTimes + " mRetryInterval = " + mmsConfigInfo.mRetryInterval + " mCenterTimeout = " + mmsConfigInfo.mCenterTimeout);
    return mmsConfigInfo;
  }
  
  MmsIcpInfo parseMmsIcpInfo(byte[] paramArrayOfbyte) {
    if (paramArrayOfbyte == null)
      return null; 
    log("parseMmsIcpInfo data = " + dumpBytes(paramArrayOfbyte));
    int i = 0;
    MmsIcpInfo mmsIcpInfo = new MmsIcpInfo();
    while (true) {
      if (i < paramArrayOfbyte.length) {
        int j = paramArrayOfbyte[i] & 0xFF;
        int k = paramArrayOfbyte[i + 1] & 0xFF;
        log("parseMmsIcpInfo tagParam = " + Integer.toHexString(j));
        log("parseMmsIcpInfo, the Tag's value len = " + k);
        if (j != 255) {
          switch (j) {
            default:
              log("unkonwn tag.");
              continue;
            case 171:
              i += 2;
              continue;
            case 128:
              decodeMmsImplementation(mmsIcpInfo, paramArrayOfbyte, i + 2, 1);
              i += 3;
              continue;
            case 129:
              str = new String(paramArrayOfbyte, i + 2, k);
              mmsIcpInfo.mRelayOrServerAddress = str;
              i = i + 2 + k;
              log("parseMmsIcpInfo, MMS_ICP_RS_TAG value = " + str);
              continue;
            case 130:
              str = new String(paramArrayOfbyte, i + 2, k);
              i = i + 2 + k;
              log("parseMmsIcpInfo, MMS_ICP_ICBI_TAG value = " + str);
              continue;
            case 131:
              decodeGateWay(mmsIcpInfo, paramArrayOfbyte, i + 2, k);
              i = i + 2 + k;
              continue;
            case 132:
              str = new String(paramArrayOfbyte, i + 2, k);
              i = i + 2 + k;
              log("parseMmsIcpInfo, MMS_ICP_AM_TAG value = " + str);
              continue;
            case 133:
              break;
          } 
          String str = new String(paramArrayOfbyte, i + 2, k);
          i = i + 2 + k;
          log("parseMmsIcpInfo, MMS_ICP_AI_TAG value = " + str);
          continue;
        } 
      } 
      return isValideIcpInfo(mmsIcpInfo) ? mmsIcpInfo : null;
    } 
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/MmsIccFileFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */