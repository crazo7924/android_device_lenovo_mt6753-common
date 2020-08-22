package com.mediatek.internal.telephony.cdma.pluscode;

public class MccIddNddSid {
   public String mCc;
   public String mIdd;
   public int mMcc;
   public String mNdd;
   public int mSidMax;
   public int mSidMin;

   public MccIddNddSid() {
      this.mMcc = -1;
      this.mCc = null;
      this.mSidMin = -1;
      this.mSidMax = -1;
      this.mIdd = null;
      this.mNdd = null;
   }

   public MccIddNddSid(int var1, String var2, int var3, int var4, String var5, String var6) {
      this.mMcc = var1;
      this.mCc = var2;
      this.mSidMin = var3;
      this.mSidMax = var4;
      this.mIdd = var5;
      this.mNdd = var6;
   }

   public MccIddNddSid(MccIddNddSid var1) {
      this.copyFrom(var1);
   }

   protected void copyFrom(MccIddNddSid var1) {
      this.mMcc = var1.mMcc;
      this.mCc = var1.mCc;
      this.mSidMin = var1.mSidMin;
      this.mSidMax = var1.mSidMax;
      this.mIdd = var1.mIdd;
      this.mNdd = var1.mNdd;
   }

   public String getCc() {
      return this.mCc;
   }

   public String getIdd() {
      return this.mIdd;
   }

   public int getMcc() {
      return this.mMcc;
   }

   public String getNdd() {
      return this.mNdd;
   }

   public int getSidMax() {
      return this.mSidMax;
   }

   public int getSidMin() {
      return this.mSidMin;
   }

   public String toString() {
      return "Mcc =" + this.mMcc + ", Cc = " + this.mCc + ", SidMin = " + this.mSidMin + ", SidMax = " + this.mSidMax + ", Idd = " + this.mIdd + ", Ndd = " + this.mNdd;
   }
}
