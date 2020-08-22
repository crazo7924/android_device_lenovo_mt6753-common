package com.mediatek.internal.telephony.uicc;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class UsimGroup implements Parcelable {
  public static final Parcelable.Creator<UsimGroup> CREATOR = new Parcelable.Creator<UsimGroup>() {
      public UsimGroup createFromParcel(Parcel param1Parcel) {
        return new UsimGroup(param1Parcel.readInt(), param1Parcel.readString());
      }
      
      public UsimGroup[] newArray(int param1Int) {
        return new UsimGroup[param1Int];
      }
    };
  
  static final String LOG_TAG = "UsimGroup";
  
  String mAlphaTag = null;
  
  int mRecordNumber;
  
  public UsimGroup(int paramInt, String paramString) {
    this.mRecordNumber = paramInt;
    this.mAlphaTag = paramString;
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
  
  public int getRecordIndex() {
    return this.mRecordNumber;
  }
  
  public boolean isEmpty() {
    return TextUtils.isEmpty(this.mAlphaTag);
  }
  
  public boolean isEqual(UsimGroup paramUsimGroup) {
    return stringCompareNullEqualsEmpty(this.mAlphaTag, paramUsimGroup.mAlphaTag);
  }
  
  public void setAlphaTag(String paramString) {
    this.mAlphaTag = paramString;
  }
  
  public void setRecordIndex(int paramInt) {
    this.mRecordNumber = paramInt;
  }
  
  public String toString() {
    return "UsimGroup '" + this.mRecordNumber + "' '" + this.mAlphaTag + "' ";
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeInt(this.mRecordNumber);
    paramParcel.writeString(this.mAlphaTag);
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/UsimGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */