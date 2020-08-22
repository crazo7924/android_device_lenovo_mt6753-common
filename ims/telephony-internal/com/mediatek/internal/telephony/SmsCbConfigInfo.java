package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class SmsCbConfigInfo implements Parcelable {
   public static final Creator<SmsCbConfigInfo> CREATOR = new Creator<SmsCbConfigInfo>() {
      public SmsCbConfigInfo createFromParcel(Parcel var1) {
         int var2 = var1.readInt();
         int var3 = var1.readInt();
         int var4 = var1.readInt();
         int var5 = var1.readInt();
         boolean var6;
         if (var1.readByte() != 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         return new SmsCbConfigInfo(var2, var3, var4, var5, var6);
      }

      public SmsCbConfigInfo[] newArray(int var1) {
         return new SmsCbConfigInfo[var1];
      }
   };
   public int mFromCodeScheme;
   public int mFromServiceId;
   public boolean mSelected;
   public int mToCodeScheme;
   public int mToServiceId;

   static {
      throw new VerifyError("bad dex opcode");
   }

   public SmsCbConfigInfo(int var1, int var2, int var3, int var4, boolean var5) {
      this.mFromServiceId = var1;
      this.mToServiceId = var2;
      this.mFromCodeScheme = var3;
      this.mToCodeScheme = var4;
      this.mSelected = var5;
   }

   public int describeContents() {
      return 0;
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mFromServiceId);
      var1.writeInt(this.mToServiceId);
      var1.writeInt(this.mFromCodeScheme);
      var1.writeInt(this.mToCodeScheme);
      byte var3;
      if (this.mSelected) {
         var3 = 1;
      } else {
         var3 = 0;
      }

      var1.writeByte((byte)var3);
   }
}
