package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;

public class NetworkInfoWithAcT implements Parcelable {
  public static final Parcelable.Creator<NetworkInfoWithAcT> CREATOR = new Parcelable.Creator<NetworkInfoWithAcT>() {
      public NetworkInfoWithAcT createFromParcel(Parcel param1Parcel) {
        return new NetworkInfoWithAcT(param1Parcel.readString(), param1Parcel.readString(), param1Parcel.readInt(), param1Parcel.readInt());
      }
      
      public NetworkInfoWithAcT[] newArray(int param1Int) {
        return new NetworkInfoWithAcT[param1Int];
      }
    };
  
  int nAct;
  
  int nPriority;
  
  String operatorAlphaName;
  
  String operatorNumeric;
  
  public NetworkInfoWithAcT(String paramString1, String paramString2, int paramInt1, int paramInt2) {
    this.operatorAlphaName = paramString1;
    this.operatorNumeric = paramString2;
    this.nAct = paramInt1;
    this.nPriority = paramInt2;
  }
  
  public int describeContents() {
    return 0;
  }
  
  public int getAccessTechnology() {
    return this.nAct;
  }
  
  public String getOperatorAlphaName() {
    return this.operatorAlphaName;
  }
  
  public String getOperatorNumeric() {
    return this.operatorNumeric;
  }
  
  public int getPriority() {
    return this.nPriority;
  }
  
  public void setAccessTechnology(int paramInt) {
    this.nAct = paramInt;
  }
  
  public void setOperatorAlphaName(String paramString) {
    this.operatorAlphaName = paramString;
  }
  
  public void setOperatorNumeric(String paramString) {
    this.operatorNumeric = paramString;
  }
  
  public void setPriority(int paramInt) {
    this.nPriority = paramInt;
  }
  
  public String toString() {
    return "NetworkInfoWithAcT " + this.operatorAlphaName + "/" + this.operatorNumeric + "/" + this.nAct + "/" + this.nPriority;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    paramParcel.writeString(this.operatorAlphaName);
    paramParcel.writeString(this.operatorNumeric);
    paramParcel.writeInt(this.nAct);
    paramParcel.writeInt(this.nPriority);
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/NetworkInfoWithAcT.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */