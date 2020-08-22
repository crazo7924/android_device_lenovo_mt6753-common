package com.mediatek.internal.telephony.uicc;

class IccFileRequest {
  public int mAppType;
  
  public byte[] mData;
  
  public String mEfPath;
  
  public int mEfType;
  
  public int mEfid;
  
  public String mKey = null;
  
  public String mPin2;
  
  public int mRecordNum;
  
  public IccFileRequest(int paramInt1, int paramInt2, int paramInt3, String paramString1, byte[] paramArrayOfbyte, int paramInt4, String paramString2) {
    this.mEfid = paramInt1;
    this.mEfType = paramInt2;
    this.mAppType = paramInt3;
    this.mEfPath = paramString1;
    this.mData = paramArrayOfbyte;
    this.mRecordNum = paramInt4;
    this.mPin2 = paramString2;
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/IccFileRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */