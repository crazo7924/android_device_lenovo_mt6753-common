package com.mediatek.internal.telephony.cdma.pluscode;

public interface IPlusCodeUtils {
   String PROPERTY_ICC_CDMA_OPERATOR_MCC = "cdma.icc.operator.mcc";
   String PROPERTY_OPERATOR_MCC = "cdma.operator.mcc";
   String PROPERTY_OPERATOR_SID = "cdma.operator.sid";
   String PROPERTY_TIME_LTMOFFSET = "cdma.operator.ltmoffset";

   boolean canFormatPlusCodeForSms();

   boolean canFormatPlusToIddNdd();

   String checkMccBySidLtmOff(String var1);

   String removeIddNddAddPlusCode(String var1);

   String removeIddNddAddPlusCodeForSms(String var1);

   String replacePlusCodeForSms(String var1);

   String replacePlusCodeWithIddNdd(String var1);
}
