package com.mediatek.internal.telephony.uicc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.Message;
import android.telephony.Rlog;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.uicc.IccFileHandler;
import com.android.internal.telephony.uicc.UiccController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class IccFileFetcherBase extends Handler {
  protected static final int APP_TYPE_3GPP = 1;
  
  protected static final int APP_TYPE_3GPP2 = 2;
  
  protected static final int APP_TYPE_ACTIVE = 0;
  
  protected static final int APP_TYPE_IMS = 3;
  
  protected static final int EF_TYPE_LINEARFIXED = 0;
  
  protected static final int EF_TYPE_TRANSPARENT = 1;
  
  protected static final int EVENT_GET_LINEARFIXED_RECORD_SIZE_DONE = 0;
  
  protected static final int EVENT_LOAD_LINEARFIXED_ALL_DONE = 1;
  
  protected static final int EVENT_LOAD_TRANSPARENT_DONE = 2;
  
  protected static final int EVENT_UPDATE_LINEARFIXED_DONE = 3;
  
  protected static final int EVENT_UPDATE_TRANSPARENT_DONE = 4;
  
  protected static final int INVALID_INDEX = -1;
  
  private static final String TAG = "IccFileFetcherBase";
  
  protected Context mContext;
  
  protected HashMap<String, Object> mData = new HashMap<String, Object>();
  
  protected IccFileHandler mFh;
  
  private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
      public void onReceive(Context param1Context, Intent param1Intent) {
        if (param1Intent != null) {
          String str = param1Intent.getAction();
          if ("android.intent.action.SIM_STATE_CHANGED".equals(str)) {
            int i = param1Intent.getIntExtra("phone", -1);
            if (IccFileFetcherBase.this.mPhoneId != i || IccFileFetcherBase.this.mPhone.getPhoneType() != 2) {
              IccFileFetcherBase.this.log("IccFileFetcherBase ACTION_SIM_STATE_CHANGED phoneId: " + i + "phonetype: " + IccFileFetcherBase.this.mPhone.getPhoneType() + ", No need to read omh file, return.");
              return;
            } 
            str = param1Intent.getStringExtra("ss");
            if ("LOADED".equals(str)) {
              IccFileFetcherBase.this.log("IccFileFetcherBase ACTION_SIM_STATE_CHANGED simStatus: " + str);
              IccFileFetcherBase.this.exchangeSimInfo();
              return;
            } 
            return;
          } 
          if ("android.intent.action.RADIO_TECHNOLOGY".equals(str)) {
            IccFileFetcherBase.this.log("IccFileFetcherBase Receive action: " + str);
            if (IccFileFetcherBase.this.mData != null) {
              IccFileFetcherBase.this.mData.clear();
              IccFileFetcherBase.this.log("IccFileFetcherBase hashmap is cleared!");
              return;
            } 
          } 
        } 
      }
    };
  
  protected Phone mPhone;
  
  protected int mPhoneId;
  
  protected UiccController mUiccController;
  
  protected IccFileFetcherBase(Context paramContext, Phone paramPhone) {
    log("IccFileFetcherBase Creating!");
    this.mPhone = paramPhone;
    this.mPhoneId = this.mPhone.getPhoneId();
    this.mContext = paramContext;
    IntentFilter intentFilter = new IntentFilter();
    intentFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
    intentFilter.addAction("android.intent.action.RADIO_TECHNOLOGY");
    this.mContext.registerReceiver(this.mIntentReceiver, intentFilter);
  }
  
  protected void exchangeSimInfo() {
    this.mUiccController = UiccController.getInstance();
    Iterator<String> iterator = onGetKeys().iterator();
    while (true) {
      String str;
      IccFileRequest iccFileRequest;
      if (iterator.hasNext()) {
        str = iterator.next();
        iccFileRequest = onGetFilePara(str);
        if (iccFileRequest == null) {
          loge("exchangeSimInfo mPhoneId:" + this.mPhoneId + "  key: " + iterator + "  get Para failed!");
          return;
        } 
      } else {
        return;
      } 
      log("exchangeSimInfo key:" + str + " mEfid:" + iccFileRequest.mEfid + " mEfType:" + iccFileRequest.mEfType + " mAppType :" + iccFileRequest.mAppType + " mEfPath:" + iccFileRequest.mEfPath + " mData:" + iccFileRequest.mData + " mRecordNum:" + iccFileRequest.mRecordNum + " mPin2:" + iccFileRequest.mPin2);
      if (iccFileRequest.mAppType == 0) {
        IccFileHandler iccFileHandler;
        if (this.mPhone.getIccCard() == null) {
          iccFileHandler = null;
        } else {
          iccFileHandler = this.mPhone.getIccCard().getIccFileHandler();
        } 
        this.mFh = iccFileHandler;
      } else {
        this.mFh = this.mUiccController.getIccFileHandler(this.mPhoneId, iccFileRequest.mAppType);
      } 
      if (this.mFh != null) {
        iccFileRequest.mKey = str;
        if ("".equals(iccFileRequest.mEfPath) || iccFileRequest.mEfPath == null)
          log("exchangeSimInfo path is null, it may get an invalid reponse!"); 
        if (iccFileRequest.mData == null) {
          loadSimInfo(iccFileRequest);
          continue;
        } 
        updateSimInfo(iccFileRequest);
        continue;
      } 
      log("exchangeSimInfo mFh[" + this.mPhoneId + "] is null, read failed!");
    } 
  }
  
  public void handleMessage(Message paramMessage) {
    log("handleMessage : " + paramMessage.what);
    try {
      AsyncResult asyncResult3;
      int[] arrayOfInt;
      IccFileRequest iccFileRequest3;
      AsyncResult asyncResult2;
      IccFileRequest iccFileRequest2;
      AsyncResult asyncResult1;
      IccFileRequest iccFileRequest1;
      IccFileRequest iccFileRequest4;
      AsyncResult asyncResult5;
      ArrayList<byte[]> arrayList;
      AsyncResult asyncResult4;
      byte[] arrayOfByte;
      switch (paramMessage.what) {
        case 0:
          asyncResult3 = (AsyncResult)paramMessage.obj;
          if (asyncResult3.exception != null) {
            log("EVENT_GET_LINEARFIXED_RECORD_SIZE_DONE Exception: " + asyncResult3.exception);
            return;
          } 
          iccFileRequest4 = (IccFileRequest)asyncResult3.userObj;
          arrayOfInt = (int[])asyncResult3.result;
          return;
        case 1:
          asyncResult5 = (AsyncResult)((Message)arrayOfInt).obj;
          iccFileRequest3 = (IccFileRequest)asyncResult5.userObj;
          if (asyncResult5.exception != null) {
            loge("EVENT_LOAD_LINEARFIXED_ALL_DONE Exception: " + asyncResult5.exception);
            onParseResult(iccFileRequest3.mKey, null, null);
            return;
          } 
          arrayList = (ArrayList)asyncResult5.result;
          log("EVENT_LOAD_LINEARFIXED_ALL_DONE key: " + iccFileRequest3.mKey + "  datas: " + arrayList);
          onParseResult(iccFileRequest3.mKey, null, arrayList);
          return;
        case 2:
          asyncResult4 = (AsyncResult)((Message)iccFileRequest3).obj;
          iccFileRequest3 = (IccFileRequest)asyncResult4.userObj;
          if (asyncResult4.exception != null) {
            loge("EVENT_LOAD_TRANSPARENT_DONE Exception: " + asyncResult4.exception);
            onParseResult(iccFileRequest3.mKey, null, null);
            return;
          } 
          arrayOfByte = (byte[])asyncResult4.result;
          log("EVENT_LOAD_TRANSPARENT_DONE key: " + iccFileRequest3.mKey + "  data: " + arrayOfByte);
          onParseResult(iccFileRequest3.mKey, arrayOfByte, null);
          return;
        case 3:
          asyncResult2 = (AsyncResult)((Message)iccFileRequest3).obj;
          if (asyncResult2.exception != null) {
            loge("EVENT_UPDATE_LINEARFIXED_DONE Exception: " + asyncResult2.exception);
            return;
          } 
          iccFileRequest2 = (IccFileRequest)asyncResult2.userObj;
          log("EVENT_UPDATE_LINEARFIXED_DONE key: " + iccFileRequest2.mKey + "  data: " + iccFileRequest2.mData);
          return;
        case 4:
          asyncResult1 = (AsyncResult)((Message)iccFileRequest2).obj;
          if (asyncResult1.exception != null) {
            loge("EVENT_UPDATE_TRANSPARENT_DONE Exception: " + asyncResult1.exception);
            return;
          } 
          iccFileRequest1 = (IccFileRequest)asyncResult1.userObj;
          log("EVENT_UPDATE_TRANSPARENT_DONE key: " + iccFileRequest1.mKey + "  data: " + iccFileRequest1.mData);
          return;
      } 
      super.handleMessage((Message)iccFileRequest1);
      return;
    } catch (IllegalArgumentException illegalArgumentException) {
      loge("Exception parsing file record" + illegalArgumentException);
      return;
    } 
  }
  
  protected void loadSimInfo(IccFileRequest paramIccFileRequest) {
    if (paramIccFileRequest.mEfType == 0) {
      this.mFh.loadEFLinearFixedAll(paramIccFileRequest.mEfid, paramIccFileRequest.mEfPath, obtainMessage(1, paramIccFileRequest));
      return;
    } 
    if (paramIccFileRequest.mEfType == 1) {
      this.mFh.loadEFTransparent(paramIccFileRequest.mEfid, paramIccFileRequest.mEfPath, obtainMessage(2, paramIccFileRequest));
      return;
    } 
    loge("loadSimInfo req.mEfType = " + paramIccFileRequest.mEfType + " is invalid!");
  }
  
  protected void log(String paramString) {
    Rlog.d("IccFileFetcherBase", paramString + " (phoneId " + this.mPhoneId + ")");
  }
  
  protected void loge(String paramString) {
    Rlog.e("IccFileFetcherBase", paramString + " (phoneId " + this.mPhoneId + ")");
  }
  
  public abstract IccFileRequest onGetFilePara(String paramString);
  
  public abstract ArrayList<String> onGetKeys();
  
  public abstract void onParseResult(String paramString, byte[] paramArrayOfbyte, ArrayList<byte[]> paramArrayList);
  
  protected void updateSimInfo(IccFileRequest paramIccFileRequest) {
    if (paramIccFileRequest.mEfType == 0) {
      this.mFh.updateEFLinearFixed(paramIccFileRequest.mEfid, paramIccFileRequest.mEfPath, paramIccFileRequest.mRecordNum, paramIccFileRequest.mData, paramIccFileRequest.mPin2, obtainMessage(3, paramIccFileRequest));
      return;
    } 
    if (paramIccFileRequest.mEfType == 1) {
      this.mFh.updateEFTransparent(paramIccFileRequest.mEfid, paramIccFileRequest.mEfPath, paramIccFileRequest.mData, obtainMessage(4, paramIccFileRequest));
      return;
    } 
    loge("updateSimInfo req.mEfType = " + paramIccFileRequest.mEfType + " is invalid!");
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/IccFileFetcherBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */