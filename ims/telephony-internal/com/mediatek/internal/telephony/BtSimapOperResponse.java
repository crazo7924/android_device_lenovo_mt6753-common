package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class BtSimapOperResponse implements Parcelable {
   private static final byte APDU_RESPONSE_MASK = 8;
   private static final byte ATR_MASK = 4;
   public static final Creator<BtSimapOperResponse> CREATOR = new Creator() {
      public BtSimapOperResponse createFromParcel(Parcel var1) {
         return new BtSimapOperResponse(var1);
      }

      public BtSimapOperResponse[] newArray(int var1) {
         return new BtSimapOperResponse[var1];
      }
   };
   private static final byte CURTYPE_MASK = 1;
   public static final int ERR_CARD_NOT_ACCESSIBLE = 2;
   public static final int ERR_CARD_POWERED_OFF = 3;
   public static final int ERR_CARD_POWERED_ON = 5;
   public static final int ERR_CARD_REMOVED = 4;
   public static final int ERR_DATA_NOT_AVAILABLE = 6;
   public static final int ERR_NOT_SUPPORTED = 7;
   public static final int ERR_NO_REASON_DEFINED = 1;
   public static final int SUCCESS = 0;
   private static final byte SUPPORTTYPE_MASK = 2;
   static final int UNKNOWN_PROTOCOL_TYPE = -1;
   private int mCurType;
   private int mParams;
   private String mStrAPDU;
   private String mStrATR;
   private int mSupportType;

   static {
      throw new VerifyError("bad dex opcode");
   }

   public BtSimapOperResponse() {
      this.mParams = 0;
      this.mCurType = -1;
      this.mSupportType = -1;
      this.mStrATR = null;
      this.mStrAPDU = null;
   }

   public BtSimapOperResponse(Parcel var1) {
      this.mParams = var1.readInt();
      this.mCurType = var1.readInt();
      this.mSupportType = var1.readInt();
      this.mStrATR = var1.readString();
      this.mStrAPDU = var1.readString();
   }

   public int describeContents() {
      return 0;
   }

   public String getApduString() {
      return this.isApduExist() ? this.mStrAPDU : null;
   }

   public String getAtrString() {
      return this.isAtrExist() ? this.mStrATR : null;
   }

   public int getCurType() {
      return this.isCurTypeExist() ? this.mCurType : -1;
   }

   public int getSupportType() {
      return this.isSupportTypeExist() ? this.mSupportType : -1;
   }

   public boolean isApduExist() {
      return (this.mParams & 8) > 0;
   }

   public boolean isAtrExist() {
      return (this.mParams & 4) > 0;
   }

   public boolean isCurTypeExist() {
      return (this.mParams & 1) > 0;
   }

   public boolean isSupportTypeExist() {
      return (this.mParams & 2) > 0;
   }

   public void readFromParcel(Parcel var1) {
      this.mParams = var1.readInt();
      this.mCurType = var1.readInt();
      this.mSupportType = var1.readInt();
      this.mStrATR = var1.readString();
      this.mStrAPDU = var1.readString();
   }

   public void setApduString(String var1) {
      if (var1 != null) {
         this.mStrAPDU = var1;
         this.mParams |= 8;
      }

   }

   public void setAtrString(String var1) {
      if (var1 != null) {
         this.mStrATR = var1;
         this.mParams |= 4;
      }

   }

   public void setCurType(int var1) {
      if (var1 == 0 || var1 == 1) {
         this.mCurType = var1;
         this.mParams |= 1;
      }

   }

   public void setSupportType(int var1) {
      if (var1 == 0 || var1 == 1 || var1 == 2) {
         this.mSupportType = var1;
         this.mParams |= 2;
      }

   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mParams);
      var1.writeInt(this.mCurType);
      var1.writeInt(this.mSupportType);
      var1.writeString(this.mStrATR);
      var1.writeString(this.mStrAPDU);
   }
}
