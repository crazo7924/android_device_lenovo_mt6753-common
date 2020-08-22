package com.mediatek.internal.telephony;

import android.content.Context;
import android.os.SystemProperties;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import java.util.Iterator;
import java.util.List;

public class DefaultSmsSimSettings {
   public static final int ASK_USER_SUB_ID = -2;
   private static final String TAG = "DefaultSmsSimSettings";

   private static boolean isoldDefaultSMSSubIdActive(List<SubscriptionInfo> var0) {
      int var1 = SubscriptionManager.getDefaultSmsSubId();
      Iterator var2 = var0.iterator();

      while(true) {
         if (var2.hasNext()) {
            if (((SubscriptionInfo)var2.next()).getSubscriptionId() != var1) {
               continue;
            }
            break;
         }

         if ("OP01".equals(SystemProperties.get("ro.operator.optr")) && (var1 == -2 || var1 == -3)) {
            break;
         }

         return false;
      }

      return true;
   }

   public static void setSmsTalkDefaultSim(List<SubscriptionInfo> var0, Context var1) {
      if (!"1".equals(SystemProperties.get("ro.mtk_bsp_package"))) {
         int var2 = SubscriptionManager.getDefaultSmsSubId();
         Log.i("DefaultSmsSimSettings", "oldSmsDefaultSIM" + var2);
         if (var0 != null) {
            Log.i("DefaultSmsSimSettings", "subInfos size = " + var0.size());
            if (var0.size() > 1) {
               if (isoldDefaultSMSSubIdActive(var0)) {
                  Log.i("DefaultSmsSimSettings", "subInfos size > 1 & old available, set to :" + var2);
                  SubscriptionManager.from(var1).setDefaultSmsSubId(var2);
                  return;
               }

               if ("OP01".equals(SystemProperties.get("ro.operator.optr"))) {
                  Log.i("DefaultSmsSimSettings", "subInfos size > 1, set to : AUTO");
                  SubscriptionManager.from(var1).setDefaultSmsSubId(-3);
               } else if ("OP09".equals(SystemProperties.get("ro.operator.optr")) && "SEGDEFAULT".equals(SystemProperties.get("ro.operator.seg"))) {
                  var2 = SubscriptionManager.from(var1).getActiveSubscriptionInfoForSimSlotIndex(0).getSubscriptionId();
                  SubscriptionManager.from(var1).setDefaultSmsSubId(var2);
                  Log.i("DefaultSmsSimSettings", "subInfos size > 1, set to " + var2);
               } else {
                  Log.i("DefaultSmsSimSettings", "subInfos size > 1, set to : ASK_USER_SUB_ID");
                  SubscriptionManager.from(var1).setDefaultSmsSubId(-2);
               }

               var2 = SubscriptionManager.from(var1).getActiveSubscriptionInfoForSimSlotIndex(0).getSubscriptionId();
               SubscriptionManager.from(var1).setDefaultSmsSubId(var2);
               return;
            }

            if (var0.size() == 1) {
               Log.i("DefaultSmsSimSettings", "sub size = 1,segment = " + SystemProperties.get("ro.operator.seg"));
               if ("OP09".equals(SystemProperties.get("ro.operator.optr")) && "SEGDEFAULT".equals(SystemProperties.get("ro.operator.seg"))) {
                  var2 = ((SubscriptionInfo)var0.get(0)).getSubscriptionId();
                  SubscriptionManager.from(var1).setDefaultSmsSubId(var2);
                  Log.i("DefaultSmsSimSettings", "subInfos size = 1, set to " + var2);
               }

               SubscriptionManager.from(var1).setDefaultSmsSubId(((SubscriptionInfo)var0.get(0)).getSubscriptionId());
               return;
            }

            Log.i("DefaultSmsSimSettings", "setSmsTalkDefaultSim SIM not insert");
            SubscriptionManager.from(var1).setDefaultSmsSubId(-1);
            return;
         }

         Log.i("DefaultSmsSimSettings", "subInfos == null, return");
         SubscriptionManager.from(var1).setDefaultSmsSubId(-1);
      }

   }
}
