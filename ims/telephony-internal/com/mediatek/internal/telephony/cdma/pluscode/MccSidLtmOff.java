package com.mediatek.internal.telephony.cdma.pluscode;

public class MccSidLtmOff {
   public static final int LTM_OFF_INVALID = 100;
   public int mLtmOffMax;
   public int mLtmOffMin;
   public int mMcc;
   public int mSid;

   public MccSidLtmOff() {
      this.mMcc = -1;
      this.mSid = -1;
      this.mLtmOffMin = 100;
      this.mLtmOffMax = 100;
   }

   public MccSidLtmOff(int var1, int var2, int var3, int var4) {
      this.mMcc = var1;
      this.mSid = var2;
      this.mLtmOffMin = var3;
      this.mLtmOffMax = var4;
   }

   public MccSidLtmOff(MccSidLtmOff var1) {
      this.copyFrom(var1);
   }

   protected void copyFrom(MccSidLtmOff var1) {
      this.mMcc = var1.mMcc;
      this.mSid = var1.mSid;
      this.mLtmOffMin = var1.mLtmOffMin;
      this.mLtmOffMax = var1.mLtmOffMax;
   }

   public int getLtmOffMax() {
      return this.mLtmOffMax;
   }

   public int getLtmOffMin() {
      return this.mLtmOffMin;
   }

   public int getMcc() {
      return this.mMcc;
   }

   public int getSid() {
      return this.mSid;
   }
}
