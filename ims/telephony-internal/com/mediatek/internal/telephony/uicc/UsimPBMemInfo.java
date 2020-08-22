package com.mediatek.internal.telephony.uicc;

import android.os.Parcel;
import android.os.Parcelable;

public class UsimPBMemInfo implements Parcelable {
  public static final Parcelable.Creator<UsimPBMemInfo> CREATOR = new Parcelable.Creator<UsimPBMemInfo>() {
      public UsimPBMemInfo createFromParcel(Parcel param1Parcel) {
        return UsimPBMemInfo.createFromParcel(param1Parcel);
      }
      
      public UsimPBMemInfo[] newArray(int param1Int) {
        return new UsimPBMemInfo[param1Int];
      }
    };
  
  public static final int INT_NOT_SET = -1;
  
  public static final String STRING_NOT_SET = "";
  
  private int mAasLength = -1;
  
  private int mAasTotal = -1;
  
  private int mAasType = -1;
  
  private int mAasUsed = -1;
  
  private int mAdnLength = -1;
  
  private int mAdnTotal = -1;
  
  private int mAdnType = -1;
  
  private int mAdnUsed = -1;
  
  private int mAnrLength = -1;
  
  private int mAnrTotal = -1;
  
  private int mAnrType = -1;
  
  private int mAnrUsed = -1;
  
  private int mCcpLength = -1;
  
  private int mCcpTotal = -1;
  
  private int mCcpType = -1;
  
  private int mCcpUsed = -1;
  
  private int mEmailLength = -1;
  
  private int mEmailTotal = -1;
  
  private int mEmailType = -1;
  
  private int mEmailUsed = -1;
  
  private int mExt1Length = -1;
  
  private int mExt1Total = -1;
  
  private int mExt1Type = -1;
  
  private int mExt1Used = -1;
  
  private int mGasLength = -1;
  
  private int mGasTotal = -1;
  
  private int mGasType = -1;
  
  private int mGasUsed = -1;
  
  private int mSliceIndex = -1;
  
  private int mSneLength = -1;
  
  private int mSneTotal = -1;
  
  private int mSneType = -1;
  
  private int mSneUsed = -1;
  
  public static UsimPBMemInfo createFromParcel(Parcel paramParcel) {
    UsimPBMemInfo usimPBMemInfo = new UsimPBMemInfo();
    usimPBMemInfo.mSliceIndex = paramParcel.readInt();
    usimPBMemInfo.mAdnLength = paramParcel.readInt();
    usimPBMemInfo.mAdnUsed = paramParcel.readInt();
    usimPBMemInfo.mAdnTotal = paramParcel.readInt();
    usimPBMemInfo.mAdnType = paramParcel.readInt();
    usimPBMemInfo.mExt1Length = paramParcel.readInt();
    usimPBMemInfo.mExt1Used = paramParcel.readInt();
    usimPBMemInfo.mExt1Total = paramParcel.readInt();
    usimPBMemInfo.mExt1Type = paramParcel.readInt();
    usimPBMemInfo.mGasLength = paramParcel.readInt();
    usimPBMemInfo.mGasUsed = paramParcel.readInt();
    usimPBMemInfo.mGasTotal = paramParcel.readInt();
    usimPBMemInfo.mGasType = paramParcel.readInt();
    usimPBMemInfo.mAnrLength = paramParcel.readInt();
    usimPBMemInfo.mAnrUsed = paramParcel.readInt();
    usimPBMemInfo.mAnrTotal = paramParcel.readInt();
    usimPBMemInfo.mAnrType = paramParcel.readInt();
    usimPBMemInfo.mAasLength = paramParcel.readInt();
    usimPBMemInfo.mAasUsed = paramParcel.readInt();
    usimPBMemInfo.mAasTotal = paramParcel.readInt();
    usimPBMemInfo.mAasType = paramParcel.readInt();
    usimPBMemInfo.mSneLength = paramParcel.readInt();
    usimPBMemInfo.mSneUsed = paramParcel.readInt();
    usimPBMemInfo.mSneTotal = paramParcel.readInt();
    usimPBMemInfo.mSneType = paramParcel.readInt();
    usimPBMemInfo.mEmailLength = paramParcel.readInt();
    usimPBMemInfo.mEmailUsed = paramParcel.readInt();
    usimPBMemInfo.mEmailTotal = paramParcel.readInt();
    usimPBMemInfo.mEmailType = paramParcel.readInt();
    usimPBMemInfo.mCcpLength = paramParcel.readInt();
    usimPBMemInfo.mCcpUsed = paramParcel.readInt();
    usimPBMemInfo.mCcpTotal = paramParcel.readInt();
    usimPBMemInfo.mCcpType = paramParcel.readInt();
    return usimPBMemInfo;
  }
  
  public int describeContents() {
    return 0;
  }
  
  public int getAasFree() {
    return this.mAasTotal - this.mAasUsed;
  }
  
  public int getAasLength() {
    return this.mAasLength;
  }
  
  public int getAasTotal() {
    return this.mAasTotal;
  }
  
  public int getAasType() {
    return this.mAasType;
  }
  
  public int getAasUsed() {
    return this.mAasUsed;
  }
  
  public int getAdnFree() {
    return this.mAdnTotal - this.mAdnUsed;
  }
  
  public int getAdnLength() {
    return this.mAdnLength;
  }
  
  public int getAdnTotal() {
    return this.mAdnTotal;
  }
  
  public int getAdnType() {
    return this.mAdnType;
  }
  
  public int getAdnUsed() {
    return this.mAdnUsed;
  }
  
  public int getAnrFree() {
    return this.mAnrTotal - this.mAnrUsed;
  }
  
  public int getAnrLength() {
    return this.mAnrLength;
  }
  
  public int getAnrTotal() {
    return this.mAnrTotal;
  }
  
  public int getAnrType() {
    return this.mAnrType;
  }
  
  public int getAnrUsed() {
    return this.mAnrUsed;
  }
  
  public int getCcpFree() {
    return this.mCcpTotal - this.mCcpUsed;
  }
  
  public int getCcpLength() {
    return this.mCcpLength;
  }
  
  public int getCcpTotal() {
    return this.mCcpTotal;
  }
  
  public int getCcpType() {
    return this.mCcpType;
  }
  
  public int getCcpUsed() {
    return this.mCcpUsed;
  }
  
  public int getEmailFree() {
    return this.mEmailTotal - this.mEmailUsed;
  }
  
  public int getEmailLength() {
    return this.mEmailLength;
  }
  
  public int getEmailTotal() {
    return this.mEmailTotal;
  }
  
  public int getEmailType() {
    return this.mEmailType;
  }
  
  public int getEmailUsed() {
    return this.mEmailUsed;
  }
  
  public int getExt1Free() {
    return this.mExt1Total - this.mExt1Used;
  }
  
  public int getExt1Length() {
    return this.mExt1Length;
  }
  
  public int getExt1Total() {
    return this.mExt1Total;
  }
  
  public int getExt1Type() {
    return this.mExt1Type;
  }
  
  public int getExt1Used() {
    return this.mExt1Used;
  }
  
  public int getGasFree() {
    return this.mGasTotal - this.mGasUsed;
  }
  
  public int getGasLength() {
    return this.mGasLength;
  }
  
  public int getGasTotal() {
    return this.mGasTotal;
  }
  
  public int getGasType() {
    return this.mGasType;
  }
  
  public int getGasUsed() {
    return this.mGasUsed;
  }
  
  public int getSliceIndex() {
    return this.mSliceIndex;
  }
  
  public int getSneFree() {
    return this.mSneTotal - this.mSneUsed;
  }
  
  public int getSneLength() {
    return this.mSneLength;
  }
  
  public int getSneTotal() {
    return this.mSneTotal;
  }
  
  public int getSneType() {
    return this.mSneType;
  }
  
  public int getSneUsed() {
    return this.mSneUsed;
  }
  
  public void setAasLength(int paramInt) {
    this.mAasLength = paramInt;
  }
  
  public void setAasTotal(int paramInt) {
    this.mAasTotal = paramInt;
  }
  
  public void setAasType(int paramInt) {
    this.mAasType = paramInt;
  }
  
  public void setAasUsed(int paramInt) {
    this.mAasUsed = paramInt;
  }
  
  public void setAdnLength(int paramInt) {
    this.mAdnLength = paramInt;
  }
  
  public void setAdnTotal(int paramInt) {
    this.mAdnTotal = paramInt;
  }
  
  public void setAdnType(int paramInt) {
    this.mAdnType = paramInt;
  }
  
  public void setAdnUsed(int paramInt) {
    this.mAdnUsed = paramInt;
  }
  
  public void setAnrLength(int paramInt) {
    this.mAnrLength = paramInt;
  }
  
  public void setAnrTotal(int paramInt) {
    this.mAnrTotal = paramInt;
  }
  
  public void setAnrType(int paramInt) {
    this.mAnrType = paramInt;
  }
  
  public void setAnrUsed(int paramInt) {
    this.mAnrUsed = paramInt;
  }
  
  public void setCcpLength(int paramInt) {
    this.mCcpLength = paramInt;
  }
  
  public void setCcpTotal(int paramInt) {
    this.mCcpTotal = paramInt;
  }
  
  public void setCcpType(int paramInt) {
    this.mCcpType = paramInt;
  }
  
  public void setCcpUsed(int paramInt) {
    this.mCcpUsed = paramInt;
  }
  
  public void setEmailLength(int paramInt) {
    this.mEmailLength = paramInt;
  }
  
  public void setEmailTotal(int paramInt) {
    this.mEmailTotal = paramInt;
  }
  
  public void setEmailType(int paramInt) {
    this.mEmailType = paramInt;
  }
  
  public void setEmailUsed(int paramInt) {
    this.mEmailUsed = paramInt;
  }
  
  public void setExt1Length(int paramInt) {
    this.mExt1Length = paramInt;
  }
  
  public void setExt1Total(int paramInt) {
    this.mExt1Total = paramInt;
  }
  
  public void setExt1Type(int paramInt) {
    this.mExt1Type = paramInt;
  }
  
  public void setExt1Used(int paramInt) {
    this.mExt1Used = paramInt;
  }
  
  public void setGasLength(int paramInt) {
    this.mGasLength = paramInt;
  }
  
  public void setGasTotal(int paramInt) {
    this.mGasTotal = paramInt;
  }
  
  public void setGasType(int paramInt) {
    this.mGasType = paramInt;
  }
  
  public void setGasUsed(int paramInt) {
    this.mGasUsed = paramInt;
  }
  
  public void setSliceIndex(int paramInt) {
    this.mSliceIndex = paramInt;
  }
  
  public void setSneLength(int paramInt) {
    this.mSneLength = paramInt;
  }
  
  public void setSneTotal(int paramInt) {
    this.mSneTotal = paramInt;
  }
  
  public void setSneType(int paramInt) {
    this.mSneType = paramInt;
  }
  
  public void setSneUsed(int paramInt) {
    this.mSneUsed = paramInt;
  }
  
  public String toString() {
    return super.toString() + " mSliceIndex: " + this.mSliceIndex + " mAdnLength: " + this.mAdnLength + " mAdnUsed: " + Integer.toString(this.mAdnUsed) + " mAdnTotal:" + Integer.toString(this.mAdnTotal) + " mAdnType:" + Integer.toString(this.mAdnType) + " mExt1Length:" + Integer.toString(this.mExt1Length) + " mExt1Used:" + Integer.toString(this.mExt1Used) + " mExt1Total" + Integer.toString(this.mExt1Total) + " mExt1Type" + Integer.toString(this.mExt1Type) + " mGasLength" + Integer.toString(this.mGasLength) + " mGasUsed" + Integer.toString(this.mGasUsed) + " mGasTotal: " + Integer.toString(this.mGasTotal) + " mGasType: " + Integer.toString(this.mGasType) + " mAnrLength: " + Integer.toString(this.mAnrLength) + " mAnrUsed: " + Integer.toString(this.mAnrUsed) + " mAnrTotal: " + Integer.toString(this.mAnrTotal) + " mAnrType: " + Integer.toString(this.mAnrType) + " mEmailLength: " + Integer.toString(this.mEmailLength) + " mEmailUsed: " + Integer.toString(this.mEmailUsed) + " mEmailTotal: " + Integer.toString(this.mEmailTotal) + " mEmailType: " + Integer.toString(this.mEmailType);
  }
  
  public void writeToParcel(Parcel paramParcel) {
    paramParcel.writeInt(this.mSliceIndex);
    paramParcel.writeInt(this.mAdnLength);
    paramParcel.writeInt(this.mAdnUsed);
    paramParcel.writeInt(this.mAdnTotal);
    paramParcel.writeInt(this.mAdnType);
    paramParcel.writeInt(this.mExt1Length);
    paramParcel.writeInt(this.mExt1Used);
    paramParcel.writeInt(this.mExt1Total);
    paramParcel.writeInt(this.mExt1Type);
    paramParcel.writeInt(this.mGasLength);
    paramParcel.writeInt(this.mGasUsed);
    paramParcel.writeInt(this.mGasTotal);
    paramParcel.writeInt(this.mGasType);
    paramParcel.writeInt(this.mAnrLength);
    paramParcel.writeInt(this.mAnrUsed);
    paramParcel.writeInt(this.mAnrTotal);
    paramParcel.writeInt(this.mAnrType);
    paramParcel.writeInt(this.mAasLength);
    paramParcel.writeInt(this.mAasUsed);
    paramParcel.writeInt(this.mAasTotal);
    paramParcel.writeInt(this.mAasType);
    paramParcel.writeInt(this.mSneLength);
    paramParcel.writeInt(this.mSneUsed);
    paramParcel.writeInt(this.mSneTotal);
    paramParcel.writeInt(this.mSneType);
    paramParcel.writeInt(this.mEmailLength);
    paramParcel.writeInt(this.mEmailUsed);
    paramParcel.writeInt(this.mEmailTotal);
    paramParcel.writeInt(this.mEmailType);
    paramParcel.writeInt(this.mCcpLength);
    paramParcel.writeInt(this.mCcpUsed);
    paramParcel.writeInt(this.mCcpTotal);
    paramParcel.writeInt(this.mCcpType);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt) {
    writeToParcel(paramParcel);
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/uicc/UsimPBMemInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */