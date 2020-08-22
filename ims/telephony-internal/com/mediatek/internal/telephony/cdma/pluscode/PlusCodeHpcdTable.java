package com.mediatek.internal.telephony.cdma.pluscode;

import android.telephony.Rlog;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class PlusCodeHpcdTable {
   private static final boolean DBG = true;
   static final String LOG_TAG = "PlusCodeHpcdTable";
   private static final MccIddNddSid[] MccIddNddSidMap;
   private static final MccSidLtmOff[] MccSidLtmOffMap;
   static final int PARAM_FOR_OFFSET = 2;
   static final Object sInstSync = new Object();
   private static PlusCodeHpcdTable sInstance;

   static {
      MccIddNddSidMap = TelephonyPlusCode.MCC_IDD_NDD_SID_MAP;
      MccSidLtmOffMap = TelephonyPlusCode.MCC_SID_LTM_OFF_MAP;
      throw new VerifyError("bad dex opcode");
   }

   private PlusCodeHpcdTable() {
   }

   public static MccIddNddSid getCcFromMINSTableBySid(String var0) {
      Rlog.d("PlusCodeHpcdTable", " [getCcFromMINSTableBySid] sid = " + var0);
      if (var0 != null && var0.length() != 0 && var0.length() <= 5) {
         int var2;
         try {
            var2 = Integer.parseInt(var0);
         } catch (NumberFormatException var5) {
            Rlog.e("PlusCodeHpcdTable", Log.getStackTraceString(var5));
            return null;
         }

         if (var2 >= 0) {
            Object var4 = null;
            int var3 = MccIddNddSidMap.length;
            int var1 = 0;

            MccIddNddSid var6;
            while(true) {
               var6 = (MccIddNddSid)var4;
               if (var1 >= var3) {
                  break;
               }

               var6 = MccIddNddSidMap[var1];
               if (var2 <= var6.mSidMax && var2 >= var6.mSidMin) {
                  break;
               }

               ++var1;
            }

            Rlog.d("PlusCodeHpcdTable", " getCcFromMINSTableBySid findMccIddNddSid = " + var6);
            return var6;
         }
      } else {
         Rlog.d("PlusCodeHpcdTable", "[getCcFromMINSTableBySid] please check the param ");
      }

      return null;
   }

   public static MccIddNddSid getCcFromTableByMcc(String var0) {
      Rlog.d("PlusCodeHpcdTable", " getCcFromTableByMcc mcc = " + var0);
      if (var0 != null && var0.length() != 0) {
         int var5;
         try {
            var5 = Integer.parseInt(var0);
         } catch (NumberFormatException var6) {
            Rlog.e("PlusCodeHpcdTable", Log.getStackTraceString(var6));
            return null;
         }

         int var4 = MccIddNddSidMap.length;
         Rlog.d("PlusCodeHpcdTable", " getCcFromTableByMcc size = " + var4);
         byte var3 = -1;
         int var1 = 0;

         int var2;
         while(true) {
            var2 = var3;
            if (var1 >= var4) {
               break;
            }

            var2 = MccIddNddSidMap[var1].getMcc();
            Rlog.d("PlusCodeHpcdTable", " getCcFromTableByMcc tempMcc = " + var2);
            if (var2 == var5) {
               var2 = var1;
               break;
            }

            ++var1;
         }

         Rlog.d("PlusCodeHpcdTable", " getCcFromTableByMcc find = " + var2);
         if (var2 > -1 && var2 < var4) {
            MccIddNddSid var7 = MccIddNddSidMap[var2];
            Rlog.d("PlusCodeHpcdTable", "Now find Mcc = " + var7.mMcc + ", Mcc = " + var7.mCc + ", SidMin = " + var7.mSidMin + ", SidMax = " + var7.mSidMax + ", Idd = " + var7.mIdd + ", Ndd = " + var7.mNdd);
            return var7;
         } else {
            Rlog.d("PlusCodeHpcdTable", "can't find one that match the Mcc");
            return null;
         }
      } else {
         Rlog.d("PlusCodeHpcdTable", "[getCcFromTableByMcc] please check the param ");
         return null;
      }
   }

   public static PlusCodeHpcdTable getInstance() {
      Object var0 = sInstSync;
      synchronized(var0){}

      Throwable var10000;
      boolean var10001;
      label131: {
         try {
            if (sInstance == null) {
               sInstance = new PlusCodeHpcdTable();
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label131;
         }

         label128:
         try {
            return sInstance;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label128;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public static ArrayList<String> getMccFromConflictTableBySid(String var0) {
      ArrayList var4 = null;
      Rlog.d("PlusCodeHpcdTable", " [getMccFromConflictTableBySid] sid = " + var0);
      ArrayList var6;
      if (var0 != null && var0.length() != 0 && var0.length() <= 5) {
         int var2;
         try {
            var2 = Integer.parseInt(var0);
         } catch (NumberFormatException var5) {
            Rlog.e("PlusCodeHpcdTable", Log.getStackTraceString(var5));
            return null;
         }

         var6 = var4;
         if (var2 >= 0) {
            var4 = new ArrayList();
            int var3 = MccSidLtmOffMap.length;
            Rlog.d("PlusCodeHpcdTable", " [getMccFromConflictTableBySid] mccSidMapSize = " + var3);
            int var1 = 0;

            while(true) {
               var6 = var4;
               if (var1 >= var3) {
                  break;
               }

               MccSidLtmOff var7 = MccSidLtmOffMap[var1];
               if (var7 != null && var7.mSid == var2) {
                  var4.add(Integer.toString(var7.mMcc));
                  Rlog.d("PlusCodeHpcdTable", "mccSidLtmOff  Mcc = " + var7.mMcc + ", Sid = " + var7.mSid + ", LtmOffMin = " + var7.mLtmOffMin + ", LtmOffMax = " + var7.mLtmOffMax);
               }

               ++var1;
            }
         }
      } else {
         Rlog.d("PlusCodeHpcdTable", "[getMccFromConflictTableBySid] please check the param ");
         var6 = var4;
      }

      return var6;
   }

   public static String getMccFromConflictTableBySidLtmOff(String var0, String var1) {
      Rlog.d("PlusCodeHpcdTable", " [getMccFromConflictTableBySidLtmOff] sSid = " + var0 + ", sLtm_off = " + var1);
      if (var0 != null && var0.length() != 0 && var0.length() <= 5 && var1 != null && var1.length() != 0) {
         int var3;
         try {
            var3 = Integer.parseInt(var0);
         } catch (NumberFormatException var9) {
            Rlog.e("PlusCodeHpcdTable", Log.getStackTraceString(var9));
            return null;
         }

         if (var3 >= 0) {
            int var4;
            try {
               var4 = Integer.parseInt(var1);
            } catch (NumberFormatException var8) {
               Rlog.e("PlusCodeHpcdTable", Log.getStackTraceString(var8));
               return null;
            }

            Rlog.d("PlusCodeHpcdTable", " [getMccFromConflictTableBySidLtmOff] sid = " + var3);
            int var5 = MccSidLtmOffMap.length;
            Rlog.d("PlusCodeHpcdTable", " [getMccFromConflictTableBySidLtmOff] mccSidMapSize = " + var5);

            for(int var2 = 0; var2 < var5; ++var2) {
               MccSidLtmOff var10 = MccSidLtmOffMap[var2];
               int var6 = var10.mLtmOffMax * 2;
               int var7 = var10.mLtmOffMin * 2;
               Rlog.d("PlusCodeHpcdTable", "[getMccFromConflictTableBySidLtmOff] mccSidLtmOff.Sid = " + var10.mSid + ", sid = " + var3 + ", ltm_off = " + var4 + ", max = " + var6 + ", min = " + var7);
               if (var10.mSid == var3 && var4 <= var6 && var4 >= var7) {
                  var0 = Integer.toString(var10.mMcc);
                  Rlog.d("PlusCodeHpcdTable", "[getMccFromConflictTableBySidLtmOff] Mcc = " + var0);
                  return var0;
               }
            }
         }
      } else {
         Rlog.d("PlusCodeHpcdTable", "[getMccFromConflictTableBySidLtmOff] please check the param ");
      }

      return null;
   }

   public static String getMccFromMINSTableBySid(String var0) {
      Rlog.d("PlusCodeHpcdTable", " [getMccFromMINSTableBySid] sid = " + var0);
      if (var0 != null && var0.length() != 0 && var0.length() <= 5) {
         int var2;
         try {
            var2 = Integer.parseInt(var0);
         } catch (NumberFormatException var4) {
            Rlog.e("PlusCodeHpcdTable", Log.getStackTraceString(var4));
            return null;
         }

         if (var2 >= 0) {
            int var3 = MccIddNddSidMap.length;
            Rlog.d("PlusCodeHpcdTable", " [getMccFromMINSTableBySid] size = " + var3);

            for(int var1 = 0; var1 < var3; ++var1) {
               MccIddNddSid var5 = MccIddNddSidMap[var1];
               Rlog.d("PlusCodeHpcdTable", " [getMccFromMINSTableBySid] sid = " + var2 + ", mccIddNddSid.SidMin = " + var5.mSidMin + ", mccIddNddSid.SidMax = " + var5.mSidMax);
               if (var2 >= var5.mSidMin && var2 <= var5.mSidMax) {
                  var0 = Integer.toString(var5.mMcc);
                  Rlog.d("PlusCodeHpcdTable", "[queryMccFromConflictTableBySid] Mcc = " + var0);
                  return var0;
               }
            }
         }
      } else {
         Rlog.d("PlusCodeHpcdTable", "[getMccFromMINSTableBySid] please check the param ");
      }

      return null;
   }

   public static String getMccMncFromSidMccMncListBySid(String var0) {
      Rlog.d("PlusCodeHpcdTable", " [getMccMncFromSidMccMncListBySid] sid = " + var0);
      if (var0 != null && var0.length() != 0 && var0.length() <= 5) {
         int var5;
         try {
            var5 = Integer.parseInt(var0);
         } catch (NumberFormatException var7) {
            Rlog.e("PlusCodeHpcdTable", Log.getStackTraceString(var7));
            return null;
         }

         if (var5 >= 0) {
            List var8 = TelephonyPlusCode.getSidMccMncList();
            int var2 = 0;
            int var1 = var8.size() - 1;
            byte var4 = 0;

            int var3;
            while(true) {
               var3 = var4;
               if (var2 > var1) {
                  break;
               }

               var3 = (var2 + var1) / 2;
               SidMccMnc var6 = (SidMccMnc)var8.get(var3);
               if (var5 < var6.mSid) {
                  var1 = var3 - 1;
               } else {
                  if (var5 <= var6.mSid) {
                     var3 = var6.mMccMnc;
                     break;
                  }

                  var2 = var3 + 1;
               }
            }

            if (var3 != 0) {
               var0 = Integer.toString(var3);
               Rlog.d("PlusCodeHpcdTable", "[getMccMncFromSidMccMncListBySid] MccMncStr = " + var0);
               return var0;
            }
         }
      } else {
         Rlog.d("PlusCodeHpcdTable", "[getMccMncFromSidMccMncListBySid] please check the param ");
      }

      return null;
   }

   public String getCcFromMINSTableByLTM(List<String> var1, String var2) {
      Rlog.d("PlusCodeHpcdTable", " getCcFromMINSTableByLTM sLtm_off = " + var2);
      if (var2 != null && var2.length() != 0 && var1 != null && var1.size() != 0) {
         String var11 = null;

         int var5;
         try {
            var5 = Integer.parseInt(var2);
         } catch (NumberFormatException var13) {
            Rlog.e("PlusCodeHpcdTable", Log.getStackTraceString(var13));
            return null;
         }

         Rlog.d("PlusCodeHpcdTable", "[getCcFromMINSTableByLTM]  ltm_off =  " + var5);
         int var6 = var1.size();
         if (var6 > 1 && MccSidLtmOffMap != null) {
            int var7 = MccSidLtmOffMap.length;
            Rlog.d("PlusCodeHpcdTable", " Conflict FindOutMccSize = " + var6);
            int var3 = 0;
            var2 = var11;

            while(true) {
               var11 = var2;
               if (var3 >= var6) {
                  break;
               }

               int var8;
               try {
                  var8 = Integer.parseInt((String)var1.get(var3));
               } catch (NumberFormatException var12) {
                  Rlog.e("PlusCodeHpcdTable", Log.getStackTraceString(var12));
                  return null;
               }

               Rlog.d("PlusCodeHpcdTable", " Conflict mcc = " + var8 + ",index = " + var3);
               int var4 = 0;

               while(true) {
                  var11 = var2;
                  if (var4 >= var7) {
                     break;
                  }

                  MccSidLtmOff var14 = MccSidLtmOffMap[var4];
                  if (var14.mMcc == var8) {
                     int var9 = var14.mLtmOffMax;
                     int var10 = var14.mLtmOffMin;
                     Rlog.d("PlusCodeHpcdTable", "mccSidLtmOff LtmOffMin = " + var14.mLtmOffMin + ", LtmOffMax = " + var14.mLtmOffMax);
                     if (var5 <= var9 * 2 && var5 >= var10 * 2) {
                        var11 = (String)var1.get(var3);
                        break;
                     }
                  }

                  ++var4;
               }

               ++var3;
               var2 = var11;
            }
         } else {
            var11 = (String)var1.get(0);
         }

         Rlog.d("PlusCodeHpcdTable", "find one that match the ltm_off mcc = " + var11);
         return var11;
      } else {
         Rlog.d("PlusCodeHpcdTable", "[getCcFromMINSTableByLTM] please check the param ");
         return null;
      }
   }
}
