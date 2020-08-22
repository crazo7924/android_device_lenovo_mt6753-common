package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class IccSmsStorageStatus implements Parcelable {
   public static final Creator<IccSmsStorageStatus> CREATOR = new Creator<IccSmsStorageStatus>() {
      public IccSmsStorageStatus createFromParcel(Parcel var1) {
         return new IccSmsStorageStatus(var1.readInt(), var1.readInt());
      }

      public IccSmsStorageStatus[] newArray(int var1) {
         return new IccSmsStorageStatus[var1];
      }
   };
   public int mTotal;
   public int mUsed;

   static {
      throw new VerifyError("bad dex opcode");
   }

   public IccSmsStorageStatus() {
      this.mUsed = 0;
      this.mTotal = 0;
   }

   public IccSmsStorageStatus(int var1, int var2) {
      this.mUsed = var1;
      this.mTotal = var2;
   }

   public int describeContents() {
      return 0;
   }

   public int getTotalCount() {
      return this.mTotal;
   }

   public int getUnused() {
      return this.mTotal - this.mUsed;
   }

   public int getUsedCount() {
      return this.mUsed;
   }

   public void reset() {
      this.mUsed = 0;
      this.mTotal = 0;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mUsed);
      var1.writeInt(this.mTotal);
   }
}
