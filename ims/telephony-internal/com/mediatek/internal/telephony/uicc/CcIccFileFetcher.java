package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import android.os.SystemProperties;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccUtils;
import java.util.ArrayList;

public final class CcIccFileFetcher extends IccFileFetcherBase {
  private static final String CSIM_PATH = "3F007FFF";
  
  private static final String KEY_ECC = "EF_ECC";
  
  private static final String[] RUIMRECORDS_PROPERTY_ECC_LIST = new String[] { "cdma.ril.ecclist", "cdma.ril.ecclist1", "cdma.ril.ecclist2", "cdma.ril.ecclist3" };
  
  private static final String RUIM_PATH = "3F007F25";
  
  private static final String TAG = "CcIccFileFetcher";
  
  private static final String[] UICC_SUPPORT_CARD_TYPE = new String[] { "gsm.ril.fulluicctype", "gsm.ril.fulluicctype.2", "gsm.ril.fulluicctype.3", "gsm.ril.fulluicctype.4" };
  
  private ArrayList<String> mFileList = new ArrayList<String>();
  
  public CcIccFileFetcher(Context paramContext, Phone paramPhone) {
    super(paramContext, paramPhone);
    this.mFileList.add("EF_ECC");
  }
  
  public IccFileRequest onGetFilePara(String paramString) {
    Rlog.d("CcIccFileFetcher", "onGetFilePara, phoneId:" + this.mPhoneId + ", key:" + paramString);
    if ("EF_ECC".equals(paramString) && this.mPhoneId >= 0 && this.mPhoneId < UICC_SUPPORT_CARD_TYPE.length) {
      paramString = "";
      String str = SystemProperties.get(UICC_SUPPORT_CARD_TYPE[this.mPhoneId], "");
      Rlog.d("CcIccFileFetcher", "onGetFilePara, full card type:" + str);
      if (str.indexOf("CSIM") != -1) {
        paramString = "3F007FFF";
      } else if (str.indexOf("RUIM") != -1) {
        paramString = "3F007F25";
      } 
      if (paramString.length() > 0)
        return new IccFileRequest(28487, 1, 2, paramString, null, -1, null); 
    } 
    return null;
  }
  
  public ArrayList<String> onGetKeys() {
    return this.mFileList;
  }
  
  public void onParseResult(String paramString, byte[] paramArrayOfbyte, ArrayList<byte[]> paramArrayList) {
    paramString = "";
    String str = paramString;
    if (paramArrayOfbyte != null) {
      int i = 0;
      while (true) {
        str = paramString;
        if (i + 2 < paramArrayOfbyte.length) {
          String str1 = IccUtils.cdmaBcdToString(paramArrayOfbyte, i, 3);
          str = paramString;
          if (str1 != null) {
            str = paramString;
            if (!str1.equals("")) {
              str = paramString;
              if (!paramString.equals(""))
                str = paramString + ","; 
            } 
          } 
          paramString = str + str1;
          i += 3;
          continue;
        } 
        break;
      } 
    } 
    Rlog.d("CcIccFileFetcher", "CDMA ECC of card " + this.mPhoneId + " :" + str);
    if (this.mPhoneId >= 0 && this.mPhoneId < RUIMRECORDS_PROPERTY_ECC_LIST.length)
      SystemProperties.set(RUIMRECORDS_PROPERTY_ECC_LIST[this.mPhoneId], str); 
    this.mData.put("EF_ECC", str);
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/CcIccFileFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */