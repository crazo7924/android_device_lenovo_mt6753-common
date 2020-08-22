package com.mediatek.internal.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PseudoBSRecord implements Parcelable {
   public static final Creator<PseudoBSRecord> CREATOR = new Creator<PseudoBSRecord>() {
      public PseudoBSRecord createFromParcel(Parcel var1) {
         return new PseudoBSRecord(var1);
      }

      public PseudoBSRecord[] newArray(int var1) {
         return new PseudoBSRecord[var1];
      }
   };
   public int mBsArfcn;
   public int mBsBsic;
   public int mBsCellId;
   public int mBsLac;
   public int mBsPlmn;
   public int mBsType;

   static {
      throw new VerifyError("bad dex opcode");
   }

   protected PseudoBSRecord() {
      this.mBsType = 0;
      this.mBsPlmn = 0;
      this.mBsLac = 0;
      this.mBsCellId = 0;
      this.mBsArfcn = 0;
      this.mBsBsic = 0;
   }

   public PseudoBSRecord(int var1, int var2, int var3, int var4, int var5, int var6) {
      this.mBsType = var1;
      this.mBsPlmn = var2;
      this.mBsLac = var3;
      this.mBsCellId = var4;
      this.mBsArfcn = var5;
      this.mBsBsic = var6;
   }

   protected PseudoBSRecord(Parcel var1) {
      this.mBsType = var1.readInt();
      this.mBsPlmn = var1.readInt();
      this.mBsLac = var1.readInt();
      this.mBsCellId = var1.readInt();
      this.mBsArfcn = var1.readInt();
      this.mBsBsic = var1.readInt();
   }

   protected PseudoBSRecord(PseudoBSRecord var1) {
      this.mBsType = var1.mBsType;
      this.mBsPlmn = var1.mBsPlmn;
      this.mBsLac = var1.mBsLac;
      this.mBsCellId = var1.mBsCellId;
      this.mBsArfcn = var1.mBsArfcn;
      this.mBsBsic = var1.mBsBsic;
   }

   private static boolean equalsHandlesNulls(Object var0, Object var1) {
      if (var0 == null) {
         return var1 == null;
      } else {
         return var0.equals(var1);
      }
   }

   public int describeContents() {
      return 0;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else {
         PseudoBSRecord var4;
         try {
            var4 = (PseudoBSRecord)var1;
         } catch (ClassCastException var3) {
            return false;
         }

         if (this.mBsType != var4.mBsType || !equalsHandlesNulls(this.mBsPlmn, var4.mBsPlmn) || !equalsHandlesNulls(this.mBsLac, var4.mBsLac) || !equalsHandlesNulls(this.mBsCellId, var4.mBsCellId) || this.mBsArfcn != var4.mBsArfcn || this.mBsBsic != var4.mBsBsic) {
            var2 = false;
         }

         return var2;
      }
   }

   public int getArfcn() {
      return this.mBsArfcn;
   }

   public int getBsic() {
      return this.mBsBsic;
   }

   public int getCi() {
      return this.mBsCellId;
   }

   public int getLac() {
      return this.mBsLac;
   }

   public int getPlmn() {
      return this.mBsPlmn;
   }

   public int getType() {
      return this.mBsType;
   }

   public int hashCode() {
      return this.mBsType + this.mBsPlmn + this.mBsLac + this.mBsCellId + this.mBsArfcn + this.mBsBsic;
   }

   public String toString() {
      StringBuffer var1 = new StringBuffer();
      var1.append(" mBsType=").append(this.mBsType);
      var1.append(" mBsPlmn=").append(this.mBsPlmn);
      var1.append(" mBsLac=").append(this.mBsLac);
      var1.append(" mBsCellId=").append(this.mBsCellId);
      var1.append(" mBsArfcn=").append(this.mBsArfcn);
      var1.append(" mBsBsic=").append(this.mBsBsic);
      return var1.toString();
   }

   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.mBsType);
      var1.writeInt(this.mBsPlmn);
      var1.writeInt(this.mBsLac);
      var1.writeInt(this.mBsCellId);
      var1.writeInt(this.mBsArfcn);
      var1.writeInt(this.mBsBsic);
   }
}
