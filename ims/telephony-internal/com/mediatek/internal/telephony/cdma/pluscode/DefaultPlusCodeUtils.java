package com.mediatek.internal.telephony.cdma.pluscode;

import android.telephony.Rlog;

public class DefaultPlusCodeUtils implements IPlusCodeUtils {
   public static final boolean DBG = true;
   private static final String LOG_TAG = "DefaultPlusCodeUtils";

   private static void log(String var0) {
      Rlog.d("DefaultPlusCodeUtils", var0);
   }

   public boolean canFormatPlusCodeForSms() {
      log("canFormatPlusCodeForSms");
      return false;
   }

   public boolean canFormatPlusToIddNdd() {
      log("canFormatPlusToIddNdd");
      return false;
   }

   public String checkMccBySidLtmOff(String var1) {
      log("checkMccBySidLtmOff mccMnc=" + var1);
      return var1;
   }

   public String removeIddNddAddPlusCode(String var1) {
      log("removeIddNddAddPlusCode number=" + var1);
      return var1;
   }

   public String removeIddNddAddPlusCodeForSms(String var1) {
      log("removeIddNddAddPlusCodeForSms number=" + var1);
      return var1;
   }

   public String replacePlusCodeForSms(String var1) {
      log("replacePlusCodeForSms number=" + var1);
      return var1;
   }

   public String replacePlusCodeWithIddNdd(String var1) {
      log("replacePlusCodeWithIddNdd number=" + var1);
      return var1;
   }
}
