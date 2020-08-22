package com.mediatek.internal.telephony.uicc;

import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.uicc.AdnRecord;
import com.android.internal.telephony.uicc.IccFileHandler;
import java.util.ArrayList;

public class CsimPhbStorageInfo extends Handler {
  static final String LOG_TAG = "CsimphbStorageInfo";
  
  static int[] sAdnRecordSize = new int[] { -1, -1, -1, -1 };
  
  static IccFileHandler sFh;
  
  static int sMaxNameLength = 0;
  
  static int sMaxnumberLength = 20;
  
  public CsimPhbStorageInfo() {
    Rlog.d("CsimphbStorageInfo", " CsimphbStorageInfo constructor finished. ");
  }
  
  public static void checkPhbRecordInfo(Message paramMessage) {
    sAdnRecordSize[2] = 20;
    sAdnRecordSize[3] = sMaxNameLength;
    Rlog.d("CsimphbStorageInfo", " [getPhbRecordInfo] sAdnRecordSize[0] = " + sAdnRecordSize[0] + " sAdnRecordSize[1] = " + sAdnRecordSize[1] + " sAdnRecordSize[2] = " + sAdnRecordSize[2] + " sAdnRecordSize[3] = " + sAdnRecordSize[3]);
    if (paramMessage != null) {
      (AsyncResult.forMessage(paramMessage)).result = sAdnRecordSize;
      paramMessage.sendToTarget();
    } 
  }
  
  public static void checkPhbStorage(ArrayList<AdnRecord> paramArrayList) {
    int i;
    int m;
    int[] arrayOfInt = getPhbRecordStorageInfo();
    int j = arrayOfInt[0];
    int k = arrayOfInt[1];
    if (paramArrayList != null) {
      m = paramArrayList.size();
      i = 0;
      int n = 0;
      while (n < m) {
        int i1 = i;
        if (!((AdnRecord)paramArrayList.get(n)).isEmpty()) {
          i1 = i + 1;
          Rlog.d("CsimphbStorageInfo", " print userRecord: " + paramArrayList.get(n));
        } 
        n++;
        i = i1;
      } 
      Rlog.d("CsimphbStorageInfo", " checkPhbStorage totalSize = " + m + " usedRecord = " + i);
      Rlog.d("CsimphbStorageInfo", " checkPhbStorage totalStorage = " + k + " usedStorage = " + j);
      if (k > -1) {
        setPhbRecordStorageInfo(k + m, i + j);
        return;
      } 
    } else {
      return;
    } 
    setPhbRecordStorageInfo(m, i);
  }
  
  public static void clearAdnRecordSize() {
    Rlog.d("CsimphbStorageInfo", " clearAdnRecordSize");
    if (sAdnRecordSize != null)
      for (int i = 0; i < sAdnRecordSize.length; i++)
        sAdnRecordSize[i] = -1;  
  }
  
  public static int[] getPhbRecordStorageInfo() {
    return sAdnRecordSize;
  }
  
  public static void setMaxNameLength(int paramInt) {
    sMaxNameLength = paramInt;
    Rlog.d("CsimphbStorageInfo", " [setMaxNameLength] sMaxNameLength = " + sMaxNameLength);
  }
  
  public static void setPhbRecordStorageInfo(int paramInt1, int paramInt2) {
    sAdnRecordSize[0] = paramInt2;
    sAdnRecordSize[1] = paramInt1;
    Rlog.d("CsimphbStorageInfo", " [setPhbRecordStorageInfo] usedRecord = " + paramInt2 + " | totalSize =" + paramInt1);
  }
  
  public static boolean updatePhbStorageInfo(int paramInt) {
    int[] arrayOfInt = getPhbRecordStorageInfo();
    int i = arrayOfInt[0];
    int j = arrayOfInt[1];
    Rlog.d("CsimphbStorageInfo", " [updatePhbStorageInfo] used " + i + " | total : " + j + " | update : " + paramInt);
    if (i > -1) {
      setPhbRecordStorageInfo(j, i + paramInt);
      return true;
    } 
    Rlog.d("CsimphbStorageInfo", " the storage info is not ready return false");
    return false;
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/CsimPhbStorageInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */