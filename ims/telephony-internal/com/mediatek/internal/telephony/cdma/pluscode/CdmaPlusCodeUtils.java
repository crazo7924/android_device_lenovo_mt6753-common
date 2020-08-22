package com.mediatek.internal.telephony.cdma.pluscode;

import android.telephony.Rlog;

public class CdmaPlusCodeUtils extends DefaultPlusCodeUtils {
   private static final String LOG_TAG = "CdmaPlusCodeUtils";

   private static void log(String var0) {
      Rlog.d("CdmaPlusCodeUtils", var0);
   }

   public boolean canFormatPlusCodeForSms() {
      log("canFormatPlusCodeForSms");
      return PlusCodeToIddNddUtils.canFormatPlusCodeForSms();
   }

   public boolean canFormatPlusToIddNdd() {
      log("canFormatPlusToIddNdd");
      return PlusCodeToIddNddUtils.canFormatPlusToIddNdd();
   }

   public String checkMccBySidLtmOff(String var1) {
      log("checkMccBySidLtmOff mccMnc=" + var1);
      return PlusCodeToIddNddUtils.checkMccBySidLtmOff(var1);
   }

   public String removeIddNddAddPlusCode(String var1) {
      return PlusCodeToIddNddUtils.removeIddNddAddPlusCode(var1);
   }

   public String removeIddNddAddPlusCodeForSms(String var1) {
      return PlusCodeToIddNddUtils.removeIddNddAddPlusCodeForSms(var1);
   }

   public String replacePlusCodeForSms(String var1) {
      return PlusCodeToIddNddUtils.replacePlusCodeForSms(var1);
   }

   public String replacePlusCodeWithIddNdd(String var1) {
      return PlusCodeToIddNddUtils.replacePlusCodeWithIddNdd(var1);
   }
}
