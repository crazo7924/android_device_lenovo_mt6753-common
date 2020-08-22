package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class MmsConfigInfo implements Parcelable {
   public static final Creator<MmsConfigInfo> CREATOR = new Creator<MmsConfigInfo>() {
      public MmsConfigInfo createFromParcel(Parcel var1) {
         MmsConfigInfo var2 = new MmsConfigInfo();
         var2.readFrom(var1);
         return var2;
      }

      public MmsConfigInfo[] newArray(int var1) {
         return new MmsConfigInfo[var1];
      }
   };
   public int mCenterTimeout;
   public int mMessageMaxSize;
   public long mRetryInterval;
   public int mRetryTimes;

   static {
      throw new VerifyError("bad dex opcode");
   }

   public int describeContents() {
      return 0;
   }

   public void readFrom(Parcel var1) {
      this.mMessageMaxSize = var1.readInt();
      this.mRetryTimes = var1.readInt();
      this.mRetryInterval = var1.readLong();
      this.mCenterTimeout = var1.readInt();
   }

   public void writeTo(Parcel var1) {
      var1.writeInt(this.mMessageMaxSize);
      var1.writeInt(this.mRetryTimes);
      var1.writeLong(this.mRetryInterval);
      var1.writeInt(this.mCenterTimeout);
   }

   public void writeToParcel(Parcel var1, int var2) {
      this.writeTo(var1);
   }
}
