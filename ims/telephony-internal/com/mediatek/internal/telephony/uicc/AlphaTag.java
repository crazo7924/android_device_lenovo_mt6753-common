package com.mediatek.internal.telephony.uicc;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class AlphaTag implements Parcelable {
  public static final Parcelable.Creator<AlphaTag> CREATOR = new Parcelable.Creator<AlphaTag>() {
      public AlphaTag createFromParcel(Parcel param1Parcel) {
        return new AlphaTag(param1Parcel.readInt(), param1Parcel.readString(), param1Parcel.readInt());
      }
      
      public AlphaTag[] newArray(int param1Int) {
        return new AlphaTag[param1Int];
      }
    };
  
  static final String LOG_TAG = "AlphaTag";
  
  String mAlphaTag = null;
  
  int mPbrIndex;
  
  int mRecordNumber;
  
  public AlphaTag(int paramInt1, String paramString, int paramInt2) {
    this.mRecordNumber = paramInt1;
    this.mAlphaTag = paramString;
    this.mPbrIndex = paramInt2;
  }
  
  private static boolean stringCompareNullEqualsEmpty(String paramString1, String paramString2) {
    if (paramString1 == paramString2)
      return true; 
    String str = paramString1;
    if (paramString1 == null)
      str = ""; 
    paramString1 = paramString2;
    if (paramString2 == null)
      paramString1 = ""; 
    return str.equals(paramString1);
  }
  
  public int describeContents() {
    return 0;
  }
  
  public String getAlphaTag() {
    return this.mAlphaTag;
  }
  
  public int getPbrIndex() {
    return this.mPbrIndex;
  }
  
  public int getRecordIndex() {
    return this.mRecordNumber;
  }
  
  public boolean isEmpty() {
    return TextUtils.isEmpty(this.mAlphaTag);
  }
  
  public boolean isEqual(AlphaTag paramAlphaTag) {
    return stringCompareNullEqualsEmpty(this.mAlphaTag, paramAlphaTag.mAlphaTag);
  }
  
  public void setAlphaTag(String paramString) {
    this.mAlphaTag = paramString;
  }
  
  public void setPbrIndex(int paramInt) {
    this.mPbrIndex = paramInt;
  }
  
  public void setRecordIndex(int paramInt) {
    this.mRecordNumber = paramInt;
  }
  
  public String toString() {
    return "AlphaTag: '" + this.mRecordNumber + "' '" + this.mAlphaTag + "' '" + this.mPbrIndex + "'";
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeInt(this.mRecordNumber);
    paramParcel.writeString(this.mAlphaTag);
    paramParcel.writeInt(this.mPbrIndex);
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/AlphaTag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */