package com.mediatek.internal.telephony.cdma.pluscode;

public class SidMccMnc {
   public int mMccMnc;
   public int mSid;

   public SidMccMnc() {
      this.mSid = -1;
      this.mMccMnc = -1;
   }

   public SidMccMnc(int var1, int var2) {
      this.mSid = var1;
      this.mMccMnc = var2;
   }

   public SidMccMnc(SidMccMnc var1) {
      this.copyFrom(var1);
   }

   protected void copyFrom(SidMccMnc var1) {
      this.mSid = var1.mSid;
      this.mMccMnc = var1.mMccMnc;
   }

   public int getMccMnc() {
      return this.mMccMnc;
   }

   public int getSid() {
      return this.mSid;
   }

   public String toString() {
      return "Sid =" + this.mSid + ", MccMnc = " + this.mMccMnc;
   }
}
