package com.mediatek.internal.telephony;

import android.os.SystemProperties;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import java.util.Iterator;
import java.util.List;

public class DefaultVoiceCallSubSettings {
   private static final String LOG_TAG = "DefaultVoiceCallSubSettings";

   private static boolean isMTKBspSupported() {
      boolean var0 = "1".equals(SystemProperties.get("ro.mtk_bsp_package"));
      logi("isMTKBspSupported(): " + var0);
      return var0;
   }

   private static boolean isoldDefaultVoiceSubIdActive(List<SubscriptionInfo> var0) {
      int var1 = SubscriptionManager.getDefaultVoiceSubId();
      Iterator var2 = var0.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(((SubscriptionInfo)var2.next()).getSubscriptionId() != var1);

      return true;
   }

   private static void logi(String var0) {
      Log.i("DefaultVoiceCallSubSettings", var0);
   }

   public static void setVoiceCallDefaultSub(List<SubscriptionInfo> var0) {
      if (!isMTKBspSupported()) {
         int var1 = SubscriptionManager.getDefaultVoiceSubId();
         logi("oldDefaultVoiceSubId = " + var1);
         if (var0 != null) {
            logi("subInfos size = " + var0.size());
            if (var0.size() > 1) {
               if (isoldDefaultVoiceSubIdActive(var0)) {
                  logi("subInfos size > 1 & old available, set to :" + var1);
                  return;
               }

               logi("subInfos size > 1, set to : ASK_USER");
               return;
            }

            if (var0.size() == 1) {
               logi("subInfos size == 1, set to :" + ((SubscriptionInfo)var0.get(0)).getSubscriptionId());
               return;
            }

            logi("subInfos size = 0 set of : INVALID_SUBSCRIPTION_ID");
            return;
         }

         logi("subInfos == null, set to : INVALID_SUBSCRIPTION_ID");
      }

   }
}
