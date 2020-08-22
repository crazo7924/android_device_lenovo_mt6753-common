package com.mediatek.internal.telephony.uicc;

import android.content.Context;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import java.util.HashMap;

public class IccFileAdapter {
  private static final String TAG = "IccFileAdapter";
  
  private static IccFileAdapter sInstance;
  
  private CcIccFileFetcher mCcIccFileFetcher;
  
  private Context mContext;
  
  private MmsIccFileFetcher mMmsIccFileFetcher;
  
  private OmhIccFileFetcher mOmhIccFileFetcher;
  
  private Phone mPhone;
  
  private int mPhoneId;
  
  private SmsIccFileFetcher mSmsIccFileFetcher;
  
  private SmsbcIccFileFetcher mSmsbcIccFileFetcher;
  
  private SsIccFileFetcher mSsIccFileFetcher;
  
  public IccFileAdapter(Context paramContext, Phone paramPhone) {
    log("IccFileAdapter Creating!");
    this.mContext = paramContext;
    this.mPhone = paramPhone;
    this.mPhoneId = this.mPhone.getPhoneId();
    this.mMmsIccFileFetcher = new MmsIccFileFetcher(this.mContext, paramPhone);
    this.mCcIccFileFetcher = new CcIccFileFetcher(this.mContext, paramPhone);
    this.mOmhIccFileFetcher = new OmhIccFileFetcher(this.mContext, paramPhone);
    this.mSsIccFileFetcher = new SsIccFileFetcher(this.mContext, paramPhone);
    this.mSmsbcIccFileFetcher = new SmsbcIccFileFetcher(this.mContext, paramPhone);
    this.mSmsIccFileFetcher = new SmsIccFileFetcher(this.mContext, paramPhone);
  }
  
  public int getBcsmsCfgFromRuim(int paramInt1, int paramInt2) {
    return this.mSmsbcIccFileFetcher.getBcsmsCfgFromRuim(paramInt1, paramInt2);
  }
  
  public int[] getFcsForApp(int paramInt1, int paramInt2, int paramInt3) {
    return this.mSsIccFileFetcher.getFcsForApp(paramInt1, paramInt2, paramInt3);
  }
  
  public Object getMmsConfigInfo() {
    return this.mMmsIccFileFetcher.getMmsConfigInfo();
  }
  
  public Object getMmsIcpInfo() {
    return this.mMmsIccFileFetcher.getMmsIcpInfo();
  }
  
  public int getNextMessageId() {
    return this.mSmsIccFileFetcher.getNextMessageId();
  }
  
  public SmsIccFileFetcher getSmsIccFileFetcher() {
    return this.mSmsIccFileFetcher;
  }
  
  public SmsbcIccFileFetcher getSmsbcIccFileFetcher() {
    return this.mSmsbcIccFileFetcher;
  }
  
  public HashMap<String, Object> getSsFeatureCode(int paramInt) {
    return this.mSsIccFileFetcher.mData;
  }
  
  public int getWapMsgId() {
    return this.mSmsIccFileFetcher.getWapMsgId();
  }
  
  public boolean isOmhCard() {
    return this.mOmhIccFileFetcher.isOmhCard();
  }
  
  protected void log(String paramString) {
    Rlog.d("IccFileAdapter", paramString + " (phoneId " + this.mPhoneId + ")");
  }
  
  protected void loge(String paramString) {
    Rlog.e("IccFileAdapter", paramString + " (phoneId " + this.mPhoneId + ")");
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/IccFileAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */