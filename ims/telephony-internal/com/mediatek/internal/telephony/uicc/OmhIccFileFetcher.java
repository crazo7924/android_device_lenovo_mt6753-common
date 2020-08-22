package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.SystemProperties;
import com.android.internal.telephony.Phone;
import java.util.ArrayList;

public final class OmhIccFileFetcher extends IccFileFetcherBase {
  private static final String OMH_CARD_NO = "0";
  
  private static final int OMH_CARD_QUERY = 1001;
  
  private static final int OMH_CARD_RETRY_COUNT = 5;
  
  private static final int OMH_CARD_RETRY_INTERVAL = 1000;
  
  private static final String OMH_CARD_UNKNOWN = "-1";
  
  private static final String OMH_CARD_YES = "1";
  
  private static final String OMH_INFO_READY = "omh_info_ready";
  
  private static final String TAG = "OmhIccFileFetcher";
  
  ArrayList<String> mFileList = new ArrayList<String>();
  
  private int mRetryTimes = 0;
  
  public OmhIccFileFetcher(Context paramContext, Phone paramPhone) {
    super(paramContext, paramPhone);
    this.mFileList.add("omh_info_ready");
  }
  
  private void notifyOmhCardDone(boolean paramBoolean) {
    String str;
    log("notifyOmhCardDone, check omh card is done with state = " + paramBoolean);
    Intent intent = new Intent("com.mediatek.internal.omh.cardcheck");
    intent.putExtra("subid", this.mPhone.getSubId());
    if (paramBoolean) {
      str = "yes";
    } else {
      str = "no";
    } 
    intent.putExtra("is_ready", str);
    this.mContext.sendBroadcast(intent);
  }
  
  protected void exchangeSimInfo() {
    log("exchangeSimInfo, just check the property.");
    String str = SystemProperties.get("ril.cdma.card.omh", "-1");
    log("exchangeSimInfo, ril.cdma.card.omh = " + str);
    if ("-1".equals(str)) {
      retryCheckOmhCard();
      this.mRetryTimes = 0;
      return;
    } 
    this.mData.put("omh_info_ready", str);
    notifyOmhCardDone(true);
  }
  
  public void handleMessage(Message paramMessage) {
    switch (paramMessage.what) {
      default:
        super.handleMessage(paramMessage);
        return;
      case 1001:
        break;
    } 
    retryCheckOmhCard();
  }
  
  protected boolean isOmhCard() {
    if (this.mData.containsKey("omh_info_ready"))
      return "1".equals(this.mData.get("omh_info_ready")); 
    String str = SystemProperties.get("ril.cdma.card.omh", "-1");
    if (!"-1".equals(str)) {
      log("isOmhCard(), omh info maybe not ready, but the card check is done!!!!!!");
      return "1".equals(str);
    } 
    return false;
  }
  
  public IccFileRequest onGetFilePara(String paramString) {
    return null;
  }
  
  public ArrayList<String> onGetKeys() {
    return this.mFileList;
  }
  
  public void onParseResult(String paramString, byte[] paramArrayOfbyte, ArrayList<byte[]> paramArrayList) {}
  
  void retryCheckOmhCard() {
    String str = SystemProperties.get("ril.cdma.card.omh", "-1");
    log("retryCheckOmhCard with omh = " + str + " mRetryTimes = " + this.mRetryTimes);
    if ("-1".equals(str) && this.mRetryTimes < 5) {
      this.mRetryTimes++;
      sendEmptyMessageDelayed(1001, 1000L);
      log("retryCheckOmhCard, retry again.");
      return;
    } 
    if ("-1".equals(str)) {
      this.mData.put("omh_info_ready", "0");
    } else {
      this.mData.put("omh_info_ready", str);
    } 
    notifyOmhCardDone(true);
    log("retryCheckOmhCard, notify app the check is ready.");
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/OmhIccFileFetcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */