package com.mediatek.internal.telephony.cdma.pluscode;

import android.telephony.Rlog;

public class PlusCodeProcessor {
   private static final String LOG_TAG = "PlusCodeProcessor";
   private static final String PLUS_CODE_IMPL_CLASS_NAME = "com.mediatek.internal.telephony.cdma.pluscode.CdmaPlusCodeUtils";
   private static final Object mLock = new Object();
   private static IPlusCodeUtils sPlusCodeUtilsInstance;

   static {
      throw new VerifyError("bad dex opcode");
   }

   public static IPlusCodeUtils getPlusCodeUtils() {
      if (sPlusCodeUtilsInstance == null) {
         label149: {
            Object var0 = mLock;
            synchronized(var0){}

            Throwable var10000;
            boolean var10001;
            label143: {
               try {
                  if (sPlusCodeUtilsInstance == null) {
                     sPlusCodeUtilsInstance = makePlusCodeUtis();
                  }
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break label143;
               }

               label140:
               try {
                  break label149;
               } catch (Throwable var12) {
                  var10000 = var12;
                  var10001 = false;
                  break label140;
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
      }

      log("getPlusCodeUtils sPlusCodeUtilsInstance=" + sPlusCodeUtilsInstance);
      return sPlusCodeUtilsInstance;
   }

   private static void log(String var0) {
      Rlog.d("PlusCodeProcessor", var0);
   }

   private static IPlusCodeUtils makePlusCodeUtis() {
      try {
         IPlusCodeUtils var0 = (IPlusCodeUtils)Class.forName("com.mediatek.internal.telephony.cdma.pluscode.CdmaPlusCodeUtils").newInstance();
         return var0;
      } catch (ClassNotFoundException var1) {
         log("makePlusCodeUtis ClassNotFoundException, return default DefaultPlusCodeUtils");
         return new DefaultPlusCodeUtils();
      } catch (IllegalAccessException var2) {
         log("makePlusCodeUtis IllegalAccessException, return default DefaultPlusCodeUtils");
         return new DefaultPlusCodeUtils();
      } catch (InstantiationException var3) {
         log("makePlusCodeUtis InstantiationException, return default DefaultPlusCodeUtils");
         return new DefaultPlusCodeUtils();
      }
   }
}
