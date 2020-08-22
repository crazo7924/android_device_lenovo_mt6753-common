package com.mediatek.internal.telephony;

public class CellBroadcastConfigInfo {
  public String channelConfigInfo = null;
  
  public boolean isAllLanguageOn = false;
  
  public String languageConfigInfo = null;
  
  public int mode = 1;
  
  public CellBroadcastConfigInfo(int paramInt, String paramString1, String paramString2, boolean paramBoolean) {
    this.mode = paramInt;
    this.channelConfigInfo = paramString1;
    this.languageConfigInfo = paramString2;
    this.isAllLanguageOn = paramBoolean;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("CellBroadcastConfigInfo: mode = ");
    stringBuilder.append(this.mode);
    stringBuilder.append(", channel = ");
    stringBuilder.append(this.channelConfigInfo);
    stringBuilder.append(", language = ");
    if (!this.isAllLanguageOn) {
      stringBuilder.append(this.languageConfigInfo);
      return stringBuilder.toString();
    } 
    stringBuilder.append("all");
    return stringBuilder.toString();
  }
}


/* Location:              /home/ishaan/Bharat/stuff/ProJects/k5fpr/IMS/jars/telephony-common-dex2jar.jar!/com/mediatek/internal/telephony/CellBroadcastConfigInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */