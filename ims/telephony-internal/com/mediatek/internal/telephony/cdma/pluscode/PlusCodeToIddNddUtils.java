package com.mediatek.internal.telephony.cdma.pluscode;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.telephony.Rlog;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import com.mediatek.internal.telephony.ITelephonyEx;
import java.util.ArrayList;

public class PlusCodeToIddNddUtils {
   public static final String INTERNATIONAL_PREFIX_SYMBOL = "+";
   static final String LOG_TAG = "PlusCodeToIddNddUtils";
   private static final SparseIntArray MOBILE_NUMBER_SPEC_MAP;
   private static PlusCodeHpcdTable sHpcd = PlusCodeHpcdTable.getInstance();
   private static MccIddNddSid sMccIddNddSid = null;

   static {
      MOBILE_NUMBER_SPEC_MAP = TelephonyPlusCode.MOBILE_NUMBER_SPEC_MAP;
      throw new VerifyError("bad dex opcode");
   }

   public static boolean canFormatPlusCodeForSms() {
      boolean var1 = false;
      String var2 = SystemProperties.get("cdma.icc.operator.mcc", "");
      Rlog.d("PlusCodeToIddNddUtils", "[canFormatPlusCodeForSms] Mcc = " + var2);
      sMccIddNddSid = null;
      boolean var0 = var1;
      if (sHpcd != null) {
         Rlog.d("PlusCodeToIddNddUtils", "[canFormatPlusCodeForSms] Mcc = " + var2);
         var0 = var1;
         if (var2 != null) {
            var0 = var1;
            if (var2.length() != 0) {
               sMccIddNddSid = PlusCodeHpcdTable.getCcFromTableByMcc(var2);
               Rlog.d("PlusCodeToIddNddUtils", "[canFormatPlusCodeForSms] getCcFromTableByMcc mccIddNddSid = " + sMccIddNddSid);
               if (sMccIddNddSid == null) {
                  return false;
               }

               var0 = true;
            }
         }
      }

      return var0;
   }

   public static boolean canFormatPlusToIddNdd() {
      Rlog.d("PlusCodeToIddNddUtils", "-------------canFormatPlusToIddNdd-------------");
      int var0 = getCdmaSubId();
      String var2 = TelephonyManager.getDefault().getNetworkOperatorForSubscription(var0);
      Rlog.d("PlusCodeToIddNddUtils", "cdmaSubId:" + var0 + ", network operator numeric:" + var2);
      if (var2.length() == 7) {
         var2 = var2.substring(0, 4);
      } else {
         var2 = var2.substring(0, 3);
      }

      String var4 = "";

      String var3;
      label82: {
         RemoteException var13;
         label81: {
            NullPointerException var10000;
            label86: {
               Bundle var5;
               boolean var10001;
               try {
                  var5 = ITelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx")).getServiceState(var0);
               } catch (RemoteException var8) {
                  var13 = var8;
                  var10001 = false;
                  break label81;
               } catch (NullPointerException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label86;
               }

               var3 = var4;
               if (var5 == null) {
                  break label82;
               }

               try {
                  var3 = Integer.toString(ServiceState.newFromBundle(var5).getSystemId());
                  break label82;
               } catch (RemoteException var6) {
                  var13 = var6;
                  var10001 = false;
                  break label81;
               } catch (NullPointerException var7) {
                  var10000 = var7;
                  var10001 = false;
               }
            }

            NullPointerException var10 = var10000;
            Rlog.d("PlusCodeToIddNddUtils", "canFormatPlusToIddNdd, NullPointerException:" + var10);
            var3 = var4;
            break label82;
         }

         RemoteException var12 = var13;
         Rlog.d("PlusCodeToIddNddUtils", "canFormatPlusToIddNdd, RemoteException:" + var12);
         var3 = var4;
      }

      var4 = TelephonyManager.getTelephonyProperty(SubscriptionManager.getPhoneId(var0), "cdma.operator.ltmoffset", "");
      Rlog.d("PlusCodeToIddNddUtils", "mcc = " + var2 + ", sid = " + var3 + ", ltm_off = " + var4);
      boolean var1 = false;
      sMccIddNddSid = null;
      if (sHpcd != null) {
         if (!var2.startsWith("2134")) {
            var1 = true;
         } else {
            var1 = false;
         }

         Rlog.d("PlusCodeToIddNddUtils", "[canFormatPlusToIddNdd] Mcc = " + var2 + ", !Mcc.startsWith(2134) = " + var1);
         if (!TextUtils.isEmpty(var2) && Character.isDigit(var2.charAt(0)) && !var2.startsWith("000") && var1) {
            sMccIddNddSid = PlusCodeHpcdTable.getCcFromTableByMcc(var2);
            Rlog.d("PlusCodeToIddNddUtils", "[canFormatPlusToIddNdd] getCcFromTableByMcc mccIddNddSid = " + sMccIddNddSid);
            if (sMccIddNddSid != null) {
               var1 = true;
            } else {
               var1 = false;
            }
         } else {
            ArrayList var11 = PlusCodeHpcdTable.getMccFromConflictTableBySid(var3);
            if (var11 != null && var11.size() != 0) {
               if (var11.size() >= 2) {
                  var2 = sHpcd.getCcFromMINSTableByLTM(var11, var4);
                  if (var2 != null && var2.length() != 0) {
                     sMccIddNddSid = PlusCodeHpcdTable.getCcFromTableByMcc(var2);
                  }

                  Rlog.d("PlusCodeToIddNddUtils", "[canFormatPlusToIddNdd] conflicts, getCcFromTableByMcc mccIddNddSid = " + sMccIddNddSid);
               } else if (var11.size() == 1) {
                  sMccIddNddSid = PlusCodeHpcdTable.getCcFromTableByMcc((String)var11.get(0));
                  Rlog.d("PlusCodeToIddNddUtils", "[canFormatPlusToIddNdd] do not conflicts, getCcFromTableByMcc mccIddNddSid = " + sMccIddNddSid);
               }
            } else {
               Rlog.d("PlusCodeToIddNddUtils", "[canFormatPlusToIddNdd] Do not find cc by SID from confilcts table, so from lookup table");
               sMccIddNddSid = PlusCodeHpcdTable.getCcFromMINSTableBySid(var3);
               Rlog.d("PlusCodeToIddNddUtils", "[canFormatPlusToIddNdd] getCcFromMINSTableBySid mccIddNddSid = " + sMccIddNddSid);
            }

            if (sMccIddNddSid != null) {
               var1 = true;
            } else {
               var1 = false;
            }
         }
      }

      Rlog.d("PlusCodeToIddNddUtils", "[canFormatPlusToIddNdd] find = " + var1 + ", mccIddNddSid = " + sMccIddNddSid);
      return var1;
   }

   public static String checkMccBySidLtmOff(String var0) {
      Rlog.d("PlusCodeToIddNddUtils", "[checkMccBySidLtmOff] mccMnc = " + var0);
      int var1 = getCdmaSubId();
      String var3 = "";

      String var2;
      label50: {
         RemoteException var11;
         label49: {
            NullPointerException var10000;
            label54: {
               Bundle var4;
               boolean var10001;
               try {
                  var4 = ITelephonyEx.Stub.asInterface(ServiceManager.getService("phoneEx")).getServiceState(var1);
               } catch (RemoteException var7) {
                  var11 = var7;
                  var10001 = false;
                  break label49;
               } catch (NullPointerException var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label54;
               }

               var2 = var3;
               if (var4 == null) {
                  break label50;
               }

               try {
                  var2 = Integer.toString(ServiceState.newFromBundle(var4).getSystemId());
                  break label50;
               } catch (RemoteException var5) {
                  var11 = var5;
                  var10001 = false;
                  break label49;
               } catch (NullPointerException var6) {
                  var10000 = var6;
                  var10001 = false;
               }
            }

            NullPointerException var9 = var10000;
            Rlog.d("PlusCodeToIddNddUtils", "checkMccBySidLtmOff, NullPointerException:" + var9);
            var2 = var3;
            break label50;
         }

         RemoteException var10 = var11;
         Rlog.d("PlusCodeToIddNddUtils", "checkMccBySidLtmOff, RemoteException:" + var10);
         var2 = var3;
      }

      var3 = TelephonyManager.getTelephonyProperty(SubscriptionManager.getPhoneId(var1), "cdma.operator.ltmoffset", "");
      Rlog.d("PlusCodeToIddNddUtils", "[checkMccBySidLtmOff] Sid = " + var2 + ", Ltm_off = " + var3);
      var3 = PlusCodeHpcdTable.getMccFromConflictTableBySidLtmOff(var2, var3);
      Rlog.d("PlusCodeToIddNddUtils", "[checkMccBySidLtmOff] MccFromConflictTable = " + var3);
      if (var3 != null) {
         var0 = var3;
      } else {
         var3 = PlusCodeHpcdTable.getMccFromMINSTableBySid(var2);
         Rlog.d("PlusCodeToIddNddUtils", "[checkMccBySidLtmOff] MccFromMINSTable = " + var3);
         if (var3 != null) {
            var0 = var3;
         }
      }

      Rlog.d("PlusCodeToIddNddUtils", "[checkMccBySidLtmOff] tempMcc = " + var0);
      if (!var0.startsWith("310") && !var0.startsWith("311")) {
         var3 = var0;
         if (!var0.startsWith("312")) {
            return var3;
         }
      }

      var2 = PlusCodeHpcdTable.getMccMncFromSidMccMncListBySid(var2);
      Rlog.d("PlusCodeToIddNddUtils", "[checkMccBySidLtmOff] MccMnc = " + var2);
      var3 = var0;
      if (var2 != null) {
         var3 = var2;
      }

      return var3;
   }

   private static String formatPlusCode(String var0) {
      String var1 = null;
      if (sMccIddNddSid != null) {
         String var3 = sMccIddNddSid.mCc;
         if (var0.startsWith(var3)) {
            var1 = sMccIddNddSid.mNdd;
            String var2;
            if (!sMccIddNddSid.mCc.equals("86") && !sMccIddNddSid.mCc.equals("853")) {
               var2 = var0.substring(var3.length(), var0.length());
               var0 = var1;
               var1 = var2;
               if (isMobileNumber(var3, var2)) {
                  Rlog.d("PlusCodeToIddNddUtils", "CC matched, isMobile = true Ndd = ");
                  var0 = "";
                  var1 = var2;
               }
            } else {
               Rlog.d("PlusCodeToIddNddUtils", "CC matched, cc is chinese");
               var2 = "00";
               var1 = var0;
               var0 = var2;
            }

            return var0 + var1;
         }

         var1 = sMccIddNddSid.mIdd + var0;
      }

      return var1;
   }

   private static int getCdmaSubId() {
      TelephonyManager var3 = TelephonyManager.getDefault();
      int var1 = var3.getSimCount();

      for(int var0 = 0; var0 < var1; ++var0) {
         int var2 = SubscriptionManager.getSubIdUsingPhoneId(var0);
         if (var3.getCurrentPhoneType(var2) == 2) {
            return var2;
         }
      }

      return -1;
   }

   private static boolean isMobileNumber(String var0, String var1) {
      if (var1 != null && var1.length() != 0) {
         if (MOBILE_NUMBER_SPEC_MAP == null) {
            Rlog.d("PlusCodeToIddNddUtils", "[isMobileNumber] MOBILE_NUMBER_SPEC_MAP == null ");
            return false;
         }

         int var3 = MOBILE_NUMBER_SPEC_MAP.size();

         int var4;
         try {
            var4 = Integer.parseInt(var0);
         } catch (NumberFormatException var5) {
            Rlog.e("PlusCodeToIddNddUtils", Log.getStackTraceString(var5));
            return false;
         }

         Rlog.d("PlusCodeToIddNddUtils", "[isMobileNumber] iCC = " + var4);

         for(int var2 = 0; var2 < var3; ++var2) {
            Rlog.d("PlusCodeToIddNddUtils", "[isMobileNumber] value = " + MOBILE_NUMBER_SPEC_MAP.valueAt(var2) + ", key =  " + MOBILE_NUMBER_SPEC_MAP.keyAt(var2));
            if (MOBILE_NUMBER_SPEC_MAP.valueAt(var2) == var4) {
               Rlog.d("PlusCodeToIddNddUtils", "[isMobileNumber]  value = icc");
               var0 = Integer.toString(MOBILE_NUMBER_SPEC_MAP.keyAt(var2));
               Rlog.d("PlusCodeToIddNddUtils", "[isMobileNumber]  prfix = " + var0);
               if (var1.startsWith(var0)) {
                  Rlog.d("PlusCodeToIddNddUtils", "[isMobileNumber]  number.startsWith(prfix) = true");
                  return true;
               }
            }
         }
      } else {
         Rlog.d("PlusCodeToIddNddUtils", "[isMobileNumber] please check the param ");
      }

      return false;
   }

   public static String removeIddNddAddPlusCode(String var0) {
      if (var0 != null && var0.length() != 0) {
         String var2 = var0;
         if (!var0.startsWith("+")) {
            if (!canFormatPlusToIddNdd()) {
               Rlog.d("PlusCodeToIddNddUtils", "[removeIddNddAddPlusCode] find no operator that match the MCC ");
               return var0;
            }

            var2 = var0;
            if (sMccIddNddSid != null) {
               String var3 = sMccIddNddSid.mIdd;
               Rlog.d("PlusCodeToIddNddUtils", "[removeIddNddAddPlusCode] find match the cc, Idd = " + var3);
               var2 = var0;
               if (var0.startsWith(var3)) {
                  var2 = var0;
                  if (var0.length() > var3.length()) {
                     var0 = var0.substring(var3.length(), var0.length());
                     var2 = "+" + var0;
                  }
               }
            }
         }

         return var2;
      } else {
         Rlog.d("PlusCodeToIddNddUtils", "[removeIddNddAddPlusCode] please check the param ");
         return var0;
      }
   }

   public static String removeIddNddAddPlusCodeForSms(String var0) {
      if (var0 != null && var0.length() != 0) {
         if (!var0.startsWith("+")) {
            if (!canFormatPlusCodeForSms()) {
               Rlog.d("PlusCodeToIddNddUtils", "[removeIddNddAddPlusCodeForSms] find no operator that match the MCC ");
               return var0;
            }

            if (sMccIddNddSid != null) {
               String var1 = sMccIddNddSid.mIdd;
               Rlog.d("PlusCodeToIddNddUtils", "[removeIddNddAddPlusCodeForSms] find match the cc, Idd = " + var1);
               if (var0.startsWith(var1) && var0.length() > var1.length()) {
                  var0 = var0.substring(var1.length(), var0.length());
                  return "+" + var0;
               }
            }
         }
      } else {
         Rlog.d("PlusCodeToIddNddUtils", "[removeIddNddAddPlusCodeForSms] please check the param ");
      }

      return var0;
   }

   public static String replacePlusCodeForSms(String var0) {
      if (var0 != null && var0.length() != 0 && var0.startsWith("+")) {
         boolean var1 = canFormatPlusCodeForSms();
         if (var1) {
            String var2 = var0;
            if (var0.startsWith("+")) {
               var2 = var0.substring(1, var0.length());
            }

            if (var1) {
               return formatPlusCode(var2);
            }
         }
      } else {
         Rlog.d("PlusCodeToIddNddUtils", "number can't format correctly, number = " + var0);
      }

      return null;
   }

   public static String replacePlusCodeWithIddNdd(String var0) {
      Rlog.d("PlusCodeToIddNddUtils", "replacePlusCodeWithIddNdd");
      if (var0 != null && var0.length() != 0 && var0.startsWith("+")) {
         boolean var1 = canFormatPlusToIddNdd();
         if (var1) {
            String var2 = var0;
            if (var0.startsWith("+")) {
               var2 = var0.substring(1, var0.length());
            }

            if (var1) {
               return formatPlusCode(var2);
            }
         }
      }

      return null;
   }
}
